package ir.tamuk.reservation.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MoviesList {

    @SerializedName("data")
    public ArrayList<Movie> movies = new ArrayList();

    public class Movie{

        @SerializedName("id")
        public int id ;

        @SerializedName("title")
        public String title ;


        @SerializedName("poster")
        public String poster ;

    }
}
