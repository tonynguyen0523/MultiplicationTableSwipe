package com.swipeacademy.multiplicationtableswipe;

import android.content.Context;
import android.content.SharedPreferences;

import com.swipeacademy.multiplicationtableswipe.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by tonyn on 5/5/2017.
 */

public class Utility {

    private static final String CURRENT_SCORE = "current_score";
    private static final String REMAINING_QUESTIONS = "remaining_questions";
    private static final int NUM_ANSWERS = 4;

    static ArrayList<Integer> generateQuestion(ArrayList<Integer> remainingQuestionIDs){

        Collections.shuffle(remainingQuestionIDs);

        ArrayList<Integer> answers = new ArrayList<>();
        for(int i = 0; i < NUM_ANSWERS; i++){
            if(i < remainingQuestionIDs.size()){
                answers.add(remainingQuestionIDs.get(i));
            }
        }

        return answers;
    }

    static int getCorrectAnswerID(ArrayList<Integer> answers){
        Random r = new Random();
        int answerIndex = r.nextInt(answers.size());
        return answers.get(answerIndex);
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
}
