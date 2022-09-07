package ir.tamuk.reservation.models;
import com.google.gson.annotations.SerializedName;


public class ResponseValidateCode {

    @SerializedName("status")
    public int status = 0;

    @SerializedName("success")
    public boolean success = false;

    @SerializedName("message")
    public String message = "";

    @SerializedName("data")
    public Data data = new Data();

    public class Data{

        @SerializedName("tokens")
        public Tokens tokens = new Tokens()  ;

        @SerializedName("user")
        public User user = new User() ;

    }
}
