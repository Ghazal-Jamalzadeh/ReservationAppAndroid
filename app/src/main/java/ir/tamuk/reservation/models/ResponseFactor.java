package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseFactor {

    @SerializedName("status")
    public int status = 0;

    @SerializedName("success")
    public boolean success = false;

    @SerializedName("message")
    public String message = "";

    @SerializedName("data")
    public Data data = new Data();

    public class Data{

        @SerializedName("date")
        public String date = "";

        @SerializedName("user")
        public String user = "";

        @SerializedName("service")
        public String service = "";

        @SerializedName("status")
        public String status = "";

        @SerializedName("insertDate")
        public String insertDate = "";

        @SerializedName("_id")
        public String id = "";

        @SerializedName("line")
        public int line = 0;

        @SerializedName("isPay")
        public boolean isPay = false;

        @SerializedName("__v")
        public int v = 0;

        @SerializedName("isDeleted")
        public boolean isDeleted = false;


    }
}
