package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.QueryUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.title;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static java.lang.System.load;

public class MovieDetailActivity extends AppCompatActivity {


    //private static String id;
    private TextView example;
    private ImageView image;
    private TextView example2;
    private TextView example3;
    private TextView example4;
    Toolbar myToolBar;
    String imgURL = null;
    Toast toast;
    Context context;
    List<String> ab = new ArrayList<>();
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

        myToolBar = (Toolbar) findViewById(R.id.child_toolbar);
        //setSupportActionBar(myToolBar);




        //example1 = (TextView) findViewById(R.id.example1);
        example2 = (TextView) findViewById(R.id.example2);
        example3 = (TextView) findViewById(R.id.example3);
        example4 =  (TextView) findViewById(R.id.example4);
        String id = null;

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


            }
            catch(Exception e){e.printStackTrace();}

            return m;
        }

        @Override
        protected void onPostExecute(SingleMovie singleMovie) {
            super.onPostExecute(singleMovie);

            myToolBar.setTitle(singleMovie.title);


            imgURL = singleMovie.poster;
            image = (ImageView) findViewById(R.id.image_movie);
            example2.setText(singleMovie.poster);
            //Log.d("poster",singleMovie.poster);
            example3.setText(singleMovie.rating);
            //Log.d("rating",singleMovie.rating);
            example4.setText(singleMovie.synopsis);

        Picasso.with(context)
                .load(imgURL).resize(320,470)
                .into(image);


        }
    }
}
