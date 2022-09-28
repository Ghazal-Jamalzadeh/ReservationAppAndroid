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

    //چون تو بعضی api ها آبجکت میاد تو بعضیاش String فعلا به مدل اضافه نشه باعث کرش میشه
//    @SerializedName("category")
//    public Category category = new Category() ;

    @SerializedName("time")
    public int time = 0;

    @SerializedName("restTime")
    public int restTime = 0;
}
