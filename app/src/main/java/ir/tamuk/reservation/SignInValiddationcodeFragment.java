package ir.tamuk.reservation;

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
        String code = prefs.getString("code", null);
        Log.d("KIANOOSH", "Verfi: "+code);
        binding.text.setText(numb);

        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        binding.one.requestFocus();


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
                        SuccsesFull();
                    }else{
                        snackBarIconError();
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        binding.four.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
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
                        SuccsesFull();
                        if (all.equals("")){

                        }

                    }else {
                        snackBarIconError();
                    }
                    //do here your stuff f
                    return true;
                }
                return false;
            }
        });

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
                SuccsesFull();
                if (all.equals("")){

                }

            }else{
                snackBarIconError();

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

    private void SuccsesFull() {
        final Snackbar snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.snackbar_layout, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);

        ((TextView) custom_view.findViewById(R.id.message)).setText("شماره با موفقیت ثبت شد");
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_baseline_perm_device_information_24);
        (custom_view.findViewById(R.id.parent_view)).setBackgroundColor(getResources().getColor(R.color.main));
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }






}