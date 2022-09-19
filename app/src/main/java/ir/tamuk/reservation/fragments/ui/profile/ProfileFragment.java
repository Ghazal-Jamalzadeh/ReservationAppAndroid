package ir.tamuk.reservation.fragments.ui.profile;

import android.content.Intent;
import android.os.Bundle;
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
import ir.tamuk.reservation.models.User;
import ir.tamuk.reservation.utils.TokenManager;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


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
            @Override
            public void onChanged(User user) {
                binding.txtName.setText(user.userName);
            }
        });

        profileViewModel.logoutIsSuccess.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess){
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
}