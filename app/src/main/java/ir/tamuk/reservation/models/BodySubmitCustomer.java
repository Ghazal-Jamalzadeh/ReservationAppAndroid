package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class BodySubmitCustomer {

    @SerializedName("firstName")
    public String firstName = "";

    @SerializedName("lastName")
    public String lastName = "";

    @SerializedName("photo")
    public String Photo = null;

    @SerializedName("birthday")
    public String birthday = null;

}
