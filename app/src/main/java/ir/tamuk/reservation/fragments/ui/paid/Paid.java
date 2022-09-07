package ir.tamuk.reservation.fragments.ui.paid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ir.tamuk.reservation.databinding.FragmentPaidBinding;


public class Paid extends Fragment {

    private FragmentPaidBinding binding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPaidBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }
}