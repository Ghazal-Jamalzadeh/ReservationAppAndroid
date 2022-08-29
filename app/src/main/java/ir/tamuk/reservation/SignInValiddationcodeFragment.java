package ir.tamuk.reservation;

import static androidx.navigation.Navigation.findNavController;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Timer;
import java.util.TimerTask;

import ir.tamuk.reservation.databinding.FragmentSignInValiddationcodeBinding;

public class SignInValiddationcodeFragment extends Fragment {
    private FragmentSignInValiddationcodeBinding  binding;

    NotificationManager NM;

    Timer t;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignInValiddationcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        SharedPreferences prefs = getActivity().getSharedPreferences("NumberPhone", Context.MODE_PRIVATE);
        String numb = prefs.getString("number", null);//"No name defined" is the default value.
        binding.text.setText(numb);


        binding.acceptButtonSigning.setOnClickListener(view -> {
            t.cancel();
            Navigation.findNavController(view).navigate(R.id.action_signInValiddationcodeFragment_to_factorFragment);
        });


        binding.one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                        if (binding.one.getText().length() == 1){
                            binding.two.requestFocus();

                        }










            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });
        binding.two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.two.getText().length() == 1){
                    binding.three.requestFocus();

                }
                if (binding.two.getText().length() == 0){
                    binding.one.requestFocus();

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.three.getText().length() == 1){
                    binding.four.requestFocus();
                }
                if (binding.three.getText().length() == 0){
                    binding.two.requestFocus();

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.four.getText().length() == 0){
                    binding.three.requestFocus();

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation a= AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
                binding.image.startAnimation(a);
                binding.textTimer.setText("120");
                binding.one.getText().clear();
                binding.two.getText().clear();
                binding.three.getText().clear();
                binding.four.getText().clear();

            }
        });

        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(() -> {
                        binding.textTimer.setText(String.valueOf(Integer.parseInt(binding.textTimer.getText().toString())-1));
                        if(Integer.parseInt(binding.textTimer.getText().toString())-1 == 0){
                            t.cancel();
                        }
                    });
                }catch (Exception e){
                    t.cancel();
                }

            }
        },1000,1000);




        binding.editing.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                t.cancel();
                findNavController(view).popBackStack();

            }
        });
        return root;
    }



    @Override
    public void onResume() {
        super.onResume();

    }





}