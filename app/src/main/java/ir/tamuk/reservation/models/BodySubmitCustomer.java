package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class BodySubmitCustomer {

    @SerializedName("firstName")
    public String firstName = "";

    @SerializedName("lastName")
    public String lastName = "";
}