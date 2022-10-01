package ir.tamuk.reservation.fragments.ui.services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentServiceInfoBinding;
import ir.tamuk.reservation.databinding.FragmentServicesBinding;
import ir.tamuk.reservation.models.ServiceData;
import ir.tamuk.reservation.models.Services;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.viewModels.ServiceInfoViewModle;
import ir.tamuk.reservation.viewModels.ServicesViewModel;


public class ServiceInfoFragment extends Fragment {
    private FragmentServiceInfoBinding binding;
    ArrayList<ServiceData> serviceData = new ArrayList<>();
    private ServiceInfoViewModle serviceInfoViewModle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceInfoBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        serviceInfoViewModle=new ViewModelProvider(this).get(ServiceInfoViewModle.class);

        onBackPress();

        serviceInfoViewModle.loading.observe(getViewLifecycleOwner(),aBoolean -> {
            if (aBoolean){
                binding.progressbar.setVisibility(View.INVISIBLE);
            }else {

                binding.progressbar.setVisibility(View.VISIBLE);
            }

        });

        binding.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
                Navigation.findNavController(view).navigate(R.id.nav_reservation);
                Log.d("MANSOUR", "onClick: "+"button");

            }
        });
        binding.image.setOnClickListener(view ->

                Navigation.findNavController(view).popBackStack()

        );

        Handler h = new Handler();
        Runnable r = () -> {
            callGetServicesApi();

        };h.postDelayed(r, 1000);


//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//                Navigation.findNavController(getView()).popBackStack(R.id.nav_reservation, false);
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        String a;

        int id = getArguments().getInt("id");
        Log.d("MANSOUR", "onCreateView: "+ id);


        // Inflate the layout for this fragment
        return root;
    }
    private void callGetServicesApi(){
        if (Connectivity.isConnected(getContext())) {
            String idService= getArguments().getString("serviceId");
            serviceInfoViewModle.getService(idService);

            serviceInfoViewModle.serviceLiveData.observe(getViewLifecycleOwner(), serviceData -> {
                binding.titleServicesInfo.setText(serviceData.get(0).name);
                binding.descriptionServicesInfo.setText(serviceData.get(0).description);

            });

        } else {
            Toast.makeText(getContext(), "اینترنت وصل نیس ", Toast.LENGTH_SHORT).show();
        }
    }

    //onBack
    public void onBackPress(){


        //BackPress Action (for back to HomeFragment)
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                getViewModelStore().clear();
                Navigation.findNavController(getView()).popBackStack();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()

    }
}