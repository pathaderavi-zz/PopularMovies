package com.example.android.popularmovies.data;

import android.util.Log;

import com.example.android.popularmovies.Movie;
import com.example.android.popularmovies.SingleMovie;

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

    private static String movieID;
    private final static String movieDetailURL = "https://api.themoviedb.org/3/movie/"+movieID+"?api_key="+API_KEY;

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
       // Log.d("URL : ", ur.toString());
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
               // Log.d("URL Connection: ", urlConnection.getResponseMessage());
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

        //Log.d("Output: ",output.toString());

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

                String id = current.getString("id");

                Movie m = new Movie(movieName,title,id);

                //Log.i("Image URL :",movieName);

                movies.add(m);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return movies;
    }
    public static SingleMovie extractJSONforMovie(String json){
        SingleMovie m;
        String t =null;
        String p=null;
        String s=null;
        String r=null;
        String d=null;

        if(json.isEmpty()){
            return null;
        }


        try{

            JSONObject current = new JSONObject(json);

            //JSONArray baseArray = baseObject.getJSONArray(json);
            //JSONObject current = baseArray.getJSONObject(i);

                t = current.getString("title");
                p = "http://image.tmdb.org/t/p/w185"+current.getString("poster_path");
                s = current.getString("overview");
                r = current.getString("vote_average")+"/10";
                d = current.getString("release_date");




        }catch (Exception e){
            e.printStackTrace();
        }finally {
            m = new SingleMovie(t,p,s,r,d);
        }

        return m;
    }
    public static SingleMovie fetchSingle(String id){
       // movieID = id;
        String movieDetailURL1 = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+API_KEY;
        //Log.d(movieID+"ID of the Movie",movieDetailURL1);
        URL url = createURLforMovie(movieDetailURL1);

        String jsonR = "";
        try{
            jsonR = makeHTTPrequest(url);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        SingleMovie m = extractJSONforMovie(jsonR);

       // Log.d("Movie Title",jsonR);
        return m;
    }

//Create URL from the given string

    public static URL createURLforMovie(String url){

        URL ur = null;

        try{
            ur = new URL(url);

        }catch(Exception e){
            e.printStackTrace();
        }
       // Log.d("URL would be: ", ur.toString());
        return ur;
    }

}
