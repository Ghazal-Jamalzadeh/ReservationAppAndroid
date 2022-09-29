package ir.tamuk.reservation.fragments.ui.factor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentFactorBinding;
import ir.tamuk.reservation.models.BodyFactor;
import ir.tamuk.reservation.utils.TokenManager;
import ir.tamuk.reservation.viewModels.FactorViewModel;


public class FactorFragment extends Fragment {

    private FragmentFactorBinding binding;
    private FactorViewModel factorViewModel;
    private BodyFactor bodyFactor = new BodyFactor();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onBackPress();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFactorBinding.inflate(inflater, container, false);
        factorViewModel = new ViewModelProvider(this).get(FactorViewModel.class);
        View root = binding.getRoot();

//        bodyFactor.serviceFactor = getArguments().getString("serviceId");
//        bodyFactor.dateFactor = "2022-09-13T18:30:00";
//
//        factorViewModel.callFactor(bodyFactor, TokenManager.getAccessToken(getContext()));
//        factorViewModel.isSuccessLiveData.observe(getViewLifecycleOwner(), aBoolean -> {
//            if (aBoolean){
//                factorViewModel.order.observe(getViewLifecycleOwner(), order -> {
//                    String s = order;
//                });
//
//            }else{
//                Navigation.findNavController(getView()).popBackStack();
//            }
//        });
//
//
//        factorViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), error -> {
//
//        });
//        binding.phoneTextForm.setText(getArguments().getString("number"));
        binding.serviceTextForm.setText(getArguments().getString("serviceName"));
        binding.timeTextForm.setText(getArguments().getString("reserveTime"));
        binding.dateTextForm.setText(getArguments().getString("reserveDate"));

        buttons();

        return root;
    }

    //OnBack
    public void onBackPress(){


        //BackPress Action (for back to HomeFragment)
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Navigation.findNavController(getView()).popBackStack(R.id.nav_home, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()

    }

    //Buttons /cancel, edit, pay/
    public void buttons(){

        binding.cancelButtonForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).popBackStack(R.id.nav_home, false);
            }
        });

        binding.editButtonForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).popBackStack(R.id.nav_reservation, false);
            }
        });


    }


}