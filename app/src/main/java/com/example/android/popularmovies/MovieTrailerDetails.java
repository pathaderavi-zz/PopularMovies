package com.example.android.popularmovies;

/**
 * Created by ravikiranpathade on 6/28/17.
 */

public class MovieTrailerDetails {

    String trailerUrl;
    String trailerName;

    public MovieTrailerDetails(String url, String name){
        this.trailerUrl = url;
        this.trailerName = name;
    }
    public String getTrailerUrl(){
        return trailerUrl;
    }
    public String getTrailerName(){
        return trailerName;
    }
}
