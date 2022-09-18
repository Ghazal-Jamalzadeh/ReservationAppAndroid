//package ir.tamuk.reservation.api;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import org.json.JSONObject;
//
//import java.util.concurrent.TimeUnit;
//
//import ir.tamuk.reservation.utils.SharedPreferencesClass;
//import ir.tamukco.tamukandroidclient.activities.SplashActivity;
//import ir.tamukco.tamukandroidclient.app.ConstantsData;
//import ir.tamukco.tamukandroidclient.models.user.User;
//import okhttp3.Authenticator;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.Route;
//
//public class AccessTokenAuthenticator implements Authenticator {
//
//    private Context context;
//
//    public AccessTokenAuthenticator(Context context) {
//        this.context = context;
//    }
//
//    @Nullable
//    @Override
//    public Request authenticate(Route route, Response response) {
////        final String accessToken = context.getSharedPreferences(ConstantsData.getSharedPrefName(),MODE_PRIVATE).getString("accessToken", "");
////        final String accessToken = SharedPreferencesClass.getAccessToken(context) ;
//
//        if (isRequestWithAccessToken(response) && accessToken.equals("")) {
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
//            return null;
//        }
//
//        final OkHttpClient client;
//        client = new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
//                .readTimeout(15, TimeUnit.SECONDS)
//                .build();
//        try {
//
//            JSONObject json = new JSONObject();
//            json.put("OldRefreshToken", context.getSharedPreferences(ConstantsData.getSharedPrefName(),MODE_PRIVATE).getString("refreshToken", ""));
//            json.put("OldAccessToken", context.getSharedPreferences(ConstantsData.getSharedPrefName(),MODE_PRIVATE).getString("accessToken", ""));
//            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
//            Request request = new Request.Builder()
//                    .url(ApiEndPoint.Base_Url + ApiEndPoint.RefreshingAccessToken)
//                    .post(body)
//                    .build();
//            Response response1 = client.newCall(request).execute();
//            String khoob = response1.body().string();
//            JSONObject response2 = new JSONObject(khoob);
//            int statusCode = response2.getInt("statusCode");
//
//            if (statusCode == 200) {
//
//                JSONObject data = response2.getJSONObject("data");
//
//                String accessToken2 = data.getString("access_token");
//                String refreshToken = data.getString("refresh_token");
//
//                SharedPreferences.Editor editor = context.getSharedPreferences(ConstantsData.getSharedPrefName(),MODE_PRIVATE).edit();
//                editor.putString("accessToken", accessToken2);
//                editor.putString("refreshToken", refreshToken);
//                editor.apply();
//
//                return newRequestWithAccessToken(response.request(), accessToken2);
//
//            } else if (statusCode == 418) {
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
//
//                ((Activity)context).finishAffinity();
//                Intent intent = new Intent(context, SplashActivity.class);
//                context.startActivity(intent);
//            }
//
//        } catch (Exception e) {
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
//            return null;
//        }
//
//        return null;
//
//    }
//
//    private boolean isRequestWithAccessToken(@NonNull Response response) {
//        String header = response.request().header("Authorization");
//        return header != null && header.startsWith("Bearer");
//    }
//
//    @NonNull
//    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
//        return request.newBuilder()
//                .header("Authorization", "Bearer " + accessToken)
//                .build();
//    }
//}