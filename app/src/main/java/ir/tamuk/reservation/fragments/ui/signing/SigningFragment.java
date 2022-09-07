package ir.tamuk.reservation.fragments.ui.signing;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentSigningBinding;
import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.utils.Tools;
import ir.tamuk.reservation.viewModels.SigningViewModel;
import retrofit2.Response;

public class SigningFragment extends Fragment {

    private FragmentSigningBinding binding;

    private BodySendActivationCode body = new BodySendActivationCode();
    private Response<ResponseSendActivationCode> responce;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SigningViewModel signingViewModel =  new ViewModelProvider(this).get(SigningViewModel.class);
        binding = FragmentSigningBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Keyboard come up
        Tools.keyboardPopUp(getActivity());
        binding.mobileEditTextSigning.requestFocus();

        //Cancel Button
        binding.cancelButtonSigning.setOnClickListener(view -> {
            Navigation.findNavController(view).popBackStack();
        });
        //Accept Button
        binding.acceptButtonSigning.setOnClickListener(view -> {

            //editText
            if (binding.mobileEditTextSigning.getText().length() == 11) {
                binding.mobileEditTextSigning.setTextColor(Color.WHITE);
                body.mobile = binding.mobileEditTextSigning.getText().toString();

                signingViewModel.callSendActivationCode(body);
                signingViewModel.isSuccessLiveData.observe(getViewLifecycleOwner(), aBoolean -> {
                    if (aBoolean){
                        //navigate to next frg
                        Log.d("KKI", "onCreateView: "+aBoolean);
                        Bundle bundle = new Bundle();
                        bundle.clear();
                        bundle.putString("number", binding.mobileEditTextSigning.getText().toString());
                        Navigation.findNavController(getView()).navigate(R.id.action_signingFragment_to_signInValiddationcodeFragment, bundle);
                    }
                });
                signingViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    }
                });


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
        });

        return root;
    }

    public  void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void removePhoneKeypad(Fragment fragment) {
        InputMethodManager inputManager = (InputMethodManager) fragment.getView()
                .getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = fragment.getView().getWindowToken();
        inputManager.hideSoftInputFromWindow(binder,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}