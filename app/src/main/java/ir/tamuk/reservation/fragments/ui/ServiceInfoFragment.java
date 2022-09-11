package ir.tamuk.reservation.fragments.ui;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentServiceInfoBinding;
import ir.tamuk.reservation.databinding.FragmentServicesBinding;
import ir.tamuk.reservation.models.Services;


public class ServiceInfoFragment extends Fragment {
    private FragmentServiceInfoBinding binding;
    ArrayList<Services> services = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentServiceInfoBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
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
}