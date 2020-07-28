package com.dicoding.cinemaindonesiansubmission4.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.dicoding.cinemaindonesiansubmission4.db.Helper;
import com.dicoding.cinemaindonesiansubmission4.widget.movieWidget.FavoriteMoviesWidget;
import com.dicoding.cinemaindonesiansubmission4.widget.tvshowWidget.FavoriteTvShowWidget;

import java.util.Objects;

import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.AUTHORITY;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CONTENT_URI_MOVIES;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CONTENT_URI_TVSHOW;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.TABLE_MOVIES;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.TABLE_TVSHOW;
import static com.dicoding.cinemaindonesiansubmission4.widget.movieWidget.FavoriteMoviesWidget.UPDATE_WIDGET_FAVORITE;

public class CinemaProvider extends ContentProvider {
    private static final int MOVIES = 1;
    private static final int MOVIES_ID = 2;
    private static final int TVSHOW = 3;
    private static final int TV_SHOW_ID = 4;
    private Helper helper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.dicoding.picodiploma.cinemaindonesia/table_movies
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIES, MOVIES);

        // content://com.dicoding.picodiploma.cinemaindonesia/table_movies/id
        sUriMatcher.addURI(AUTHORITY,
                TABLE_MOVIES + "/#",
                MOVIES_ID);

        // content://com.dicoding.picodiploma.cinemaindonesia/table_tvshow
        sUriMatcher.addURI(AUTHORITY, TABLE_TVSHOW, TVSHOW);

        // content://com.dicoding.picodiploma.cinemaindonesia/table_tvshow/id
        sUriMatcher.addURI(AUTHORITY,
                TABLE_TVSHOW + "/#",
                TV_SHOW_ID);
    }

    @Override
    public boolean onCreate() {
        helper = Helper.getInstance(getContext());
        helper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                cursor = helper.queryAll(TABLE_MOVIES);
                break;
            case MOVIES_ID:
                cursor = helper.queryById(TABLE_MOVIES, uri.getLastPathSegment());
                break;
            case TVSHOW:
                cursor = helper.queryAll(TABLE_TVSHOW);
                break;
            case TV_SHOW_ID:
                cursor = helper.queryById(TABLE_TVSHOW, uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        Uri content;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                added = helper.insert(TABLE_MOVIES, values);
                content = CONTENT_URI_MOVIES;
                updateWidgetMovies();
                break;
            case TVSHOW:
                added = helper.insert(TABLE_TVSHOW, values);
                content = CONTENT_URI_TVSHOW;
                updateWidgetTvShow();
                break;
            default:
                added = 0;
                content = null;
                break;
        }

        getContext().getContentResolver().notifyChange(content, null);

        return Uri.parse(content + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        Uri content;
        switch (sUriMatcher.match(uri)) {
            case MOVIES_ID:
                deleted = helper.deleteById(TABLE_MOVIES, uri.getLastPathSegment());
                content = CONTENT_URI_MOVIES;
                updateWidgetMovies();
                break;
            case TV_SHOW_ID:
                deleted = helper.deleteById(TABLE_TVSHOW, uri.getLastPathSegment());
                content = CONTENT_URI_TVSHOW;
                updateWidgetTvShow();
                break;
            default:
                deleted = 0;
                content = null;
                break;
        }
        getContext().getContentResolver().notifyChange(content, null);

        return deleted;
    }

    public void updateWidgetMovies() {
        Intent intent = new Intent(getContext(), FavoriteMoviesWidget.class);
        intent.setAction(UPDATE_WIDGET_FAVORITE);
        Objects.requireNonNull(getContext()).sendBroadcast(intent);
    }

    public void updateWidgetTvShow() {
        Intent intent = new Intent(getContext(), FavoriteTvShowWidget.class);
        intent.setAction(UPDATE_WIDGET_FAVORITE);
        Objects.requireNonNull(getContext()).sendBroadcast(intent);
    }
}
