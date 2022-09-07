package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.models.ResponseValidateCode;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class SigningValiddationCodeRepository {


    public Call<ResponseValidateCode> callReceiveActivationCode(BodySendActivationCode body){
        //callApi
        Call<ResponseValidateCode> callSigningValiddation = Tools.getApiServicesInstance().validateCode(body);

        return callSigningValiddation;
    }
}
