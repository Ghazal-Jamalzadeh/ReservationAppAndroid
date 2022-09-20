package ir.tamuk.reservation.fragments.ui.signing;

import static androidx.navigation.Navigation.findNavController;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentSignInValiddationcodeBinding;
import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.models.ResponseValidateCode;
import ir.tamuk.reservation.repository.SigningValiddationCodeRepository;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.Tools;
import ir.tamuk.reservation.viewModels.SigningValiddationCodeViewModel;
import ir.tamuk.reservation.viewModels.SigningViewModel;
import retrofit2.Response;

public class SignInValiddationcodeFragment extends Fragment {

    private FragmentSignInValiddationcodeBinding  binding;

    private BodySendActivationCode body = new BodySendActivationCode();
    private Response<ResponseValidateCode> responce;
    private SigningValiddationCodeViewModel signingViewModel;
    private ir.tamuk.reservation.utils.Timer timer = new ir.tamuk.reservation.utils.Timer();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        signingViewModel =  new ViewModelProvider(this).get(SigningValiddationCodeViewModel.class);
        binding = FragmentSignInValiddationcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //numberPhone
        binding.text.setText(getArguments().getString("number"));

        VerferiEditText();
        allButtons();
        backPress();
        timerDo();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                timer.cancelTimer();
                timer.stopTimer(binding.textTimer);
                Navigation.findNavController(requireView()).popBackStack();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        return root;}


    @Override
    public void onResume() {
        super.onResume();


    }

    //edittext ValidationCode
    public void VerferiEditText(){

        //Keyboard Come Up
        Tools.keyboardPopUp(getActivity());
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

                if (binding.four.getText().length() == 1){
                    binding.five.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //Number5 Code
        binding.five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.five.getText().length() == 0){
                    binding.four.requestFocus();

                }
                if (binding.five.getText().length() == 1){
                    binding.six.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //Number6 Code
        binding.six.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.six.getText().length() == 0){
                    binding.five.requestFocus();

                }

                if (binding.six.getText().length() != 0) {


                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    //Buttons /accept, image, editing/
    public void allButtons(){

        //Action Keyboard Button
        binding.acceptButtonSigning.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                String all = binding.one.getText()+binding.two.getText().toString()
                        +binding.three.getText().toString()+binding.four.getText().toString();
                //do here your stuff f
                return true;
            }
            return false;
        });
        //Accept Button
        binding.acceptButtonSigning.setOnClickListener(view -> {
            String all = binding.one.getText()+binding.two.getText().toString()
                    +binding.three.getText().toString()+binding.four.getText().toString()
                    +binding.five.getText().toString()+binding.six.getText().toString();
            Log.d("KIA", "Code: "+all);

            String mobile = getArguments().getString("number");
            Log.d("KIA", "onCreateView: "+mobile);
            body.mobile = mobile;
            body.code = all;
            signingViewModel.callReceiveActivationCode(body, getContext());
            binding.progressCircularSigning.setVisibility(View.VISIBLE);
            binding.acceptButtonSigning.setTextColor(Color.WHITE);

            signingViewModel.isSuccessLiveData.observe(getViewLifecycleOwner(), aBoolean -> {
                if (aBoolean){
                    //navigate to next frg
                    Log.d(Constants.TAG_KIA, "isSuccessLiveData: "+aBoolean);
                    if (Tools.checkDestination(view, R.id.signInValiddationcodeFragment)) {

                        signingViewModel.isLogin.observe(getViewLifecycleOwner(), aBoolean1 -> {
                            if (aBoolean1){
                                Log.d(Constants.TAG_KIA, "if: "+aBoolean1);
                                getViewModelStore().clear();
                                Navigation.findNavController(getView()).popBackStack() ;
                                Navigation.findNavController(getView())
                                        .navigate(R.id.action_signInValiddationcodeFragment_to_completeProfileInfoFragment);

                            }else{
                                Log.d(Constants.TAG_KIA, "else: "+aBoolean1);
                                getViewModelStore().clear();
                                Navigation.findNavController(getView()).popBackStack() ;
                                Navigation.findNavController(getView())
                                        .navigate(R.id.action_to_factorFragment);
                            }
                        });
                    }

                    timer.cancelTimer();
                }
            });

            signingViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
//                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    Snackbar.make(getView(), s, Toast.LENGTH_SHORT).show();
                }
            });

        });

        binding.image.setOnClickListener(view -> {

            if (timer.updateTimer(binding.textTimer).equals("0:00")) {
                timer.cancelTimer();
                timer.stopTimer(binding.textTimer);
                Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);
                binding.image.startAnimation(a);

            }

        });

        binding.editing.setOnClickListener(view -> {
            timer.cancelTimer();
            findNavController(view).popBackStack();

        });
    }

    //onBackPress
    public void backPress(){


    }

    //Timer
    public void timerDo(){
        timer.updateTimer(binding.textTimer);
        Log.d(Constants.TAG_KIA, "timerDo: ->" + timer.updateTimer(binding.textTimer));
        timer.startTimer(binding.textTimer);

    }

}