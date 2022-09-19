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
import java.util.List;
import java.util.Map;

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
    public int tabLastPos =  -1 ; //keep tabLayout last position
    private boolean isFirst =  true ; //call get categories api just once
    public boolean ignoreMessage = false ; // handle duplicate toast messages
    private final HomeRepository homeRepository =  new HomeRepository() ;

    public MutableLiveData<ArrayList<Category>> categoriesLiveData  = new MutableLiveData<>(); ;
    public MutableLiveData<ArrayList<Service>> servicesLiveData = new MutableLiveData<>();
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();    }


        public MutableLiveData<ArrayList<Category>> getAllCategories(){

            if(isFirst){
                //callApi
                callGetAllCategories();
            }
            return categoriesLiveData;
    }

    public void callGetAllCategories(){

        if (Connectivity.isConnected(context)) {
            Call<ResponseCategoriesList> call = homeRepository.callGetAllCategories() ;
            call.enqueue(new Callback<ResponseCategoriesList>() {
                @Override
                public void onResponse(Call<ResponseCategoriesList> call, Response<ResponseCategoriesList> response) {

                    if(response.isSuccessful())
                    {
                        if(response.body()!=null)
                        {
                            if(response.body().status == 200)
                            {
                                isFirst = false ;
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
            Call<ResponseSearchServices> call1 = homeRepository.callGetAllServices(page,limit,categories);

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
