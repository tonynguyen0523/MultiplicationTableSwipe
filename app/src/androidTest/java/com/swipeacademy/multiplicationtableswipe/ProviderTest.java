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

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by tonyn on 5/21/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ProviderTest {

    public void deleteAllRecordFromDB(){
        TableDbHelper dbHelper = new TableDbHelper(getTargetContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        database.delete(TableContract.TableEntry.TABLE_NAME, null, null);
        database.delete(TableContract.ResultsEntry.TABLE_NAME, null, null);
        database.close();
    }

    public void deleteAllRecords(){
        deleteAllRecordFromDB();
    }

    @Before
    public void setUp() throws Exception{
       deleteAllRecords();
    }

    @Test
    public void testBasicTablesQuery(){
        TableDbHelper dbHelper = new TableDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createOnesTablesValues();
        long tablesID = TestUtilities.insertTableTablesValue(getTargetContext());

        ContentValues resultsValues = TestUtilities.createResultsValues(tablesID);

        long resultsRowID = db.insert(TableContract.ResultsEntry.TABLE_NAME,null,resultsValues);
        assertTrue("Unable to Insert ResultsEntry into the Database", resultsRowID != -1);

        db.close();

        Cursor tableCursor = getTargetContext().getContentResolver().query(
                TableContract.ResultsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicTablesQuery", tableCursor, resultsValues);
    }

    @Test
    public void testBasicTableQueries(){
         TableDbHelper dbHelper = new TableDbHelper(getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createOnesTablesValues();
        long tableRowId = TestUtilities.insertTableTablesValue(getTargetContext());

        Cursor tableCursor = getTargetContext().getContentResolver().query(
                TableContract.TableEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        TestUtilities.validateCursor("testBasicTableQueries, table query", tableCursor, testValues);
    }

    @Test
    public void testGetType(){
        String type = getTargetContext().getContentResolver().getType(TableContract.ResultsEntry.CONTENT_URI);
        assertEquals("Error: the ResultsEntry CONTENT_URI should return ResultsEntry.CONTENT_TYPE",
                TableContract.ResultsEntry.CONTENT_TYPE, type);

        String testTable = "ones";
        type = getTargetContext().getContentResolver().getType(
                TableContract.ResultsEntry.buildTablesResults(testTable));
        assertEquals("Error: the ResultsEntry CONTENT_URI with table should return ResultsEntry.CONTENT_TYPE",
                TableContract.ResultsEntry.CONTENT_TYPE, type);

    }
}
