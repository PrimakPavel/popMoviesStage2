package com.pavelprymak.popularmovies.utils.convertors;

import com.pavelprymak.popularmovies.db.FavoriteMovieEntry;
import com.pavelprymak.popularmovies.network.pojo.moviesList.ResultsItem;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMovieEntryToResultsItemConverter {

    public static List<ResultsItem> convert(List<FavoriteMovieEntry> favoriteMovieEntries){
        ArrayList<ResultsItem> resultList = new ArrayList<>();
        for (FavoriteMovieEntry favoriteMovieEntry:favoriteMovieEntries){
            ResultsItem resultsItem = new ResultsItem();
            resultsItem.setUpdatedAt(favoriteMovieEntry.getUpdatedAt().getTime());
            resultsItem.setId(favoriteMovieEntry.getId());
            resultsItem.setOriginalTitle(favoriteMovieEntry.getTitle());
            resultsItem.setPosterPath(favoriteMovieEntry.getPosterPath());
            resultsItem.setReleaseDate(favoriteMovieEntry.getReleaseDate());
            resultsItem.setVoteAverage(favoriteMovieEntry.getImdbRating());
            resultsItem.setOriginalLanguage(favoriteMovieEntry.getOriginalLanguage());
            resultList.add(resultsItem);
        }
        return resultList;
    }
}
