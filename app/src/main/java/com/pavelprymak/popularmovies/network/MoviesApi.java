package com.pavelprymak.popularmovies.network;

import com.pavelprymak.popularmovies.network.pojo.movieInfo.MovieDetailsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieReviews.MovieReviewsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieVideos.MovieVideosResponse;
import com.pavelprymak.popularmovies.network.pojo.moviesList.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {

    @GET(Constants.PATH_MAIN + Constants.PATH_POPULAR)
    Call<MoviesResponse> getPopularMoviesList(@Query("api_key") String apiKey, @Query("page") int pageNum);

    @GET(Constants.PATH_MAIN + Constants.PATH_TOP_RATED)
    Call<MoviesResponse> getTopRatedMoviesList(@Query("api_key") String apiKey, @Query("page") int pageNum);

    @GET(Constants.PATH_MAIN + "{movie_id}")
    Call<MovieDetailsResponse> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET(Constants.PATH_MAIN + "{movie_id}" + Constants.PATH_VIDEOS)
    Call<MovieVideosResponse> getMovieVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET(Constants.PATH_MAIN + "{movie_id}" + Constants.PATH_REVIEWS)
    Call<MovieReviewsResponse> getMovieReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
