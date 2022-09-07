package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseSendActivationCode {

    ArrayList<ResponseValidateCode> responseValidateCodes = new ArrayList<>();

    @SerializedName("status")
    public int status = 0;

    @SerializedName("success")
    public boolean success = false;

    @SerializedName("message")
    public String message = "";

    @SerializedName("data")
    public String data = "";
}
