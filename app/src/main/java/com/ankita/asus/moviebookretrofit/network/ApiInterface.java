package com.ankita.asus.moviebookretrofit.network;

import com.ankita.asus.moviebookretrofit.models.MovieData;
import com.ankita.asus.moviebookretrofit.models.TrailerData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular")
    Call<MovieData> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/{path}")
    Call<TrailerData> getMovieTrailers(@Path("id") int id,@Path("path") String path,
                                       @Query("api_key") String apikey);
    @GET("movie/top_rated")
    Call<MovieData> getTopRatedMovies(@Query("api_key") String apikey);
}
