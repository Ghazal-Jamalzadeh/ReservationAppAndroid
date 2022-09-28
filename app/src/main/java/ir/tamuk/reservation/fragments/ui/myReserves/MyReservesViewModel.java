package ir.tamuk.reservation.fragments.ui.myReserves;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.IOException;
import java.util.ArrayList;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.models.Reserve;
import ir.tamuk.reservation.models.ResponseGetMyReserve;
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
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>() ;
    public MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    public void callGetAllCategories(Context context , String token , String status){

        if (Connectivity.isConnected(context)) {
            Log.d(TAG, "api called  ");
            isLoading.setValue(true);

            Call<ResponseGetMyReserve> call = repository.callGetMyReserves(token , status) ;
            call.enqueue(new Callback<ResponseGetMyReserve>() {
                @Override
                public void onResponse(Call<ResponseGetMyReserve> call, Response<ResponseGetMyReserve> response) {

                    if(response.isSuccessful())
                    {
                        if(response.body()!=null)
                        {
                            if(response.body().status == 200)
                            {
                                myReservesLiveData.setValue(response.body().myReserves);
                                isLoading.setValue(false);
                            }else{
                                errorMessageLiveData.setValue(response.body().message);
                                isLoading.setValue(false);

                            }
                        }else{
                            errorMessageLiveData.setValue(context.getString(R.string.null_response_body_error));
                            isLoading.setValue(false);

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

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }
}
