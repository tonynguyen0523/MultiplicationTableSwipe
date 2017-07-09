package com.swipeacademy.multiplicationtableswipe;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import com.swipeacademy.multiplicationtableswipe.data.TableContract;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by tonyn on 5/21/2017.
 */
@RunWith(AndroidJUnit4.class)
public class ContractTest {

    private static final String TEST_TABLES = "/ones";

    @Test
    public void testBuildTableTables() {
        Uri tablesUri = TableContract.ResultsEntry.buildTablesResults(TEST_TABLES);
        assertNotNull("Error: Null Uri returned. You must fill-in buildTableTables in TablesContract.", tablesUri);

        assertEquals("Error: Results table not properly appended to the end of the Uri",
                TEST_TABLES, tablesUri.getLastPathSegment());
        assertEquals("Error: Results table Uri doesn't match our expected result",
                tablesUri.toString(),
                "content://com.swipeacademy.multiplicationtableswipe/results/%2Fones");
    }
}
