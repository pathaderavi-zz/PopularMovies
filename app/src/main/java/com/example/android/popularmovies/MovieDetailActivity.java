package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.QueryUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.title;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static java.lang.System.load;

public class MovieDetailActivity extends AppCompatActivity {

    private RecyclerView mRecycler;

    private RecyclerView mReviewView;
    ReviewAdapter mReviewAdapter;
    //ReviewDetails mReviewDetails;

    //private static String id;
    //private TextView example;
    private ImageView image;
    private TextView movie_title;

    private TextView example3;
    private TextView example4;
    private TextView date;
    List<ReviewDetails> mReviews;

    String imgURL = null;
    ContentValues cv;
    Toast toast;
    Context context;
    //List<String> ab = new ArrayList<>();
    //Intent watchTrailer;
    Button addToFav;
    String id ;
    List<MovieTrailerDetails> mTrailers;

    MovieTrailerDetails trailers;
    TrailerAdapter trailerAdapter;

    Cursor mCursor;
    SingleMovie m;

    String cursorName = null;
    String cursorID = null;
    String cursorVote = null ;
    String cursorOverview = null;
    String cursorDate = null ;
    //boolean isCon = checkConnection();

   // ListView rootView;

    //LayoutInflater inflater;

    public MovieDetailActivity(){

    }



    public MovieDetailActivity (Context cont){
        context = cont;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        context = getApplicationContext();

       // button = (Button) findViewById(R.id.testButton);

        mRecycler = (RecyclerView) findViewById(R.id.trailerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        date = (TextView) findViewById(R.id.date);
        mReviewView = (RecyclerView) findViewById(R.id.reviewsList);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mReviewView.setLayoutManager(layoutManager1);
        mReviewView.setHasFixedSize(true);



        //setSupportActionBar(myToolBar);
        movie_title = (TextView) findViewById(R.id.movie_title_detail);
        example3 = (TextView) findViewById(R.id.example3);
        example4 =  (TextView) findViewById(R.id.example4);

        mTrailers = new ArrayList<>();
        mReviews = new ArrayList<>();

        //inflater = LayoutInflater.from(context);
        trailerAdapter = new TrailerAdapter(mTrailers);
        mRecycler.setAdapter(trailerAdapter);

        mReviewAdapter = new ReviewAdapter(mReviews);
        mReviewView.setAdapter(mReviewAdapter);
        addToFav = (Button) findViewById(R.id.saveFavourite);
        //rootView  = (ListView) findViewById(R.id.trailerList);



        Intent movieid = getIntent();
        //Log.d("Activity Started",id);
        if(movieid!=null){
            if(movieid.hasExtra(Intent.EXTRA_TEXT)){
                id = movieid.getStringExtra(Intent.EXTRA_TEXT);


            }
        }
        if(!checkConnection()){
            TextView trailersHeading = (TextView) findViewById(R.id.trailersHeading);
            TextView reviewHeading = (TextView) findViewById(R.id.reviewHeading);

            trailersHeading.setText("");
            reviewHeading.setText("");



        }
        new FetchSingleMovie().execute(id);


    }


    public class FetchSingleMovie extends AsyncTask<String, Void, SingleMovie>{
        Toast test;
        @Override
        protected SingleMovie doInBackground(String... params) {


        try{
                if(checkConnection()) {

                    m = QueryUtils.fetchSingle(params[0]);

                    mTrailers = QueryUtils.fetchVids(id);


                    mReviews = QueryUtils.getReviews(id);


                }

            mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(String.valueOf(id)).build(),
                   new String[]{"ID"},
                    String.valueOf(id),
                    null,
                    null,
                    null);
            mCursor.moveToLast();
            cursorName = mCursor.getString(1);
            cursorID = mCursor.getString(0);
            cursorOverview = mCursor.getString(2);
            cursorVote = mCursor.getString(3);
            cursorDate = mCursor.getString(4);

            }
            catch(Exception e){e.printStackTrace();}

            return m;
        }


        @Override
        protected void onPostExecute(final SingleMovie singleMovie) {
            super.onPostExecute(singleMovie);


            if(mCursor.getCount()!=0){

                //Log.d(cursorName+"Cursor Nmae",cursorID);
                addToFav.setBackgroundColor(Color.RED);
                addToFav.setText("Remove from Favouites");

            }
            if (singleMovie != null) {
                //myToolBar.setTitle(singleMovie.title);

                if(checkConnection()) {
                    movie_title.setText(m.title);
                    imgURL = singleMovie.poster;
                    example3.setText("Rating : " + singleMovie.rating);
                    example4.setText(singleMovie.synopsis);
                    date.setText(singleMovie.date);

                    image = (ImageView) findViewById(R.id.image_movie);
                    Picasso.with(context)
                            .load(imgURL).resize(320, 470)
                            .into(image);
                }
            }


            if (mTrailers != null) {
                trailerAdapter.setData(mTrailers);
            }
            if (mReviews != null) {
                mReviewAdapter.setData(mReviews);
            }

            if(!checkConnection() && mCursor.getCount()!=0){
               // mCursor.moveToNext();
                movie_title.setText(cursorName);
                addToFav.setBackgroundColor(Color.RED);
                addToFav.setText("Remove from Favouites");
                date.setText("Release Date : "+cursorDate);
                example3.setText("Rating : "+cursorVote);
                example4.setText("Overview : "+cursorOverview);
            }
            if(!checkConnection() && mCursor.getCount()==0){
                addToFav.setVisibility(View.INVISIBLE);
            }


            final String finalCursorID = cursorID;

            final String finalCursorName = cursorName;
            final String finalCursorOverview = cursorOverview;
            final String finalCursorDate = cursorDate;
            final String finalCursorVote = cursorVote;
            addToFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mCursor.getCount() != 0) {
                            Log.d("1","First");
                            Uri uriDelete = MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(finalCursorID).build();

                            getContentResolver().delete(uriDelete, null, null);

                            addToFav.setBackgroundColor(getResources().getColor(R.color.aquaBanner));
                            addToFav.setText("Add to Favourites");
                            //Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                            mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(finalCursorID).build(),
                                    new String[]{"ID"},
                                    String.valueOf(finalCursorID),
                                    null,
                                    null,
                                    null);


                        } else {
                            Log.d("2","Second");
                            if(!checkConnection()){
                                Log.d("3","Third");
                                cv = new ContentValues();
                                mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(String.valueOf(id)).build(),
                                        new String[]{"ID"},
                                        String.valueOf(id),
                                        null,
                                        null,
                                        null);
                                if(mCursor.getCount()==0){
                                Log.d("Check cursor "+finalCursorID,finalCursorName);
                                cv.put(MovieContract.MovieEntry.COLUMN_ID, finalCursorID);
                                cv.put(MovieContract.MovieEntry.COLUMN_NAME, finalCursorName);

                                cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, finalCursorOverview);

                                cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, finalCursorDate);

