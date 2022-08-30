package ir.tamuk.reservation;

import static androidx.navigation.Navigation.findNavController;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.Timer;
import java.util.TimerTask;

import ir.tamuk.reservation.databinding.FragmentSignInValiddationcodeBinding;

public class SignInValiddationcodeFragment extends Fragment {
    private FragmentSignInValiddationcodeBinding binding;

    NotificationManager NM;

    Timer t;

    TimerTask timerTask;


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


        binding.acceptButtonSigning.setOnClickListener(view -> {
            t.cancel();
            Navigation.findNavController(view).navigate(R.id.action_signInValiddationcodeFragment_to_factorFragment);
        });

        binding.codeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                switch (charSequence.length()) {
                    case 0:
                        binding.edit1.setText("__");
                        break;
                    case 1:
                        binding.edit1.setText(String.valueOf(charSequence.charAt(0)));
                        binding.edit2.setText("__");
                        break;
                    case 2:
                        binding.edit2.setText(String.valueOf(charSequence.charAt(1)));
                        binding.edit3.setText("__");
                        break;
                    case 3:
                        binding.edit3.setText(String.valueOf(charSequence.charAt(2)));
                        binding.edit4.setText("__");
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
                if(binding.textTimer.getText().equals("1")){
                    Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
                    binding.image.startAnimation(a);
                    binding.textTimer.setText("120");
                    binding.textTimer.getText().toString();
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


            }
        });

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


        binding.editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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