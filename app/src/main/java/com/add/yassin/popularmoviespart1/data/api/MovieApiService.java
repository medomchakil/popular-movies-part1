package com.add.yassin.popularmoviespart1.data.api;

import com.add.yassin.popularmoviespart1.data.model.Movie;
import com.add.yassin.popularmoviespart1.data.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("page") int page);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") long id);

}
