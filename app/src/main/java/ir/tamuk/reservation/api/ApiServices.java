package ir.tamuk.reservation.api;

import ir.tamuk.reservation.models.MoviesList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("/api/v1/movies?")
    Call<MoviesList> getMovies(@Query("page") int page) ;

}
