package com.example.android.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.resource;

/**
 * Created by ravikiranpathade on 4/10/17.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Context context, List<Movie> objects) {

        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File imageFile = null;
        Movie m = getItem(position);

        //Log.d("Name of the Movie: ",m.getMovieName());
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.flavor_item, parent, false);
        }

        ImageView id = (ImageView) convertView.findViewById(R.id.image_poster);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortO = sharedPreferences.getString("sort_by_c","");
        Cursor m1 = getContext().getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().build(),
                null,
                null,
                null,
                sortO);
        String filename;
        boolean showFav = sharedPreferences.getBoolean("showFav",false);
        if(!checkConnection()){
            showFav=true;
        }
        if (m1 != null && m1.moveToPosition(position) && showFav) {

            String id_cursor = m1.getString(m1.getColumnIndex("ID"));
            filename = m1.getString(m1.getColumnIndex("ID"));
            imageFile = new File("/data/data/com.example.android.popularmovies/app_PopMov/" + filename + ".jpg");
            Log.d(String.valueOf(imageFile.exists()), " File Status " + m1.getString(m1.getColumnIndex("NAME")));
            Log.d(imageFile.toString(), sortO);
            Picasso.with(getContext()).load(imageFile).into(id);
            //Log.d(m1.getString(m1.getColumnIndex("ID")), "Check Cursor ID through Movie Adapter ");
        }

        if (checkConnection() && !showFav) {
            Picasso.with(getContext()).load(m.id).into(id);
        }

        //
        m1.close();
        //name.setText(m.movieName);


        return convertView;
    }

    public boolean checkConnection() {

        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
