package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

public class ServiceData {

    @SerializedName("_id")
    public String id="";

    @SerializedName("faName")
    public String name="";

    @SerializedName("enName")
    public String enName="";

    @SerializedName("description")
    public String description="";

    @SerializedName("mainPhoto")
    public Photo mainPhoto=new Photo();

    @SerializedName("price")
    public int price=0;

    @SerializedName("category")
    public Category category=new Category();

    @SerializedName("sort")
    public int sort=0;

    @SerializedName("time")
    public int time=0;

}
