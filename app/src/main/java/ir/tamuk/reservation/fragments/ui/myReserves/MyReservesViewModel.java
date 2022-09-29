package ir.tamuk.reservation.fragments.ui.myReserves;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.IOException;
import java.util.ArrayList;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.enums.StatusReserve;
import ir.tamuk.reservation.models.Reserve;
import ir.tamuk.reservation.models.ResponseGetMyReserve;
import ir.tamuk.reservation.models.ResponseReserveDetail;
import ir.tamuk.reservation.models.ResponseSearchServices;
import ir.tamuk.reservation.repository.MyReservesRepository;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.Tools;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReservesViewModel extends ViewModel {

    private static final String TAG = "MyReservesFragment";
    private boolean isFirst =  true ; //call api just once
    public boolean ignoreMessage = false ; // handle duplicate toast messages

    private MyReservesRepository repository = new MyReservesRepository() ;

    public MutableLiveData<ArrayList<Reserve>> myReservesLiveData = new MutableLiveData<>() ;
    public MutableLiveData<Reserve> reserveDetailLiveData = new MutableLiveData<>() ;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    private ArrayList<Reserve> doneList  ;
    private ArrayList<Reserve> reservedList ;
    private ArrayList<Reserve> canceledList ;


    public void getDoneList(Context context , String token , String status){
        if (doneList == null ){
            callGetMyReserves(context , token , status);
        }else {
            myReservesLiveData.setValue(doneList);
        }
    }
    public void getReservedList(Context context , String token , String status){
        if (reservedList == null ){
            callGetMyReserves(context , token , status);
        }else {
            myReservesLiveData.setValue(reservedList);
        }
    }
    public void getCanceledList(Context context , String token , String status){
        if (canceledList == null ){
            callGetMyReserves(context , token , status);
        }else {
            myReservesLiveData.setValue(canceledList);
        }
    }

    public void callGetMyReserves(Context context , String token , String status){

        if (Connectivity.isConnected(context)) {

            isLoading.setValue(true);

            Call<ResponseGetMyReserve> call = repository.callGetMyReserves(token , status) ;
            call.enqueue(new Callback<ResponseGetMyReserve>() {
                @Override
                public void onResponse(Call<ResponseGetMyReserve> call, Response<ResponseGetMyReserve> response) {

                    if(response.isSuccessful())
                    {
                        isLoading.setValue(false);

                        if(response.body()!=null) {
                            if(response.body().status == 200) {
//                                myReservesLiveData.setValue(response.body().myReserves);
                                if(status.equals(StatusReserve.DONE.label)){

                                    Log.d(TAG, "onResponse: done " + response.body().myReserves.size());

                                    doneList = new ArrayList<>() ;
                                    doneList.addAll(response.body().myReserves) ;
                                    myReservesLiveData.setValue(doneList);

                                }else if (status.equals(StatusReserve.RESERVED.label)){

                                    Log.d(TAG, "onResponse: reserved " + response.body().myReserves.size());

                                    reservedList = new ArrayList<>() ;
                                    reservedList.addAll(response.body().myReserves) ;
                                    myReservesLiveData.setValue(reservedList);

                                }else if (status.equals(StatusReserve.CANCELED.label)){

                                    Log.d(TAG, "onResponse: canceled " + response.body().myReserves.size());

                                    canceledList = new ArrayList<>() ;
                                    canceledList.addAll(response.body().myReserves) ;
                                    myReservesLiveData.setValue(canceledList);

                                }
                            }else{
                                errorMessageLiveData.setValue(response.body().message);

                            }
                        }else{
                            errorMessageLiveData.setValue(context.getString(R.string.null_response_body_error));

                        }
                    }else{

                        try {
                            errorMessageLiveData.setValue(Tools.extractErrorBodyMessage(response.errorBody().string()));
                            isLoading.setValue(false);

                        } catch (IOException e) {
                            e.printStackTrace();
                            isLoading.setValue(false);

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseGetMyReserve> call, Throwable t) {

                    errorMessageLiveData.setValue(t.getMessage());
                    isLoading.setValue(false);


                }
            });

        } else {
            errorMessageLiveData.setValue(context.getString(R.string.internet_not_connected_error));
            isLoading.setValue(false);

        }

    }

    public void callGetReserveDetail(Context context , String token , String id){

        if (Connectivity.isConnected(context)) {

            isLoading.setValue(true);

            Call<ResponseReserveDetail> call = repository.callGetReserveDetail(token , id) ;
            call.enqueue(new Callback<ResponseReserveDetail>() {
                @Override
                public void onResponse(Call<ResponseReserveDetail> call, Response<ResponseReserveDetail> response) {

                    if(response.isSuccessful())
                    {
                        isLoading.setValue(false);

                        if(response.body()!=null) {
                            if(response.body().status == 200) {
                                reserveDetailLiveData.setValue(response.body().reserve);
                            }else{
                                errorMessageLiveData.setValue(response.body().message);

                            }
                        }else{
                            errorMessageLiveData.setValue(context.getString(R.string.null_response_body_error));

                        }
                    }else{

                        try {
                            errorMessageLiveData.setValue(Tools.extractErrorBodyMessage(response.errorBody().string()));
                            isLoading.setValue(false);

                        } catch (IOException e) {
                            e.printStackTrace();
                            isLoading.setValue(false);

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseReserveDetail> call, Throwable t) {

                    errorMessageLiveData.setValue(t.getMessage());
                    isLoading.setValue(false);


                }
            });

        } else {
            errorMessageLiveData.setValue(context.getString(R.string.internet_not_connected_error));
            isLoading.setValue(false);

        }

    }


}
