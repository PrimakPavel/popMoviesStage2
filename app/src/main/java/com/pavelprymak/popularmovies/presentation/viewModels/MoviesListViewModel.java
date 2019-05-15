package com.pavelprymak.popularmovies.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.pavelprymak.popularmovies.network.pojo.moviesList.ResultsItem;
import com.pavelprymak.popularmovies.presentation.common.ActionLiveData;
import com.pavelprymak.popularmovies.presentation.paging.MoviesDataSourceFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.POPULAR_MOVIES;

public class MoviesListViewModel extends ViewModel {
    private static final int MOVIE_ITEM_ON_PAGE = 20;
    private static final int THREAD_POOL_SIZE = 3;

    private MediatorLiveData<PagedList<ResultsItem>> mMoviesData = new MediatorLiveData<>();
    private LiveData<PagedList<ResultsItem>> mMoviesPagingLiveData;
    private MutableLiveData<Boolean> mLoadData = new MutableLiveData<>();
    private ActionLiveData<Throwable> mErrorData = new ActionLiveData<>();
    private Executor executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private int mMovieFilterType = POPULAR_MOVIES;


    public void prepareMoviePagedList() {
        if (mMoviesPagingLiveData == null || mMoviesPagingLiveData.getValue() == null) {
            mLoadData.postValue(false);
            mErrorData.postValue(null);
            MoviesDataSourceFactory moviesDataSourceFactory = new MoviesDataSourceFactory(mLoadData, mErrorData, mMovieFilterType);
            PagedList.Config pagedListConfig = new PagedList.Config.Builder().setEnablePlaceholders(false)
                    .setPageSize(MOVIE_ITEM_ON_PAGE)
                    .build();

            mMoviesPagingLiveData = new LivePagedListBuilder<>(moviesDataSourceFactory, pagedListConfig)
                    .setFetchExecutor(executor)
                    .build();
            mMoviesData.addSource(mMoviesPagingLiveData, resultsItems -> mMoviesData.setValue(resultsItems));
        }
    }

    public void changeMoviePagedListType(int movieType) {
        if (movieType != mMovieFilterType) {
            if (mMoviesPagingLiveData != null) {
                mMoviesData.removeSource(mMoviesPagingLiveData);
            }
            cleanData();
            mMovieFilterType = movieType;
        }
        prepareMoviePagedList();
    }

    public void cleanData() {
        mMoviesPagingLiveData = null;
        mLoadData.setValue(false);
        mErrorData.setValue(null);
    }

    public LiveData<PagedList<ResultsItem>> getMoviesData() {
        return mMoviesData;
    }

    public LiveData<Boolean> getLoadingData() {
        return mLoadData;
    }

    public LiveData<Throwable> getErrorData() {
        return mErrorData;
    }

    public int getSelectedFilterType() {
        return mMovieFilterType;
    }
}