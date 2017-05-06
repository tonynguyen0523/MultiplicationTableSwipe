package com.swipeacademy.multiplicationtableswipe.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.swipeacademy.multiplicationtableswipe.data.TableContract.*;

/**
 * Created by tonyn on 5/5/2017.
 */

public class TableDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "samts.db";

    public TableDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATE TABLE tables (_id INTEGER PRIMARY KEY,numbers_table TEXT UNIQUE NOT NULL);
        final String SQL_CREATE_TABLES_TABLE = "CREATE TABLE " + TableEntry.TABLE_NAME + " ("
                + TableEntry._ID + " INTEGER PRIMARY KEY ,"
                + TableEntry.COLUMN_TABLES + " TEXT UNIQUE NOT NULL);";

        final String SQL_CREATE_RESULTS_TABLE = "CREATE TABLE " + ResultsEntry.TABLE_NAME + " ("
                + ResultsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ResultsEntry.COLUMN_TABLES_KEY + " TEXT NOT NULL, "
                + ResultsEntry.COLUMN_DATE + " INTEGER NOT NULL, "
                + ResultsEntry.COLUMN_TOTAL_RIGHT + " INTEGER NOT NULL, "
                + ResultsEntry.COLUMN_TIME + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + ResultsEntry.COLUMN_TABLES_KEY + ") REFERENCES "
                + TableEntry.TABLE_NAME + " ("
                + TableEntry._ID + "));";

        db.execSQL(SQL_CREATE_TABLES_TABLE);
        db.execSQL(SQL_CREATE_RESULTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}