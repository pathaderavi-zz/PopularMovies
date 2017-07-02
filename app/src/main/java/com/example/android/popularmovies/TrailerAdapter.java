package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.MovieTrailerDetails;
import com.example.android.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.start;
import static java.security.AccessController.getContext;

/**
 * Created by ravikiranpathade on 6/28/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieViewHolder> {
    public List<MovieTrailerDetails> mTrailerData;



    MovieTrailerDetails trailer;
    Intent intent;
    Context mContext;




    public interface TrailerOnClickListener{
        void onClick(String url);
    }
    private TrailerOnClickListener mTrailerListener;
    public void setOnClickListener(TrailerOnClickListener mListener){
        mTrailerListener = mListener;
    }
    public TrailerAdapter(List<MovieTrailerDetails> mTrailer) {
       mTrailerData = mTrailer;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.list_trailers;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttach = false;

        View view = inflater.inflate(layoutId,parent,shouldAttach);
        MovieViewHolder mHolder = new MovieViewHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mTrailerData.size();
    }

    public void setData(List<MovieTrailerDetails> mDetails){
        mTrailerData = mDetails;
        notifyDataSetChanged();
    }
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView trailerName;
        Context holderContext ;

        public MovieViewHolder(View itemView) {
            super(itemView);
            holderContext = itemView.getContext();
            trailerName = (TextView) itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(this);
        }
        public void bind(int id){
            trailerName.setText(mTrailerData.get(id).trailerName);

        }

        @Override
        public void onClick(View v) {
            //

            //Log.d(trailer.trailerName,trailer.trailerUrl+" Recycler");
            int pos = getAdapterPosition();
            String tName = mTrailerData.get(pos).trailerName;
            String tUrl = mTrailerData.get(pos).trailerUrl;
            intent = new Intent(Intent.ACTION_VIEW,Uri.parse(tUrl));
            holderContext.startActivity(intent);
            //Log.d(tName,tUrl+" Recycler");
        }
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        mContext = getContext();
//        trailer = getItem(position);
//        if(convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_trailers,parent,false);
//
//        }
//        TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_name);
//        trailerName.setText(trailer.getTrailerName());
//
//        Log.d(trailer.trailerName,trailer.trailerUrl+" Here");
//        trailerName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.trailerUrl)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//
//            }
//        });
//
//        return convertView;
//    }
}
