package ir.tamuk.reservation.fragments.ui.home;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.models.Category;
import ir.tamuk.reservation.models.ResponseCategoriesList;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.models.Service;
import ir.tamuk.reservation.repository.HomeRepository;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private final Context context ;
    public int tabLastPos =  0 ;
    public MutableLiveData<ArrayList<Category>> categoriesLiveData ;
    public MutableLiveData<ArrayList<Service>> servicesLiveData = new MutableLiveData<>();
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();    }


        public MutableLiveData<ArrayList<Category>> getAllCategories(){

            if(categoriesLiveData == null){
                categoriesLiveData = new MutableLiveData<>();
                //callApi
                callGetAllCategories();
            }
            return categoriesLiveData;
    }

    public void callGetAllCategories(){

        Log.d("ghazal", "getAllCategories: api called ?");
        if (Connectivity.isConnected(context)) {

            Call<ResponseCategoriesList> call = HomeRepository.callGetAllCategories() ;
            call.enqueue(new Callback<ResponseCategoriesList>() {
                @Override
                public void onResponse(Call<ResponseCategoriesList> call, Response<ResponseCategoriesList> response) {

                    if(response.isSuccessful())
                    {
                        if(response.body()!=null)
                        {
                            if(response.body().status == 200)
                            {
                                categoriesLiveData.setValue(response.body().categories);
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
                public void onFailure(Call<ResponseCategoriesList> call, Throwable t) {

                    errorMessageLiveData.setValue(t.getMessage());

                }
            });

        } else {
           errorMessageLiveData.setValue(context.getString(R.string.internet_not_connected_error));
        }

    }

    public void getAllServices (int page,int limit , String categories){

        if (Connectivity.isConnected(context)) {
            Call<ResponseSearchServices> call1 = HomeRepository.callGetAllServices(page,limit,categories);

            call1.enqueue(new Callback<ResponseSearchServices>() {
                @Override
                public void onResponse(Call<ResponseSearchServices> call, Response<ResponseSearchServices> response) {

                    if(response.isSuccessful())
                    {
                        if(response.body()!=null)
                        {
                            if(response.body().status == 200)
                            {
                                servicesLiveData.setValue(response.body().data.services);
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
                public void onFailure(Call<ResponseSearchServices> call, Throwable t) {

                    errorMessageLiveData.setValue(t.getMessage());
                }
            });
        }else {
            errorMessageLiveData.setValue(context.getString(R.string.internet_not_connected_error));
        }


    }



}