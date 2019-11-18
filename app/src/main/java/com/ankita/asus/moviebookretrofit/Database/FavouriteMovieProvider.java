package com.ankita.asus.moviebookretrofit.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavouriteMovieProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mDbHelper;
    static final int FAV_MOVIES = 100;
    static final int FAV_MOVIE_ITEM = 101;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PopulorMovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PopulorMovieContract.FAVOURITE_MOVIE, FAV_MOVIES);
        matcher.addURI(authority, PopulorMovieContract.FAVOURITE_MOVIE + "/#", FAV_MOVIE_ITEM);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());

        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAV_MOVIES: {
                cursor = mDbHelper.getReadableDatabase().query(
                        PopulorMovieContract.PopularMovieEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

        @Override
        public String getType ( Uri uri){
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case FAV_MOVIES:
                    return PopulorMovieContract.PopularMovieEntry.CONTENT_TYPE;
                case FAV_MOVIE_ITEM:
                    return PopulorMovieContract.PopularMovieEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);

            }


        }


        @Override
        public Uri insert (Uri uri, ContentValues values){
         final SQLiteDatabase db = mDbHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         Uri returnUri;

         switch (match) {
             case FAV_MOVIES: {
                 long _id = db.insert(PopulorMovieContract.PopularMovieEntry.TABLE_NAME, null, values);
                 if (_id > 0)
                     returnUri = PopulorMovieContract.PopularMovieEntry.buildMovieUri(_id);
                 else
                     throw new android.database.SQLException("Failed to insert row into " + uri);
                 break;
             }

             default:
                 throw new UnsupportedOperationException("Unknown uri: " + uri);
         }
         getContext().getContentResolver().notifyChange(uri,null);

               return returnUri;
                 }



     @Override
        public int delete (@NonNull Uri uri, @Nullable String selection, @Nullable String[]
        selectionArgs) {
         final SQLiteDatabase db = mDbHelper.getWritableDatabase();
         final int match = sUriMatcher.match(uri);
         int rowsDeleted;
         if (null == selection) selection = "1";
         switch (match) {
             case FAV_MOVIES:
                 rowsDeleted = db.delete(PopulorMovieContract.PopularMovieEntry.TABLE_NAME, selection, selectionArgs);
                 break;
             default:
                 throw new UnsupportedOperationException("Unknown uri : " + uri);

         }

         if (rowsDeleted != 0) {
             getContext().getContentResolver().notifyChange(uri, null);

         }
         return rowsDeleted;


     }

        @Override
        public int update (@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
        selection, @Nullable String[]selectionArgs){
           final SQLiteDatabase db = mDbHelper.getWritableDatabase();
           final int match = sUriMatcher.match(uri);
           int rowsUpdated;
           switch (match){
               case rowsUpdated = db.update(PopulorMovieContract.PopularMovieEntry.TABLE_NAME,values,selection,selectionArgs)
               break;
        default:
            throw new UnsupportedOperationException("Unknown uri : " + uri);




           }
           if(rowsUpdated != 0){
               getContext().getContentResolver().notifyChange(uri, null);



           }
               return rowsUpdated;
        }
    }
