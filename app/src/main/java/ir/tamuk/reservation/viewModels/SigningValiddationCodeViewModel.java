package ir.tamuk.reservation.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.models.ResponseValidateCode;
import ir.tamuk.reservation.repository.SigningRepository;
import ir.tamuk.reservation.repository.SigningValiddationCodeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigningValiddationCodeViewModel extends ViewModel {

    public SigningValiddationCodeRepository repository = new SigningValiddationCodeRepository();

    public MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>() ;

    public void callReceiveActivationCode(BodySendActivationCode body){
        //CallApi
        Call<ResponseValidateCode> call = repository.callReceiveActivationCode(body);
        //Response
        call.enqueue(new Callback<ResponseValidateCode>() {
            @Override
            public void onResponse(Call<ResponseValidateCode> call, Response<ResponseValidateCode> response) {


                if (response.body() != null){
                Log.d("KIA", "onResponse: "+ response.body().status);
                    if (response.body().status == 200){
                        isSuccessLiveData.setValue(true);
                    }else {

                        isSuccessLiveData.setValue(false);
                        errorMessageLiveData.setValue(response.body().message);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValidateCode> call, Throwable t) {

                Log.d("ghazal", "onFailer: "+ t.getMessage());
                isSuccessLiveData.setValue(false);
                errorMessageLiveData.setValue(t.getMessage());
            }
        });
    }

}
