package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by ravikiranpathade on 7/6/17.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.android.popularmovies";
    public static final Uri baseUri = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_MOVIES = "movies";



    public static final class MovieEntry implements BaseColumns{

        public static final Uri FINAL_URI = baseUri.buildUpon().appendPath(PATH_MOVIES).build();


        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_OVERVIEW = "OVERVIEW";
        public static final String COLUMN_VOTE = "VOTE";
        public static final String COLUMN_RELEASE_DATE = "RELEASE_DATE";


    }

}
