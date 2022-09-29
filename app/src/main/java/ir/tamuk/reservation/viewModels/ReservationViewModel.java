package ir.tamuk.reservation.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;

import ir.tamuk.reservation.models.BodyFactor;
import ir.tamuk.reservation.models.FreeTimeAm;
import ir.tamuk.reservation.models.FreeTimePm;
import ir.tamuk.reservation.models.ResponseFactor;
import ir.tamuk.reservation.models.ResponseReservation;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.repository.FactorRepository;
import ir.tamuk.reservation.repository.ReservationRepository;
import ir.tamuk.reservation.repository.ServicesRepository;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Service>> servicesLiveData = new MutableLiveData<>();
//    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MutableLiveData<ArrayList<String>> freeAm = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> freePm = new MutableLiveData<>();
    public MutableLiveData<String> errorMessageRes = new MutableLiveData<>();
    public MutableLiveData<Boolean> notRes = new MutableLiveData<>();

    public FactorRepository repository = new FactorRepository();
    public MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>() ;
    public MutableLiveData<String> order = new MutableLiveData<>();

    public void getServices (int page , int limit , String categories ) {
        Call<ResponseSearchServices> call = ReservationRepository.callSpinnerServices(page, limit, categories);
//        loading.setValue(true);
        call.enqueue(new Callback<ResponseSearchServices>() {
            @Override
            public void onResponse(Call<ResponseSearchServices> call, Response<ResponseSearchServices> response) {
//                loading.setValue(false);
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        if (response.body().status == 200) {
                            Log.d("ghazal", "onResponse: ");
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
//                loading.setValue(false);
                errorMessage.setValue(t.getMessage());

            }
        });
    }

    public void getReservations (String time, String service ) {
        Call<ResponseReservation> call = ReservationRepository.getReservationsApi(time, service);
        call.enqueue(new Callback<ResponseReservation>() {
            @Override
            public void onResponse(Call<ResponseReservation> call, Response<ResponseReservation> response) {
                if (response.isSuccessful()) {
                    Log.d("rhmn", "isSuccessful: ");
                    if (response.body() != null) {
                        Log.d("rhmn", "!null: ");
                        if (response.body().status == 200) {
                            Log.d("rhmn", "200: ");

                            freeAm.setValue(response.body().data.freeTimeAms);
                            freePm.setValue(response.body().data.freeTimePms);
                            notRes.setValue(false);
                        } else {
                            Log.d("rhmn", "!200: ");
                            errorMessage.setValue(response.body().message);
                            notRes.setValue(true);
                        }
                    }else {
                        Log.d("rhmn", "null: ");
                        errorMessage.setValue("خطا در دریافت اطلاعات");
                    }
                } else {
                    Log.d("rhmn", "!isSuccessful: ");

                    try {
                        errorMessageRes.setValue(Tools.extractErrorBodyMessage(response.errorBody().string()));
                        Log.d("rhmn", "!isSuccessful Try: "+ response.errorBody());
                        notRes.setValue(true);
                    } catch (IOException e) {
                        Log.d("rhmn", "!isSuccessful Catch: "+ e);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseReservation> call, Throwable t) {
//                loading.setValue(false);
                errorMessageRes.setValue(t.getMessage());
                Log.d("rhmn", "onFail: "+t.getMessage());

            }
        });
    }

    public void callFactor(BodyFactor body, String token) {
        //CallApi
        Call<ResponseFactor> call = repository.callFactor(body, token);
        //Response
        call.enqueue(new Callback<ResponseFactor>() {
            @Override
            public void onResponse(Call<ResponseFactor> call, Response<ResponseFactor> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(Constants.TAG_KIA, "onResponseSVC: ->" + response.body().status);
                        if (response.body().status == 200) {
                            Log.d("JALLAD", "View200: ");
                            isSuccessLiveData.setValue(true);
                            order.setValue(response.body().data.id);

                        }
                    }
                } else {
                    try {

                        String err = Tools.extractErrorBodyMessage(response.errorBody().string());
                        Log.d("JALLAD", "!200: ");
                        errorMessageLiveData.setValue(err);
                        isSuccessLiveData.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFactor> call, Throwable t) {

                Log.d("JALLAD", "onFailerSVC: ->" + t.getMessage());
                isSuccessLiveData.setValue(false);
//                errorMessageLiveData.setValue(t.getMessage());
            }
        });
    }
}
