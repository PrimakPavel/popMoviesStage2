package com.pavelprymak.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.pavelprymak.popularmovies.db.AppDatabase;
import com.pavelprymak.popularmovies.db.repo.DbRepoImpl;
import com.pavelprymak.popularmovies.utils.AppExecutors;

public class App extends Application {
    public static AppExecutors appExecutors;
    public static DbRepoImpl dbRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        //Stetho
        Stetho.initializeWithDefaults(this);
        //AppExecutor
        appExecutors = new AppExecutors();
        //DB Repository
        dbRepo = new DbRepoImpl(AppDatabase.getInstance(getApplicationContext()), appExecutors.diskIO());
    }
}
