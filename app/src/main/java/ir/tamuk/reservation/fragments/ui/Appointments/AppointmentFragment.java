package ir.tamuk.reservation.fragments.ui.Appointments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentAppointmentBinding;
import ir.tamuk.reservation.databinding.FragmentProfileBinding;
import ir.tamuk.reservation.fragments.ui.profile.ProfileViewModel;

public class AppointmentFragment extends Fragment {

    private FragmentAppointmentBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProfileViewModel slideshowViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentAppointmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }
}