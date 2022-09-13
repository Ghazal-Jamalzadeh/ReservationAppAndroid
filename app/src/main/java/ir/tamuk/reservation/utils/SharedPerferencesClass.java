package ir.tamuk.reservation.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ir.tamuk.reservation.utils.Constants;

public class SharedPerferencesClass {

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("UserNameAcrossApplication", context.MODE_PRIVATE);
    }

    //get
    public static String getPrefsAccess(Context context) {
        return getPrefs(context).getString(Constants.ACCESS_TOKEN, "default");
    }
    public static String getPrefsRefresh(Context context) {

        return getPrefs(context).getString(Constants.REFRESH_TOKEN, "default");
    }

    //set
    public static void setPrefsAccess(Context context, String value) {
        // perform validation etc..
        getPrefs(context).edit().putString(Constants.ACCESS_TOKEN, value).commit();
    }
    public static void setPrefsRefresh(Context context, String value) {
        // perform validation etc..
        getPrefs(context).edit().putInt(Constants.REFRESH_TOKEN, value).commit();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Set your value by calling
    //ReturningClass.setPrefsAccess(mContext,22);

    //Once You have set your sharedPreference.Then just call this method.Just pass the context.from your class
    //String usersharedpreference = SharedPerferencesClass.getPrefsAccess(mContext);

}
