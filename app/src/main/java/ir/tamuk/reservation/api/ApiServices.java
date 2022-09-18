package ir.tamuk.reservation.api;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.MoviesList;
import ir.tamuk.reservation.models.ResponseCategoriesList;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.models.ResponseSubmitCustomer;
import ir.tamuk.reservation.models.ResponseValidateCode;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {

    //http://moeenkashisaz.ir/laser/api/v1/send-code-activation
    @POST("v1/send-code-activation")
    Call<ResponseSendActivationCode> sendActivationCode(@Body BodySendActivationCode bodySendActivationCode);

    //http://moeenkashisaz.ir/laser/api/v1/send-code-activation
    @POST("v1/validate-code-activation")
    Call<ResponseValidateCode> validateCode(@Body BodySendActivationCode bodySendActivationCode);

    //http://moeenkashisaz.ir/laser/api/v1/get-all-category-by-customer
    @GET("v1/get-all-category-by-customer")
    Call<ResponseCategoriesList> getAllCategories ();

    //http://moeenkashisaz.ir/laser/api/v1/search-services?pageNumber=1&limit=20&categories=631867b5222c9efbb3dd899b
    @GET("v1/search-services?")
    Call<ResponseSearchServices> getServices (
            @Query("pageNumber") int page ,
            @Query("limit") int limit  ,
            @Query("categories") String categories ) ;

    //http://moeenkashisaz.ir/laser/api/v1/submit-customer
    @POST("v1/submit-customer")
    Call<ResponseSubmitCustomer> sendCustomer(@Body BodySubmitCustomer bodySubmitCustomer
            , @Header("authorization") String token);

}
