package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.R.attr.id;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.os.Build.ID;

/**
 * Created by ravikiranpathade on 7/6/17.
 */

public class MovieContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIES_ID = 101;

    private static final UriMatcher sUrimatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIES_ID);

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

        //uri = Uri.parse(uri.toString()+"/#");
        final SQLiteDatabase db = movieDBHelper.getReadableDatabase();

        //uri.buildUpon().appendPath("ID").build();
        int match = sUrimatcher.match(uri);

//        Log.d(uri.toString(),selection);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        //sortOrder = pref.getString("sort_by_c","");
        Cursor retCusor;

        switch (match) {
            case MOVIES_ID: {
                selectionArgs = new String[]{"ID=?"};
                retCusor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        "ID=?",
                        new String[]{selection},
                        null,
                        null,
                        null);
                Log.d(String.valueOf(retCusor.getCount()), "Here");
                break;
            }
            //TODO
            case MOVIES: {
                retCusor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unable to find " + uri);
        }

        return retCusor;
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
        Log.d("URI", uri.toString());

        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();


        Uri returnUri = null;

        switch (matchUri) {
            case MOVIES:
                long id;
                try {
                    id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                } catch (Exception e) {
                    throw new android.database.SQLException("Failed to insert");
                }
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.FINAL_URI, id);
                    Log.d("Hee", returnUri.toString());
                }
                break;
            case MOVIES_ID:

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        int match = sUrimatcher.match(uri);

        int movieDeleted;

        switch (match) {
            case MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                Log.d("path segment", id);

                movieDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME, "ID=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri " + uri);

        }
        if (movieDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            Log.d("Movie Deleted ", String.valueOf(movieDeleted));
        }
        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
