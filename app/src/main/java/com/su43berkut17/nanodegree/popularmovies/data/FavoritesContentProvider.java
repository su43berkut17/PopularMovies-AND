package com.su43berkut17.nanodegree.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static  com.su43berkut17.nanodegree.popularmovies.data.favoritesContract.FavoritesEntry.TABLE_NAME;

public class FavoritesContentProvider extends ContentProvider{
    public static final int FAVORITES = 100;
    public static final int FAVORITE_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(favoritesContract.AUTHORITY,favoritesContract.PATH_FAVORITES,FAVORITES);
        uriMatcher.addURI(favoritesContract.AUTHORITY,favoritesContract.PATH_FAVORITES+"/#",FAVORITE_ID);

        return uriMatcher;
    }

    private favoritesDbHelper mFavoriteHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteHelper = new favoritesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
