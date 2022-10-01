package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.ResponseGetMyReserve;
import ir.tamuk.reservation.models.ResponseReserveDetail;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class MyReservesRepository {

    public Call<ResponseGetMyReserve> callGetMyReserves(String token , String status){

        return Tools.getApiServicesInstance().getMyReserve(token  , status);
    }

    public Call<ResponseReserveDetail> callGetReserveDetail( String token , String id){

        return Tools.getApiServicesInstance().getReserveDetail(token , id);
    }


}
