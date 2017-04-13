package com.example.android.popularmovies.data;

import android.util.Log;

import com.example.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by ravikiranpathade on 4/13/17.
 */

public class QueryUtils {

        private final static String API_KEY = "c9a7eeb9aa2533f06119e9eb9bfeb800";

    private final static String stringURL = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&primary_release_date.gte=2017-01-01&primary_release_date.lte=2017-03-03&api_key="+API_KEY;


    public QueryUtils(){

    }

    public static List<Movie> fetchMovies(){
        URL url = createURL(stringURL);

        String jsonR = "";
        try{
            jsonR = makeHTTPrequest(url);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        List<Movie> movies = extractJSON(jsonR);


        return movies;
    }

//Create URL from the given string

    public static URL createURL(String url){

        URL ur = null;

        try{
            ur = new URL(url);

        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d("URL : ", ur.toString());
        return ur;
    }

    // To make an HTTP request

    public static String makeHTTPrequest(URL url) throws IOException {
        String response = "";


        if(url==null){return response;}
        HttpURLConnection urlConnection = null;

        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            }else{
                Log.d("URL Connection: ", urlConnection.getResponseMessage());
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }


        return response;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if(inputStream!=null){
            InputStreamReader isReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(isReader);
            String line = reader.readLine();

            if(line!=null){
                output.append(line);
                line = reader.readLine();
            }


        }

        Log.d("Output: ",output.toString());

        return output.toString();
    }

    public static List<Movie> extractJSON(String json){
        if(json.isEmpty()){
            return null;
        }
        List<Movie> movies = new ArrayList<>();

        try{

            JSONObject baseObject = new JSONObject(json);

            JSONArray baseArray = baseObject.getJSONArray("results");
            for(int i = 0 ; i < baseArray.length() ; i++){

                JSONObject current = baseArray.getJSONObject(i);

                String mName = current.getString("poster_path");
                String title = current.getString("title");

                String movieName = "http://image.tmdb.org/t/p/w185"+mName;

                Movie m = new Movie(movieName,title);

                //Log.i("Image URL :",movieName);

                movies.add(m);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return movies;
    }


}
