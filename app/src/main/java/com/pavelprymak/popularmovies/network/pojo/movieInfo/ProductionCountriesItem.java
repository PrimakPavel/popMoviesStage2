package com.pavelprymak.popularmovies.network.pojo.movieInfo;

import com.squareup.moshi.Json;

public class ProductionCountriesItem{

	@Json(name = "iso_3166_1")
	private String iso31661;

	@Json(name = "name")
	private String name;

	public void setIso31661(String iso31661){
		this.iso31661 = iso31661;
	}

	public String getIso31661(){
		return iso31661;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return 
			"ProductionCountriesItem{" + 
			"iso_3166_1 = '" + iso31661 + '\'' + 
			",name = '" + name + '\'' + 
			"}";
		}
}