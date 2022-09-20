package ir.tamuk.reservation.fragments.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ir.tamuk.reservation.activities.MainActivity;
import ir.tamuk.reservation.databinding.FragmentProfileBinding;
import ir.tamuk.reservation.fragments.ui.home.HomeViewModel;
import ir.tamuk.reservation.models.User;
import ir.tamuk.reservation.utils.TokenManager;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        Log.d(TAG, "onCreateView: is first " + profileViewModel.isFirst);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogout.setOnClickListener(view1 -> {
            profileViewModel.logoutUser();
        });

        profileViewModel.getMyProfile().observe(getViewLifecycleOwner(), new Observer<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(User user) {
                binding.txtName.setText(user.firstName + " " + user.lastName);
                binding.txtPhoneNumber.setText(user.mobile);
            }
        });

        profileViewModel.logoutIsSuccess.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Toast.makeText(getContext(), "خروج موفق", Toast.LENGTH_SHORT).show();

                    TokenManager.removeAccessToken(getContext());
                    TokenManager.removeRefreshToken(getContext());

                    getActivity().finishAffinity();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getContext().startActivity(intent);

                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}