package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class Reserve {

    @SerializedName("_id")
    public String id = "" ;


    @SerializedName("date")
    public String date = "" ;


    @SerializedName("user")
    public String user = "" ;


    @SerializedName("service")
    public Service service = new Service() ;

    @SerializedName("line")
    public int line = 1 ;


    @SerializedName("status")
    public String status = "" ;


    @SerializedName("ispay")
    public boolean isPay = false ;



}
