package ir.tamuk.reservation.fragments.ui.Splash;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentSplashBinding;
import ir.tamuk.reservation.utils.TokenManager;


public class SplashFragment extends Fragment {

    private static final String TAG = "SplashFragment";
    private FragmentSplashBinding binding ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false);

        if (TokenManager.hasAccessToken(getContext())) {
            Log.d(TAG, "access token : " + TokenManager.getAccessToken(getContext()));
            Log.d(TAG, "refresh token : " + TokenManager.getRefreshToken(getContext()));
        }else {
            Log.d(TAG, "pls login .... ");
        }

        Handler h = new Handler();
        Runnable r = () -> {
            Navigation.findNavController(container).popBackStack() ;
            Navigation.findNavController(container).navigate(R.id.action_to_navHome);
        };h.postDelayed(r, 2000);

        return binding.getRoot();
    }
}