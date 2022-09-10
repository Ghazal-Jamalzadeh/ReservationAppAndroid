package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class Service {

    @SerializedName("_id")
    public String id ="";

    @SerializedName("faName")
    public String name ="";

    @SerializedName("description")
    public String description ="";

    @SerializedName("mainPhoto")
    public Photo mainPhoto= new Photo();

    @SerializedName("price")
    public int price = 0;

    @SerializedName("category")
    public Category category = new Category() ;

    @SerializedName("time")
    public int time = 0;

}
