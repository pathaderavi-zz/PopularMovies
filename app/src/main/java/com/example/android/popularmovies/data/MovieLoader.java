package com.example.android.popularmovies.data;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmovies.Movie;

import java.util.List;

/**
 * Created by ravikiranpathade on 4/13/17.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private String mURL;

    public MovieLoader(Context context) {
        super(context);
    }



    @Override
    protected void onStartLoading() {
        //super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {

//        if(){
//
//        }

        //List<Movie> movies = QueryUtils.fetchMovies();
        return null;
    }
}
