package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.popularmovies.data.QueryUtils;

import static android.R.attr.id;

public class MovieDetailActivity extends AppCompatActivity {


    //private static String id;
    private TextView example;
    private TextView example1;
    private TextView example2;
    private TextView example3;
    private TextView example4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

//        example = (TextView) findViewById(R.id.example);
//        Intent movieid = getIntent();
//
//        if(movieid!=null){
//            if(movieid.hasExtra(Intent.EXTRA_TEXT)){
//                id = movieid.getStringExtra(Intent.EXTRA_TEXT);
//                example.setText(id);
//
//            }
//        }
        String id = null;
        Intent movieid = getIntent();
        //Log.d("Activity Started",id);
        if(movieid!=null){
            if(movieid.hasExtra(Intent.EXTRA_TEXT)){
                id = movieid.getStringExtra(Intent.EXTRA_TEXT);
                //example.setText(id);

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
               // Log.d("doInBackGround",m.rating);

            }
            catch(Exception e){e.printStackTrace();}

            return m;
        }

        @Override
        protected void onPostExecute(SingleMovie singleMovie) {
            super.onPostExecute(singleMovie);
            example = (TextView) findViewById(R.id.example);
            example1 = (TextView) findViewById(R.id.example1);
            example2 = (TextView) findViewById(R.id.example2);
            example3 = (TextView) findViewById(R.id.example3);
            example4 = (TextView) findViewById(R.id.example4);
            example.setText(singleMovie.title);
            Log.d("Title",singleMovie.title);
            example1.setText(singleMovie.date);
            Log.d("date",singleMovie.date);
            example2.setText(singleMovie.poster);
            Log.d("poster",singleMovie.poster);
            example3.setText(singleMovie.rating);
            Log.d("rating",singleMovie.rating);
            example4.setText(singleMovie.synopsis);


        }
    }
}
