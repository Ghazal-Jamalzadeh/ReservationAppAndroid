package ir.tamuk.reservation.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Timer;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    NavController navController;



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

//    @Override
//    public void onBackPressed() {
//        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
//
//                    if (navDestination.getId() == R.id.signInValiddationcodeFragment) {
//
//                    } else {
//                        super.onBackPressed();
//                    }
//        });
//
//
//    }
}