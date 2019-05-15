package com.pavelprymak.popularmovies.presentation.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.facebook.stetho.server.http.HttpStatus;
import com.pavelprymak.popularmovies.network.Constants;
import com.pavelprymak.popularmovies.network.MoviesApiController;
import com.pavelprymak.popularmovies.network.pojo.movieInfo.MovieDetailsResponse;
import com.pavelprymak.popularmovies.presentation.common.StatesBatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsViewModel extends ViewModel {
    public StatesBatch<MovieDetailsResponse> movieDetailsData = new StatesBatch<>();

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
}
