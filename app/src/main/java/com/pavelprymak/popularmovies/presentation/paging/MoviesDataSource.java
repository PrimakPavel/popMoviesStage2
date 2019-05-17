package com.pavelprymak.popularmovies.presentation.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PositionalDataSource;

import com.facebook.stetho.server.http.HttpStatus;
import com.pavelprymak.popularmovies.network.Constants;
import com.pavelprymak.popularmovies.network.MoviesApiController;
import com.pavelprymak.popularmovies.network.pojo.moviesList.MoviesResponse;
import com.pavelprymak.popularmovies.network.pojo.moviesList.ResultsItem;
import com.pavelprymak.popularmovies.presentation.common.ActionLiveData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesDataSource extends PositionalDataSource<ResultsItem> {
    public static final int POPULAR_MOVIES = 1;
    public static final int TOP_RATED_MOVIES = 2;

    private static final int FIRST_PAGE_NUM = 1;

    private MutableLiveData<Boolean> mLoadData;
    private ActionLiveData<Throwable> mErrorData;
    private int mCurrentPageNum = FIRST_PAGE_NUM;
    private int mLastPageNum = mCurrentPageNum;
    private int mMovieType;

    public MoviesDataSource(MutableLiveData<Boolean> loadData, ActionLiveData<Throwable> errorData, int movieType) {
        mLoadData = loadData;
        mErrorData = errorData;
        mMovieType = movieType;
    }

    private Call<MoviesResponse> getMoviesListByTypeResponseCall() {
        Call<MoviesResponse> responseCall = MoviesApiController.getInstance().getMoviesApi().getPopularMoviesList(Constants.API_KEY, mCurrentPageNum);
        if (mMovieType == TOP_RATED_MOVIES) {
            responseCall = MoviesApiController.getInstance().getMoviesApi().getTopRatedMoviesList(Constants.API_KEY, mCurrentPageNum);
        }
        return responseCall;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<ResultsItem> callback) {
        mErrorData.postValue(null);
        mLoadData.postValue(true);
        getMoviesListByTypeResponseCall().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                mLoadData.postValue(false);
                if (response.code() == HttpStatus.HTTP_OK) {
                    if (response.body() != null) {
                        mCurrentPageNum = response.body().getPage() + 1;
                        mLastPageNum = response.body().getTotalPages();
                        callback.onResult(response.body().getResults(), 0);
                    }
                } else {
                    mErrorData.postValue(new Throwable(String.valueOf(response.code())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                mLoadData.postValue(false);
                mErrorData.postValue(t);
            }
        });
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<ResultsItem> callback) {
        //if pageNum out of pages scope
        if (mCurrentPageNum > mLastPageNum) {
            callback.onResult(new ArrayList<>());
            return;
        }
        mErrorData.postValue(null);
        mLoadData.postValue(true);
        getMoviesListByTypeResponseCall().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                mLoadData.postValue(false);
                if (response.code() == HttpStatus.HTTP_OK) {
                    if (response.body() != null) {
                        mCurrentPageNum = response.body().getPage() + 1;
                        callback.onResult(response.body().getResults());
                    }
                } else {
                    mErrorData.postValue(new Throwable(String.valueOf(response.code())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                mLoadData.postValue(false);
                mErrorData.postValue(t);
            }
        });
    }
}
