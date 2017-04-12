package com.example.android.popularmovies;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private String[] mMovieData;

    private Movie[] movie;

    public MovieAdapter(Movie[] m) {
        movie = m;
        //this.mMovieOnClick = mMovieOnClick;
        //mMovieOnClick = null;
    }






   // private final MovieAdapterOnClickHandler mMovieOnClick;



//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        Movie m = getItem(position);
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.flavor_item, parent, false);
//        }
//        //TextView id = (TextView) convertView.findViewById(R.id.flavor_id);
//        TextView name = (TextView) convertView.findViewById(R.id.flavor_text);
//
//        //id.setText(m.id);
//        name.setText(m.movieName);
//
//        return convertView;
//    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context cont = parent.getContext();
        int layoutid = R.layout.flavor_item;
        boolean shouldAttach = false;

        LayoutInflater inflater = LayoutInflater.from(cont);

        View view = inflater.inflate(layoutid,parent,false);



        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String movie = mMovieData[position];
        holder.mMovieText.setText(movie);


    }

    @Override
    public int getItemCount() {
        if(mMovieData==null){
        return 0;}
        else{
            return mMovieData.length;
        }
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView mMovieText;


        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieText = (TextView) itemView.findViewById(R.id.flavor_text);
        }

    }
}
