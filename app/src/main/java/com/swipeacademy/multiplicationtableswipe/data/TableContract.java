package com.swipeacademy.multiplicationtableswipe.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tonyn on 5/5/2017.
 */

public class TableContract {

    // Content authority.
    public static final String CONTENT_AUTHORITY = "com.swipeacademy.multiplicationswipe";

    // Base Uri to contact the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_TABLE = "table";
    public static final String PATH_RESULTS = "results";

    /** Tables entries */
    public static final class TableEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TABLE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String TABLE_NAME = "tables";

        public static final String COLUMN_TABLES = "numbers_table";

        public static Uri buildTablesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    /** Results entries */
    public static final class ResultsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESULTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESULTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESULTS;

        public static final String TABLE_NAME = "results";

        public static final String COLUMN_TABLES_KEY = "tables_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TOTAL_RIGHT = "total_right";
        public static final String COLUMN_TIME = "time";

        public static Uri buildResultsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getTablesFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }


}