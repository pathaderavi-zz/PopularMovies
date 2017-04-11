package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.flavor_item, parent, false);
        }
        //TextView id = (TextView) convertView.findViewById(R.id.flavor_id);
        TextView name = (TextView) convertView.findViewById(R.id.flavor_text);

        //id.setText(m.id);
        name.setText(m.movieName);

        return convertView;
    }
}
