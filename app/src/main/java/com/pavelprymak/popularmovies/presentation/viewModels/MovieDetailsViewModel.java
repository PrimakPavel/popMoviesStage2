package com.pavelprymak.popularmovies.presentation.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.facebook.stetho.server.http.HttpStatus;
import com.pavelprymak.popularmovies.App;
import com.pavelprymak.popularmovies.db.FavoriteMovieEntry;
import com.pavelprymak.popularmovies.network.Constants;
import com.pavelprymak.popularmovies.network.MoviesApiController;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.MovieDetailsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieReviews.MovieReviewsResponse;
import com.pavelprymak.popularmovies.network.pojo.movieVideos.MovieVideosResponse;
import com.pavelprymak.popularmovies.presentation.common.StatesBatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsViewModel extends ViewModel {
    public StatesBatch<MovieDetailsResponse> movieDetailsData = new StatesBatch<>();
    public StatesBatch<MovieVideosResponse> movieVideosData = new StatesBatch<>();
    public StatesBatch<MovieReviewsResponse> movieReviewsData = new StatesBatch<>();
    private LiveData<FavoriteMovieEntry> favoriteMovieInfo;

    public LiveData<FavoriteMovieEntry> getFavoriteMovieInfo(int movieId) {
        if (favoriteMovieInfo == null) {
            favoriteMovieInfo = App.dbRepo.loadFavoriteMovieById(movieId);
        }
        return favoriteMovieInfo;
    }

    public void getMovieDetailsById(int id, boolean refreshData) {
        if (movieDetailsData.getData().getValue() == null || refreshData) {

            movieDetailsData.postLoading(true);
            MoviesApiController.getInstance().getMoviesApi().getMovieDetails(id, Constants.API_KEY).enqueue(new Callback<MovieDetailsResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieDetailsResponse> call, @NonNull Response<MovieDetailsResponse> response) {
                    if (response.code() == HttpStatus.HTTP_OK) {
                        movieDetailsData.postValue(response.body());
                    } else {
                        movieDetailsData.postError(new Throwable(String.valueOf(response.code())));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieDetailsResponse> call, @NonNull Throwable t) {
                    movieDetailsData.postError(t);
                }
            });
        }
    }

    public void getMovieVideosById(int id, boolean refreshData) {
        if (movieVideosData.getData().getValue() == null || refreshData) {

            movieVideosData.postLoading(true);
            MoviesApiController.getInstance().getMoviesApi().getMovieVideos(id, Constants.API_KEY).enqueue(new Callback<MovieVideosResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieVideosResponse> call, @NonNull Response<MovieVideosResponse> response) {
                    if (response.code() == HttpStatus.HTTP_OK) {
                        movieVideosData.postValue(response.body());
                    } else {
                        movieVideosData.postError(new Throwable(String.valueOf(response.code())));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieVideosResponse> call, @NonNull Throwable t) {
                    movieVideosData.postError(t);
                }
            });
        }
    }

    public void getMovieReviewsById(int id, boolean refreshData) {
        if (movieReviewsData.getData().getValue() == null || refreshData) {

            movieReviewsData.postLoading(true);
            MoviesApiController.getInstance().getMoviesApi().getMovieReviews(id, Constants.API_KEY).enqueue(new Callback<MovieReviewsResponse>() {
                @Override
                public void onResponse(@NonNull Call<MovieReviewsResponse> call, @NonNull Response<MovieReviewsResponse> response) {
                    if (response.code() == HttpStatus.HTTP_OK) {
                        movieReviewsData.postValue(response.body());
                    } else {
                        movieReviewsData.postError(new Throwable(String.valueOf(response.code())));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MovieReviewsResponse> call, @NonNull Throwable t) {
                    movieReviewsData.postError(t);
                }
            });
        }
    }

    public void addToFavorite(FavoriteMovieEntry favoriteMovie) {
        App.dbRepo.insertFavoriteMovie(favoriteMovie);
    }

    public void deleteFromFavorite(int movieId) {
        App.dbRepo.deleteByMovieId(movieId);
    }
}
