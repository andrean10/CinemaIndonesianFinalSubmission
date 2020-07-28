package com.dicoding.cinemaindonesiansubmission4.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

public class Helper {
    private DatabaseHelper databaseHelper;
    private static Helper INSTANCE;
    private static SQLiteDatabase database;

    public Helper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static Helper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Helper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor queryAll(String table) {
        return database.query(
                table,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC"
        );
    }

    public Cursor queryById(String table, String id) {
        return database.query(
                table,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null
        );
    }

    public long insert(String table, ContentValues values) {
        return database.insert(table, null, values);
    }

    public int deleteById(String table, String id) {
        return database.delete(
                table,
                _ID + " = ?",
                new String[]{id}
        );
    }
}