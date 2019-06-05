package com.pavelprymak.popularmovies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movies ORDER BY updated_at")
    LiveData<List<FavoriteMovieEntry>> loadAllFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(FavoriteMovieEntry favoriteMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(FavoriteMovieEntry favoriteMovie);

    @Delete
    void deleteFavoriteMovie(FavoriteMovieEntry favoriteMovie);

    @Query("DELETE FROM favorite_movies WHERE id = :movieId")
    void deleteByMovieId(long movieId);

    @Query("SELECT * FROM favorite_movies WHERE id = :id")
    LiveData<FavoriteMovieEntry> loadFavoriteMovieById(int id);
}
