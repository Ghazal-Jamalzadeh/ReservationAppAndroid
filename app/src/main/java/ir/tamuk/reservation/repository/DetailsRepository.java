package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.ResponseGetServices;
import ir.tamuk.reservation.models.ResponseSubmitCustomer;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class DetailsRepository {

    public Call<ResponseGetServices> callGetServiceDetail(String id){
        return Tools.getApiServicesInstance().getServicesById(id);
    }

}
