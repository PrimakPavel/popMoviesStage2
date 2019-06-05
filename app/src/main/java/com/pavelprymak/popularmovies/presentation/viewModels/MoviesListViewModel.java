package com.pavelprymak.popularmovies.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.pavelprymak.popularmovies.App;
import com.pavelprymak.popularmovies.network.Constants;
import com.pavelprymak.popularmovies.network.pojo.moviesList.ResultsItem;
import com.pavelprymak.popularmovies.presentation.common.ActionLiveData;
import com.pavelprymak.popularmovies.presentation.paging.MoviesDataSourceFactory;
import com.pavelprymak.popularmovies.utils.convertors.FavoriteMovieEntryToResultsItemConverter;
import com.pavelprymak.popularmovies.utils.convertors.ListToPagedListConverter;

import java.util.List;

import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.FAVORITE_MOVIES;
import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.POPULAR_MOVIES;
import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.TOP_RATED_MOVIES;

public class MoviesListViewModel extends ViewModel {
    private static final int MOVIE_ITEM_ON_PAGE = 20;

    private MediatorLiveData<PagedList<ResultsItem>> mMoviesData = new MediatorLiveData<>();
    private LiveData<PagedList<ResultsItem>> mMoviesPagingLiveData;
    private LiveData<PagedList<ResultsItem>> mMoviesFavoriteLiveData;
    private MutableLiveData<Boolean> mLoadData = new MutableLiveData<>();
    private ActionLiveData<Throwable> mErrorData = new ActionLiveData<>();
    private int mMovieFilterType = POPULAR_MOVIES;


    public void prepareMoviePagedList() {
        if (mMovieFilterType == POPULAR_MOVIES || mMovieFilterType == TOP_RATED_MOVIES)
            preparePopularAndTopPagedList();
        else if (mMovieFilterType == FAVORITE_MOVIES)
            prepareFavoritesPagedList();
    }

    public void changeMoviePagedListType(int movieType) {
        if (movieType != mMovieFilterType) {
            cleanData();
            mMovieFilterType = movieType;
            if (movieType == FAVORITE_MOVIES) {
                prepareFavoritesPagedList();
            } else {
                preparePopularAndTopPagedList();
            }
        }
    }

    public void cleanData() {
        if (mMoviesPagingLiveData != null) {
            mMoviesData.removeSource(mMoviesPagingLiveData);
        }
        if (mMoviesFavoriteLiveData != null) {
            mMoviesData.removeSource(mMoviesFavoriteLiveData);
        }
        mMoviesPagingLiveData = null;
        mMoviesFavoriteLiveData = null;
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

    private void preparePopularAndTopPagedList() {
        if (mMoviesPagingLiveData == null || mMoviesPagingLiveData.getValue() == null) {
            mLoadData.postValue(false);
            mErrorData.postValue(null);
            MoviesDataSourceFactory moviesDataSourceFactory = new MoviesDataSourceFactory(mLoadData, mErrorData, mMovieFilterType);
            PagedList.Config pagedListConfig = new PagedList.Config.Builder().setEnablePlaceholders(false)
                    .setPageSize(MOVIE_ITEM_ON_PAGE)
                    .build();

            mMoviesPagingLiveData = new LivePagedListBuilder<>(moviesDataSourceFactory, pagedListConfig)
                    .setFetchExecutor(App.appExecutors.networkIO())
                    .build();
            mMoviesData.addSource(mMoviesPagingLiveData, resultsItems -> mMoviesData.setValue(resultsItems));
        }
    }

    private void prepareFavoritesPagedList() {
        mLoadData.postValue(false);
        mErrorData.postValue(null);
        if (mMoviesFavoriteLiveData == null || mMoviesFavoriteLiveData.getValue() == null) {
            mMoviesFavoriteLiveData = Transformations.map(App.dbRepo.loadAllFavorites(), input -> {
                if (input != null && input.size() > 0) {
                    List<ResultsItem> resultsItemList = FavoriteMovieEntryToResultsItemConverter.convert(input);
                    return ListToPagedListConverter.getInstance().convert(resultsItemList);
                } else {
                    mErrorData.setValue(new Throwable(Constants.DbErrorCodes.DB_EMPTY_ERROR));
                    return null;
                }
            });
            mMoviesData.addSource(mMoviesFavoriteLiveData, resultsItems -> mMoviesData.setValue(resultsItems));
        }
    }
}