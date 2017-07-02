package com.example.android.popularmovies;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.android.popularmovies.data.QueryUtils;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityFragment extends Fragment  {

    private MovieAdapter mAdapter;
    private GridView gView;
    private ImageView imgView;



    Activity test;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        new FetchMovies().execute("popular");


    }
    View rootView;
    public String location = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main_activity,container,false);
        gView = (GridView) rootView.findViewById(R.id.fragment_movie);
        //imgView = (ImageView) rootView.findViewById(R.id.image_poster);


        // Inflate the layout for this fragment
        return rootView;
        //setHasOptionsMenu(true);
    }




    public class FetchMovies extends AsyncTask<String, Void, List<Movie>>{


        @Override
        protected List doInBackground(String... params) {
            List<Movie> newList = new ArrayList<>();
            String q = params[0];

            try{
                newList = QueryUtils.fetchMovies(q);
                return newList;

            }
            catch(Exception e){e.printStackTrace();}

            return newList;
        }
        Toast toast = null;
        @Override
        protected void onPostExecute(final List<Movie> movies) {
            super.onPostExecute(movies);

            if(movies!=null){
                //movies = new ArrayList<Movie>();
                mAdapter = new MovieAdapter(getContext(), movies);

                gView.setAdapter(mAdapter);

                gView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

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

                        Intent detail = new Intent(getContext(),MovieDetailActivity.class);
                        detail.setType("text/plain");
                        detail.putExtra(Intent.EXTRA_TEXT,m.uniqueID);
                        startActivity(detail);



                    }
                });

            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();

        if(menuItem == R.id.action_sort){
            String sortby = "top_rated";
            new FetchMovies().execute(sortby);
        }
        if(menuItem == R.id.action_sort_pop){
            String sortby = "popular";
            new FetchMovies().execute(sortby);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort,menu);

    }
}
