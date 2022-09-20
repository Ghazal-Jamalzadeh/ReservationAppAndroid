package ir.tamuk.reservation.fragments.ui.profile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;

import ir.tamuk.reservation.R;
import ir.tamuk.reservation.models.Category;
import ir.tamuk.reservation.models.ResponseGetMyProfile;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.models.User;
import ir.tamuk.reservation.repository.ProfileRepository;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.TokenManager;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    private final Context context ;

    public boolean isFirst =  true ; //call getMyProfile api just once
    public boolean ignoreMessage = false ; // handle duplicate toast messages

    private final ProfileRepository profileRepository =  new ProfileRepository() ;

    public MutableLiveData<Boolean> logoutIsSuccess =  new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    public MutableLiveData<User> userLiveData =  new MutableLiveData<>() ;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();    }

    public MutableLiveData<User> getMyProfile(){

        if(isFirst){
            //callApi
            callGetMyProfile();
        }
        return userLiveData;
    }



    public void callGetMyProfile(){

        if (Connectivity.isConnected(context)) {
            Call<ResponseGetMyProfile> call = profileRepository.callGetMyProfile(TokenManager.getAccessToken(context)) ;
            call.enqueue(new Callback<ResponseGetMyProfile>() {
                @Override
                public void onResponse(Call<ResponseGetMyProfile> call, Response<ResponseGetMyProfile> response) {

                    if(response.isSuccessful())
                    {
                        if(response.body()!=null)
                        {
                            if(response.body().status == 200)
                            {
                                isFirst =  false ;
                                userLiveData.setValue(response.body().user);
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
                public void onFailure(Call<ResponseGetMyProfile> call, Throwable t) {

                    errorMessageLiveData.setValue(t.getMessage());

                }
            });

        } else {
            errorMessageLiveData.setValue(context.getString(R.string.internet_not_connected_error));
        }

    }

    public void logoutUser(){

        if (Connectivity.isConnected(context)) {
            Call<ResponseSendActivationCode> call = profileRepository.callLogout(TokenManager.getAccessToken(context)) ;
            call.enqueue(new Callback<ResponseSendActivationCode>() {
                @Override
                public void onResponse(Call<ResponseSendActivationCode> call, Response<ResponseSendActivationCode> response) {

                    if(response.isSuccessful())
                    {
                        if(response.body()!=null)
                        {
                            if(response.body().status == 200)
                            {
                                logoutIsSuccess.setValue(response.body().success);
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
                public void onFailure(Call<ResponseSendActivationCode> call, Throwable t) {

                    errorMessageLiveData.setValue(t.getMessage());

                }
            });

        } else {
            errorMessageLiveData.setValue(context.getString(R.string.internet_not_connected_error));
        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("ProfileFragment", "onCleared: ");
    }
}