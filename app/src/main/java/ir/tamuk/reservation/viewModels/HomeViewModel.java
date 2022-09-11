package ir.tamuk.reservation.viewModels;

import android.app.Application;
import android.content.Context;

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
import ir.tamuk.reservation.repository.HomeRepository;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Category>> categoriesLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void getAllCategories(){

        loading.setValue(true);
        Call<ResponseCategoriesList> call = HomeRepository.callGetAllCategories() ;

        call.enqueue(new Callback<ResponseCategoriesList>() {
            @Override
            public void onResponse(Call<ResponseCategoriesList> call, Response<ResponseCategoriesList> response) {

                loading.setValue(false);
                if(response.isSuccessful())
                {
                    if(response.body()!=null)
                    {
                        if(response.body().status == 200)
                        {
                         categoriesLiveData.setValue(response.body().categories);
                        }else{
                            errorMessage.setValue(response.body().message);
                        }
                    }else{
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
            public void onFailure(Call<ResponseCategoriesList> call, Throwable t) {

                loading.setValue(false);
                errorMessage.setValue(t.getMessage());

            }
        });

    }



}