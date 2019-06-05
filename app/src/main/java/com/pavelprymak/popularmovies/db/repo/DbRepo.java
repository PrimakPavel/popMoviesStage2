package com.pavelprymak.popularmovies.db.repo;

import androidx.lifecycle.LiveData;

import com.pavelprymak.popularmovies.db.FavoriteMovieEntry;

import java.util.List;

public interface DbRepo {
    void insertFavoriteMovie(FavoriteMovieEntry favoriteMovie);
    void updateFavoriteMovie(FavoriteMovieEntry taskEntry);
    void deleteFavoriteMovie(FavoriteMovieEntry taskEntry);
    void deleteByMovieId(long movieId);
    LiveData<List<FavoriteMovieEntry>> loadAllFavorites();
    LiveData<FavoriteMovieEntry> loadFavoriteMovieById(int id);
}
