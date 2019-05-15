package com.pavelprymak.popularmovies.presentation.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.pavelprymak.popularmovies.network.pojo.moviesList.ResultsItem;
import com.pavelprymak.popularmovies.presentation.common.ActionLiveData;

public class MoviesDataSourceFactory extends DataSource.Factory<Integer, ResultsItem> {
    private MutableLiveData<Boolean> mLoadData;
    private ActionLiveData<Throwable> mErrorData;
    private int mMovieType;

    public MoviesDataSourceFactory(MutableLiveData<Boolean> loadData, ActionLiveData<Throwable> errorData, int movieType) {
        mLoadData = loadData;
        mErrorData = errorData;
        mMovieType = movieType;
    }

    @NonNull
    @Override
    public DataSource<Integer, ResultsItem> create() {
        return new MoviesDataSource(mLoadData, mErrorData, mMovieType);
    }
}
