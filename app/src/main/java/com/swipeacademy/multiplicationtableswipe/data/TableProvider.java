package com.swipeacademy.multiplicationtableswipe.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.swipeacademy.multiplicationtableswipe.data.TableContract.ResultsEntry;
import com.swipeacademy.multiplicationtableswipe.data.TableContract.TableEntry;

import static com.swipeacademy.multiplicationtableswipe.data.TableContract.CONTENT_AUTHORITY;
import static com.swipeacademy.multiplicationtableswipe.data.TableContract.PATH_RESULTS;
import static com.swipeacademy.multiplicationtableswipe.data.TableContract.PATH_TABLE;

/**
 * Created by tonyn on 5/5/2017.
 */

public class TableProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TableDbHelper mOpenHelper;

    // Content type constants.
    public static final int RESULTS = 100;
    public static final int RESULTS_WITH_TABLES = 101;
    public static final int RESULTS_WITH_TABLES_AND_DATE = 102;
    public static final int TABLES = 200;

    @Override
    public boolean onCreate() {
        mOpenHelper = new TableDbHelper(getContext());
        return true;
    }

    /**
     * INNER JOIN between results and tables table.
     */
    private static final SQLiteQueryBuilder sTableWithResultsQueryBuilder;

    static {
        sTableWithResultsQueryBuilder = new SQLiteQueryBuilder();

        // results INNER JOIN tables ON results.tables_id = tables._id
        sTableWithResultsQueryBuilder.setTables(
                ResultsEntry.TABLE_NAME + " INNER JOIN "
                        + TableEntry.TABLE_NAME
                        + " ON "
                        + ResultsEntry.TABLE_NAME
                        + "."
                        + ResultsEntry.COLUMN_TABLES_KEY
                        + " = "
                        + TableEntry.TABLE_NAME
                        + " . "
                        + TableEntry._ID
        );
    }

    /**
     * Retrieve data from selection tables.
     * tables.numbers_table = ?
     */
    private static final String sTablesSelection = TableEntry.TABLE_NAME + " . "
            + TableEntry.COLUMN_TABLES + " = ? ";

    private Cursor getResultsByTables(Uri uri, String[] projection, String sortOrder) {

        String tables = ResultsEntry.getTablesFromUri(uri);

        String[] selectionArgs = new String[]{tables};
        String selection = sTablesSelection;

        return sTableWithResultsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    /**
     * Construct UriMatcher with the content type constants
     */
    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PATH_RESULTS, RESULTS);
        matcher.addURI(authority, PATH_RESULTS + "/*", RESULTS_WITH_TABLES);
        matcher.addURI(authority, PATH_RESULTS + "/*/#", RESULTS_WITH_TABLES_AND_DATE);
        matcher.addURI(authority, PATH_TABLE, TABLES);

        return matcher;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case RESULTS_WITH_TABLES_AND_DATE:
                return ResultsEntry.CONTENT_ITEM_TYPE;
            case RESULTS_WITH_TABLES:
                return ResultsEntry.CONTENT_TYPE;
            case RESULTS:
                return ResultsEntry.CONTENT_TYPE;
            case TABLES:
                return TableEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case RESULTS_WITH_TABLES: {
                retCursor = getResultsByTables(uri, projection, sortOrder);
                break;
            }
            case RESULTS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ResultsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TABLES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TableEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RESULTS: {
                long _id = db.insert(ResultsEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ResultsEntry.buildResultsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case TABLES: {
                long _id = db.insert(TableEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TableEntry.buildTablesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
