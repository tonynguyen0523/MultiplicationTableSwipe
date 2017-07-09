package com.swipeacademy.multiplicationtableswipe;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Random;

import static com.swipeacademy.multiplicationtableswipe.data.TableContract.ResultsEntry;
import static com.swipeacademy.multiplicationtableswipe.data.TableContract.TableEntry;

/**
 * Created by tonyn on 5/5/2017.
 *
 */

class PrefUtility {

    private static final String CURRENT_SCORE = "current_score";
    private static final String REMAINING_QUESTIONS = "remaining_questions";
    private static final String AMOUNT_SELECTED = "amount_selected";
    private static final String TABLE_SELECTED = "table_selected";
    private static final String ASSET_SELECTED = "asset_selected";
    private static final String IS_CORRECTIONS = "is_corrections";
    private static final String USER_SCHOOL_GRADE = "school_grade";
    private static final String RECENT_24 = "recent_24";
    private static final String RECENT_48 = "recent_48";
    private static final String RECENT_72 = "recent_72";

    static int chooseQuestionID(ArrayList<Integer> remainingQuestions) {
        Random r = new Random();
        int answerIndex = r.nextInt(remainingQuestions.size());
        return remainingQuestions.get(answerIndex);
    }

    static boolean userCorrect(int correctAnswer, int userAnswer) {
        return userAnswer == correctAnswer;
    }

    static int getCurrentScore(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CURRENT_SCORE, 0);
    }

    static void setCurrentScore(Context context, int currentScore) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(CURRENT_SCORE, currentScore);
        spe.apply();
    }

    static int getRemainingQuestions(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(REMAINING_QUESTIONS, 10);
    }

    static void setRemainingQuestions(Context context, int remainingQuestions) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(REMAINING_QUESTIONS, remainingQuestions);
        spe.apply();
    }

    static void setSelectedAmount(Context context, int amount) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_amount_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(AMOUNT_SELECTED, amount);
        spe.apply();
    }

    static int getSelectedAmount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_amount_key), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(AMOUNT_SELECTED, 0);
    }

    static void setSelectedTable(Context context, String table) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_selected_table_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putString(TABLE_SELECTED, table);
        spe.apply();
    }

    static String getSelectedTable(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_selected_table_key), Context.MODE_PRIVATE);
        return sharedPreferences.getString(TABLE_SELECTED, "table");

    }

    static void setRecent24(Context context, int result) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_recent_24_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(RECENT_24, result);
        spe.apply();
    }

    static String getRecent24(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_recent_24_key), Context.MODE_PRIVATE);
        int result = sharedPreferences.getInt(RECENT_24, -1);
        if (result == -1) {
            return "--";
        } else {
            return Integer.toString(result);
        }
    }

    static void setRecent48(Context context, int result) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_recent_48_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(RECENT_48, result);
        spe.apply();
    }

    static String getRecent48(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_recent_48_key), Context.MODE_PRIVATE);
        int result = sharedPreferences.getInt(RECENT_48, -1);
        if (result == -1) {
            return "--";
        } else {
            return Integer.toString(result);
        }
    }

    static void setRecent72(Context context, int result) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_recent_72_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(RECENT_72, result);
        spe.apply();
    }

    static String getRecent72(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_recent_72_key), Context.MODE_PRIVATE);
        int result = sharedPreferences.getInt(RECENT_72, -1);
        if (result == -1) {
            return "--";
        } else {
            return Integer.toString(result);
        }
    }

    static void setSelectedAsset(Context context, String asset) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_selected_assets_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putString(ASSET_SELECTED, asset);
        spe.apply();
    }

    static String getSelectedAsset(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_selected_assets_key), Context.MODE_PRIVATE);
        return sharedPreferences.getString(ASSET_SELECTED, "asset");

    }

    static void setIsCorrections(Context context, boolean bool) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_is_corrections), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putBoolean(IS_CORRECTIONS, bool);
        spe.apply();
    }

    static boolean getIsCorrections(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_is_corrections), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_CORRECTIONS, false);
    }

    static int getSchoolGrade(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(USER_SCHOOL_GRADE, -1);
    }

    static void setSchoolGrade(Context context, int grade) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(USER_SCHOOL_GRADE, grade);
        editor.apply();
    }

    static void saveResults(Context context, String table, String date, int correct) {

        long tableId = addTableToDatabase(context, table);

        ContentValues values = new ContentValues();
        values.put(ResultsEntry.COLUMN_TABLES_KEY, tableId);
        values.put(ResultsEntry.COLUMN_DATE, date);
        values.put(ResultsEntry.COLUMN_TOTAL_RIGHT, correct);
        values.put(ResultsEntry.COLUMN_TIME, 0);

        context.getContentResolver().insert(ResultsEntry.CONTENT_URI, values);
    }

    private static long addTableToDatabase(Context context, String table) {

        long tableId;

        Cursor tableCursor = context.getContentResolver().query(
                TableEntry.CONTENT_URI,
                new String[]{TableEntry._ID},
                TableEntry.COLUMN_TABLES + " =? ",
                new String[]{table},
                null
        );

        assert tableCursor != null;
        if (tableCursor.moveToFirst()) {
            int tableIndex = tableCursor.getColumnIndex(TableEntry._ID);
            tableId = tableCursor.getLong(tableIndex);
        } else {
            ContentValues tableValues = new ContentValues();
            tableValues.put(TableEntry.COLUMN_TABLES, table);

            Uri insertedUri = context.getContentResolver().insert(
                    TableEntry.CONTENT_URI,
                    tableValues
            );
            tableId = ContentUris.parseId(insertedUri);
        }
        tableCursor.close();
        return tableId;
    }
}
