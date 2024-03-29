package ir.tamuk.reservation.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.models.ResponseGetServices;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.models.ServiceData;
import ir.tamuk.reservation.repository.ServicesRepository;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ServiceData>> serviceLiveData = new MutableLiveData<ArrayList<ServiceData>>();
    public MutableLiveData<ArrayList<Service>> servicesLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void searchServices(int page, int limit, String categories) {
        Call<ResponseSearchServices> call = ServicesRepository.callGetServices(page, limit, categories);
        loading.setValue(true);
        call.enqueue(new Callback<ResponseSearchServices>() {
            @Override
            public void onResponse(Call<ResponseSearchServices> call, Response<ResponseSearchServices> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        if (response.body().status == 200) {
                            servicesLiveData.setValue(response.body().data.services);
                        } else {
                            errorMessage.setValue(response.body().message);
                        }
                    else {
                        errorMessage.setValue("خطا در دریافت اطلاعات");
                    }
                } else {

                    try {
                        errorMessage.setValue(Tools.extractErrorBodyMessage(response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSearchServices> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue(t.getMessage());

            }
        });
    }


    public void getService(String id) {
        Call<ResponseGetServices> call = ServicesRepository.callGetServices1(id);
        loading.setValue(true);
        call.enqueue(new Callback<ResponseGetServices>() {
            @Override
            public void onResponse(Call<ResponseGetServices> call, Response<ResponseGetServices> response) {
                loading.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null)
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
                    else {
                        errorMessage.setValue("خطا در دریافت اطلاعات");
                    }
                } else {

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

