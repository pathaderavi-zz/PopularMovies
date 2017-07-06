package com.example.android.popularmovies.data;


import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.android.popularmovies.Movie;
import com.example.android.popularmovies.MovieDetailActivity;
import com.example.android.popularmovies.MovieTrailerDetails;
import com.example.android.popularmovies.ReviewDetails;
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
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static android.R.attr.id;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.media.CamcorderProfile.get;

/**
 * Created by ravikiranpathade on 4/13/17.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class QueryUtils {


    private final static String API_KEY = "c9a7eeb9aa2533f06119e9eb9bfeb800";

    private final static String stringURL = "http://api.themoviedb.org/3/movie/";

    public final static String lastAppend = "?api_key="+API_KEY;;
    // "&sort_by=popularity.desc"
    private final static String sortQuery = "sort_by";
    private static String movieID;
    private final static String movieDetailURL = "https://api.themoviedb.org/3/movie/"+movieID+"?api_key="+API_KEY+"";

    private final static String videoTrailerURL = "http://api.themoviedb.org/3/movie/";

    private final static String reviewsUrl = "http://api.themoviedb.org/3/movie/";
    private final static String finalReview = "/reviews?api_key=";

    public QueryUtils(){

    }

    public static List<Movie> fetchMovies(String query){

        Uri uri = Uri.parse(stringURL+query+lastAppend)
                .buildUpon().
                        build();

        URL url = createURL(uri.toString());
        Log.d("stringurl",uri.toString());

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

                String movieName = "http://image.tmdb.org/t/p/w600"+mName;

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
    public static List<MovieTrailerDetails> extarctJSONforTrailers(String json){
        List<MovieTrailerDetails> trailers = new ArrayList<>();
        try{
            JSONObject trailersObject = new JSONObject(json);
            JSONArray baseArray = trailersObject.getJSONArray("results");
            for(int i = 0 ; i < baseArray.length(); i++ ){
                JSONObject current = baseArray.getJSONObject(i);
                String videoUrl = "https://www.youtube.com/watch?v="+current.getString("key");

                String trailerName = current.getString("name");
                //Log.d(trailerName,videoUrl);
                String typeVid = current.getString("type");
                if(typeVid.equals("Trailer")){

                MovieTrailerDetails m = new MovieTrailerDetails(videoUrl,trailerName);
                trailers.add(m);
                    //Log.d(trailerName,videoUrl);
                }


            }
        }
        catch (Exception e){}
        return trailers;
    }
    public static List<ReviewDetails> extractJSONforReviews(String json){
        if(json.isEmpty()){
            return null;
        }
        List<ReviewDetails> mReviewDetails = new ArrayList<>();
        try{
            JSONObject baseObject = new JSONObject(json);
            JSONArray jsonArray = baseObject.getJSONArray("results");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject current = jsonArray.getJSONObject(i);

                String author = current.getString("author");
                String content = current.getString("content");
                String url = current.getString("url");
                mReviewDetails.add(new ReviewDetails(author,content,url));
                //Log.d(author,url);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return mReviewDetails;
    }
    public static List<MovieTrailerDetails> fetchVids(String idMovie){
        String vidsURL = videoTrailerURL+idMovie+"/videos?api_key="+API_KEY;


        //Log.d("URL here",vidsURL);

        URL url = createURLforMovie(vidsURL);
        String jsonResponse = "";
        try
        {
            jsonResponse = makeHTTPrequest(url);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        List<MovieTrailerDetails> trailers = new ArrayList();

        trailers = extarctJSONforTrailers(jsonResponse);


        return trailers;
    }
    public static List<ReviewDetails> getReviews(String movieId){
        String reviewUrl = reviewsUrl+movieId+finalReview+API_KEY;
        //Log.d("Review URL",reviewUrl);
        URL url = createURLforMovie(reviewUrl);
        String jsonResponseReviews = "";
        try{
            jsonResponseReviews = makeHTTPrequest(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        List<ReviewDetails> reviews = new ArrayList<>();
        reviews = extractJSONforReviews(jsonResponseReviews);
        return reviews;
    }
    public static SingleMovie extractJSONforMovie(String json){
        SingleMovie m;
        String t =null;
        String p=null;
        String s=null;
        String r=null;
        String d=null;
        String tr = null;
        String d1 = null;
        String id_movie = null;
        if(json.isEmpty()){
            return null;
        }


        try{

            JSONObject current = new JSONObject(json);

            //JSONArray baseArray = baseObject.getJSONArray(json);
            //JSONObject current = baseArray.getJSONObject(i);
                //String d1;
                t = current.getString("title");
                p = "http://image.tmdb.org/t/p/w185"+current.getString("poster_path");
                s = current.getString("overview");
                r = current.getString("vote_average")+"/10";
                d = current.getString("release_date");
               // tr = current.getString("");
                id_movie = current.getString("id");
                //Added code for Dateformat

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat df1 = new SimpleDateFormat("MM dd yyyy");
                try{

                    Date startDate = df.parse(d);

                    d1 = df1.format(startDate);

                    //d = newDate;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            //Date Code end





        }catch (Exception e){
            e.printStackTrace();
        }finally {

            m = new SingleMovie(t,p,s,r,d,id_movie);
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
