package com.su43berkut17.nanodegree.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.su43berkut17.nanodegree.popularmovies.data.favoritesContract.FavoritesEntry;

public class favoritesDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="favorites.db";

    private static final int VERSION=1;

    favoritesDbHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE="CREATE TABLE " +FavoritesEntry.TABLE_NAME
                +"("
                +FavoritesEntry._ID+" INTEGER PRIMARY KEY, "
                +FavoritesEntry.COLUMN_MOVIE_ID+ " TEXT NOT NULL, "
                +FavoritesEntry.COLUMN_TITLE+" TEXT NOT NULL, "
                +FavoritesEntry.COLUMN_DESCRIPTION+" TEXT NOT NULL, "
                +FavoritesEntry.COLUMN_URL+" TEXT NOT NULL, "
                +FavoritesEntry.COLUMN_WIDE_URL+" TEXT NOT NULL, "
                +FavoritesEntry.COLUMN_RELEASE_DATE+" TEXT NOT NULL, "
                +FavoritesEntry.COLUMN_VOTE_AVERAGE+" TEXT NOT NULL"
                +");";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}