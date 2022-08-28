package ir.tamuk.reservation;

import static androidx.navigation.Navigation.findNavController;
import static androidx.navigation.Navigation.setViewNavController;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ir.tamuk.reservation.databinding.FragmentReservationBinding;
import ir.tamuk.reservation.databinding.FragmentSignInValiddationcodeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInValiddationcodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInValiddationcodeFragment extends Fragment {
     private FragmentSignInValiddationcodeBinding  binding;

     @NonNull
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public SignInValiddationcodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInValiddationcodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInValiddationcodeFragment newInstance(String param1, String param2) {
        SignInValiddationcodeFragment fragment = new SignInValiddationcodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignInValiddationcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.codeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                switch (charSequence.length()){
                    case 0:
                        binding.edit1.setText("");
                        break;
                    case 1:
                        binding.edit1.setText(String.valueOf(charSequence.charAt(0)));
                        binding.edit2.setText("");
                        break;
                    case 2:
                        binding.edit2.setText(String.valueOf(charSequence.charAt(1)));
                        binding.edit3.setText("");
                        break;
                    case 3:
                        binding.edit3.setText(String.valueOf(charSequence.charAt(2)));
                        binding.edit4.setText("");
                        break;
                    case 4:
                        binding.edit4.setText(String.valueOf(charSequence.charAt(3)));
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        binding.flashe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation a= AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
                binding.image.startAnimation(a);
                binding.textTimer.setText("120");

            }
        });

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.textTimer.setText(String.valueOf(Integer.parseInt(binding.textTimer.getText().toString())-1));
                        if(Integer.parseInt(binding.textTimer.getText().toString())-1 == 0){
                            t.cancel();
                        }
                    }
                });

            }
        },1000,1000);




        binding.editing.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){
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