package ir.tamuk.reservation.viewModels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseValidateCode;
import ir.tamuk.reservation.repository.SigningValiddationCodeRepository;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.SharedPerferencesClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashViewModel extends AndroidViewModel {

//    public SigningValiddationCodeRepository repository = new SigningValiddationCodeRepository();
//    public MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>() ;
//    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>() ;
//
    public SplashViewModel(@NonNull Application application) {
        super(application);
    }
//
//    public void callReceiveActivationCode(BodySendActivationCode body, Context context){
//        //CallApi
//        Call<ResponseValidateCode> call = repository.callReceiveActivationCode(body);
//        //Response
//        call.enqueue(new Callback<ResponseValidateCode>() {
//            @Override
//            public void onResponse(Call<ResponseValidateCode> call, Response<ResponseValidateCode> response) {
//
//
//                if (response.body() != null){
//                    Log.d("KIA", "onResponse: "+ response.body().status);
//                    if (response.body().status == 200){
//                        isSuccessLiveData.setValue(true);
//                        if (response.body().data.user.userStatus == 0){
//
//                        }else {
//                            SharedPerferencesClass.setPrefsAccess(context
//                                    , response.body().data.tokens.accessToken);
//                            SharedPerferencesClass.setPrefsRefresh(context
//                                    , response.body().data.tokens.refreshToken);
//
//                            Log.d(Constants.TAG_KIA, "onResponse: " + response.body().data.tokens.accessToken);
//                            Log.d(Constants.TAG_KIA, "onResponse: " + response.body().data.tokens.refreshToken);
//                        }
//
//                    }else {
//
//                        isSuccessLiveData.setValue(false);
//                        errorMessageLiveData.setValue(response.body().message);
//
//
//
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseValidateCode> call, Throwable t) {
//
//                Log.d("ghazal", "onFailer: "+ t.getMessage());
//                isSuccessLiveData.setValue(false);
//                errorMessageLiveData.setValue(t.getMessage());
//            }
//        });
//    }
}
