package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.QueryUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.title;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static com.example.android.popularmovies.R.id.empty_view;
import static com.example.android.popularmovies.R.id.reviewHeading;
import static com.example.android.popularmovies.R.id.trailersHeading;
import static com.example.android.popularmovies.R.id.viewGroup;
import static java.lang.System.load;

public class MovieDetailActivity extends AppCompatActivity {
    File f1;
    private RecyclerView mRecycler;

    private RecyclerView mReviewView;
    ReviewAdapter mReviewAdapter;

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

    Button addToFav;
    String id;
    List<MovieTrailerDetails> mTrailers;

    MovieTrailerDetails trailers;
    TrailerAdapter trailerAdapter;

    Cursor mCursor;
    SingleMovie m;

    String cursorName = null;
    String cursorID = null;
    double cursorVote = 0;
    String cursorOverview = null;
    String cursorDate = null;
    double cursorPop = 0;

    File fileDelete;
    boolean deleteStatus = false;
    TextView trailersHeading;
    TextView reviewHeading;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;


    public MovieDetailActivity() {

    }


    public MovieDetailActivity(Context cont) {
        context = cont;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        context = getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        // button = (Button) findViewById(R.id.testButton);
        image = (ImageView) findViewById(R.id.image_movie);
        mRecycler = (RecyclerView) findViewById(R.id.trailerList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        date = (TextView) findViewById(R.id.date);
        mReviewView = (RecyclerView) findViewById(R.id.reviewsList);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewView.setLayoutManager(layoutManager1);
        mReviewView.setHasFixedSize(true);


        //setSupportActionBar(myToolBar);
        movie_title = (TextView) findViewById(R.id.movie_title_detail);
        example3 = (TextView) findViewById(R.id.example3);
        example4 = (TextView) findViewById(R.id.example4);

        mTrailers = new ArrayList<>();
        mReviews = new ArrayList<>();

        //inflater = LayoutInflater.from(context);
        trailerAdapter = new TrailerAdapter(mTrailers);
        mRecycler.setAdapter(trailerAdapter);

        mReviewAdapter = new ReviewAdapter(mReviews);
        mReviewView.setAdapter(mReviewAdapter);
        addToFav = (Button) findViewById(R.id.saveFavourite);
        //rootView  = (ListView) findViewById(R.id.trailerList);
        //
        trailersHeading = (TextView) findViewById(R.id.trailersHeading);
        reviewHeading = (TextView) findViewById(R.id.reviewHeading);

        Intent movieid = getIntent();
        //Log.d("Activity Started",id);
        if (movieid != null) {
            if (movieid.hasExtra(Intent.EXTRA_TEXT)) {
                id = movieid.getStringExtra(Intent.EXTRA_TEXT);


            }
        }
        Log.d(" First Check ID" , String.valueOf(id));
        mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(id).build(),
                new String[]{"ID"},
                String.valueOf(id),
                null,
                null,
                null);

        if (!checkConnection()) {


            trailersHeading.setText("");
            reviewHeading.setText("");
            Log.d("Check", "Connection" + String.valueOf(mCursor.getCount()));
            if (mCursor.getCount() == 0) {
                Log.d("Check", "Cursor");
//            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.viewGroup);
//            viewGroup.setVisibility(View.INVISIBLE);
                LinearLayout l = (LinearLayout) findViewById(R.id.wholeLayout);
                l.setVisibility(View.GONE);
                TextView textView = (TextView) findViewById(R.id.empty_view);
                textView.setVisibility(View.VISIBLE);

            }

        }//else if(mCursor!=null) {
        //  Log.d("Check","Fetch");
        new FetchSingleMovie().execute(id);
        //}

    }


    public class FetchSingleMovie extends AsyncTask<String, Void, SingleMovie> {
        Toast test;

