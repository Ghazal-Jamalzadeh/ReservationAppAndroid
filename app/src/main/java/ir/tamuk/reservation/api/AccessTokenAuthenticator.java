package ir.tamuk.reservation.api;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;
import ir.tamuk.reservation.Interfaces.ApplicationCallBacks;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.TokenManager;
import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

//401 Unauthorized — Renew JWT tokens with OkHttp3 Authenticator
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

        final String accessToken = TokenManager.getAccessToken(context) ;

        if (isRequestWithAccessToken(response) && accessToken.equals("")) {
            TokenManager.removeAccessToken(context);
            TokenManager.removeRefreshToken(context);

            applicationCallBacks.restartApplication();

            return null;
        }

        final OkHttpClient client;

        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        try {
            //refreshing tokens
            JSONObject json = new JSONObject();

            json.put("oldRefreshToken", TokenManager.getRefreshToken(context));
            json.put("oldAccessToken", TokenManager.getAccessToken(context));

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            Request request = new Request.Builder()
                    .url(Constants.BASE_URL + Constants.RefreshingAccessToken)
                    .post(body)
                    .build();
            Response response1 = client.newCall(request).execute();

            String khoob = response1.body().string();
            JSONObject response2 = new JSONObject(khoob);
            int statusCode = response2.getInt("status");

            if (statusCode == 200) {
                //Renewing Token(s)
                JSONObject data = response2.getJSONObject("data");
                JSONObject tokens =  data.getJSONObject("tokens") ;

                String accessToken2 = tokens.getString("accessToken");
                String refreshToken = tokens.getString("refreshToken");

                TokenManager.setAccessToken(context , accessToken2);
                TokenManager.setRefreshToken(context , refreshToken);

                //If user’s auth tokens were successfully renewed,
                // the authenticator will attempt to retry the original request.
                return newRequestWithAccessToken(response.request(), accessToken2);

            } else if (statusCode == 418) {
                //refresh token has been expired
                TokenManager.removeAccessToken(context);
                TokenManager.removeRefreshToken(context);
                applicationCallBacks.restartApplication();

            }

        } catch (Exception e) {

            TokenManager.removeAccessToken(context);
            TokenManager.removeRefreshToken(context);

            applicationCallBacks.restartApplication();

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