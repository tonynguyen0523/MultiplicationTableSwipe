package com.swipeacademy.multiplicationtableswipe;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.swipeacademy.multiplicationtableswipe.R;
import com.swipeacademy.multiplicationtableswipe.data.TableContract;
import com.swipeacademy.multiplicationtableswipe.data.TableDbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.swipeacademy.multiplicationtableswipe.data.TableContract.*;

/**
 * Created by tonyn on 5/5/2017.
 */

public class Utility {

    private static final String CURRENT_SCORE = "current_score";
    private static final String REMAINING_QUESTIONS = "remaining_questions";
    private static final String FINISHED_TIME = "finished_time";
    private static final String AMOUNT_SELECTED = "amount_selected";
//    private static final int NUM_ANSWERS = 4;

//    static ArrayList<Integer> generateQuestion(ArrayList<Integer> remainingQuestionIDs){
//
//        Collections.shuffle(remainingQuestionIDs);
//
//        ArrayList<Integer> answers = new ArrayList<>();
//        for(int i = 0; i < NUM_ANSWERS; i++){
//            if(i < remainingQuestionIDs.size()){
//                answers.add(remainingQuestionIDs.get(i));
//            }
//        }
//
//        return answers;
//    }
//
//    static int getCorrectAnswerID(ArrayList<Integer> answers){
//        Random r = new Random();
//        int answerIndex = r.nextInt(answers.size());
//        return answers.get(answerIndex);
//    }

    static int chooseQuestionID(ArrayList<Integer> remainingQuestions){
        Random r = new Random();
        int answerIndex = r.nextInt(remainingQuestions.size());
        return remainingQuestions.get(answerIndex);
    }

    static boolean userCorrect(int correctAnswer, int userAnswer){
        return userAnswer == correctAnswer;
    }

    static int getCurrentScore(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CURRENT_SCORE,0);
    }

    static void setCurrentScore(Context context, int currentScore){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(CURRENT_SCORE,currentScore);
        spe.apply();
    }

    static int getRemainingQuestions(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(REMAINING_QUESTIONS,10);
    }

    static void setRemainingQuestions(Context context, int remainingQuestions){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(REMAINING_QUESTIONS,remainingQuestions);
        spe.apply();
    }

    static void setFinishedTime(Context context, long finishedTime){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putLong(FINISHED_TIME, finishedTime);
        spe.apply();
    }

    static long getFinishedTime(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getLong(FINISHED_TIME, 0);
    }

    static void setSelectedAmount(Context context,int amount){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_amount_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sharedPreferences.edit();
        spe.putInt(AMOUNT_SELECTED,amount);
        spe.apply();
    }

    static int getSelectedAmount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.pref_amount_key), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(AMOUNT_SELECTED, 0);
    }

    static void saveResults(Context context, String table, String date, int correct, long time){

        long tableId = addTableToDatabase(context, table);

        ContentValues values = new ContentValues();
        values.put(ResultsEntry.COLUMN_TABLES_KEY,tableId);
        values.put(ResultsEntry.COLUMN_DATE,date);
        values.put(ResultsEntry.COLUMN_TOTAL_RIGHT,correct);
        values.put(ResultsEntry.COLUMN_TIME,time);

        context.getContentResolver().insert(ResultsEntry.CONTENT_URI, values);
    }

    static long addTableToDatabase(Context context, String table){

        long tableId;

        Cursor tableCursor = context.getContentResolver().query(
                TableEntry.CONTENT_URI,
                new String[]{TableEntry._ID},
                TableEntry.COLUMN_TABLES + " =? ",
                new String[]{table},
                null
        );

        if (tableCursor.moveToFirst()) {
            int tableIndex = tableCursor.getColumnIndex(TableEntry._ID);
            tableId = tableCursor.getLong(tableIndex);
        } else {
            ContentValues tableValues = new ContentValues();
            tableValues.put(TableEntry.COLUMN_TABLES,table);

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
