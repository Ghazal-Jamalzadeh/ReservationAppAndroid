package ir.tamuk.reservation.repository;

import ir.tamuk.reservation.models.BodyFactor;
import ir.tamuk.reservation.models.ResponseFactor;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;

public class FactorRepository {

    public  Call<ResponseFactor> callFactor(BodyFactor bodyFactor, String token){
        Call<ResponseFactor> call = Tools.getApiServicesInstance().getFactor(bodyFactor, token );
        return call ;
    }
}
