package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
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


    //private static String id;
    private TextView example;
    private ImageView image;
    private TextView movie_title;

    private TextView example3;
    private TextView example4;
    private TextView date;

    String imgURL = null;

    Toast toast;
    Context context;
    List<String> ab = new ArrayList<>();
    Intent watchTrailer;
   // Button button;
    String id ;
    List<MovieTrailerDetails> mTrailers;
    MovieTrailerDetails trailers;
    TrailerAdapter trailerAdapter;

    ListView rootView;

    LayoutInflater inflater;

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



        //setSupportActionBar(myToolBar);
        movie_title = (TextView) findViewById(R.id.movie_title_detail);
        example3 = (TextView) findViewById(R.id.example3);
        example4 =  (TextView) findViewById(R.id.example4);
        mTrailers = new ArrayList<>();

        //inflater = LayoutInflater.from(context);
        trailerAdapter = new TrailerAdapter(mTrailers);
        mRecycler.setAdapter(trailerAdapter);

        //rootView  = (ListView) findViewById(R.id.trailerList);



        Intent movieid = getIntent();
        //Log.d("Activity Started",id);
        if(movieid!=null){
            if(movieid.hasExtra(Intent.EXTRA_TEXT)){
                id = movieid.getStringExtra(Intent.EXTRA_TEXT);


            }
        }

        new FetchSingleMovie().execute(id);






    }


    public class FetchSingleMovie extends AsyncTask<String, Void, SingleMovie>{
        @Override
        protected SingleMovie doInBackground(String... params) {
            SingleMovie m = null;

        try{

                m = QueryUtils.fetchSingle(params[0]);

                mTrailers = QueryUtils.fetchVids(id);







            }
            catch(Exception e){e.printStackTrace();}

            return m;
        }


        @Override
        protected void onPostExecute(final SingleMovie singleMovie) {
            super.onPostExecute(singleMovie);

            //myToolBar.setTitle(singleMovie.title);
            movie_title.setText(singleMovie.title);
            date = (TextView) findViewById(R.id.date);
            imgURL = singleMovie.poster;
            image = (ImageView) findViewById(R.id.image_movie);
            example3.setText("Rating : "+singleMovie.rating);

            example4.setText(singleMovie.synopsis);

            ViewGroup view = (ViewGroup) findViewById(R.id.viewGroup);
//            LayoutInflater inflater = LayoutInflater.from(context);
//            View customView = inflater.inflate(R.layout.list_trailers,view,false);


//            TrailerAdapter listAdapter = new TrailerAdapter(context,mTrailers);
//            ListView listView = (ListView) findViewById(R.id.trailerList);
//
//            listView.setAdapter(listAdapter);

            Log.d("Size of array",String.valueOf(mTrailers.size()));


            if(mTrailers!=null){
                trailerAdapter.setData(mTrailers);
            }


            date.setText(singleMovie.date);



        Picasso.with(context)
                .load(imgURL).resize(320,470)
                .into(image);





        }

    }
}
