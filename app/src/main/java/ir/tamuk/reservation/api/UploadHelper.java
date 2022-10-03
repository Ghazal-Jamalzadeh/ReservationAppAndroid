package ir.tamuk.reservation.api;

import android.content.Context;
import android.util.Log;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import ir.tamuk.reservation.Interfaces.ApiCommunicationInterface;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.TokenManager;
import okhttp3.OkHttpClient;

//upload files using android networking library
public class UploadHelper {
    private static final String TAG = "ApiCall";
    final private Context context;
    final private OkHttpClient client;
    final private Map<String, String> requestHeaders = new HashMap<>();
    final private ApiCommunicationInterface communicationInterface;

    public UploadHelper(Context context, ApiCommunicationInterface communicationInterface) {
        this.context = context;
        this.communicationInterface = communicationInterface;

        requestHeaders.put("Authorization", TokenManager.getAccessToken(context));

        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .authenticator(new AccessTokenAuthenticator(context))
                .build();


        AndroidNetworking.initialize(context, client);

    }

    public JSONObject apiResponseJsonCreator(String requestUrl, boolean isSuccess, int code, Object response) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("request", requestUrl);
            jsonObject.put("isSuccess", isSuccess);
            jsonObject.put("code", code);
            jsonObject.put("result", response);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    public void apiErrorHandling(ANError error, String url) {

        try {
            JSONObject errJSON = new JSONObject(String.valueOf(error.getErrorBody()));
            if (errJSON.has("message")) {
                communicationInterface.onResponse(
                        apiResponseJsonCreator(
                                url,
                                false,
                                error.getErrorCode(),
                                errJSON.getString("message")
                        ));
            } else {
                communicationInterface.onResponse(
                        apiResponseJsonCreator(
                                url,
                                false,
                                error.getErrorCode(),
                                errJSON.getString("Message")
                        ));
            }

        } catch (JSONException e) {
            communicationInterface.onResponse(
                    apiResponseJsonCreator(
                            url,
                            false,
                            0,
                            "خطا در ارتباط با سرور. لطفا مجددا تلاش کنید"
                    ));
        }
    }


    public void uploadPhoto(File photo) {

        AndroidNetworking.upload(Constants.BASE_URL + Constants.UploadPhoto_URL )
                .addHeaders(requestHeaders)
                .addMultipartFile("file", photo)
                .addMultipartParameter("public", "true")
                .setPriority(Priority.HIGH)
                .setOkHttpClient(client)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Log.d(TAG, "onResponse: upload photo success " + response);
                            int statusCode = response.getInt("status");

                            if (statusCode == 200) {

                                communicationInterface.onResponse(
                                        apiResponseJsonCreator(
                                                Constants.UploadPhoto_URL,
                                                true,
                                                statusCode,
                                                response
                                        ));

                            } else {
                                communicationInterface.onResponse(
                                        apiResponseJsonCreator(
                                                Constants.UploadPhoto_URL,
                                                false,
                                                statusCode,
                                                response.getString("message")
                                        ));
                            }

                        } catch (Exception e) {
                            Log.d(TAG, "onResponse: faield  " + e.getMessage());
                            communicationInterface.onResponse(
                                    apiResponseJsonCreator(
                                            Constants.UploadPhoto_URL,
                                            false,
                                            0,
                                            "خطا در دریافت اطلاعات"
                                    ));
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "onError: " + error.getErrorBody());
                        apiErrorHandling(error, Constants.UploadPhoto_URL);
                    }
                });
    }
}
