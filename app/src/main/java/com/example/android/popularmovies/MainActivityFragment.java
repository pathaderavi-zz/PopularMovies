package com.example.android.popularmovies;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.QueryUtils;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.menuCategory;
import static android.R.attr.name;
import static android.R.attr.offset;
import static android.widget.Toast.makeText;
import static com.example.android.popularmovies.R.string.sortby;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityFragment extends Fragment {

    private MovieAdapter mAdapter;
    private GridView gView;
    private ImageView imgView;
    String sby;
    boolean showFav;
    Activity test;
    private ProgressBar spinnerLoading;

  //  Bundle bundle_uni;
    //Cursor mFavourites;
//    String title = "Popular Movies ";
//    String action1 = "All";
//    String action2 = " Most Popular";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String sortby;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public MainActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActivityFragment newInstance(String param1, String param2) {
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
       // bundle_uni = new Bundle();

       // ((MainActivityFragment) getActivity()).getActionBar().setTitle(action1+action2);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        //Log.d(" Pref Stat ",sharedPreferences.getString("sort_by_i",""));
        editor = sharedPreferences.edit();
        if(sharedPreferences.getString("sort_by_i","").equals("")){
                    sortby = "popular";
                    editor.putString("sort_by_i",sortby);
        }
        editor.putString("sort_by_c","POPULARITY DESC");
        //editor.putBoolean("showFav",false);
        editor.apply();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        new FetchMovies().execute(sharedPreferences.getString("sort_by_i",""));


    }

    View rootView;
    public String location = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //spinnerLoading = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        gView = (GridView) rootView.findViewById(R.id.fragment_movie);
        //imgView = (ImageView) rootView.findViewById(R.id.image_poster);


        // Inflate the layout for this fragment
        return rootView;
        //setHasOptionsMenu(true);
    }




        public class FetchMovies extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            Log.d("Check Incoming",String.valueOf(sharedPreferences.getBoolean("checkConAfter",false)));
            //spinnerLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected List doInBackground(String... params) {



            List<Movie> newList = new ArrayList<>();
            List<Movie> newListCursor = new ArrayList<>();
            String q = params[0];
            boolean isCon = checkConnection();
            sby = sharedPreferences.getString("sort_by_c","");
            if(!checkConnection()){
                showFav=true;
            }
            try {
                if (isCon && !sharedPreferences.getBoolean("showFav",true)) {
                    newList = QueryUtils.fetchMovies(q);
                } else {
                    //Log.d(" Else "," Loop ");
//                MovieDetailActivity mm = new MovieDetailActivity();
//                Log.d(String.valueOf(mm.checkConnection()),"COn Stat");
                    Cursor m1 = getContext().getContentResolver().query(MovieContract.MovieEntry.FINAL_URI.buildUpon().build(),
                            null,
                            null,
                            null,
                            sby);

                    while (m1.moveToNext()) {
                        String id_c = m1.getString(m1.getColumnIndex("ID"));
                        String name_c = m1.getString(m1.getColumnIndex("NAME"));
                        Log.d(id_c, name_c + " Here Log");
                        newList.add(new Movie(id_c, id_c, id_c));
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(" Count ", String.valueOf(newList.size()) );
            return newList;
        }

        Toast toast = null;

        @Override
        protected void onPostExecute(final List<Movie> movies) {
            super.onPostExecute(movies);
            //((MainActivity) getActivity()).setTitle(title+action1+action2);
            //spinnerLoading.setVisibility(View.VISIBLE);
            if (movies != null) {
                //movies = new ArrayList<Movie>();
                mAdapter = new MovieAdapter(getContext(), movies);

                gView.setAdapter(mAdapter);

                int test = sharedPreferences.getInt("index_value",0);
                boolean setAdaptertoPosition = sharedPreferences.getBoolean("checkConAfter",false) && sharedPreferences.getBoolean("checkConBefore",false);

                Log.d("Check to see Position ", String.valueOf(setAdaptertoPosition));

                if((test>0 && setAdaptertoPosition) || showFav){
                    gView.setSelection(test);
                }
                gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Movie m = mAdapter.getItem(position);
//
//                        if(toast!=null){
//                            toast.cancel();
//                        }
//
//                        toast = Toast.makeText(getContext(),m.movieName,Toast.LENGTH_SHORT);
//
//                        toast.show();
                        int index = gView.getFirstVisiblePosition();

                        editor.putInt("index_value",position);
                       // Log.d("Editor "+String.valueOf(position),String.valueOf(sharedPreferences.getInt("index_value",0)));
                        editor.putBoolean("checkConBefore",checkConnection());
                        editor.apply();
                       // Log.d("Editor "+String.valueOf(position),String.valueOf(sharedPreferences.getInt("index_value",0)));
                        //                        bundle_uni.putInt("index_value",index);
//                        Log.d("Click Listener",String.valueOf(bundle_uni));
                        Intent detail = new Intent(getContext(), MovieDetailActivity.class);
                        detail.setType("text/plain");
                        detail.putExtra(Intent.EXTRA_TEXT, m.uniqueID);
                        startActivity(detail);


                    }
                });

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        editor.putInt("index_value",0);
          if (menuItem == R.id.action_sort) {
            sortby = "top_rated";
            editor.putString("sort_by_c","VOTE DESC");
            editor.putString("sort_by_i",sortby);
            editor.apply();
            //action2=" Top Rated";
            new FetchMovies().execute(sharedPreferences.getString("sort_by_i",""));
        }
        if (menuItem == R.id.action_sort_pop) {
            sortby = "popular";
            editor.putString("sort_by_c","POPULARITY DESC");
            editor.putString("sort_by_i",sortby);
            editor.apply();
            new FetchMovies().execute(sharedPreferences.getString("sort_by_i",""));


        }
        if(menuItem==R.id.action_show_fav){
            showFav = true;
            editor.putBoolean("showFav",true);
            editor.apply();
            new FetchMovies().execute(sharedPreferences.getString("sort_by_i",""));

        }
        if(menuItem==R.id.action_show_all){
            showFav = false;
            editor.putBoolean("showFav",false);
            editor.apply();
            new FetchMovies().execute(sharedPreferences.getString("sort_by_i",""));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort, menu);

    }

    public boolean checkConnection() {
        //editor.putInt("index_value",0);
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int index = gView.getFirstVisiblePosition();
        outState.putInt("index_value",index);
        editor.putInt("index_value",index);
        editor.apply();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO Destroy SharedPreferences
    }

    //
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("SharedPref Boolean",String.valueOf(sharedPreferences.getBoolean("showFav",true)));


        if(savedInstanceState!=null) {
            int index_here = sharedPreferences.getInt("index_value",0);
            Log.d("Index Log Here",String.valueOf(savedInstanceState.containsKey("index_value")));
            //new FetchMovies().execute(sharedPreferences.getString("sort_by_i",""));
            gView.setSelection(index_here);
        }


    }
}
