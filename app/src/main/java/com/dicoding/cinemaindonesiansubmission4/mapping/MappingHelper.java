package com.dicoding.cinemaindonesiansubmission4.mapping;

import android.database.Cursor;

import com.dicoding.cinemaindonesiansubmission4.entity.Database;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.DATE;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.DESCRIPTION;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.IMAGES;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.POPULARITY;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.RATING;
import static com.dicoding.cinemaindonesiansubmission4.db.DatabaseContract.CinemaColumns.TITLE;

public class MappingHelper {

    public static ArrayList<Database> mapCursorToArrayList(Cursor cinemaCursor) {
        ArrayList<Database> databaseList = new ArrayList<>();

        while (cinemaCursor.moveToNext()) {
            int id = cinemaCursor.getInt(cinemaCursor.getColumnIndexOrThrow(_ID));
            String title = cinemaCursor.getString(cinemaCursor.getColumnIndexOrThrow(TITLE));
            String date = cinemaCursor.getString(cinemaCursor.getColumnIndexOrThrow(DATE));
            float rating = cinemaCursor.getFloat(cinemaCursor.getColumnIndexOrThrow(RATING));
            String popularity = cinemaCursor.getString(cinemaCursor.getColumnIndexOrThrow(POPULARITY));
            String description = cinemaCursor.getString(cinemaCursor.getColumnIndexOrThrow(DESCRIPTION));
            String image = cinemaCursor.getString(cinemaCursor.getColumnIndexOrThrow(IMAGES));

            databaseList.add(new Database(id, rating, title, date, popularity, description, image));
        }
        return databaseList;
    }
}
