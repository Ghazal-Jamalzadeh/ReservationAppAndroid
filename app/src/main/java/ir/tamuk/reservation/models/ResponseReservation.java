package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseReservation {

        @SerializedName("status")
        public int status = 0;

        @SerializedName("success")
        public boolean success = false;

        @SerializedName("message")
        public String message = "";

        @SerializedName("data")
        public Data data = new Data();

        public class Data{

                @SerializedName("freeTimeAm")
                public ArrayList<String> freeTimeAms = new ArrayList<>();



                @SerializedName("freeTimePm")
                public ArrayList<String> freeTimePms = new ArrayList<>();

        }


}
