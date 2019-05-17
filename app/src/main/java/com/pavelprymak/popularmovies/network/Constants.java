package com.pavelprymak.popularmovies.network;

import com.pavelprymak.popularmovies.BuildConfig;

public class Constants {

    public static final String API_KEY = BuildConfig.API_KEY;

    static final String BASE_URL = "https://api.themoviedb.org/";
    public static final String PICTURE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    public static final String PICTURE_SMALL_BASE_URL = "https://image.tmdb.org/t/p/w92";
    public static final String PICTURE_HUGE_BASE_URL = "https://image.tmdb.org/t/p/w342";

    static final String PATH_MAIN = "3/movie/";
    static final String PATH_POPULAR = "popular";
    static final String PATH_TOP_RATED = "top_rated";

    public  class HttpErrorCodes{
        public static final String HTTP_AUTH_ERROR = "401";
        public static final String HTTP_NOT_FOUND_ERROR = "404";
    }
}
