package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.ResponseReservation;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class ReservationRepository {


    public static Call<ResponseSearchServices> callSpinnerServices(int page , int limit , String categories){
        Call<ResponseSearchServices> call = Tools.getApiServicesInstance().getServices(page , limit , categories );
        return call ;
    }

    public static Call<ResponseReservation> getReservationsApi(String time, String services){
        //callApi
        Call<ResponseReservation> call1 = Tools.getApiServicesInstance().getReservations(time, services);
        return call1;
    }
}
