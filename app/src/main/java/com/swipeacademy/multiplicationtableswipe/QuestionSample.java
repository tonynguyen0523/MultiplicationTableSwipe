package com.swipeacademy.multiplicationtableswipe;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tonyn on 5/17/2017.
 */

public class QuestionSample {

    private int mQuestionID;
    private ArrayList<Integer> mQuestions;
    private ArrayList<Integer> mChoices;
    private int mAnswer;

    private QuestionSample(int questionId, ArrayList<Integer> question, ArrayList<Integer> choices, int answer) {
        mQuestionID = questionId;
        mQuestions = question;
        mChoices = choices;
        mAnswer = answer;
    }

    static QuestionSample getQuestionByID(Context context,String assetJson, int questionID) {
        JsonReader reader;
        try {
            reader = readJsonFile(context,assetJson);
            reader.beginArray();
            while (reader.hasNext()) {
                QuestionSample currentQuestion = readEntry(reader);
                if (currentQuestion.getQuestionID() == questionID) {
                    reader.close();
                    return currentQuestion;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static ArrayList<Integer> getAllQuestionsIDs(Context context,String assetJson) {
        JsonReader reader;
        ArrayList<Integer> questionIDs = new ArrayList<>();
        try {
            reader = readJsonFile(context, assetJson);
            reader.beginArray();
                while (reader.hasNext()) {
                    questionIDs.add(readEntry(reader).getQuestionID());
                }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(questionIDs);
        return questionIDs;
    }

    static ArrayList<Integer> getAllCorrectionsIDs(ArrayList<String> correctionIDs){
        ArrayList<Integer> correctionsIDs = new ArrayList<>();

        for(String stringValue : correctionIDs){
            correctionsIDs.add(Integer.parseInt(stringValue.trim()));
        }

        return correctionsIDs;
    }

    private static QuestionSample readEntry(JsonReader reader) {

        ArrayList<Integer> questions = null;
        ArrayList<Integer> choices = null;
        int id = -1;
        int answer = -1;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "id":
                        id = reader.nextInt();
                        break;
                    case "question":
                        questions = readChoices(reader);
                        break;
                    case "choices":
                        choices = readChoices(reader);
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

        return new QuestionSample(id, questions, choices, answer);
    }

    private static ArrayList<Integer> readChoices(JsonReader reader) throws IOException {

        ArrayList<Integer> choices = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            choices.add(reader.nextInt());
        }
        reader.endArray();
        return choices;
    }

    private static JsonReader readJsonFile(Context context, String assetJson) throws IOException {
        AssetManager assetManager = context.getAssets();

        InputStream inputStream = assetManager.open(assetJson);

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

    public ArrayList<Integer> getChoices() {
        return mChoices;
    }

    public ArrayList<Integer> getQuestions() {return mQuestions;}

    public int getQuestionID() {
        return mQuestionID;
    }

    public void setQuestionID(int questionID) {
        mQuestionID = questionID;
    }


}




