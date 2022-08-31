package ir.tamuk.reservation.fragments.ui.signing;

import static androidx.navigation.Navigation.findNavController;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentSignInValiddationcodeBinding;

public class SignInValiddationcodeFragment extends Fragment {

    private FragmentSignInValiddationcodeBinding  binding;
    private Timer t;
    private TimerTask timerTask;

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
        //SharedPreferences for get Number & CodeVerification
        SharedPreferences prefs = getActivity().getSharedPreferences("NumberPhone", Context.MODE_PRIVATE);
        String numb = prefs.getString("number", null);//"No name defined" is the default value.
        String code = prefs.getString("code", null);
        Log.d("KIANOOSH", "Verfi: "+code);
        binding.text.setText(numb);
        //for Keyboard come Up
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        binding.one.requestFocus();
        //Number1 Code
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
        //Number2 Code
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
        //Number3 Code
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
        //Number4 Code
        binding.four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.four.getText().length() == 0){
                    binding.three.requestFocus();

                }

                if (binding.four.getText().length() != 0) {
                    imgr.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String all = binding.one.getText()+binding.two.getText().toString()
                            +binding.three.getText().toString()+binding.four.getText().toString();
                    if (all.equals(code)) {
                        t.cancel();
                        Navigation.findNavController(getView()).navigate(R.id.action_signInValiddationcodeFragment_to_factorFragment);
                        binding.one.getText().clear();
                        binding.two.getText().clear();
                        binding.three.getText().clear();
                        binding.four.getText().clear();
                        Successful();
                    }else{
                        snackBarIconError();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //Action Keyboard Button
        binding.four.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                imgr.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                String all = binding.one.getText()+binding.two.getText().toString()
                        +binding.three.getText().toString()+binding.four.getText().toString();
                if (all.equals(code)){
                    t.cancel();
                    Navigation.findNavController(getView()).navigate(R.id.action_signInValiddationcodeFragment_to_factorFragment);
                    binding.one.getText().clear();
                    binding.two.getText().clear();
                    binding.three.getText().clear();
                    binding.four.getText().clear();
                    Successful();
                    if (all.equals("")){

                    }

                }else {
                    snackBarIconError();
                }
                //do here your stuff f
                return true;
            }
            return false;
        });
        //Accept Button
        binding.acceptButtonSigning.setOnClickListener(view -> {
            String all = binding.one.getText()+binding.two.getText().toString()
                    +binding.three.getText().toString()+binding.four.getText().toString();
            Log.d("KIANOOSH", "VerfCode: "+all);
            Log.d("KIANOOSH", "Verf: "+code);

            if (all.equals(code)){
                t.cancel();
                Navigation.findNavController(view).navigate(R.id.action_signInValiddationcodeFragment_to_factorFragment);
                binding.one.getText().clear();
                binding.two.getText().clear();
                binding.three.getText().clear();
                binding.four.getText().clear();
                Successful();
                if (all.equals("")){

                }

            }else{
                snackBarIconError();
            }
        });
        //Refresh Icon
        binding.image.setOnClickListener(view -> {
            if(binding.textTimer.getText().equals("1")){
            Animation a= AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
            binding.image.startAnimation(a);
            binding.textTimer.setText("120");
            binding.textTimer.getText().toString();

            binding.one.getText().clear();
            binding.two.getText().clear();
            binding.three.getText().clear();
            binding.four.getText().clear();
            //Timer
                t = new Timer();
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            getActivity().runOnUiThread(() -> {
                                binding.textTimer.setText(String.valueOf(Integer.parseInt(binding.textTimer.getText().toString()) - 1));
                                if (Integer.parseInt(binding.textTimer.getText().toString()) - 1 == 0) {
                                    t.cancel();
                                }
                            });
                        } catch (Exception e) {
                            t.cancel();
                        }

                    }
                };
                t.schedule(timerTask,1000,1000);

            }


        });
        //Timer
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(() -> {
                        binding.textTimer.setText(String.valueOf(Integer.parseInt(binding.textTimer.getText().toString()) - 1));
                        if (Integer.parseInt(binding.textTimer.getText().toString()) - 1 == 0) {
                            t.cancel();
                        }
                    });
                } catch (Exception e) {
                    t.cancel();
                }

            }
        };
        t = new Timer();
        t.schedule(timerTask, 1000, 1000);

        //back Button (Edit Number Phone)
        binding.editing.setOnClickListener(view -> {
            t.cancel();
            findNavController(view).popBackStack();

        });


        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    //Error SnackBar
    private void snackBarIconError() {
        final Snackbar snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_layout, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText("کد تایید اشتباه است");
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_baseline_perm_device_information_24);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(getResources().getColor(R.color.red));
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }
    //successful SnackBAr
    private void Successful() {
        final Snackbar snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_layout, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText("با موفقیت وارد شدید");
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_baseline_perm_device_information_24);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(getResources().getColor(R.color.main));
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }






}