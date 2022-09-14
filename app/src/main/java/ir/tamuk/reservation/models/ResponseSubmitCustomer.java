package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseSubmitCustomer {

    @SerializedName("status")
    public int status = 0;

    @SerializedName("success")
    public boolean success = false;

    @SerializedName("message")
    public  String message = "";

    @SerializedName("data")
    public Data data = new Data();

    public class Data {

        @SerializedName("_id")
        public String id = "";

        @SerializedName("mobile")
        public String mobile = "";

        @SerializedName("status")
        public int status = 0;

        @SerializedName("isActive")
        public boolean isActive = false;

        @SerializedName("insertDate")
        public String insertDate = "";

        @SerializedName("isDeleteAble")
        public boolean isDeleteAble = false;

        @SerializedName("isDeleted")
        public boolean isDeleted = false;

        @SerializedName("birthday")
        public String birthday = "";

        @SerializedName("firstName")
        public String firstName = "";

        @SerializedName("lastName")
        public String lastName = "";

        @SerializedName("photo")
        public String photo = "";

        @SerializedName("updateDate")
        public String updateDate = "";


    }
}
