package com.pavelprymak.popularmovies.network.pojo.movieVideos;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class MovieVideosResponse{

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<TrailersResultsItem> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<TrailersResultsItem> results){
		this.results = results;
	}

	public List<TrailersResultsItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"MovieVideosResponse{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}