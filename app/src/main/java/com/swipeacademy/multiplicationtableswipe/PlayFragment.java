package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.swipeacademy.multiplicationtableswipe.Util.OnSwipeTouchListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayFragment extends Fragment {

    @BindView(R.id.swipe_view)
    View mSwipeView;
    @BindView(R.id.play_question)TextView mQuestionTV;

    private static final String REMAINING_QUESTIONS_KEY = "remaining_questions";
    private ArrayList<Integer> mRemainingQuestionsIDs;
    private ArrayList<Integer> mQuestionIDs;
    private int mAnswerQuestionID;
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

        boolean isNewGame = !getActivity().getIntent().hasExtra(REMAINING_QUESTIONS_KEY);

        if(isNewGame){
            // Load all questions
            mRemainingQuestionsIDs = Question.getAllQuestionsIDs(getContext());
        } else {
            mRemainingQuestionsIDs = getActivity().getIntent().getIntegerArrayListExtra(REMAINING_QUESTIONS_KEY);
        }

        generateQuestion(mRemainingQuestionsIDs);



        mSwipeView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
                nextQuestion();
                Toast.makeText(getContext(), "top", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                nextQuestion();
                Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                nextQuestion();
                Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
                nextQuestion();
                Toast.makeText(getContext(), "bottom", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void nextQuestion() {

        int mCurrentScore = Utility.getCurrentScore(getContext());
        int mRemainingQuestions = Utility.getRemainingQuestions(getContext());

        mCurrentScore++;
        mRemainingQuestions--;

        Utility.setCurrentScore(getContext(), mCurrentScore);
        Utility.setRemainingQuestions(getContext(), mRemainingQuestions);

        mRemainingQuestionsIDs.remove(Integer.valueOf(mAnswerQuestionID));

        if (mRemainingQuestions < 0) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        } else {
            generateQuestion(mRemainingQuestionsIDs);

            ((PlayActivity) getActivity()).updateNumbers(mCurrentScore, mRemainingQuestions);
        }
    }

    private void generateQuestion(ArrayList<Integer> remainingQuestions){
        // Generate question and get the correct answer
        mQuestionIDs = Utility.generateQuestion(remainingQuestions);
        mAnswerQuestionID = Utility.getCorrectAnswerID(mQuestionIDs);

        // Get current question and set to text view
        Question question = Question.getQuestionByID(getContext(),mAnswerQuestionID);
        assert question != null;
        mQuestionTV.setText(question.getQuestion());
    }


}
