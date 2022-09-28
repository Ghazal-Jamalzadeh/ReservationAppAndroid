package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.ResponseGetMyReserve;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class MyReservesRepository {

    public Call<ResponseGetMyReserve> callGetMyReserves(String token , String status){
        //callApi
        Call<ResponseGetMyReserve> call = Tools.getApiServicesInstance().getMyReserve(token  , status);

        return call;
    }


}
