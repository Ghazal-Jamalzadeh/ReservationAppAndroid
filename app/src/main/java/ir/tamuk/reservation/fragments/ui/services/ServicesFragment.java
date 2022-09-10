package ir.tamuk.reservation.fragments.ui.services;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.tamuk.reservation.models.Services;
import ir.tamuk.reservation.adapter.ServicesAdapter;
import ir.tamuk.reservation.fragments.ui.reservation.ReservationViewModel;
import ir.tamuk.reservation.databinding.FragmentServicesBinding;


public class ServicesFragment extends Fragment {

    ArrayList<Services> services = new ArrayList<>() ;
    private FragmentServicesBinding binding;
    Services ser = new Services();
    Services ser2 = new Services();
    private ServicesAdapter adapter ;


    public View onCreateView(@NonNull LayoutInflater  inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReservationViewModel galleryViewModel =
                new ViewModelProvider(this).get(ReservationViewModel.class);

        binding.swipeRefreshLayout.setEnabled(true);

        ser.text1 = "salam";
        ser.text2 = "hello";

        ser2.text1 = "salam";
        ser2.text2 = "hello";
        services.clear();
        services.add(ser);
        services.add(ser2);


        binding = FragmentServicesBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        adapter = new ServicesAdapter(getContext() ,services);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.recycler.setAdapter(adapter);
        binding.recycler.setLayoutManager(layoutManager);
        binding.recycler.setHasFixedSize(true);

        return root;



//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
    }
}