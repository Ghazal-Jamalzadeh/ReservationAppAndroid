package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.models.ResponseSubmitCustomer;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class CompleteProfileInfoRepository {

    public Call<ResponseSubmitCustomer> callSubmitCustomer(BodySubmitCustomer body, String token){
        //callApi
        Call<ResponseSubmitCustomer> call = Tools.getApiServicesInstance().sendCustomer(body, token);

        return call;
    }
}
