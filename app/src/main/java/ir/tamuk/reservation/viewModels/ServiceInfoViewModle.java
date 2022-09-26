package ir.tamuk.reservation.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;

import ir.tamuk.reservation.models.ResponseGetServices;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.models.ServiceData;
import ir.tamuk.reservation.repository.GetServices1Repository;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceInfoViewModle extends ViewModel {
    public MutableLiveData<ArrayList<ServiceData>> serviceLiveData=new MutableLiveData<ArrayList<ServiceData>>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void getService(String id){
        Call<ResponseGetServices> call= GetServices1Repository.callGetServices1(id);
        loading.setValue(true);
        call.enqueue(new Callback<ResponseGetServices>() {
            @Override
            public void onResponse(Call<ResponseGetServices> call, Response<ResponseGetServices> response) {
                loading.setValue(false);
                if(response.isSuccessful())
                {
                    if(response.body()!=null)
                        if (response.body().status == 200) {
                            ServiceData serviceData = new ServiceData();
                            serviceData.id = response.body().serviceData.id;
                            serviceData.name = response.body().serviceData.name;
                            serviceData.description = response.body().serviceData.description;
                            serviceData.mainPhoto = response.body().serviceData.mainPhoto;
                            loading.setValue(true);
                            ArrayList<ServiceData> serviceDataArrayList = new ArrayList<>();
                            serviceDataArrayList.add(serviceData);
                            serviceLiveData.setValue(serviceDataArrayList);
                        } else {
                            errorMessage.setValue(response.body().message);
                        }
                    else{
                        errorMessage.setValue("خطا در دریافت اطلاعات");
                    }
                }else{

                    try {
                        errorMessage.setValue(Tools.extractErrorBodyMessage(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGetServices> call, Throwable t) {

                loading.setValue(false);
                errorMessage.setValue(t.getMessage());

            }
        });


    }


}
