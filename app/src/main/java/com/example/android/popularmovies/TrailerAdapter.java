package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.MovieTrailerDetails;
import com.example.android.popularmovies.R;

import java.util.List;

import static android.R.attr.start;

/**
 * Created by ravikiranpathade on 6/28/17.
 */

public class TrailerAdapter extends ArrayAdapter<MovieTrailerDetails> {
    MovieTrailerDetails trailer;
    Intent intent;
    Context mContext;

    public TrailerAdapter(Context context, List<MovieTrailerDetails> objects) {

        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        mContext = getContext();
        trailer = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_trailers,parent,false);

        }
        TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_name);
        trailerName.setText(trailer.getTrailerName());

        Log.d(trailer.trailerName,trailer.trailerUrl+" Here");
        trailerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.trailerUrl)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

        return convertView;
    }
}
