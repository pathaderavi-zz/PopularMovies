package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ravikiranpathade on 7/6/17.
 */

public class MovieContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIES_ID = 101;

    private static final UriMatcher sUrimatcher = buildUriMatcher();




    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES,MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES+"/#",MOVIES_ID);

        return uriMatcher;
    }
    private MovieDBHelper movieDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDBHelper = new MovieDBHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int matchUri = sUrimatcher.match(uri);
        Log.d("URI",uri.toString());

        final SQLiteDatabase db  = movieDBHelper.getWritableDatabase();



        Uri returnUri = null;

        switch (matchUri){
            case MOVIES:
                    long id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);

                if(id>0){
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.FINAL_URI,id);
                }else{
                    throw new SQLException("Failed to insert");
                }

            case MOVIES_ID:

                default:
        }
        getContext().getContentResolver().notifyChange(returnUri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