        @Override
        protected SingleMovie doInBackground(String... params) {


            try {
                if (checkConnection()) {

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

                mCursor.moveToPosition(0);
                cursorID = mCursor.getString(mCursor.getColumnIndex("ID"));
                cursorName = mCursor.getString(mCursor.getColumnIndex("NAME"));
                cursorOverview = mCursor.getString(mCursor.getColumnIndex("OVERVIEW"));
                cursorVote = mCursor.getDouble(mCursor.getColumnIndex("VOTE"));
                cursorDate = mCursor.getString(mCursor.getColumnIndex("RELEASE_DATE"));
                cursorPop = mCursor.getDouble(mCursor.getColumnIndex("POPULARITY"));
                Log.d(String.valueOf(mCursor.getString(mCursor.getColumnIndex("POPULARITY"))), cursorName + " Check Final Double");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return m;
        }


        @Override
        protected void onPostExecute(final SingleMovie singleMovie) {
            super.onPostExecute(singleMovie);


            if (mCursor.getCount() != 0) {

                //Log.d(cursorName+"Cursor Nmae",cursorID);
                addToFav.setBackgroundColor(Color.RED);
                addToFav.setText("Remove from Favouites");

            }
            final File[] f2Test = new File[1];
            f2Test[0] = new File("/data/data/com.example.android.popularmovies/app_PopMov/" + id + ".jpg");
            if (singleMovie != null) {
                //myToolBar.setTitle(singleMovie.title);

                if (checkConnection()) {
                    Log.d(f2Test[0].toString(), String.valueOf(f2Test[0].exists()));

                    movie_title.setText(m.title);
                    imgURL = singleMovie.poster;
                    example3.setText("Rating : " + singleMovie.rating);
                    example4.setText(singleMovie.synopsis);
                    date.setText(singleMovie.date);


                    Picasso.with(context)
                            .load(imgURL).resize(320, 470)
                            .into(image);
                } else {

                    Log.d(f2Test[0].toString(), String.valueOf(f2Test[0].exists()));
                    Picasso.with(context)
                            .load(f2Test[0]).resize(320, 470)
                            .into(image);


                }
            }
            if (!checkConnection() && mCursor != null) {
                Picasso.with(context)
                        .load(f2Test[0]).resize(320, 470)
                        .into(image);
            }

            if (mTrailers != null) {
                trailerAdapter.setData(mTrailers);
            }
            if (mReviews != null) {
                mReviewAdapter.setData(mReviews);
            }

            if (!checkConnection() && mCursor.getCount() != 0) {
                // mCursor.moveToNext();
                movie_title.setText(cursorName);
                addToFav.setBackgroundColor(Color.RED);
                addToFav.setText("Remove from Favouites");
                date.setText("Release Date : " + cursorDate);
                example3.setText("Rating : " + cursorVote);
                example4.setText("Overview : " + cursorOverview);
            }
            if (!checkConnection() && mCursor.getCount() == 0) {
                addToFav.setVisibility(View.INVISIBLE);
            }
            if (mCursor != null || mCursor.getCount() != 0) {
                mCursor.moveToLast();
            }

            final String finalCursorID = cursorID;

            final String finalCursorName = cursorName;
            final String finalCursorOverview = cursorOverview;
            final String finalCursorDate = cursorDate;
            final double finalCursorVote = cursorVote;
            final double finalCursorPop = cursorPop;



            fileDelete = f2Test[0];
            deleteStatus = false;
            addToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(id).build(),
                            new String[]{"ID"},
                            String.valueOf(id),
                            null,
                            null,
                            null);

//                            if(checkConnection()){
//                                //imageDownload(m.poster,m.id_m,context);
//
//
//                                Log.d(String.valueOf(f2Test[0].exists()),"File Status");
//                            }

                    if (mCursor.getCount() != 0) {
                        Log.d("1", "First");
                        Uri uriDelete = MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(id).build();

                        getContentResolver().delete(uriDelete, null, null);

                        addToFav.setBackgroundColor(getResources().getColor(R.color.aquaBanner));
                        addToFav.setText("Add to Favourites");
                        //Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                        mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(id).build(),
                                new String[]{"ID"},
                                String.valueOf(id),
                                null,
                                null,
                                null);
                        deleteStatus = true;


                    } else {
                        Log.d("2", "Second");
                        if (!checkConnection()) {

                            Log.d("3", "Third");
                            cv = new ContentValues();
                            mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(String.valueOf(id)).build(),
                                    new String[]{"ID"},
                                    String.valueOf(id),
                                    null,
                                    null,
                                    null);
                            if (mCursor.getCount() == 0) {

                                Log.d("Check cursor " + finalCursorID, finalCursorName);
                                cv.put(MovieContract.MovieEntry.COLUMN_ID, finalCursorID);
                                cv.put(MovieContract.MovieEntry.COLUMN_NAME, finalCursorName);

                                cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, finalCursorOverview);

                                cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, finalCursorDate);

                                cv.put(MovieContract.MovieEntry.COLUMN_VOTE, finalCursorVote);
                                cv.put(MovieContract.MovieEntry.COLUMN_POPULARITY,finalCursorPop);
                                //TODO
                                Log.d("Uri", MovieContract.MovieEntry.FINAL_URI.toString());
                                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.FINAL_URI, cv);

//                                if (uri != null) {
//                                    Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show();
//                                }
                                addToFav.setBackgroundColor(Color.RED);
                                addToFav.setText("Remove from Facourites");
                                deleteStatus = false;
                            } else {

                                Uri uriDelete = MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(id).build();

                                getContentResolver().delete(uriDelete, null, null);

                                addToFav.setBackgroundColor(getResources().getColor(R.color.aquaBanner));
                                addToFav.setText("Add to Favourites");
                                //Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                                mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(id).build(),
                                        new String[]{"ID"},
                                        String.valueOf(id),
                                        null,
                                        null,
                                        null);
                                deleteStatus = true;

                            }

                        } else {
                            Log.d("4", "Fourth");
                            cv = new ContentValues();
                            cv.put(MovieContract.MovieEntry.COLUMN_ID, singleMovie.id_m);
                            cv.put(MovieContract.MovieEntry.COLUMN_NAME, singleMovie.title);

                            cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, singleMovie.synopsis);

                            cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, singleMovie.date);

                            cv.put(MovieContract.MovieEntry.COLUMN_VOTE, singleMovie.rating);
                            cv.put(MovieContract.MovieEntry.COLUMN_POPULARITY,singleMovie.popular);
                            //TODO
                            Log.d("Uri", MovieContract.MovieEntry.FINAL_URI.toString());
                            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.FINAL_URI, cv);
