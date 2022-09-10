package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseSearchServices {

    @SerializedName("status")
    public int status = 0;

    @SerializedName("success")
    public boolean success = false;

    @SerializedName("message")
    public String message = "";

    @SerializedName("data")
    public Data data = new Data();

    public class Data {

        @SerializedName("docs")
        public ArrayList<Service> services = new ArrayList<>() ;
    }

}
