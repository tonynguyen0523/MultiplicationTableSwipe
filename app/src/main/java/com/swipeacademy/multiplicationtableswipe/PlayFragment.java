package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;
import com.swipeacademy.multiplicationtableswipe.Util.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayFragment extends Fragment {

    @BindView(R.id.play_answer_choices_area)
    View mChoiceArea;
    @BindView(R.id.play_question1)TextView mQuestion1;
    @BindView(R.id.play_question2)TextView mQuestion2;
    @BindView(R.id.choice1)TextView mChoice1;
    @BindView(R.id.choice2)TextView mChoice2;
    @BindView(R.id.choice3)TextView mChoice3;
    @BindView(R.id.choice4)TextView mChoice4;


    private ArrayList<Integer> mRemainingQuestionsIDs;
    private ArrayList<String> mCorrections;
    private int mAnswerQuestionID;
    private int mCorrectAnswer;
    private boolean isCorrections;
    private int mQuestionSize;
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

        isCorrections = !CorrectionsUtil.getCorrections(getContext()).isEmpty();

        int questionAmount = Utility.getSelectedAmount(getContext());

        // Retrieve available questions
        if (!isCorrections) {
            mRemainingQuestionsIDs = QuestionSample.getAllQuestionsIDs(getContext(), questionAmount);
            mCorrections = new ArrayList<>();
        } else {
            mCorrections = new ArrayList<>(CorrectionsUtil.getCorrections(getContext()));
            mRemainingQuestionsIDs = QuestionSample.getAllCorrectionsIDs(mCorrections);
        }

        mQuestionSize = mRemainingQuestionsIDs.size();

        final TextView[] mChoicesIDs = {mChoice1,mChoice2,mChoice3,mChoice4};
        generateQuestion(mRemainingQuestionsIDs, mChoicesIDs);

        mChoiceArea.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
                nextQuestion(mChoicesIDs, Integer.valueOf(mChoice2.getText().toString()));
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                nextQuestion(mChoicesIDs, Integer.valueOf(mChoice3.getText().toString()));
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                nextQuestion(mChoicesIDs, Integer.valueOf(mChoice1.getText().toString()));
            }

            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
                nextQuestion(mChoicesIDs, Integer.valueOf(mChoice4.getText().toString()));
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void nextQuestion(TextView[] choicesTV, int userAnswer) {

        int mCurrentScore = Utility.getCurrentScore(getContext());
        int mRemainingQuestions = Utility.getRemainingQuestions(getContext());
        boolean correct = Utility.userCorrect(mCorrectAnswer,userAnswer);

        //If user chose correct answer, increase score by 1,
        //else add questionId to correctionsList
        if(correct){
            mCurrentScore++;
        } else{
            mCorrections.add(Integer.toString(mAnswerQuestionID));
        }

        mRemainingQuestions--;

        Utility.setCurrentScore(getContext(), mCurrentScore);
        Utility.setRemainingQuestions(getContext(), mRemainingQuestions);

        // Remove question so no repeat
        mRemainingQuestionsIDs.remove(Integer.valueOf(mAnswerQuestionID));

        // Check if there is any questions left, if not end game
        if (mRemainingQuestions == 0) {
            Intent intent = new Intent(getActivity(), PlayResultActivity.class);
            ((PlayActivity) getActivity()).stopTimer();
            CorrectionsUtil.editCorrectionsList(getContext(),mCorrections);
            startActivity(intent);
        } else {
            // Generate new question
            generateQuestion(mRemainingQuestionsIDs, choicesTV);
            // Update numbers
            ((PlayActivity) getActivity()).updateNumbers(mCurrentScore, mRemainingQuestions);
        }
    }

    private void generateQuestion(ArrayList<Integer> remainingQuestions, TextView[] choicesTV){

        int questionID = Utility.chooseQuestionID(remainingQuestions);
        QuestionSample qs = QuestionSample.getQuestionByID(getContext(),questionID);

        setUpQuestion(qs);
        mCorrectAnswer = qs != null ? qs.getAnswer() : 0;
        mAnswerQuestionID = questionID;

        Log.d("REMAINING", Integer.toString(remainingQuestions.size()));

        initializeChoices(qs != null ? qs.getChoices() : null, choicesTV);
    }

    private void setUpQuestion(QuestionSample qs){

        ArrayList<Integer> questions = qs.getQuestions();
        Collections.shuffle(questions);

        mQuestion1.setText(Integer.toString(questions.get(0)));
        mQuestion2.setText(Integer.toString(questions.get(1)));

    }

    private void initializeChoices(ArrayList<Integer> choicesList, TextView[] choicesTV){

        Collections.shuffle(choicesList);

        TextView[] choices = new TextView[choicesTV.length];
        for(int i = 0; i < choicesList.size(); i++){
            TextView currentChoice = choicesTV[i];
            choices[i] = currentChoice;
            currentChoice.setText(Integer.toString(choicesList.get(i)));
        }
    }
}
