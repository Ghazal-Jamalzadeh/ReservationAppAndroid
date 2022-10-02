package ir.tamuk.reservation.repository;

import java.util.HashMap;
import java.util.Map;

import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.ResponseCategoriesList;
import ir.tamuk.reservation.models.ResponseGetMyProfile;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.models.ResponseSubmitCustomer;
import ir.tamuk.reservation.models.ResponseUploadFile;
import ir.tamuk.reservation.utils.Tools;
import okhttp3.MultipartBody;
import retrofit2.Call;

public class ProfileRepository {

    public Call<ResponseSendActivationCode> callLogout(String token){
        //callApi
        Call<ResponseSendActivationCode> call = Tools.getApiServicesInstance().logout(token);

        return call;
    }

    public Call<ResponseGetMyProfile> callGetMyProfile(String token){
        //callApi
        Call<ResponseGetMyProfile> call = Tools.getApiServicesInstance().getMyProfile(token);

        return call;
    }

    public Call<ResponseSubmitCustomer> callEditProfile(BodySubmitCustomer body , String token ){

        Call<ResponseSubmitCustomer> call =  Tools.getApiServicesInstance().sendCustomer(body , token) ;

        return call ;

    }

    public Call<ResponseUploadFile> callUploadFile(String token , MultipartBody.Part image){

        Call<ResponseUploadFile> call = Tools.getApiServicesInstance().uploadFile(token , image);

        return call ;
    }



}
