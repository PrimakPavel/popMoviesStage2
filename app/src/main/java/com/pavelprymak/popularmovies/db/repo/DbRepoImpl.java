package com.pavelprymak.popularmovies.db.repo;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.pavelprymak.popularmovies.db.AppDatabase;
import com.pavelprymak.popularmovies.db.FavoriteMovieEntry;

import java.util.List;
import java.util.concurrent.Executor;

public class DbRepoImpl implements DbRepo {
    private final AppDatabase mDb;
    private final Executor diskIO;

    public DbRepoImpl(@NonNull AppDatabase appDatabase, @NonNull Executor discIOExecutor) {
        mDb = appDatabase;
        diskIO = discIOExecutor;
    }

    @Override
    public void insertFavoriteMovie(FavoriteMovieEntry favoriteMovie) {
        diskIO.execute(() -> mDb.favoriteMoviesDao().insertFavoriteMovie(favoriteMovie));
    }

    @Override
    public void updateFavoriteMovie(FavoriteMovieEntry favoriteMovie) {
        diskIO.execute(() -> mDb.favoriteMoviesDao().updateFavoriteMovie(favoriteMovie));
    }

    @Override
    public void deleteFavoriteMovie(FavoriteMovieEntry favoriteMovie) {
        diskIO.execute(() -> mDb.favoriteMoviesDao().deleteFavoriteMovie(favoriteMovie));
    }

    @Override
    public void deleteByMovieId(final long movieId) {
        diskIO.execute(() -> mDb.favoriteMoviesDao().deleteByMovieId(movieId));
    }

    @Override
    public LiveData<List<FavoriteMovieEntry>> loadAllFavorites() {
        return mDb.favoriteMoviesDao().loadAllFavorites();
    }

    @Override
    public LiveData<FavoriteMovieEntry> loadFavoriteMovieById(int id) {
        return mDb.favoriteMoviesDao().loadFavoriteMovieById(id);
    }
}
