package ir.tamuk.reservation.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.ActivityMainBinding;
import ir.tamuk.reservation.fragments.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;
    private boolean isDubblePress= false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ///navigatin bar
        BottomNavigationView navView = findViewById(R.id.bottomNav);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);


        ///////drawer////////

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
        //////////////////////

        //navController Hide When OpenSigning Fragment
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                Handler h = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {

                        if (navDestination.getId() == R.id.signingFragment ||
                                navDestination.getId() == R.id.signInValiddationcodeFragment) {
                            binding.bottomNav.setVisibility(View.GONE);
                        } else {
                            binding.bottomNav.setVisibility(View.VISIBLE);
                        }
                        if (navDestination.getId()==R.id.serviceInfoFragment){
                            binding.bottomNav.setVisibility(View.GONE);
                        }else {
                            binding.bottomNav.setVisibility(View.VISIBLE);
                        }
                    }
                };
                h.postDelayed(r, 100);


            }
        });
        ////////////////////////////////////////////////////////

    }




    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return super.onSupportNavigateUp();
    }

    //when in honme Fragment backPress dont work else doublePress
    @Override
    public void onBackPressed() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

            if (navController.getCurrentDestination().getId() == R.id.nav_home) {
                if (isDubblePress) {

                    Log.d("isDubblePress", "onBackPressed: ");
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
            } else {
                super.onBackPressed();

            }


    }


}