package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.ResponseCategoriesList;
import ir.tamuk.reservation.models.ResponseGetMyProfile;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.utils.Tools;
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



}