//                        if(uri!=null){
//                            Toast.makeText(context,uri.toString(),Toast.LENGTH_SHORT).show();
//                        }
                            addToFav.setBackgroundColor(Color.RED);
                            addToFav.setText("Remove from Facourites");
                            imageDownload(m.poster, m.id_m, context);
                            mCursor = getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().appendPath(String.valueOf(m.id_m)).build(),
                                    new String[]{"ID"},
                                    String.valueOf(m.id_m),
                                    null,
                                    null,
                                    null);
                            deleteStatus = false;
                            //finalCursorID = m.id_m;


                        }
                    }

                }

            });

            if (mTrailers.isEmpty()) {
                trailersHeading.setVisibility(View.GONE);
            }
            if (mReviews.isEmpty()) {
                reviewHeading.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fileDelete.exists() && deleteStatus == true) {
            fileDelete.delete();
        }

        editor.putBoolean("checkConAfter",checkConnection());
        editor.apply();
        Log.d("Back Pressed",String.valueOf(sharedPreferences.getBoolean("checkConAfter",false)));
        mCursor.close();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_return = item.getItemId();
        if (id_return == android.R.id.home) {
            if (deleteStatus == true && fileDelete.exists()) {
                fileDelete.delete();
            }
            mCursor.close();
        }

        editor.putBoolean("checkConAfter",checkConnection());
        editor.apply();

        Log.d("Up Pressed",String.valueOf(sharedPreferences.getBoolean("checkConAfter",false)));
        return super.onOptionsItemSelected(item);
    }

    public boolean checkConnection() {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void imageDownload(String url, final String name, final Context con) {

        Picasso.with(con).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                ContextWrapper cw = new ContextWrapper(con);
                File dir = cw.getDir("PopMov", Context.MODE_PRIVATE);

                f1 = new File(dir, name + ".jpg");

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(f1);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Log.d(String.valueOf(f1.exists()), "Check Dir");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }


}

