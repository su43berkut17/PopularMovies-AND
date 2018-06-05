package com.su43berkut17.nanodegree.popularmovies.Utils;

import android.net.Uri;

import com.su43berkut17.nanodegree.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetUtils {
    final static String MOVIE_URL="https://api.themoviedb.org/3/movie/";
    final static String API_KEY= BuildConfig.API_KEY;

    //build the url with the api key and preferences
    public static URL urlBuilder(String sortPreference){
        Uri builtUri = Uri.parse(MOVIE_URL+sortPreference).buildUpon()
                .appendQueryParameter("api_key",API_KEY)
                .appendQueryParameter("language","en-US")
                .appendQueryParameter("page","1")
                .build();

        URL url=null;
        try{
            url=new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    //build the url for trailers
    public static URL urlBuilderTrailers(String videoId){
        Uri builtUri = Uri.parse(MOVIE_URL+videoId+"/videos").buildUpon()
                .appendQueryParameter("api_key",API_KEY)
                .appendQueryParameter("language","en-US")
                .build();

        URL url=null;
        try{
            url=new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    //build the url for the reviews
    public static URL urlBuilderReviews(String videoId){
        Uri builtUri = Uri.parse(MOVIE_URL+videoId+"/reviews").buildUpon()
                .appendQueryParameter("api_key",API_KEY)
                .appendQueryParameter("language","en-US")
                .appendQueryParameter("page","1")
                .build();

        URL url=null;
        try{
            url=new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    //gets the stream from getMovieList
    public static String getMovieList(URL url) throws IOException{
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();

        try{
            InputStream streamMovies=connection.getInputStream();

            Scanner scanner=new Scanner(streamMovies);
            scanner.useDelimiter("\\A");
            String finalString="";

            boolean hasInput=scanner.hasNext();
                if (hasInput) {
                    do{
                        String toAdd=scanner.next();
                        finalString=finalString+toAdd;
                        hasInput=scanner.hasNext();
                    }while (hasInput==true);
                    return finalString;
                } else {
                    return null;
                }

        }finally {
            connection.disconnect();
        }
    }
}
