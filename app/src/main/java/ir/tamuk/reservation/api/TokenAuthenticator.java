package ir.tamuk.reservation.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.lang.reflect.Proxy;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {
        // Refresh your access_token using a synchronous api request
//        newAccessToken = service.refreshToken();

        // Add new header to rejected request and retry it
        return response.request().newBuilder()
                .header("AUTHORIZATION", "newAccessToken")
                .build();

    }


}