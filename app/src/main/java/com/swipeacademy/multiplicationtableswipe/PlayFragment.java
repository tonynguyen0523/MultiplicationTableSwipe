package com.swipeacademy.multiplicationtableswipe;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;
import com.swipeacademy.multiplicationtableswipe.Util.MyCountdownTimer;
import com.swipeacademy.multiplicationtableswipe.Util.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayFragment extends Fragment {

    @BindView(R.id.play_answer_choices_area)
    View mChoiceArea;
    @BindView(R.id.play_question1)
    TextView mQuestion1;
    @BindView(R.id.play_question2)
    TextView mQuestion2;
    @BindView(R.id.choice1)
    TextView mChoice1;
    @BindView(R.id.choice2)
    TextView mChoice2;
    @BindView(R.id.choice3)
    TextView mChoice3;
    @BindView(R.id.choice4)
    TextView mChoice4;
    @BindView(R.id.question_border_1)
    View mBorder1;
    @BindView(R.id.question_border_2)
    View mBorder2;
    @BindView(R.id.question_border_3)
    View mBorder3;
    @BindView(R.id.question_border_4)
    View mBorder4;
//    @BindView(R.id.countdown_timer_text_view)
//    TextView mCountdownTimerTV;
    @BindView(R.id.play_pause_button)
    ImageButton mPauseButton;

    private static final String REMAINING_QUESTIONS_KEY = "remainingList";
    private static final String CORRECTIONS_KEY = "correctionsList";
    private static final String CURRENT_QUESTION = "currentQuestion";
    private static final int DELAY = 250;
    private static final int CORRECTIONS_DELAY = 1000;
    private MyCountdownTimer countdownTimer;
    private ArrayList<Integer> mRemainingQuestionsIDs;
    private ArrayList<String> mCorrections;
    private String mSelectedAsset;
    private int mQuestionID;
    private int mCorrectAnswer;
    private int mCorrectTextViewID;
    private long mTimeLeft;
    private boolean mIsCorrection;
    private boolean mIsFinished;
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

        // Create array with choicesTV and generate questions and answers
        final TextView[] mChoicesIDs = {mChoice1, mChoice2, mChoice3, mChoice4};

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PlayPauseActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for textViews
        mChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion(mChoicesIDs, 0, Integer.valueOf(mChoice1.getText().toString()));

            }
        });
        mChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion(mChoicesIDs, 1, Integer.valueOf(mChoice2.getText().toString()));

            }
        });
        mChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion(mChoicesIDs, 2, Integer.valueOf(mChoice3.getText().toString()));

            }
        });
        mChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion(mChoicesIDs, 3, Integer.valueOf(mChoice4.getText().toString()));
            }
        });

        // Set swipe listener
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
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        pauseTimer();
        super.onPause();
    }

    @Override
    public void onResume() {
        resumeTimer();
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        final TextView[] mChoicesIDs = {mChoice1, mChoice2, mChoice3, mChoice4};

        // Check if corrections mode
        mIsCorrection = PrefUtility.getIsCorrections(getContext());
        // Check which asset to display
        mSelectedAsset = PrefUtility.getSelectedAsset(getContext());

        if(!mIsFinished) {
            if (savedInstanceState == null) {
                // Retrieve available questions
                if (mIsCorrection) {
                    mCorrections = new ArrayList<>(CorrectionsUtil.getCorrections(getContext()));
                    mRemainingQuestionsIDs = QuestionSample.getAllCorrectionsIDs(mCorrections);
                    generateQuestion(mRemainingQuestionsIDs, mChoicesIDs, false);
                } else {
                    mRemainingQuestionsIDs = QuestionSample.getAllQuestionsIDs(getContext(), mSelectedAsset);
                    mCorrections = new ArrayList<>();
                    generateQuestion(mRemainingQuestionsIDs, mChoicesIDs, false);
                }
            } else {
                mRemainingQuestionsIDs = savedInstanceState.getIntegerArrayList(REMAINING_QUESTIONS_KEY);
                mCorrections = savedInstanceState.getStringArrayList(CORRECTIONS_KEY);
                mQuestionID = savedInstanceState.getInt(CURRENT_QUESTION);
                generateQuestion(mRemainingQuestionsIDs, mChoicesIDs, true);
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(REMAINING_QUESTIONS_KEY, mRemainingQuestionsIDs);
        outState.putStringArrayList(CORRECTIONS_KEY, mCorrections);
        outState.putInt(CURRENT_QUESTION, mQuestionID);
    }

    /**
     * Get next question method
     */
    private void nextQuestion(final TextView[] choicesTV, final int choiceTVID, int userAnswer) {

        // Get current score and remaining question
        boolean correct = PrefUtility.userCorrect(mCorrectAnswer, userAnswer);
        int mCurrentScore = PrefUtility.getCurrentScore(getContext());
        int mRemainingQuestions = PrefUtility.getRemainingQuestions(getContext());
        int delay;

        // If user chose correct answer, increase score by 1,
        // else add questionId to correctionsList
        if (correct) {
            mCurrentScore++;
            changeSelectedColor(choicesTV, choiceTVID, ContextCompat.getColor(getContext(),R.color.correct), ContextCompat.getColor(getContext(),R.color.correct));
            delay = DELAY;
        } else if (mIsCorrection) {
            mCorrections.add(Integer.toString(mQuestionID));
            changeSelectedColor(choicesTV, choiceTVID, ContextCompat.getColor(getContext(),R.color.wrong), ContextCompat.getColor(getContext(),R.color.wrong));
            choicesTV[mCorrectTextViewID].setTextColor(ContextCompat.getColor(getContext(),R.color.correct));
            delay = CORRECTIONS_DELAY;
        } else {
            mCorrections.add(Integer.toString(mQuestionID));
            changeSelectedColor(choicesTV, choiceTVID, Color.RED, Color.RED);
            delay = DELAY;
        }

        // Reduce remaining question by 1
        mRemainingQuestions--;
        countdownTimer.cancel();

        // Update preferences
        PrefUtility.setCurrentScore(getContext(), mCurrentScore);
        PrefUtility.setRemainingQuestions(getContext(), mRemainingQuestions);

        // Remove question so no repeat
        mRemainingQuestionsIDs.remove(Integer.valueOf(mQuestionID));

        // Delay for user to see if they were correct of wrong
        final Handler handler = new Handler();
        final int finalMRemainingQuestions = mRemainingQuestions;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if there is any questions left, if not end game
                if (finalMRemainingQuestions == 0) {
                    mIsFinished = true;
                    countdownTimer.cancel();
                    ((PlayActivity) getActivity()).playFinished();
                    CorrectionsUtil.editCorrectionsList(getContext(), mCorrections);
                    Intent intent = new Intent(getContext(),PlayResultActivity.class);
                    ComponentName cn = intent.getComponent();
                    Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                    startActivity(mainIntent);
                } else {
                    // Generate new question
                    choicesTV[mCorrectTextViewID].setTextColor(Color.BLACK);
                    generateQuestion(mRemainingQuestionsIDs, choicesTV, false);
                    changeSelectedColor(choicesTV, choiceTVID, Color.BLACK, ContextCompat.getColor(getContext(), R.color.colorPrimary));
                    // Update numbers
                    ((PlayActivity) getActivity()).updateNumbers(finalMRemainingQuestions);
                }
            }
        }, delay);
    }

    /**
     * Generate new questions method
     */
    private void generateQuestion(ArrayList<Integer> remainingQuestions, TextView[] choicesTV, boolean reCreated) {

        if (!reCreated) {
            mQuestionID = PrefUtility.chooseQuestionID(remainingQuestions);
        }
        QuestionSample qs = QuestionSample.getQuestionByID(getContext(), mSelectedAsset, mQuestionID);

        setUpQuestion(qs);
        mCorrectAnswer = qs != null ? qs.getAnswer() : 0;
        initializeChoices(qs != null ? qs.getChoices() : null, choicesTV);
        startTimer();
    }

    /**
     * Shuffle the two question numbers and random arrange them
     */
    private void setUpQuestion(QuestionSample qs) {

        ArrayList<Integer> questions = qs.getQuestions();
        Collections.shuffle(questions);

        mQuestion1.setText(Integer.toString(questions.get(0)));
        setAnimation(mQuestion1);
        mQuestion2.setText(Integer.toString(questions.get(1)));
        setAnimation(mQuestion2);
    }

    /**
     * Shuffle choices and set to text view
     */
    private void initializeChoices(ArrayList<Integer> choicesList, TextView[] choicesTV) {

        Collections.shuffle(choicesList);

        TextView[] choices = new TextView[choicesTV.length];
        for (int i = 0; i < choicesList.size(); i++) {
            TextView currentChoice = choicesTV[i];
            int chosenChoice = choicesList.get(i);
            choices[i] = currentChoice;
            currentChoice.setText(Integer.toString(chosenChoice));
            setAnimation(currentChoice);
            if (PrefUtility.userCorrect(mCorrectAnswer, chosenChoice)) {
                mCorrectTextViewID = i;
            }
        }
    }

    /**
     * Show results dialog
     */
    private void resultsDialog() {
        DialogFragment dialogFragment = new PlayResultsDialog();
        dialogFragment.setCancelable(false);
        dialogFragment.setRetainInstance(true);
        dialogFragment.show(getFragmentManager(), "resultsdialog");
    }

    /**
     * Change question border color
     */
    private void changeSelectedColor(TextView[] choicesTV, int choiceTVID, int choiceColor, int borderColor) {

        if (choiceTVID != 5) {
            TextView choiceID = choicesTV[choiceTVID];
            choiceID.setTextColor(choiceColor);
        }

        mBorder1.setBackgroundColor(borderColor);
        mBorder2.setBackgroundColor(borderColor);
        mBorder3.setBackgroundColor(borderColor);
        mBorder4.setBackgroundColor(borderColor);
    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.grow);
        viewToAnimate.startAnimation(animation);
    }

    private void startTimer(){

        final TextView[] mChoicesIDs = {mChoice1, mChoice2, mChoice3, mChoice4};

        countdownTimer = new MyCountdownTimer(5000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                nextQuestion(mChoicesIDs, 5, -1);
            }
        }.start();
        ((PlayActivity)getActivity()).countdownCircle();
    }

    public void pauseTimer(){
        countdownTimer.pause();
        ((PlayActivity)getActivity()).pauseCountdownCircle();
    }

    public void resumeTimer(){
            countdownTimer.resume();
            ((PlayActivity) getActivity()).resumeCountdownCircle(mTimeLeft);
            Log.d("COUNTDOWN", "ONRESUME");
    }

    public void cancelTimer(){
        countdownTimer.cancel();
    }
}
