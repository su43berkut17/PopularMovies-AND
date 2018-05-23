package com.su43berkut17.nanodegree.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class favoritesContract {
    public static final String AUTHORITY="com.su43berkut17.nanodefree.popularmovies";

    public static final Uri BASE_URI=Uri.parse("content://"+AUTHORITY);

    public static final String PATH_FAVORITES="favorites";

    public static final class FavoritesEntry implements BaseColumns{
        public static final Uri FAVORITES_URI=
                BASE_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        //table
        public static final String TABLE_NAME="favorites";
        public static final String COLUMN_MOVIE_ID="movieId";
        public static final String COLUMN_DESCRIPTION="description";
    }
}
