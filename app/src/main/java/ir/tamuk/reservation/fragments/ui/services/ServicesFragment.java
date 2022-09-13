package ir.tamuk.reservation.fragments.ui.services;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.models.Services;
import ir.tamuk.reservation.adapter.ServicesAdapter;
import ir.tamuk.reservation.fragments.ui.reservation.ReservationViewModel;
import ir.tamuk.reservation.databinding.FragmentServicesBinding;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.viewModels.HomeViewModel;
import ir.tamuk.reservation.viewModels.ServicesViewModel;


public class ServicesFragment extends Fragment {

    ArrayList<Service> services = new ArrayList<>() ;
    private FragmentServicesBinding binding;
    Services ser = new Services();
    Services ser2 = new Services();
    private ServicesAdapter adapter ;
    private ServicesViewModel servicesViewModel;



    public View onCreateView(@NonNull LayoutInflater  inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentServicesBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        servicesViewModel = new ViewModelProvider(this).get(ServicesViewModel.class);



        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        //call api
        callSearchServicesApi();
        servicesViewModel.loading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                binding.swipeRefreshLayout.setRefreshing(b);
                if (!b) {
                    binding.serviceContainer.setVisibility(View.VISIBLE);
                }

            }
        });

//        ser.text1 = "salam";
//        ser.text2 = "hello";
//
//        ser2.text1 = "salam";
//        ser2.text2 = "hello";
//        services.clear();
//        services.add(ser);
//        services.add(ser2);



//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);




        servicesViewModel.servicesLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<Service>>() {
            @Override
            public void onChanged(ArrayList<Service> services) {
                Log.d("ghazal", "size : " + services.size());
                adapter = new ServicesAdapter(getContext() ,services);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                binding.recycler.setAdapter(adapter);
                binding.recycler.setLayoutManager(layoutManager);
                binding.recycler.setHasFixedSize(true);

                adapter.notifyDataSetChanged();

            }
        });


        return root;


//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
    }

    private void callSearchServicesApi(){
        if (Connectivity.isConnected(getContext())) {
            servicesViewModel.searchServices(1,20,"");
        } else {
            Toast.makeText(getContext(), "اینترنت وصل نیس ", Toast.LENGTH_SHORT).show();
        }
    }
}