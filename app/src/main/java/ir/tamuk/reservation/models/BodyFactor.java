package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class BodyFactor {

    @SerializedName("date")
    public String dateFactor = "";

    @SerializedName("service")
    public String serviceFactor = "";

    @SerializedName("line")
    public int line = 2;
}
