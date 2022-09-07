package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class Tokens {

    @SerializedName("accessToken")
    public String accessToken = "";

    @SerializedName("refreshToken")
    public String refreshToken = "";
}
