package com.su43berkut17.nanodegree.popularmovies.Utils;

import android.database.Cursor;
import android.content.Context;
import com.su43berkut17.nanodegree.popularmovies.data.favoritesContract;

public class DatabaseToJson {

    public static String FullDatabaseToJson(Context context){
        String finalResult;
        Boolean doWeNeedToDelete;
        doWeNeedToDelete=false;

        //we load the database in a cursor
        Cursor cursor=context.getContentResolver().query(favoritesContract.FavoritesEntry.FAVORITES_URI,
                null,
                null,
                null,
                null,
                null
        );

        finalResult="{\"page\":1,\"total_results\":361415,\"total_pages\":18071,\"results\":[";


        try {
            while (cursor.moveToNext()) {
                finalResult+="{";
                finalResult+="\"vote_average\":";
                finalResult+=cursor.getString(cursor.getColumnIndex(favoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE));
                finalResult+=",";

                finalResult+="\"id\":";
                finalResult+=cursor.getString(cursor.getColumnIndex(favoritesContract.FavoritesEntry.COLUMN_MOVIE_ID));
                finalResult+=",";

                finalResult+="\"original_title\":";
                finalResult+="\"";
                finalResult+=cursor.getString(cursor.getColumnIndex(favoritesContract.FavoritesEntry.COLUMN_TITLE));
                finalResult+="\"";
                finalResult+=",";

                finalResult+="\"poster_path\":";
                finalResult+="\"";
                finalResult+=cursor.getString(cursor.getColumnIndex(favoritesContract.FavoritesEntry.COLUMN_URL));
                finalResult+="\"";
                finalResult+=",";

                finalResult+="\"backdrop_path\":";
                finalResult+="\"";
                finalResult+=cursor.getString(cursor.getColumnIndex(favoritesContract.FavoritesEntry.COLUMN_WIDE_URL));
                finalResult+="\"";
                finalResult+=",";

                finalResult+="\"release_date\":";
                finalResult+="\"";
                finalResult+=cursor.getString(cursor.getColumnIndex(favoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE));
                finalResult+="\"";
                finalResult+=",";

                finalResult+="\"overview\":";
                finalResult+="\"";
                finalResult+=cursor.getString(cursor.getColumnIndex(favoritesContract.FavoritesEntry.COLUMN_DESCRIPTION));
                finalResult+="\"";

                finalResult+="},";
                doWeNeedToDelete=true;
            }

            //finish the object
            //we extract the last 2 digits
            if (doWeNeedToDelete) {
                finalResult = finalResult.substring(0, finalResult.length() - 1);
            }

            finalResult+="]}";
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
        }

        //we cycle for each cursor
        //{"page":1,"total_results":361415,"total_pages":18071,"results":[{"vote_count":8,"id":510819,"video":false,"vote_average":2.9,"title":"Dirty Dead Con Men","popularity":538.731117,"poster_path":"\/r70GGoZ5PqqokDDRnVfTN7PPDtJ.jpg","original_language":"en","original_title":"Dirty Dead Con Men","genre_ids":[28,80,18],"backdrop_path":"\/75RJi3yVZnZtVj4Kn1bYGzkhiEx.jpg","adult":false,"overview":"A cool and dangerous neo-noir crime film that revolves around the disturbed lives of two unlikely partners: Mickey Rady, a rogue undercover cop and Kook Packard, a smooth and charismatic con man. Together they rip off those operating outside of the law...for their own personal gain. But things go awry when one heist suck them deep into a city-wide conspiracy...","release_date":"2018-03-30"},{"vote_count":3811,"id":299536,"video":false,"vote_average":8.5,"title":"Avengers: Infinity War","popularity":531.896529,"poster_path":"\/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg","original_language":"en","original_title":"Avengers: Infinity War","genre_ids":[12,878,14,28],"backdrop_path":"\/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg","adult":false,"overview":"As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is

        return finalResult;
    }
}
