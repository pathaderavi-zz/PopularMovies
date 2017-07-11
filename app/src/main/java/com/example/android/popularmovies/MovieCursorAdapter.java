package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import static android.R.attr.id;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by ravikiranpathade on 7/10/17.
 */

public class MovieCursorAdapter extends CursorAdapter {
    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_main_activity,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        ImageView img = (ImageView) view.findViewById(R.id.image_poster);

        String filename = cursor.getString(cursor.getColumnIndex("ID"));
        File imageFile = new File("/data/data/com.example.android.popularmovies/app_PopMov/" + filename + ".jpg");


        Picasso.with(context).load(imageFile).into(img);

    }
}
