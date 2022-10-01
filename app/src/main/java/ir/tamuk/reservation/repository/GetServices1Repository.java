package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.ResponseGetServices;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class GetServices1Repository {
    public static Call<ResponseGetServices> callGetServices1(String id){
        Call<ResponseGetServices> call= Tools.getApiServicesInstance().getServicesByld(id);
        return call;

    }

}
