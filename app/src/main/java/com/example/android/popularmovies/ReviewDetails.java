package com.example.android.popularmovies;

/**
 * Created by ravikiranpathade on 7/2/17.
 */

public class ReviewDetails {
    String author;
    String content;
    String url;

    public ReviewDetails(String mAuthor,String mContent,String mUrl){
        this.author = mAuthor;
        this.content = mContent;
        this.url = mUrl;
    }
    public String getAuthor(){
        return author;
    }
    public String getContent(){
        return content;
    }
    public String getUrl(){
        return url;
    }
}
