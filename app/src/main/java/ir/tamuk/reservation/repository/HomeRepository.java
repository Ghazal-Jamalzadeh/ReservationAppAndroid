package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.BodySendActivationCode;
import ir.tamuk.reservation.models.ResponseCategoriesList;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.models.ResponseSendActivationCode;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class HomeRepository {

    public static Call<ResponseCategoriesList> callGetAllCategories(){
        //callApi
        Call<ResponseCategoriesList> call = Tools.getApiServicesInstance().getAllCategories();

        return call;
    }

    public static Call<ResponseSearchServices> callGetAllServices(int page,int limit , String categories){
        //callApi
        Call<ResponseSearchServices> call1 = Tools.getApiServicesInstance().getServices(page,limit,categories);
        return call1;
    }

}
