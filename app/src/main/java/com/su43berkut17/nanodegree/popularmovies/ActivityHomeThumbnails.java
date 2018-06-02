package com.su43berkut17.nanodegree.popularmovies;

import com.su43berkut17.nanodegree.popularmovies.RV_related.thumb_adapter;
import com.su43berkut17.nanodegree.popularmovies.Utils.NetUtils;
import com.su43berkut17.nanodegree.popularmovies.Utils.DatabaseToJson;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

//recyclerview related
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.su43berkut17.nanodegree.popularmovies.RV_related.thumbnail;
import com.su43berkut17.nanodegree.popularmovies.RV_related.thumb_adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;


public class ActivityHomeThumbnails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, thumb_adapter.movieClickListener{
    //loader variables
    private static int LOADER_MOVIE_ID=1994;

    //menu variable
    private static String SORT_POPULAR="popular";
    private static String SORT_TOP="top_rated";
    private static String SORT_FAVORITES="favorites";

    //recycler view
    private RecyclerView mainView;
    private RecyclerView.Adapter adapter;
    private List<thumbnail> thumbnailList;

    //interface
    private ProgressBar w_progress;
    private TextView tv_noConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_thumbnails);

        w_progress=(ProgressBar)findViewById(R.id.w_progress);
        tv_noConnection=(TextView)findViewById(R.id.noConnection);

        Bundle typeOfSort=new Bundle();
        typeOfSort.putString("TYPE_SORT",SORT_POPULAR);

        //load the initial URL
        //getSupportLoaderManager().initLoader(LOADER_MOVIE_ID, typeOfSort, this);
        thumbnailList=new ArrayList<>();

        initiateLoading(SORT_POPULAR);
    }

    private void initiateLoading(String sort){
        if (sort!=SORT_FAVORITES){
            Context context=getApplicationContext();

            //we check if there's connection
            ConnectivityManager cm =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            Log.i("MAIN_ACT","The status of the net is "+" the status of the boolean is "+isConnected);

            if (isConnected==true) {
                tv_noConnection.setVisibility(View.INVISIBLE);
                LoaderManager loaderManager = getSupportLoaderManager();
                Loader<String> movieLoader = loaderManager.getLoader(LOADER_MOVIE_ID);
                Bundle typeOfSort = new Bundle();
                typeOfSort.putString("TYPE_SORT", sort);

                if (movieLoader == null) {
                    loaderManager.initLoader(LOADER_MOVIE_ID, typeOfSort, this);
                } else {
                    loaderManager.restartLoader(LOADER_MOVIE_ID, typeOfSort, this);
                }
            }else{
                //we show the no connection
                tv_noConnection.setText(R.string.no_connection);
                tv_noConnection.setVisibility(View.VISIBLE);
                w_progress.setVisibility(View.INVISIBLE);
            }
        }else{
            LoaderManager loaderManager=getSupportLoaderManager();
            Loader<String> movieLoader = loaderManager.getLoader(LOADER_MOVIE_ID);
            Bundle typeOfSort = new Bundle();
            typeOfSort.putString("TYPE_SORT", sort);
            if (movieLoader == null) {
                loaderManager.initLoader(LOADER_MOVIE_ID, typeOfSort, this);
            } else {
                loaderManager.restartLoader(LOADER_MOVIE_ID, typeOfSort, this);
            }
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                w_progress.setVisibility(View.VISIBLE);
               forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    Bundle arg=args;
                    String sorting=args.getString("TYPE_SORT");

                    if (sorting!=SORT_FAVORITES) {
                        //for the web loaders
                        String finalString = NetUtils.getMovieList(NetUtils.urlBuilder(sorting));
                        return finalString;
                    }else{
                        //for the content provider loader
                        Log.i("where to go","we are GOING TO DATABASE");
                        String finalString=DatabaseToJson.FullDatabaseToJson(getContext());
                        return finalString;
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        //we create the recyclerview here
        try {
            Log.i("retrieved from: ",data);
            if (data!=null) {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                thumbnailList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    thumbnail thumbs = new thumbnail(object.getString("original_title")
                            ,object.getString("poster_path")
                            ,object.getString("backdrop_path")
                            ,object.getString("overview")
                            ,object.getString("release_date")
                            ,object.getInt("vote_average")
                            ,object.getString("id"));
                    thumbnailList.add(thumbs);
                }

                //we check if it is empty to show the favorites
                if (jsonArray.length()==0){
                    tv_noConnection.setText(R.string.no_favorite);
                    tv_noConnection.setVisibility(View.VISIBLE);
                    w_progress.setVisibility(View.INVISIBLE);
                }else {
                    w_progress.setVisibility(View.INVISIBLE);
                    tv_noConnection.setVisibility(View.INVISIBLE);
                }

                //create the recyclerview
                mainView = (RecyclerView) findViewById(R.id.rv_thumbnails);
                mainView.setLayoutManager(new GridLayoutManager(this, 2));
                adapter = new thumb_adapter(thumbnailList, getApplicationContext(), this);
                mainView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else{
                w_progress.setVisibility(View.INVISIBLE);
                tv_noConnection.setVisibility(View.VISIBLE);
                tv_noConnection.setText(R.string.no_connection);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onMovieItemClick(thumbnail contentClicked){
        //start new activity
        Intent intent = new Intent(getBaseContext(), detailActivity.class);
        intent.putExtra("detailParce",contentClicked);
        startActivity(intent);
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_popular:
                initiateLoading(SORT_POPULAR);
                return true;
            case R.id.id_top:
                initiateLoading(SORT_TOP);
                return true;
            case R.id.id_favorites:
                initiateLoading(SORT_FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
