package com.pavelprymak.popularmovies.network.pojo.movieInfo;

import com.squareup.moshi.Json;


public class SpokenLanguagesItem {

    @Json(name = "name")
    private String name;

    @Json(name = "iso_639_1")
    private String iso6391;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getIso6391() {
        return iso6391;
    }

    @Override
    public String toString() {
        return
                "SpokenLanguagesItem{" +
                        "name = '" + name + '\'' +
                        ",iso_639_1 = '" + iso6391 + '\'' +
                        "}";
    }
}