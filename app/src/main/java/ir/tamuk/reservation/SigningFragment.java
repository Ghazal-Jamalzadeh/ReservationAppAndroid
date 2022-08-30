package ir.tamuk.reservation;

import static android.content.Context.MODE_PRIVATE;
import static androidx.navigation.Navigation.findNavController;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.tamuk.reservation.databinding.FragmentSigningBinding;
import ir.tamuk.reservation.fragments.ui.reservation.ReservationViewModel;
import ir.tamuk.reservation.fragments.ui.reservation.notification.Constants;
import ir.tamuk.reservation.fragments.ui.reservation.notification.MyNotificationManager;

public class SigningFragment extends Fragment {

    private FragmentSigningBinding binding;
    String result = "";


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
        ShuffleNumb();


        binding.cancelButtonSigning.setOnClickListener(view -> {
            Navigation.findNavController(view).popBackStack();
        });

        binding.acceptButtonSigning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //editText
                if (binding.mobileEditTextSigning.getText().length() == 11) {
                    binding.mobileEditTextSigning.setTextColor(Color.WHITE);

                    String x = binding.mobileEditTextSigning.getText().toString();
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("NumberPhone", MODE_PRIVATE).edit();
                    editor.remove("NumberPhone").apply();
                    editor.putString("number", x);
                    editor.putString("code", result);
                    Log.d("KIAN", "onClick: " + result);
                    editor.apply();

                    Notif();

                    findNavController(view).navigate(R.id.action_signingFragment_to_signInValiddationcodeFragment);

                } else {
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
//


            }
        });


        //editText mobile
        binding.mobileEditTextSigning.setOnClickListener(view -> {
            //input Mobile number Pattern
            if (binding.mobileEditTextSigning.getText().length() == 11) {
                binding.mobileEditTextSigning.setTextColor(Color.WHITE);

                String x = binding.mobileEditTextSigning.getText().toString();
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("NumberPhone", MODE_PRIVATE).edit();
                editor.remove("NumberPhone").apply();
                editor.putString("number", x);
                editor.putString("code", result);
                editor.apply();
                Notif();

                findNavController(view).navigate(R.id.action_signingFragment_to_signInValiddationcodeFragment);

            } else {
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

    public void Notif() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        /*
         * Displaying a notification locally
         */
        MyNotificationManager.getInstance(getContext()).displayNotification("کد تایید شماره", result);
    }

    public void ShuffleNumb() {
        int max = 9;
        int min = 1;
        int range = max - min + 1;
        int rand = 0;
        List<String> myAnswerList = new ArrayList<>();

        // generate random numbers within 1 to 10
        for (int i = 0; i < 4; i++) {
            rand = (int) (Math.random() * range) + min;
            String randS = String.valueOf(rand);
            myAnswerList.add(randS);
            // Output is different everytime this code is executed

        }

        Log.d("KIA", "ShuffleNumb: "+ myAnswerList);
        result = myAnswerList.get(0)+myAnswerList.get(1)+
                myAnswerList.get(2)+myAnswerList.get(3);
        Log.d("KIA", "ShuffleNumb: "+result);

    }
}
