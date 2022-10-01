package ir.tamuk.reservation.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("UserNameAcrossApplication", context.MODE_PRIVATE);
    }

    //getters
    public static String getAccessToken(Context context) {
        return getPrefs(context).getString(Constants.ACCESS_TOKEN, "");
    }

    public static String getRefreshToken(Context context) {
        return getPrefs(context).getString(Constants.REFRESH_TOKEN, "");
    }

    //setters
    public static void setAccessToken(Context context, String value) {
        String token  ;
        if (value.startsWith("Bearer ")){
            token =  value ;
        }else {
            token = "Bearer " + value ;
        }
        getPrefs(context).edit().putString(Constants.ACCESS_TOKEN, token).apply();
    }

    public static void setRefreshToken(Context context, String value) {
        getPrefs(context).edit().putString(Constants.REFRESH_TOKEN, value).apply();
    }

    //check if token exist or not
    public static Boolean hasAccessToken(Context context) {
        return !getAccessToken(context).equals("");
    }

    public static Boolean hasRefreshToken(Context context) {
        return !getRefreshToken(context).equals("");
    }

    //remove tokens
    public static void removeAccessToken(Context context){
        getPrefs(context).edit().remove(Constants.ACCESS_TOKEN).apply(); ;
    }

    public static void removeRefreshToken(Context context){
        getPrefs(context).edit().remove(Constants.REFRESH_TOKEN).apply(); ;
    }

}
