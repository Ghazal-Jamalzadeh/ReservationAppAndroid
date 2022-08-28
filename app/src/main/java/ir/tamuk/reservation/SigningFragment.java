package ir.tamuk.reservation;

import static androidx.navigation.Navigation.findNavController;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import ir.tamuk.reservation.databinding.FragmentReservationBinding;
import ir.tamuk.reservation.databinding.FragmentSigningBinding;
import ir.tamuk.reservation.fragments.ui.reservation.ReservationViewModel;

public class SigningFragment extends Fragment {

    private FragmentSigningBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ReservationViewModel galleryViewModel =
                new ViewModelProvider(this).get(ReservationViewModel.class);
        binding = FragmentSigningBinding.inflate(inflater, container, false);
        View root = binding.getRoot();





        binding.cancelButtonSigning.setOnClickListener(view -> {
            Navigation.findNavController(view).popBackStack();
        });

        binding.acceptButtonSigning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    findNavController(view).navigate(R.id.action_signingFragment_to_signInValiddationcodeFragment);

            }
        });





        //editText mobile
        binding.mobileEditTextSigning.setOnClickListener(view -> {
            //input Mobile number Pattern
            if (binding.mobileEditTextSigning.getText().length() == 11){
                Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
                binding.mobileEditTextSigning.setTextColor(Color.WHITE);


            }else{
                //EditText Field error enable
                binding.textField.setErrorEnabled(true);
                binding.textField.setBoxStrokeErrorColor(ColorStateList.valueOf(Color.RED));
                binding.textField.setErrorIconDrawable(R.drawable.ic_baseline_cancel_24);
                binding.mobileEditTextSigning.setTextColor(Color.RED);
                binding.textField.setError("شماره اشتباه است");
                //delete error icon
                binding.textField.setErrorIconOnClickListener(view1 -> {
                    binding.mobileEditTextSigning.getText().clear();
                    binding.textField.setErrorEnabled(false);
                    binding.mobileEditTextSigning.setTextColor(Color.WHITE);


                });
                //if changed number error false
                binding.mobileEditTextSigning.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (binding.mobileEditTextSigning.getText().toString().trim().length() > 0) {
                            binding.textField.setErrorEnabled(false);
                            binding.mobileEditTextSigning.setTextColor(Color.WHITE);

                        }
                    }
                });

            }
        });

        return root;
    }
}