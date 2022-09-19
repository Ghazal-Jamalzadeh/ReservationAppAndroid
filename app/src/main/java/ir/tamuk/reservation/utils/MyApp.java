package ir.tamuk.reservation.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyApp extends Application {
    private static Context appContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context getMyAppContext() {
        return MyApp.appContext;
    }


}
