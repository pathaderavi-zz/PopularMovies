package com.example.android.popularmovies;

/**
 * Created by ravikiranpathade on 4/10/17.
 */

public class Movie {
    public String getMovieName() {
        return movieName;
    }



    String movieName;
    String id;
    String uniqueID;






   public Movie(String i, String name, String uid){
       this.movieName = name;
       this.id = i;
       this.uniqueID = uid;
   }



}
