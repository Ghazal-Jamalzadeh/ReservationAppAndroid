package ir.tamuk.reservation.fragments.ui.editProfile;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentEditProfileBinding;
import ir.tamuk.reservation.fragments.ui.profile.ProfileViewModel;
import ir.tamuk.reservation.models.User;
import ir.tamuk.reservation.utils.Constants;


public class EditProfileFragment extends Fragment {
     private FragmentEditProfileBinding binding ;
     private ProfileViewModel profileViewModel ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentEditProfileBinding.inflate(inflater , container , false) ;
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        return binding.getRoot() ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Navigation.findNavController(view).popBackStack() ;
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //titles
        binding.firstNameField.title.setText("نام");
        binding.lastNameField.title.setText("نام خانوادگی");
        binding.birthDateField.title.setText("تاریخ تولد");

        profileViewModel.getMyProfile().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
        binding.firstNameField.edt.setText(user.firstName);
        binding.lastNameField.edt.setText(user.lastName);
        binding.birthDateField.txt.setText(user.birthday);
        if (!user.photo.filename.equals("")){
            Glide.with(getContext()).load(Constants.DOWNLOAD_PHOTO_URL + user.photo.filename)
                    .into(binding.imgProfile);
        }
//        binding.birthDateField.txt.setText(user);

            }
        });

        binding.profileImageLay.setOnClickListener(view1 -> {
            
        });



    }
}