package com.swipeacademy.multiplicationtableswipe;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;
import com.swipeacademy.multiplicationtableswipe.Util.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayFragment extends Fragment {

    @BindView(R.id.play_answer_choices_area)View mChoiceArea;
    @BindView(R.id.play_question1)TextView mQuestion1;
    @BindView(R.id.play_question2)TextView mQuestion2;
    @BindView(R.id.choice1)TextView mChoice1;
    @BindView(R.id.choice2)TextView mChoice2;
    @BindView(R.id.choice3)TextView mChoice3;
    @BindView(R.id.choice4)TextView mChoice4;
    @BindView(R.id.question_border_1)View mBorder1;
    @BindView(R.id.question_border_2)View mBorder2;
    @BindView(R.id.question_border_3)View mBorder3;
    @BindView(R.id.question_border_4)View mBorder4;

    private static final int CORRECT_ANSWER_DELAY_MILLIS = 500;
    private static final String REMAINING_QUESTIONS_KEY = "remainingList";
    private static final String CORRECTIONS_KEY = "correctionsList";

    private ArrayList<Integer> mRemainingQuestionsIDs;
    private ArrayList<String> mCorrections;
    private int mAnswerQuestionID;
    private int mCorrectAnswer;
    private boolean isCorrections;
    private Unbinder unbinder;

    public PlayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        unbinder = ButterKnife.bind(this, view);

        // Check if corrections mode
        isCorrections = Utility.getIsCorrections(getContext());
        // Get amount of questions selected
        int questionAmount = Utility.getSelectedAmount(getContext());

        if(savedInstanceState != null){
            mRemainingQuestionsIDs = savedInstanceState.getIntegerArrayList(REMAINING_QUESTIONS_KEY);
            mCorrections = savedInstanceState.getStringArrayList(CORRECTIONS_KEY);
        } else {
            // Retrieve available questions
            if (isCorrections) {
                mCorrections = new ArrayList<>(CorrectionsUtil.getCorrections(getContext()));
                mRemainingQuestionsIDs = QuestionSample.getAllCorrectionsIDs(mCorrections);
                Log.d("Check boolean", "true");
            } else {
                mRemainingQuestionsIDs = QuestionSample.getAllQuestionsIDs(getContext(), questionAmount);
                mCorrections = new ArrayList<>();
                Log.d("Check boolean", "false");
            }
        }

        Log.d("RemainingID SIZE", Integer.toString(mRemainingQuestionsIDs.size()));

        // Create array with choicesTV and generate questions and answers
        final TextView[] mChoicesIDs = {mChoice1,mChoice2,mChoice3,mChoice4};

        mChoiceArea.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
                nextQuestion(mChoicesIDs, 1, Integer.valueOf(mChoice2.getText().toString()));
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                nextQuestion(mChoicesIDs, 2, Integer.valueOf(mChoice3.getText().toString()));
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                nextQuestion(mChoicesIDs, 0, Integer.valueOf(mChoice1.getText().toString()));
            }

            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
                nextQuestion(mChoicesIDs, 3, Integer.valueOf(mChoice4.getText().toString()));
            }
        });

        generateQuestion(mRemainingQuestionsIDs, mChoicesIDs);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(REMAINING_QUESTIONS_KEY,mRemainingQuestionsIDs);
        outState.putStringArrayList(CORRECTIONS_KEY, mCorrections);
    }

    /**
     * Get next question method
     */
    private void nextQuestion(final TextView[] choicesTV, final int choiceTVID , int userAnswer) {

        // Get current score and remaining question
        int mCurrentScore = Utility.getCurrentScore(getContext());
        int mRemainingQuestions = Utility.getRemainingQuestions(getContext());

        boolean correct = Utility.userCorrect(mCorrectAnswer,userAnswer);

        //If user chose correct answer, increase score by 1,
        //else add questionId to correctionsList
        if(correct){
            mCurrentScore++;
            changeSelectedColor(choicesTV,choiceTVID,Color.GREEN, Color.GREEN);
        } else{
            mCorrections.add(Integer.toString(mAnswerQuestionID));
            changeSelectedColor(choicesTV,choiceTVID,Color.RED, Color.RED);
        }

        // Reduce remaining question by 1
        mRemainingQuestions--;

        // Update preferences
        Utility.setCurrentScore(getContext(), mCurrentScore);
        Utility.setRemainingQuestions(getContext(), mRemainingQuestions);

        // Remove question so no repeat
        mRemainingQuestionsIDs.remove(Integer.valueOf(mAnswerQuestionID));

        // Delay for user to see if they were correct of wrong
        final Handler handler = new Handler();
        final int finalMRemainingQuestions = mRemainingQuestions;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if there is any questions left, if not end game
                if (finalMRemainingQuestions == 0) {
                    ((PlayActivity) getActivity()).stopTimer();
                    CorrectionsUtil.editCorrectionsList(getContext(),mCorrections);
                    resultsDialog();
                } else {
                    // Generate new question
                    generateQuestion(mRemainingQuestionsIDs, choicesTV);
                    changeSelectedColor(choicesTV,choiceTVID,Color.BLACK,ContextCompat.getColor(getContext(),R.color.colorPrimary));
                    // Update numbers
                    ((PlayActivity) getActivity()).updateNumbers(finalMRemainingQuestions);
                }
            }
        }, CORRECT_ANSWER_DELAY_MILLIS );
    }

    /**
     * Generate new questions method
     */
    private void generateQuestion(ArrayList<Integer> remainingQuestions, TextView[] choicesTV){

        int questionID = Utility.chooseQuestionID(remainingQuestions);
        QuestionSample qs = QuestionSample.getQuestionByID(getContext(),questionID);

        setUpQuestion(qs);
        mCorrectAnswer = qs != null ? qs.getAnswer() : 0;
        mAnswerQuestionID = questionID;

        Log.d("REMAINING", Integer.toString(remainingQuestions.size()));

        initializeChoices(qs != null ? qs.getChoices() : null, choicesTV);
    }

    /**
     * Shuffle the two question numbers and random arrange them
     */
    private void setUpQuestion(QuestionSample qs){

        ArrayList<Integer> questions = qs.getQuestions();
        Collections.shuffle(questions);

        mQuestion1.setText(Integer.toString(questions.get(0)));
        mQuestion2.setText(Integer.toString(questions.get(1)));

    }

    /**
     * Shuffle choices and set to text view
     */
    private void initializeChoices(ArrayList<Integer> choicesList, TextView[] choicesTV){

        Collections.shuffle(choicesList);

        TextView[] choices = new TextView[choicesTV.length];
        for(int i = 0; i < choicesList.size(); i++){
            TextView currentChoice = choicesTV[i];
            choices[i] = currentChoice;
            currentChoice.setText(Integer.toString(choicesList.get(i)));
        }
    }

    /**
     * Show results dialog
     */
    private void resultsDialog(){
        DialogFragment dialogFragment = new ResultsDialog();
        dialogFragment.setCancelable(false);
        dialogFragment.show(getFragmentManager(),"resultsdialog");
    }

    /**
     * Change question border color
     */
    private void changeSelectedColor(TextView[] choicesTV, int choiceTVID, int choiceColor, int borderColor){

        TextView choiceID = choicesTV[choiceTVID];
        choiceID.setTextColor(choiceColor);

        mBorder1.setBackgroundColor(borderColor);
        mBorder2.setBackgroundColor(borderColor);
        mBorder3.setBackgroundColor(borderColor);
        mBorder4.setBackgroundColor(borderColor);
    }
}
