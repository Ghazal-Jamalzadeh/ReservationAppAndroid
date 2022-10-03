package ir.tamuk.reservation.fragments.ui.profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import ir.tamuk.reservation.Interfaces.DialogCommunicationInterface;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.activities.MainActivity;
import ir.tamuk.reservation.databinding.FragmentProfileBinding;
import ir.tamuk.reservation.models.User;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.DialogManager;
import ir.tamuk.reservation.utils.TokenManager;
import ir.tamuk.reservation.utils.Tools;

public class ProfileFragment extends Fragment implements DialogCommunicationInterface {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Buttons :
        //logout
        binding.btnLogout.setOnClickListener(view1 -> {
            DialogManager.showExitDialog(getContext() , this);
        });

        binding.btnMyAppointments.setOnClickListener(view13 -> {
            if (Tools.checkDestination(view13, R.id.nav_profile)) {
                Navigation.findNavController(view13).navigate(R.id.action_nav_profile_to_appointmentFragment);
            }
        });
        binding.btnEditProfile.setOnClickListener(view12 -> {
            if (binding.progressCircular.getVisibility() == View.VISIBLE) {
                Tools.showToast(getContext(), "اطلاعات کاربری شما در حال بارگذاری است. کمی صبر کنید...");
            } else {
                if (Tools.checkDestination(view12, R.id.nav_profile)) {
                    Navigation.findNavController(view12).navigate(R.id.action_nav_profile_to_editProfileFragment);
                }
            }
        });

        profileViewModel.getMyProfile().observe(getViewLifecycleOwner(), new Observer<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(User user) {

                binding.progressCircular.setVisibility(View.INVISIBLE);

                binding.txtName.setText(user.firstName + " " + user.lastName);
                binding.txtPhoneNumber.setText(user.mobile);

                if (!user.photo.filename.equals("")) {
                    Glide.with(getContext()).load(Constants.DOWNLOAD_PHOTO_URL + user.photo.filename)
                            .into(binding.imgProfile);
                }
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

        profileViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                //progress stop
                if (profileViewModel.ignoreMessage) {
                    profileViewModel.ignoreMessage = false;
                } else {
                    Tools.showToast(getContext(), errorMessage);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        profileViewModel.ignoreMessage = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        profileViewModel.ignoreMessage = true;
    }

    @Override
    public void yesAction(Dialog dialog) {
        profileViewModel.logoutUser();
    }

    @Override
    public void noAction(Dialog dialog) {
        dialog.dismiss();
    }
}