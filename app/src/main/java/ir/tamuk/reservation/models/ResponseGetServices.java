package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class ResponseGetServices {
    @SerializedName("status")
    public int status = 0;

    @SerializedName("success")
    public boolean success = false;

    @SerializedName("message")
    public String message = "";

    @SerializedName("data")
    public ServiceData serviceData = new ServiceData();
}
