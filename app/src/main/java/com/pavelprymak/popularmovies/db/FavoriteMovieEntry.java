package com.pavelprymak.popularmovies.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "favorite_movies")
public class FavoriteMovieEntry {

    @PrimaryKey//(autoGenerate = false)
    private int id;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @ColumnInfo(name = "imdb_rating")
    private Double imdbRating;

    @ColumnInfo(name = "original_language")
    private String originalLanguage;

    public FavoriteMovieEntry(int id, Date updatedAt, String title, String posterPath, String releaseDate, Double imdbRating, String originalLanguage) {
        this.id = id;
        this.updatedAt = updatedAt;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.imdbRating = imdbRating;
        this.originalLanguage = originalLanguage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    @Override
    public String toString() {
        return "FavoriteMovieEntry{" +
                "id=" + id +
                ", updatedAt=" + updatedAt +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", imdbId='" + imdbRating + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                '}';
    }
}
