package ir.tamuk.reservation.fragments.ui.signing;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentCompleteProfileInfoBinding;
import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.ResponseSubmitCustomer;
import ir.tamuk.reservation.utils.TokenManager;
import ir.tamuk.reservation.utils.Tools;
import ir.tamuk.reservation.viewModels.CompleteProfileInfoViewModel;
import retrofit2.Response;


public class CompleteProfileInfoFragment extends Fragment {
    private FragmentCompleteProfileInfoBinding binding;
    private BodySubmitCustomer body = new BodySubmitCustomer();
    private CompleteProfileInfoViewModel completeProfileInfoViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        completeProfileInfoViewModel =  new ViewModelProvider(this).get(CompleteProfileInfoViewModel.class);
        binding = FragmentCompleteProfileInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Cancel Button
        binding.cancelButtonSigning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getView()).popBackStack(R.id.nav_home, false);
            }
        });

        //Accept Button
         binding.acceptButtonSigning.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (binding.nameEditComplete.getText().length() >2 &&
                         binding.lastNameEditComplete.getText().length() > 3) {

                     String token = TokenManager.getAccessToken(getContext());
                     body.firstName = binding.nameEditComplete.getText().toString();
                     body.lastName = binding.lastNameEditComplete.getText().toString();
                     completeProfileInfoViewModel.callSendSubmit(body, token);
                     binding.acceptButtonSigning.setTextColor(Color.WHITE);
                     binding.progressCircularSigning.setVisibility(View.VISIBLE);
                     completeProfileInfoViewModel.isSuccessLiveData.observe(getViewLifecycleOwner(), aBoolean -> {
                         if (aBoolean){
                             //navigate to next frg
                             if (Tools.checkDestination(view, R.id.completeProfileInfoFragment)) {
                                 getViewModelStore().clear();
                                 Bundle bundle = new Bundle();
                                 bundle.putString("number", getArguments().getString("number"));
                                 bundle.putString("serviceId", getArguments().getString("serviceId"));
                                 bundle.putString("serviceName", getArguments().getString("serviceName"));
                                 bundle.putString("reserveTime", getArguments().getString("reserveTime"));
                                 bundle.putString("reserveDate", getArguments().getString("reserveDate"));
                                 bundle.putString("reserveDateMiladi", getArguments().getString("reserveDateMiladi"));
                                 bundle.putBoolean("isFactor", getArguments().getBoolean("isFactor"));


                                 Navigation.findNavController(getView())
                                         .navigate(R.id.action_to_factorFragment, bundle);
                             }
                         }else{
                             binding.acceptButtonSigning.setTextColor(getResources().getColor(R.color.backgroundSigning));
                             binding.progressCircularSigning.setVisibility(View.GONE);
                         }
                     });

                     completeProfileInfoViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
                         @Override
                         public void onChanged(String s) {
//                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                             Snackbar.make(getView(), s, Toast.LENGTH_SHORT).show();
                             binding.acceptButtonSigning.setTextColor(getResources().getColor(R.color.backgroundSigning));
                             binding.progressCircularSigning.setVisibility(View.GONE);
                         }
                     });

                 }else {
                     Snackbar.make(getView(),"لطفا فیلد هارا پر کنید" , Toast.LENGTH_SHORT).show();

                 }
             }
         });

        return root;
    }
}