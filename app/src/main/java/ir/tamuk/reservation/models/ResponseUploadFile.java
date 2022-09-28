package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class ResponseUploadFile {
    @SerializedName("status")
    public int status = 0;

    @SerializedName("success")
    public boolean success = false;

    @SerializedName("message")
    public String message = "";

    @SerializedName("data")
    public Photo photo = new Photo() ;


}
