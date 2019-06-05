package com.pavelprymak.popularmovies.utils.convertors;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PositionalDataSource;

import com.pavelprymak.popularmovies.App;
import com.pavelprymak.popularmovies.network.pojo.moviesList.ResultsItem;

import java.util.ArrayList;
import java.util.List;

public class ListToPagedListConverter {

    private static ListToPagedListConverter mInstance;

    public static ListToPagedListConverter getInstance() {
        if (mInstance == null) {
            mInstance = new ListToPagedListConverter();
        }
        return mInstance;
    }

    public PagedList<ResultsItem> convert(List<ResultsItem> list) {
        StringDataSource dataSource = new StringDataSource(list);
        PagedList.Config myConfig = new PagedList.Config.Builder().setEnablePlaceholders(false)
                .setPageSize(list.size())
                .build();
        return new PagedList.Builder<>(dataSource, myConfig)
                .setFetchExecutor(App.appExecutors.networkIO())
                .setNotifyExecutor(App.appExecutors.networkIO())
                .build();
    }


    private class StringDataSource extends PositionalDataSource<ResultsItem> {

        private List<ResultsItem> list;

        private StringDataSource(List<ResultsItem> list) {
            this.list = list;
        }

        @Override
        public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<ResultsItem> callback) {
            callback.onResult(list, 0);
        }

        @Override
        public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<ResultsItem> callback) {
            callback.onResult(new ArrayList<>());
        }
    }
}
