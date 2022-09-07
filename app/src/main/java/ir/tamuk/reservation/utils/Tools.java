package ir.tamuk.reservation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.widget.NestedScrollView;

import ir.tamuk.reservation.api.ApiClient;
import ir.tamuk.reservation.api.ApiServices;

public class Tools {


    private static ApiServices apiServices = null ;

    public static ApiServices getApiServicesInstance (){

        if (apiServices == null ){
            //client
            apiServices = ApiClient.getClient().create(ApiServices.class);
        }

        return apiServices ;
    }


    public static void scrollToPosition(NestedScrollView scrollView , View targetView) {

        scrollView.postDelayed(() -> scrollView.smoothScrollTo(0, targetView.getTop()), 300);
    }


    public static void keyboardPopUp(Activity activity){

        InputMethodManager imgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }



}