                                cv.put(MovieContract.MovieEntry.COLUMN_VOTE, finalCursorVote);

                                Log.d("Uri", MovieContract.MovieEntry.FINAL_URI.toString());
                                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.FINAL_URI, cv);

                        if(uri!=null){
                            Toast.makeText(context,uri.toString(),Toast.LENGTH_SHORT).show();
                        }
                                addToFav.setBackgroundColor(Color.RED);
                                addToFav.setText("Remove from Facourites");}
                                else{

                                    Uri uriDelete = MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(finalCursorID).build();

                                    getContentResolver().delete(uriDelete, null, null);

                                    addToFav.setBackgroundColor(getResources().getColor(R.color.aquaBanner));
                                    addToFav.setText("Add to Favourites");
                                    //Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                                    mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(finalCursorID).build(),
                                            new String[]{"ID"},
                                            String.valueOf(finalCursorID),
                                            null,
                                            null,
                                            null);

                                }

                            }else{
                                Log.d("4","Fourth");
                            cv = new ContentValues();
                            cv.put(MovieContract.MovieEntry.COLUMN_ID, singleMovie.id_m);
                            cv.put(MovieContract.MovieEntry.COLUMN_NAME, singleMovie.title);

                            cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, singleMovie.synopsis);

                            cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, singleMovie.date);

                            cv.put(MovieContract.MovieEntry.COLUMN_VOTE, singleMovie.rating);

                            Log.d("Uri", MovieContract.MovieEntry.FINAL_URI.toString());
                            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.FINAL_URI, cv);
//                        if(uri!=null){
//                            Toast.makeText(context,uri.toString(),Toast.LENGTH_SHORT).show();
//                        }
                            addToFav.setBackgroundColor(Color.RED);
                            addToFav.setText("Remove from Facourites");

                            mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(String.valueOf(m.id_m)).build(),
                                    new String[]{"ID"},
                                    String.valueOf(m.id_m),
                                    null,
                                    null,
                                    null);

                        }
                        }

                    }

                });



        }
        }
    public boolean checkConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    }

