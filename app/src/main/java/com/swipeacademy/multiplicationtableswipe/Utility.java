package com.swipeacademy.multiplicationtableswipe;

import android.content.Context;
import android.content.SharedPreferences;

import com.swipeacademy.multiplicationtableswipe.R;

/**
 * Created by tonyn on 5/5/2017.
 */

public class Utility {

    private static final String CURRENT_SCORE = "current_score";
    private static final String REMAINING_QUESTIONS = "remaining_questions";

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
}
