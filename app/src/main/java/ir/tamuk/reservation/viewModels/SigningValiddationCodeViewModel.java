package ir.tamuk.reservation.viewModels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseValidateCode;
import ir.tamuk.reservation.repository.SigningValiddationCodeRepository;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.TokenManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigningValiddationCodeViewModel extends AndroidViewModel {

    public SigningValiddationCodeRepository repository = new SigningValiddationCodeRepository();
    public MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>() ;
    public MutableLiveData<Boolean> isLogin = new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>() ;

    public SigningValiddationCodeViewModel(@NonNull Application application) {
        super(application);
    }

    public void callReceiveActivationCode(BodySendActivationCode body, Context context){
        //CallApi
        Call<ResponseValidateCode> call = repository.callReceiveActivationCode(body);
        //Response
        call.enqueue(new Callback<ResponseValidateCode>() {
            @Override
            public void onResponse(Call<ResponseValidateCode> call, Response<ResponseValidateCode> response) {

                if (response.body() != null){
                Log.d(Constants.TAG_KIA, "onResponseSVC: ->"+ response.body().status);
                    if (response.body().status == 200){
                        isSuccessLiveData.setValue(true);
                        Log.d(Constants.TAG_KIA, "user_status: ->" + response.body().data.user.userStatus);

                        TokenManager.setAccessToken( context
                                , "Bearer " + response.body().data.tokens.accessToken);
                        TokenManager.setRefreshToken(context
                                , response.body().data.tokens.refreshToken);

                        Log.d(Constants.TAG_KIA, "accessToken: ->"+ response.body().data.tokens.accessToken);
                        Log.d(Constants.TAG_KIA, "refreshToken: ->"+ response.body().data.tokens.refreshToken);

                    }else {

                        isSuccessLiveData.setValue(false);
                        errorMessageLiveData.setValue(response.body().message);

                    }

                    if (response.body().data.user.userStatus == 0 ){
                        isLogin.setValue(true);
                        Log.d(Constants.TAG_KIA, "user_status true: ->" + response.body().data.user.userStatus);

                    }else {
                        isLogin.setValue(false);
                        Log.d(Constants.TAG_KIA, "user_status false: ->" + response.body().data.user.userStatus);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseValidateCode> call, Throwable t) {

                Log.d(Constants.TAG_KIA, "onFailerSVC: ->"+ t.getMessage());
                isSuccessLiveData.setValue(false);
                errorMessageLiveData.setValue(t.getMessage());
            }
        });
    }

}
