package ir.tamuk.reservation.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.tamuk.reservation.models.BodyFactor;
import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.ResponseFactor;
import ir.tamuk.reservation.models.ResponseSubmitCustomer;
import ir.tamuk.reservation.repository.CompleteProfileInfoRepository;
import ir.tamuk.reservation.repository.FactorRepository;
import ir.tamuk.reservation.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FactorViewModel extends ViewModel {

    public FactorRepository repository = new FactorRepository();
    public MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>() ;
    public MutableLiveData<String> order = new MutableLiveData<>();


    public void callFactor(BodyFactor body, String token) {
        //CallApi
        Call<ResponseFactor> call = repository.callFactor(body, token);
        //Response
        call.enqueue(new Callback<ResponseFactor>() {
            @Override
            public void onResponse(Call<ResponseFactor> call, Response<ResponseFactor> response) {

                if (response.body() != null) {
                    Log.d(Constants.TAG_KIA, "onResponseSVC: ->" + response.body().status);
                    if (response.body().status == 200) {
                        isSuccessLiveData.setValue(true);
                        order.setValue(response.body().data.id);

                    } else {

                        isSuccessLiveData.setValue(false);
                        errorMessageLiveData.setValue(response.body().message);

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFactor> call, Throwable t) {

                Log.d(Constants.TAG_KIA, "onFailerSVC: ->" + t.getMessage());
                isSuccessLiveData.setValue(false);
                errorMessageLiveData.setValue(t.getMessage());
            }
        });
    }
}
