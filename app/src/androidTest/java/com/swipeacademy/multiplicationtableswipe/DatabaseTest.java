package com.swipeacademy.multiplicationtableswipe;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import com.swipeacademy.multiplicationtableswipe.data.TableContract;
import com.swipeacademy.multiplicationtableswipe.data.TableDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by tonyn on 5/21/2017.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {


    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(TableDbHelper.DATABASE_NAME);
    }


    @Test
    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(TableContract.TableEntry.TABLE_NAME);
        tableNameHashSet.add(TableContract.ResultsEntry.TABLE_NAME);

        getTargetContext().deleteDatabase(TableDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new TableDbHelper(getTargetContext()).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain both the tables entry
        // and movie entry tables
        assertTrue("Error: Your database was created without both the tables entry and movie entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + TableContract.TableEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a Hashset of all of the column name we want to look for
        final HashSet<String> tablesColumnHashSet = new HashSet<String>();
        tableNameHashSet.add(TableContract.TableEntry._ID);
        tableNameHashSet.add(TableContract.TableEntry.COLUMN_TABLES);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            tableNameHashSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required tables
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required tables entry columns",
                tableNameHashSet.isEmpty());

        db.close();
    }

    @Test
    public void testTablesTable() {
        insertTables();
    }

    @Test
    public void testResultsTable() {
        long tablesId = insertTables();

        assertFalse("Error: tables not inserted correctly.", tablesId == -1L);

        TableDbHelper dbHelper = new TableDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues resultsValues = TestUtilities.createResultsValues(tablesId);

        long resultsRowId = db.insert(TableContract.ResultsEntry.TABLE_NAME, null, resultsValues);
        assertTrue(resultsRowId != -1);

        Cursor resultsCursor = db.query(
                TableContract.ResultsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: No Records returned from results query", resultsCursor.moveToFirst());

        TestUtilities.validateCurrentRecord("testInsertReadDb resultsEntry failed to validate",
                resultsCursor, resultsValues);

        assertFalse("Error: More than one record returned from results query",
                resultsCursor.moveToNext());

        resultsCursor.close();
        dbHelper.close();
    }

    public long insertTables() {
        TableDbHelper dbHelper = new TableDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues textValues = TestUtilities.createOnesTablesValues();

        long tablesId;
        tablesId = db.insert(TableContract.TableEntry.TABLE_NAME, null, textValues);

        assertTrue(tablesId != -1);

        Cursor cursor = db.query(
                TableContract.TableEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue(" Error: No records returned from tables query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: tables Query Validation Failed", cursor, textValues);

        assertFalse("Error: More than one record returned from tables query", cursor.moveToNext());

        cursor.close();
        db.close();

        return tablesId;
    }
}