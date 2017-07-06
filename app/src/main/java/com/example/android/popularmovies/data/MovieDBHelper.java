package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.R.attr.version;

/**
 * Created by ravikiranpathade on 7/6/17.
 */

public class MovieDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "moviesDB.db";

    private static final int VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context,DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Table","Created");
        final String CREATE_TABLE = "CREATE TABLE "+ MovieContract.MovieEntry.TABLE_NAME+" ("+
                MovieContract.MovieEntry.COLUMN_ID+" INTEGER PRIMARY KEY, "
                + MovieContract.MovieEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_OVERVIEW+" TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_RELEASE_DATE+" TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_VOTE+" TEXT NOT NULL);";

        Log.d("CREATE TABLE NAME : ", CREATE_TABLE);
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
