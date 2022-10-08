package ir.tamuk.reservation.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

import net.time4j.android.ApplicationStarter;

import ir.tamuk.reservation.Interfaces.ApplicationCallBacks;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.api.AccessTokenAuthenticator;
import ir.tamuk.reservation.api.ApiClient;
import ir.tamuk.reservation.databinding.ActivityMainBinding;
import ir.tamuk.reservation.fragments.ui.home.HomeViewModel;
import ir.tamuk.reservation.fragments.ui.profile.ProfileViewModel;
import ir.tamuk.reservation.utils.TokenManager;


public class MainActivity extends AppCompatActivity implements ApplicationCallBacks {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    NavController navController;
    private boolean isDubblePress = false;
    private int currentDestination = R.id.nav_home;

    private String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ApplicationStarter.initialize(this, true); // with prefetch on background thread

        AccessTokenAuthenticator.applicationCallBacks = this::restartApplication;

        //create homeViewModel
        new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(HomeViewModel.class);
        //create profile viewModel
        new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProfileViewModel.class);

        //nav controller
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        //drawer
        binding.drawerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(binding.drawerMenu);
                binding.drawerMenu.setOnClickListener(null);
            }
        });

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(binding.drawerMenu);
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                Handler h = new Handler();
                Runnable r = new Runnable() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public void run() {

                        currentDestination = navDestination.getId();

                        switch (currentDestination) {
                            case R.id.signingFragment:
                            case R.id.signInValiddationcodeFragment:
                            case R.id.completeProfileInfoFragment:
                            case R.id.splashFragment:
                            case R.id.editProfileFragment:
                            case R.id.appointmentFragment:
                            case R.id.paid:
                                binding.bottomNav.setVisibility(View.GONE);
                                binding.toolbarconstraintLayout.setVisibility(View.GONE);
                                break;



                            default:
                                binding.bottomNav.setVisibility(View.VISIBLE);
                                binding.toolbarconstraintLayout.setVisibility(View.VISIBLE);
                                break;

                        }
                    }
                };
                h.postDelayed(r, 100);


            }
        });

        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id != currentDestination) {
                    switch (id) {
                        case R.id.nav_home:
                            navController.navigate(R.id.action_to_navHome);
                            return true;
                        case R.id.nav_services:
                            navController.navigate(R.id.action_to_navServices);
                            return true;
                        case R.id.nav_reservation:
                            navController.navigate(R.id.action_to_navReservation);
                            return true;
                        case R.id.nav_profile:
                            if (TokenManager.hasAccessToken(MainActivity.this)) {
                                navController.navigate(R.id.action_to_navProfile);
                            } else {
                                navController.navigate(R.id.action_to_signingFragment);
                            }
                            return true;
                    }
                }
                return false;
            }

        });

        //get token from firebase and send it to server
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("firebase", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("firebase", "onComplete: " + token);
                    }
                });


        //getting device Id
        try {
            android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("firebase", "android id: " + android_id);
    }

    public void clickBottomNav(int fragmentId) {

        binding.bottomNav.setSelectedItemId(fragmentId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return super.onSupportNavigateUp();
    }

    //handle double press to exit inside home fragment
    @Override
    public void onBackPressed() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        if (binding.drawerLayout.isDrawerOpen(binding.drawerMenu)) {
            binding.drawerLayout.closeDrawer(binding.drawerMenu);


        }else if (navController.getCurrentDestination().getId() == R.id.nav_home) {
            if (isDubblePress) {

                finish();

            } else {
                this.isDubblePress = true;
                Toast.makeText(this, "یبار دیگ بزن;)", Toast.LENGTH_SHORT).show();
                Handler h = new Handler();
                Runnable r = () -> {
                    isDubblePress = false;
                };
                h.postDelayed(r, 4000);
            }
        }



        else {
            super.onBackPressed();
        }
    }


    @Override
    public void restartApplication() {
        finishAffinity();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}