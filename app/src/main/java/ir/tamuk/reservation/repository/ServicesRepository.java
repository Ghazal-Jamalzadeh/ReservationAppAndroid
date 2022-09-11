package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class ServicesRepository {

    public static Call<ResponseSearchServices> callGetServices (int page , int limit , String categories){
        Call<ResponseSearchServices> call = Tools.getApiServicesInstance().getServices(page , limit , categories );
        return call ;
    }
}
