package com.example.android.popularmovies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView mMovieRecyclerView;

    private MovieAdapter mAdapter;

    private RecyclerView.LayoutManager layoutM;

    Movie[] allMovies = {
            new Movie(1,"Interstellar"),
            new Movie(2,"Star wars"),
            new Movie(3,"The Prestige"),
            new Movie(4,"Scene"),
            new Movie(5,"Captain America"),
            new Movie(6,"Batman"),
            new Movie(7,"Sultan"),
            new Movie(8,"Superman")

    };






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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_activity,container,false);

        mMovieRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_movie);

        layoutM = new GridLayoutManager(getActivity(),2);

        mAdapter = new MovieAdapter(allMovies);

        mMovieRecyclerView.setLayoutManager(layoutM);

        GridView gView = (GridView) rootView.findViewById(R.id.fragment_movie);
        mMovieRecyclerView.setHasFixedSize(true);
        mMovieRecyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

}
