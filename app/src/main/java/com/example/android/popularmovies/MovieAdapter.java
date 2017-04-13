package com.example.android.popularmovies;

import android.content.Context;
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

import com.squareup.picasso.Picasso;

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

        Movie m = getItem(position);

        //Log.d("Name of the Movie: ",m.getMovieName());
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.flavor_item, parent, false);
        }
        ImageView id = (ImageView) convertView.findViewById(R.id.image_poster);
        TextView name = (TextView) convertView.findViewById(R.id.flavor_text);

        Picasso.with(getContext()).load(m.id).resize(390,500).into(id);
        //name.setText(m.movieName);


        return convertView;
    }
}
