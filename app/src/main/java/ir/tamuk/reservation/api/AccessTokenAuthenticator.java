package ir.tamuk.reservation.api;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import ir.tamuk.reservation.Interfaces.ApplicationCallBacks;
import ir.tamuk.reservation.activities.MainActivity;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.TokenManager;
import ir.tamuk.reservation.utils.Tools;
import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class AccessTokenAuthenticator implements Authenticator {

    private Context context;
    public static ApplicationCallBacks applicationCallBacks ;

    private static final String TAG = "AccessTokenAuth";

    public AccessTokenAuthenticator(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) {
//        final String accessToken = context.getSharedPreferences(ConstantsData.getSharedPrefName(),MODE_PRIVATE).getString("accessToken", "");
        final String accessToken = TokenManager.getAccessToken(context) ;


        if (isRequestWithAccessToken(response) && accessToken.equals("")) {
//            SharedPreferences.Editor editor = context.getSharedPreferences(ConstantsData.getSharedPrefName(), MODE_PRIVATE).edit();
//
//            editor.putBoolean("isGuestChoiceSelect", false);
//            editor.remove("accessToken");
//            editor.remove("refreshToken");
//            editor.remove("FirebaseToken");
//            editor.remove("onSuccess");
//            editor.apply();
//
//            ConstantsData.setUserInfo(new User());
//
//            ((Activity)context).finishAffinity();
//            Intent intent = new Intent(context, SplashActivity.class);
//            context.startActivity(intent);
            TokenManager.removeAccessToken(context);
            TokenManager.removeRefreshToken(context);

            applicationCallBacks.restartApplication();
//            ((Activity)context).finishAffinity();
//            Intent intent = new Intent(context, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
            Log.d(TAG, "authenticate: you don't have any token  ");

            return null;
        }

        final OkHttpClient client;

        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        try {

            Log.d(TAG, "authenticate: refreshing token ");
            JSONObject json = new JSONObject();
            json.put("oldRefreshToken", TokenManager.getRefreshToken(context));
            json.put("oldAccessToken", TokenManager.getAccessToken(context));
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            Request request = new Request.Builder()
                    .url(Constants.BASE_URL + Constants.RefreshingAccessToken)
                    .post(body)
                    .build();
            Response response1 = client.newCall(request).execute();
            Log.d(TAG, "authenticate: okhttp called ");
            String khoob = response1.body().string();
            Log.d(TAG, "response body: " + khoob);
            JSONObject response2 = new JSONObject(khoob);
            int statusCode = response2.getInt("statusCode");

            if (statusCode == 200) {


                JSONObject data = response2.getJSONObject("data");

                String accessToken2 = data.getString("access_token");
                String refreshToken = data.getString("refresh_token");

                Log.d(TAG, "authenticate: 200 " );
                Log.d(TAG, "new access token : " + accessToken2 );
                Log.d(TAG, "new refresh token : " + refreshToken );
//                SharedPreferences.Editor editor = context.getSharedPreferences(ConstantsData.getSharedPrefName(),MODE_PRIVATE).edit();
//                editor.putString("accessToken", accessToken2);
//                editor.putString("refreshToken", refreshToken);
//                editor.apply();
                TokenManager.setAccessToken(context , accessToken2);
                TokenManager.setRefreshToken(context , refreshToken);

                return newRequestWithAccessToken(response.request(), accessToken2);

            } else if (statusCode == 418) {
//                SharedPreferences.Editor editor = context.getSharedPreferences(ConstantsData.getSharedPrefName(), MODE_PRIVATE).edit();
//
//                editor.putBoolean("isGuestChoiceSelect", false);
//                editor.remove("accessToken");
//                editor.remove("refreshToken");
//                editor.remove("FirebaseToken");
//                editor.remove("onSuccess");
//                editor.apply();
//
//                ConstantsData.setUserInfo(new User());

                Log.d(TAG, "authenticate: 418 ");
                TokenManager.removeAccessToken(context);
                TokenManager.removeRefreshToken(context);
                applicationCallBacks.restartApplication();
//                ((Activity)context).finishAffinity();
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
            }

        } catch (Exception e) {
            Log.d(TAG, "authenticate: exp : " + e.getMessage());
//            SharedPreferences.Editor editor = context.getSharedPreferences(ConstantsData.getSharedPrefName(), MODE_PRIVATE).edit();
//
//            editor.putBoolean("isGuestChoiceSelect", false);
//            editor.remove("accessToken");
//            editor.remove("refreshToken");
//            editor.remove("FirebaseToken");
//            editor.remove("onSuccess");
//            editor.apply();
//
//            ConstantsData.setUserInfo(new User());
//
//            ((Activity)context).finishAffinity();
//            Intent intent = new Intent(context, SplashActivity.class);
//            context.startActivity(intent);
            TokenManager.removeAccessToken(context);
            TokenManager.removeRefreshToken(context);
            applicationCallBacks.restartApplication();
//            ((Activity)context).finishAffinity();
//            Intent intent = new Intent(context, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);

            return null;
        }

        return null;

    }

    private boolean isRequestWithAccessToken(@NonNull Response response) {
        String header = response.request().header("Authorization");
        return header != null && header.startsWith("Bearer");
    }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
        String token ;
        if (accessToken.startsWith("Bearer ")){
            token =  accessToken ;
        }else {
            token = "Bearer " + accessToken ;
        }
        return request.newBuilder()
                .header("Authorization", token)
                .build();
    }
}