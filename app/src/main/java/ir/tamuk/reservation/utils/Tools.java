package ir.tamuk.reservation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import ir.tamuk.reservation.R;
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

    public static void showToast(Context context , String message ){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public static void scrollToPosition(ScrollView scrollView , View targetView) {

        scrollView.postDelayed(() -> scrollView.smoothScrollTo(0, targetView.getTop()), 300);

    }


    public static void keyboardPopUp(Activity activity){

        InputMethodManager imgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    public static void removePhoneKeypad(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String extractErrorBodyMessage(String error){
        String message = "" ;
        try {
            JSONObject  jObjError = new JSONObject(error);
            message =  jObjError.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return message ;
    }

    //use this before navigate between fragments
    public static Boolean checkDestination (View view , int fragmentId){

      return  Navigation.findNavController(view).getCurrentDestination() == Navigation.findNavController(view).findDestination(fragmentId);

    }



}
