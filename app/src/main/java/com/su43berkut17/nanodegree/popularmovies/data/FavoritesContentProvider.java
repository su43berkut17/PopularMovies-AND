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
import android.util.Log;

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
        final SQLiteDatabase db=mFavoriteHelper.getReadableDatabase();
        int match=sUriMatcher.match(uri);

        Cursor returnCursor;

        Log.i("FAV_CP","We will do a query");
        switch (match){
            case FAVORITES:
                Log.i("FAV_CP","We are reading from case favorites, the query");
                returnCursor=db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                        );
                break;
            case FAVORITE_ID:
                Log.i("FAV_CP","We are reading from case individual, the query");
                returnCursor=null;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoriteHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case FAVORITES:
                long id=db.insert(TABLE_NAME,null,values);
                if (id>0){
                    returnUri=ContentUris.withAppendedId(favoritesContract.FavoritesEntry.FAVORITES_URI,id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case FAVORITES:
                int id=db.delete(TABLE_NAME,selection,selectionArgs);
                if (id>0){
                    returnUri=ContentUris.withAppendedId(favoritesContract.FavoritesEntry.FAVORITES_URI,id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }

                return id;
            default:
                throw new UnsupportedOperationException("Unknown uri"+uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
