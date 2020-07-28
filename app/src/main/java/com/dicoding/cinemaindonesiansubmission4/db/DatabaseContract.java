package com.dicoding.cinemaindonesiansubmission4.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_MOVIES = "table_movies";
    public static String TABLE_TVSHOW = "table_tvshow";
    public static final String AUTHORITY = "com.dicoding.picodiploma.cinemaindonesia";
    private static final String SCHEME = "content";

    public static class CinemaColumns implements BaseColumns {
        public static String TITLE = "title";
        public static String DATE = "date";
        public static String RATING = "rating";
        public static String POPULARITY = "popularity";
        public static String DESCRIPTION = "description";
        public static String IMAGES = "images";
    }

    // content://com.dicoding.picodiploma.cinemaindonesia/table_movies
    public static final Uri CONTENT_URI_MOVIES = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIES)
            .build();

    // content://com.dicoding.picodiploma.cinemaindonesia/table_tvshow
    public static final Uri CONTENT_URI_TVSHOW = new Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_TVSHOW)
            .build();
}
