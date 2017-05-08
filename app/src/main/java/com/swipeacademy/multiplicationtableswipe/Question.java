package com.swipeacademy.multiplicationtableswipe;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by tonyn on 5/5/2017.
 */

public class Question {

    private int mQuestionID;
    private String mQuestion;
    private int mAnswer;

    private Question(int questionId, String question, int answer){
        mQuestionID = questionId;
        mQuestion = question;
        mAnswer = answer;
    }



    static Question getQuestionByID(Context context, int questionID){
        JsonReader reader;
        try {
            reader = readJsonFile(context);
            reader.beginArray();
            while (reader.hasNext()){
                Question currentQuestion = readEntry(reader);
                if(currentQuestion.getQuestionID() == questionID){
                    reader.close();
                    return  currentQuestion;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static ArrayList<Integer> getAllQuestionsIDs(Context context){
        JsonReader reader;
        ArrayList<Integer> questionIDs = new ArrayList<>();
        try {
            reader = readJsonFile(context);
            reader.beginArray();
            while (reader.hasNext()){
                questionIDs.add(readEntry(reader).getQuestionID());
                Log.d("QLOGTAG", Integer.toString(readEntry(reader).getQuestionID()));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return questionIDs;
    }

    private static Question readEntry(JsonReader reader){

        String question = null;
        int id = -1;
        int answer = -1;

        try{
            reader.beginObject();
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name){
                    case "name":
                        question = reader.nextString();
                        break;
                    case "id":
                        id = reader.nextInt();
                        break;
                    case "answer":
                        answer = reader.nextInt();
                        break;
                    default:
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Question(id,question,answer);
    }

    private static JsonReader readJsonFile(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
//        String uri = null;
//
//        try{
//            for(String asset : assetManager.list("")){
//                if(asset.endsWith("mix.json")){
//                    uri = "asset:///" + asset;
//                }
//            }
//
//        } catch (IOException e) {
//            Toast.makeText(context, "Error reading json file!", Toast.LENGTH_LONG)
//                    .show();
//        }

        InputStream inputStream = assetManager.open("mix.json");

        JsonReader reader;
        reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));


        return reader;
    }

    // Getters and Setters
    public int getAnswer() {

        return mAnswer;
    }

    public void setAnswer(int answer) {
        mAnswer = answer;
    }

    public String getQuestion() {

        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public int getQuestionID() {
        return mQuestionID;
    }

    public void setQuestionID(int questionID) {
        mQuestionID = questionID;
    }



}


