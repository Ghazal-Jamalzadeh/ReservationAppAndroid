package ir.tamuk.reservation.viewModels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.repository.SigningRepository;
import ir.tamuk.reservation.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigningViewModel extends ViewModel {
//    public MutableLiveData<ResponseSendActivationCode> sendActivationLiveData;
    public SigningRepository repository = new SigningRepository();

    public MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>() ;
    public MutableLiveData<Boolean> isProgress = new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>() ;



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

    public void callSendActivationCode(BodySendActivationCode body, Context context){
        //CallApi
        Call<ResponseSendActivationCode> call = repository.callSendActivationCode(body);
        //Response
        call.enqueue(new Callback<ResponseSendActivationCode>() {
            @Override
            public void onResponse(Call<ResponseSendActivationCode> call, Response<ResponseSendActivationCode> response) {
                if (response.body() != null){
                    if (response.body().status == 200){
                        isSuccessLiveData.setValue(true);
                        isProgress.setValue(false);
                    }else {
                        isSuccessLiveData.setValue(false);
                        errorMessageLiveData.setValue(response.body().message);
                        Toast.makeText(context, response.body().message, Toast.LENGTH_SHORT).show();


                    }
                }else {
                    Toast.makeText(context, "asa", Toast.LENGTH_SHORT).show();
                    isProgress.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseSendActivationCode> call, Throwable t) {

                Log.d("VIEWMODELCALLTEST", "onFailer: "+ t.getMessage());
                isSuccessLiveData.setValue(false);
                errorMessageLiveData.setValue(t.getMessage());
            }
        });
    }


}
