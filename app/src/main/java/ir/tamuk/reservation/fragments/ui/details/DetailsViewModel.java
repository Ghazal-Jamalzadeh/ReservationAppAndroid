package ir.tamuk.reservation.fragments.ui.details;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.models.ResponseGetMyProfile;
import ir.tamuk.reservation.models.ResponseGetServices;
import ir.tamuk.reservation.models.ServiceData;
import ir.tamuk.reservation.repository.DetailsRepository;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.TokenManager;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsViewModel extends ViewModel{

    private DetailsRepository repository = new DetailsRepository() ;
    public MutableLiveData<ServiceData> serviceDetailLiveData = new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>() ;

    public void getDetail(Context context , String id){

        if (Connectivity.isConnected(context)) {

            loading.setValue(true);

            Call<ResponseGetServices> call = repository.callGetServiceDetail(id) ;
            call.enqueue(new Callback<ResponseGetServices>() {
                @Override
                public void onResponse(Call<ResponseGetServices> call, Response<ResponseGetServices> response) {

                    loading.setValue(false);

                    if(response.isSuccessful())
                    {
                        if(response.body()!=null)
                        {
                            if(response.body().status == 200)
                            {
                                serviceDetailLiveData.setValue(response.body().serviceData);
                            }else{
                                errorMessageLiveData.setValue(response.body().message);
                            }
                        }else{
                            errorMessageLiveData.setValue(context.getString(R.string.null_response_body_error));
                        }
                    }else{

                        try {
                            errorMessageLiveData.setValue(Tools.extractErrorBodyMessage(response.errorBody().string()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetServices> call, Throwable t) {

                    loading.setValue(false);
                    errorMessageLiveData.setValue(t.getMessage());

                }
            });

        } else {
            errorMessageLiveData.setValue(context.getString(R.string.internet_not_connected_error));
            loading.setValue(false);
        }
    }

}
