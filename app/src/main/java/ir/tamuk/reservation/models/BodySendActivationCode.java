package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class BodySendActivationCode {

    @SerializedName("mobile")
    public String mobile = "";

    @SerializedName("code")
    public String code = "";
}
