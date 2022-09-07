package ir.tamuk.reservation.api;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.MoviesList;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.models.ResponseValidateCode;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {

    //http://moeenkashisaz.ir/laser/api/v1/send-code-activation
    @POST("/laser/api/v1/send-code-activation")
    Call<ResponseSendActivationCode> sendActivationCode(@Body BodySendActivationCode bodySendActivationCode);

    //http://moeenkashisaz.ir/laser/api/v1/validate-code-activation
    @POST("/laser/api/v1/validate-code-activation")
    Call<ResponseValidateCode> validateCode(@Body BodySendActivationCode bodySendActivationCode);

}
