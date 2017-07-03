package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.R.attr.id;

/**
 * Created by ravikiranpathade on 7/2/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    ReviewDetails reviewDetails;
    Context context;
    Intent intent;
    public List<ReviewDetails> mReviewsData;

    public interface ReviewOnClickListener{
        void onClick(String url);
    }
    public ReviewOnClickListener mClickListener;

    public void setOnClickListener(ReviewOnClickListener mListener){
        mClickListener = mListener;
    }


    public ReviewAdapter(List<ReviewDetails> mReviews){
        mReviewsData = mReviews;
    }
    public void setData(List<ReviewDetails> rDetails){
        mReviewsData = rDetails;
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.reviews_list;
        context = parent.getContext();
        boolean shouldAttach = false;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, shouldAttach);
        ReviewViewHolder viewHolder = new ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
            holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mReviewsData.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView reviewName;
        TextView content;
        Context holderContext;
        Intent reviewLink;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            holderContext = itemView.getContext();
            reviewName = (TextView) itemView.findViewById(R.id.review_name);
            content = (TextView) itemView.findViewById(R.id.review_content);
            itemView.setOnClickListener(this);

        }

        public void bind(int id){

            if(mReviewsData.get(id).content.length() >250){
                content.setText(mReviewsData.get(id).content.substring(0,250)+"...TAP TO SEE FULL REVIEW");
            }else{
            content.setText(mReviewsData.get(id).content);}
            reviewName.setText(mReviewsData.get(id).author);



        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if(mReviewsData.get(pos).content.length() >250){

            String url = mReviewsData.get(pos).url;
            reviewLink = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
            holderContext.startActivity(reviewLink);
            }

            //Toast.makeText(holderContext,,Toast.LENGTH_SHORT).show();

        }
    }


}
