package com.example.android.popularmovies;

/**
 * Created by ravikiranpathade on 4/13/17.
 */

public class SingleMovie {
    String title; // title
    String poster; //poster_path
    String synopsis; //overview
    double rating; //vote_average
    String date; //release_date
    String id_m;
    double popular;

    public SingleMovie(String t, String p, String s, double r, String d, String id_movie,double pop){
        this.title = t;

        this.poster = p;

        this.synopsis = s;

        this.rating = r;

        this.date = d ;

        this.id_m = id_movie;

        this.popular = pop;

        //this.trailerUrl = trailer;

    }

}
