package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("_id")
    public String id ="";

    @SerializedName("faName")
    public String name ="";

    @SerializedName("description")
    public String description ="";

    @SerializedName("icon")
    public Photo icon= new Photo();

    @SerializedName("photo")
    public Photo photo= new Photo();

    @SerializedName("isActive")
    public boolean isActive =true;


}

