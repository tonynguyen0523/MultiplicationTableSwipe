package com.swipeacademy.multiplicationtableswipe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.swipeacademy.multiplicationtableswipe.data.TableContract;
import com.swipeacademy.multiplicationtableswipe.data.TableDbHelper;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by tonyn on 5/21/2017.
 */

public class TestUtilities {

    private static String TEST_TABLES = "ones";
    private static final long TEST_DATE = 1419033600L; // December 20th, 2014
    private static final long TEST_TIME = 100000;

    static ContentValues createOnesTablesValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(TableContract.TableEntry.COLUMN_TABLES, TEST_TABLES);
        return testValues;
    }

    static ContentValues createResultsValues(long tablesId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableContract.ResultsEntry.COLUMN_TABLES_KEY, tablesId);
        contentValues.put(TableContract.ResultsEntry.COLUMN_DATE, TEST_DATE);
        contentValues.put(TableContract.ResultsEntry.COLUMN_TOTAL_RIGHT, 50);
        contentValues.put(TableContract.ResultsEntry.COLUMN_TIME, TEST_TIME);

        return contentValues;
    }


    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static long insertTableTablesValue(Context context) {
        TableDbHelper dbHelper = new TableDbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createOnesTablesValues();

        long tablesID;
        tablesID = database.insert(TableContract.TableEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert ones tables value.", tablesID != -1);

        return tablesID;
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }
}



