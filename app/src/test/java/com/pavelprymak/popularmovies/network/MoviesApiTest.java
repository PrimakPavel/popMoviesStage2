package com.pavelprymak.popularmovies.network;

import com.pavelprymak.popularmovies.network.pojo.movieInfo.MovieDetailsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieReviews.MovieReviewsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieVideos.MovieVideosResponse;
import com.pavelprymak.popularmovies.network.pojo.moviesList.MoviesResponse;

import org.junit.Before;
import org.junit.Test;

import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class MoviesApiTest {
    private MoviesApi api;

    @Before
    public void before() {
        api = MoviesApiController.getInstance().getMoviesApi();
    }


    @Test
    public void getPopularMovieList() throws Exception {
        int pageNumber = 5;
        Response<MoviesResponse> response = api.getPopularMoviesList(Constants.API_KEY, pageNumber).execute();
        MoviesResponse moviesResponse = response.body();
        assertNotNull(moviesResponse);
        assertEquals(pageNumber, moviesResponse.getPage());
    }

    @Test
    public void getTopMovieList() throws Exception {
        int pageNumber = 48;
        Response<MoviesResponse> response = api.getTopRatedMoviesList(Constants.API_KEY, pageNumber).execute();
        MoviesResponse moviesResponse = response.body();
        assertNotNull(moviesResponse);
        assertEquals(pageNumber, moviesResponse.getPage());

    }

    @Test
    public void getMovieDetails() throws Exception {
        final int movieId = 550;
        Response<MovieDetailsResponse> response = api.getMovieDetails(movieId, Constants.API_KEY).execute();
        MovieDetailsResponse movieDetailsResponse = response.body();
        assertNotNull(movieDetailsResponse);
        assertEquals(movieId, movieDetailsResponse.getId());
    }

    @Test
    public void getMovieVideos() throws Exception {
        final int movieId = 550;
        Response<MovieVideosResponse> response = api.getMovieVideos(movieId, Constants.API_KEY).execute();
        MovieVideosResponse movieVideosResponse = response.body();
        assertNotNull(movieVideosResponse);
        assertEquals(movieId, movieVideosResponse.getId());
    }

    @Test
    public void getMovieReviews() throws Exception {
        final int movieId = 550;
        Response<MovieReviewsResponse> response = api.getMovieReviews(movieId, Constants.API_KEY).execute();
        MovieReviewsResponse movieReviewsResponse = response.body();
        assertNotNull(movieReviewsResponse);
        assertEquals(movieId, movieReviewsResponse.getId());
    }
}