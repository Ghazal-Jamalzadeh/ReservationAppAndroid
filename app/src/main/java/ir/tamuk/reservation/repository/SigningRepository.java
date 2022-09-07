package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class SigningRepository {

    public Call<ResponseSendActivationCode> callSendActivationCode(BodySendActivationCode body){
        //callApi
        Call<ResponseSendActivationCode> call = Tools.getApiServicesInstance().sendActivationCode(body);

        return call;
    }
}
