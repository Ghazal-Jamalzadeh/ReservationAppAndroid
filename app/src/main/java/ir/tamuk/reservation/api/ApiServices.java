package ir.tamuk.reservation.api;

import ir.tamuk.reservation.models.BodyFactor;
import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.MoviesList;
import ir.tamuk.reservation.models.ResponseCategoriesList;
import ir.tamuk.reservation.models.ResponseFactor;
import ir.tamuk.reservation.models.ResponseGetMyReserve;
import ir.tamuk.reservation.models.ResponseReservation;
import ir.tamuk.reservation.models.ResponseGetMyProfile;
import ir.tamuk.reservation.models.ResponseReserveDetail;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.models.ResponseSubmitCustomer;
import ir.tamuk.reservation.models.ResponseUploadFile;
import ir.tamuk.reservation.models.ResponseValidateCode;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServices {

    //http://moeenkashisaz.ir/laser/api/v1/send-code-activation
    @POST("v1/send-code-activation")
    Call<ResponseSendActivationCode> sendActivationCode(
            @Body BodySendActivationCode bodySendActivationCode);

    //http://moeenkashisaz.ir/laser/api/v1/send-code-activation
    @POST("v1/validate-code-activation")
    Call<ResponseValidateCode> validateCode(
            @Body BodySendActivationCode bodySendActivationCode);

    //http://moeenkashisaz.ir/laser/api/v1/get-all-category-by-customer
    @GET("v1/get-all-category-by-customer")
    Call<ResponseCategoriesList> getAllCategories();

    //http://moeenkashisaz.ir/laser/api/v1/search-services?pageNumber=1&limit=20&categories=631867b5222c9efbb3dd899b
    @GET("v1/search-services?")
    Call<ResponseSearchServices> getServices(
            @Query("pageNumber") int page,
            @Query("limit") int limit,
            @Query("categories") String categories);

    //http://moeenkashisaz.ir/laser/api/v1/submit-customer
    @POST("v1/submit-customer")
    Call<ResponseSubmitCustomer> sendCustomer(
            @Body BodySubmitCustomer bodySubmitCustomer,
            @Header("authorization") String token);

    //http://moeenkashisaz.ir/laser/api/v1/logout
    @POST("v1/logout")
    Call<ResponseSendActivationCode> logout(
            @Header("authorization") String token);

    //http://moeenkashisaz.ir/laser/api/v1/get-my-profile
    @GET("v1/get-my-profile")
    Call<ResponseGetMyProfile> getMyProfile(
            @Header("authorization") String token);

    //http://moeenkashisaz.ir/laser/api/v1/finde-free-time?service=631869b10bfaf719ef8b76cf&date=2022-09-12&line=1
    @GET("v1/finde-free-time?service?line=1")
    Call<ResponseReservation> getReservations(
            @Query("date") String time,
            @Query("service") String service);

    //http://moeenkashisaz.ir/laser/api/v1/upload-file/
    @Multipart
    @Headers( "Content-Type: application/x-www-form-urlencoded")
    @POST("v1/upload-file/")
    Call<ResponseUploadFile> uploadFile(
            @Header("authorization") String token,
            @Part MultipartBody.Part file);


    //http://moeenkashisaz.ir/laser/api/v1/get-my-reserve?status=reserved
    @GET("v1/get-my-reserve?")
    Call<ResponseGetMyReserve> getMyReserve (
            @Header("authorization") String token ,
            @Query("status") String status
    );

    //http://localhost:3001/laser/api/v1/get-reserve-user/631c47d9d0db77fb798849c3
    @GET("v1/get-reserve-user/{id}")
    Call<ResponseReserveDetail> getReserveDetail(
            @Header("authorization") String token ,
            @Path("id") String id)  ;

    //http://moeenkashisaz.ir/laser/api/v1/insert-reserve
    @POST("v1/insert-reserve")
    Call<ResponseFactor> getFactor(@Body BodyFactor bodyFactor, @Header("authorization") String token);

}
