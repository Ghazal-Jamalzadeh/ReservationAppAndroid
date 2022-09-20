package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("_id")
    public String _id;

    @SerializedName("userName")
    public String userName;

    @SerializedName("mobile")
    public String mobile;

    @SerializedName("status")
    public int userStatus;

    @SerializedName("firstName")
    public String firstName = "" ;

    @SerializedName("lastName")
    public String lastName = "" ;


}
