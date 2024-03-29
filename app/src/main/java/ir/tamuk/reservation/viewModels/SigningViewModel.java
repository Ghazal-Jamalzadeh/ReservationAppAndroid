package ir.tamuk.reservation.viewModels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.telephony.CellSignalStrength;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.repository.SigningRepository;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigningViewModel extends ViewModel {

    public SigningRepository repository = new SigningRepository();
    public MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>();
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();


//    public LiveData<ResponseSendActivationCode> getActivationCodeResponse(BodySendActivationCode body){
//
//            if(sendActivationLiveData == null){
//                sendActivationLiveData = new MutableLiveData<>();
//                //callApi
//                callSendActivationCode(body);
//
//            }
//            return sendActivationLiveData;
//    }

    public void callSendActivationCode(BodySendActivationCode body) {
//        CallApi
        Call<ResponseSendActivationCode> call = repository.callSendActivationCode(body);
        errorMessageLiveData = new MutableLiveData<>() ;
        //Response
        call.enqueue(new Callback<ResponseSendActivationCode>() {
            @Override
            public void onResponse(Call<ResponseSendActivationCode> call, Response<ResponseSendActivationCode> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        if (response.body().status == 200) {

                              isSuccessLiveData.setValue(true);

                        } else {

                            errorMessageLiveData.postValue(response.body().message);

                        }
                    }


                } else {
                    try {

                        String err = Tools.extractErrorBodyMessage(response.errorBody().string()) ;

                        errorMessageLiveData.setValue(err);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }

            @Override
            public void onFailure(Call<ResponseSendActivationCode> call, Throwable t) {

                errorMessageLiveData.postValue(t.getMessage());

            }
        });
    }

}
