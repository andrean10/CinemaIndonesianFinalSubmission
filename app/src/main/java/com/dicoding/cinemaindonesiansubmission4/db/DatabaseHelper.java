package com.dicoding.cinemaindonesiansubmission4.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.DATE;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.DESCRIPTION;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.IMAGES;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.POPULARITY;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.RATING;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.TITLE;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.TABLE_MOVIES;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.TABLE_TVSHOW;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "Database";
    private static final int DATABASE_VERSION = 1;

    private static String SQL_CREATE_TABLE_MOVIES = String.format(
            "CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s FLOAT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL)",
            TABLE_MOVIES, _ID, TITLE, DATE, RATING, POPULARITY, DESCRIPTION, IMAGES
    );

    private static String SQL_CREATE_TABLE_TVSHOW = String.format(
            "CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s FLOAT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL,"
                    + " %s TEXT NOT NULL)",
            TABLE_TVSHOW, _ID, TITLE, DATE, RATING, POPULARITY, DESCRIPTION, IMAGES
    );

    private static String SQL_DELETE_TABLE_MOVIES = String.format("DROP TABLE IF EXISTS %s", TABLE_MOVIES);
    private static String SQL_DELETE_TABLE_TVSHOW = String.format("DROP TABLE IF EXISTS %s", TABLE_TVSHOW);

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIES);
        db.execSQL(SQL_CREATE_TABLE_TVSHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_MOVIES);
        db.execSQL(SQL_DELETE_TABLE_TVSHOW);
        onCreate(db);
    }
}