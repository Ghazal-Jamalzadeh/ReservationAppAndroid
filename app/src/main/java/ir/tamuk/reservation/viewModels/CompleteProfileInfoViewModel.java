package ir.tamuk.reservation.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.ResponseSubmitCustomer;
import ir.tamuk.reservation.repository.CompleteProfileInfoRepository;
import ir.tamuk.reservation.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  CompleteProfileInfoViewModel extends ViewModel {

    public CompleteProfileInfoRepository repository = new CompleteProfileInfoRepository();
    public MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>() ;


    public void callSendSubmit(BodySubmitCustomer body, String token) {
        //CallApi
        Call<ResponseSubmitCustomer> call = repository.callSubmitCustomer(body, token);
        //Response
        call.enqueue(new Callback<ResponseSubmitCustomer>() {
            @Override
            public void onResponse(Call<ResponseSubmitCustomer> call, Response<ResponseSubmitCustomer> response) {

                if (response.body() != null) {
                    Log.d(Constants.TAG_KIA, "onResponseSVC: ->" + response.body().status);
                    if (response.body().status == 200) {
                        isSuccessLiveData.setValue(true);

                    } else {

                        isSuccessLiveData.setValue(false);
                        errorMessageLiveData.setValue(response.body().message);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSubmitCustomer> call, Throwable t) {

                Log.d(Constants.TAG_KIA, "onFailerSVC: ->" + t.getMessage());
                isSuccessLiveData.setValue(false);
                errorMessageLiveData.setValue(t.getMessage());
            }
        });
    }
}
