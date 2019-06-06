package com.pavelprymak.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.pavelprymak.popularmovies.presentation.paging.MoviesDataSource.POPULAR_MOVIES;

public class SharedPrefApp {
    private static final String PREF_MOVIES_FILTER_TYPE = "MoviesListFilterType";

    public static void setMoviesFilterType(Context context, int filterType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(PREF_MOVIES_FILTER_TYPE, filterType);
        editor.apply();
    }

    public static int getMovieFilterType(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_MOVIES_FILTER_TYPE, POPULAR_MOVIES);
    }
}
