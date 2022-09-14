package ir.tamuk.reservation.fragments.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentCompleteProfileInfoBinding;
import ir.tamuk.reservation.databinding.FragmentServiceInfoBinding;
import ir.tamuk.reservation.databinding.FragmentSignInValiddationcodeBinding;
import ir.tamuk.reservation.databinding.FragmentSigningBinding;


public class CompleteProfileInfoFragment extends Fragment {
    private FragmentCompleteProfileInfoBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCompleteProfileInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.cancelButtonSigning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).popBackStack(R.id.nav_home, false);
            }
        });
         binding.acceptButtonSigning.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (binding.nameEditComplete.getText().length() !=0 &&
                         binding.lastNameEditComplete.getText().length() !=0) {
                     Navigation.findNavController(getView()).navigate(R.id.action_completeProfileInfoFragment_to_factorFragment);

                 }else {
                     Toast.makeText(getContext(), "فرم کامل کنید", Toast.LENGTH_SHORT).show();

                 }
             }
         });

        return root;
    }
}