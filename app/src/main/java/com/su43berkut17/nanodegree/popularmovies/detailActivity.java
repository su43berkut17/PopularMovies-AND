package com.su43berkut17.nanodegree.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.su43berkut17.nanodegree.popularmovies.RV_related.review_adapter;
import com.su43berkut17.nanodegree.popularmovies.RV_related.reviews;
import com.su43berkut17.nanodegree.popularmovies.RV_related.thumbnail;
import com.su43berkut17.nanodegree.popularmovies.RV_related.trailer;
import com.su43berkut17.nanodegree.popularmovies.RV_related.trailer_adapter;
import com.su43berkut17.nanodegree.popularmovies.Utils.NetUtils;
import com.su43berkut17.nanodegree.popularmovies.data.favoritesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class detailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>,trailer_adapter.movieClickListener,review_adapter.movieClickListener{
    //loader ids
    private static int LOADER_TRAILER_ID=1008;
    private static int LOADER_REVIEWS_ID=1009;

    //recycler views
    private RecyclerView trailerView;
    private RecyclerView.Adapter trailerAdapter;
    private RecyclerView reviewView;
    private RecyclerView.Adapter reviewAdapter;

    //content lists
    private List<trailer> trailerList;
    private List<reviews> reviewList;

    //generate the object that has all the variables to store
    private thumbnail object;

    //get the view ids
    private TextView tv_title;
    private TextView tv_release;
    private TextView tv_plot;
    private ImageView iv_poster;
    private ImageView iv_thumb;
    private RatingBar rv_score;
    private ImageView iv_favorite;

    //get the scrollview
    private ScrollView scrollView;
    private static int[] loadedState=new int[]{0,0};
    private static Parcelable loadedStateTrailer;
    private static Parcelable loadedStateReviews;

    //favorites related
    private Boolean mIsFavorite;

    //other bars
    private String movieID;

    //loading bar
    private ProgressBar w_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        scrollView=(ScrollView)findViewById(R.id.scrollViewDetail);

        //get the views
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_release=(TextView)findViewById(R.id.tv_release);
        tv_plot=(TextView)findViewById(R.id.tv_plot);
        iv_poster=(ImageView)findViewById(R.id.iv_poster);
        iv_thumb=(ImageView)findViewById(R.id.iv_thumb);
        rv_score=(RatingBar)findViewById(R.id.rat_score);
        iv_favorite=(ImageView)findViewById(R.id.bFavorite);
        w_progress=(ProgressBar)findViewById(R.id.w_detail_progress);

        //we set the listener to the favorites button
        iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsFavorite!=null) {
                    if (mIsFavorite) {
                        //it is a favorite so we delete
                        deleteFavorite();
                    } else {
                        //it is not a favorite so we save
                        saveFavorite();
                    }
                }
            }
        });

        //get the parcelable
        object=(thumbnail) getIntent().getParcelableExtra("detailParce");
        tv_title.setText(object.getTitle());
        tv_release.setText(object.getRelease_date());
        tv_plot.setText(object.getDetails());
        rv_score.setRating(object.getVote_average());
        movieID=object.getmovieId();

        //we load the trailers and the reviews only is the rv is null
        if (trailerView==null) {
            //we show the progress bar
            w_progress.setVisibility(View.VISIBLE);

            //load the image
            Picasso.get().load("https://image.tmdb.org/t/p/w500"+object.getUrl()).into(iv_thumb);
            Picasso.get().load("https://image.tmdb.org/t/p/w500"+object.getwideUrl()).into(iv_poster);

            //initialize the empty trailer lists
            trailerList=new ArrayList<>();
            reviewList=new ArrayList<>();

            loadTrailers(movieID, "trailer");

            //we check if it is already in favorites
            checkFavorite();
        }
    }

    //scroll saving
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("scrollX",scrollView.getScrollX());
        outState.putInt("scrollY",scrollView.getScrollY());
        if (trailerView!=null) {
            outState.putParcelable("savedTrailer", trailerView.getLayoutManager().onSaveInstanceState());
        }
        if (reviewView!=null) {
            outState.putParcelable("savedReview", reviewView.getLayoutManager().onSaveInstanceState());
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        loadedState[0]=savedInstanceState.getInt("scrollX");
        loadedState[1]=savedInstanceState.getInt("scrollY");

        loadedStateTrailer=savedInstanceState.getParcelable("savedTrailer");
        loadedStateReviews=savedInstanceState.getParcelable("savedReview");
    }

    @Override
    protected void onPause() {
        super.onPause();

        //loadedState=new int[]{0,0};
        loadedState[0]=scrollView.getScrollX();
        loadedState[1]=scrollView.getScrollY();

        if (trailerView!=null) {
            loadedStateTrailer = trailerView.getLayoutManager().onSaveInstanceState();
        }
        if (reviewView!=null) {
            loadedStateReviews = reviewView.getLayoutManager().onSaveInstanceState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (loadedState!=null){
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(loadedState[0],loadedState[1]);
                }
            });
        }
    }

    //function that loads trailers
    private void loadTrailers(String movieID,String typeOfLoad){
        Context context=getApplicationContext();

        //we check if there's connection
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            //tv_noConnection.setVisibility(View.INVISIBLE);
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> movieLoader = loaderManager.getLoader(LOADER_TRAILER_ID);
            Bundle typeOfSort = new Bundle();
            typeOfSort.putString("TYPE_LOAD", typeOfLoad);

            if (movieLoader == null) {
                loaderManager.initLoader(LOADER_TRAILER_ID, typeOfSort, this);
            } else {
                loaderManager.restartLoader(LOADER_TRAILER_ID, typeOfSort, this);
            }
        }else{
            //we show the no connection
            //tv_noConnection.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public Loader<List<String>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<String>>(this) {
                @Override
            protected void onStartLoading() {
                //w_progress.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public List<String> loadInBackground() {
                try {
                    Bundle arg=args;
                    String type_load=args.getString("TYPE_LOAD");
                    List<String> toReturn=new ArrayList<>();;
                    if (type_load=="trailer"){
                        String finalString = NetUtils.getMovieList(NetUtils.urlBuilderTrailers(movieID));
                        toReturn.add(type_load);
                        toReturn.add(finalString);
                        return toReturn;
                    }else{
                        String finalString = NetUtils.getMovieList(NetUtils.urlBuilderReviews(movieID));
                        toReturn.add(type_load);
                        toReturn.add(finalString);
                        return toReturn;
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        //we create the recyclerview here
        try {
            if (data!=null && data.get(0)!=null && data.get(1)!=null) {
                if (data.get(0) == "trailer"){
                    JSONObject jsonObject = new JSONObject(data.get(1));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    trailerList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        trailer trailerThumbs = new trailer(object.getString("key")
                                , object.getString("name")
                                , object.getString("type"));
                        trailerList.add(trailerThumbs);
                    }

                    //create the recyclerview
                    trailerView = (RecyclerView) findViewById(R.id.rv_trailers);
                    trailerView.setLayoutManager(new LinearLayoutManager(this));
                    trailerAdapter = new trailer_adapter(trailerList, getApplicationContext(), this);
                    trailerView.setAdapter(trailerAdapter);
                    trailerAdapter.notifyDataSetChanged();
                    //w_progress.setVisibility(View.INVISIBLE);

                    loadTrailers(movieID, "reviews");
                }else{
                    //for reviews
                    JSONObject jsonObject = new JSONObject(data.get(1));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    reviewList.clear();

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);

                        reviews reviewThumbs=new reviews(object.getString("author"),
                                object.getString("content"),
                                object.getString("url"));
                        reviewList.add(reviewThumbs);
                    }

                    //create the review recyclerList
                    reviewView = (RecyclerView) findViewById(R.id.rv_reviews);
                    reviewView.setLayoutManager(new LinearLayoutManager(this));
                    reviewAdapter = new review_adapter(reviewList, getApplicationContext(), this);
                    reviewView.setAdapter(reviewAdapter);
                    reviewAdapter.notifyDataSetChanged();


                    if (loadedState!=null){
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.scrollTo(loadedState[0],loadedState[1]);
                            }
                        });
                    }

                    if(loadedStateTrailer!=null){
                        trailerView.getLayoutManager().onRestoreInstanceState(loadedStateTrailer);
                    }

                    if (loadedStateReviews!=null){
                        reviewView.getLayoutManager().onRestoreInstanceState(loadedStateReviews);
                    }

                    //we finished the load
                    w_progress.setVisibility(View.INVISIBLE);
                }
            }else{
                w_progress.setVisibility(View.INVISIBLE);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    //open trailer
    @Override
    public void onMovieItemClick(trailer clicked){
        //we play the video
        String youtubeURL="https://www.youtube.com/watch?v="+clicked.getMovieID();

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + clicked.getMovieID()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + clicked.getMovieID()));

        Context context=getApplicationContext();
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    //open review
    @Override
    public void onMovieItemClick(reviews clicked){
        Intent webIntent=new Intent(Intent.ACTION_VIEW , Uri.parse(clicked.getUrl()));

        Context context=getApplicationContext();
        context.startActivity(webIntent);
    }

    //content provider write
    private void deleteFavorite(){
        //we delete the current object
        int rowDeleted=0;

        String selection;
        String[] selectionArgs={""};

        selection=favoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + " = ?";
        selectionArgs[0]=movieID;

        rowDeleted=getContentResolver().delete(favoritesContract.FavoritesEntry.FAVORITES_URI,
                selection,
                selectionArgs);

        //we check if something was deleted
        checkFavorite();
    }

    private void saveFavorite(){
        //we save the current object
        ContentValues contentValues=new ContentValues();
        contentValues.put(favoritesContract.FavoritesEntry.COLUMN_MOVIE_ID,object.getmovieId());
        contentValues.put(favoritesContract.FavoritesEntry.COLUMN_TITLE,object.getTitle());
        contentValues.put(favoritesContract.FavoritesEntry.COLUMN_DESCRIPTION,object.getDetails());
        contentValues.put(favoritesContract.FavoritesEntry.COLUMN_URL,object.getUrl());
        contentValues.put(favoritesContract.FavoritesEntry.COLUMN_WIDE_URL,object.getwideUrl());
        contentValues.put(favoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE,object.getRelease_date());
        contentValues.put(favoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE,object.getVote_average());

        Uri uri=getContentResolver().insert(favoritesContract.BASE_URI.buildUpon().appendPath(favoritesContract.PATH_FAVORITES).build(),contentValues);

        if (uri!=null){
            //we change the graphic and the value
            mIsFavorite=true;
            changeGraphicFavorites();
        }
    }

    private void changeGraphicFavorites(){
        if (mIsFavorite){
            //we activate the color star
            iv_favorite.setImageResource(android.R.drawable.btn_star_big_on);
        }else{
            //we deactivate the color star
            iv_favorite.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    //method that checks if the favorite exists
    private void checkFavorite(){
        String selection;
        String[] selectionArgs={""};

        selection=favoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + " = ?";
        selectionArgs[0]=movieID;

        Cursor cursor=getApplicationContext().getContentResolver().query(favoritesContract.FavoritesEntry.FAVORITES_URI,
                null,
                selection,
                selectionArgs,
                null,
                null
        );

        //we check is the cursor has a child
        if (cursor.moveToFirst()==true){
            //is favorite
            mIsFavorite=true;
        }else{
            //it is not favorite
            mIsFavorite=false;
        }

        //we change the favorite graphic
        changeGraphicFavorites();
    }
}