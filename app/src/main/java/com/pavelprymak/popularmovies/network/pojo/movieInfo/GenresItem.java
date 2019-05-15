package com.pavelprymak.popularmovies.network.pojo.movieInfo;

import com.squareup.moshi.Json;

public class GenresItem{

	@Json(name = "name")
	private String name;

	@Json(name = "id")
	private int id;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"GenresItem{" + 
			"name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}