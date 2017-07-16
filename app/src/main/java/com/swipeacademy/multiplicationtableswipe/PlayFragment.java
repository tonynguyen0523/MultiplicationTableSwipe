package com.swipeacademy.multiplicationtableswipe;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.serhatsurguvec.continuablecirclecountdownview.ContinuableCircleCountDownView;
import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;
import com.swipeacademy.multiplicationtableswipe.Util.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PlayFragment extends Fragment {

    @BindView(R.id.play_answer_choices_area) View mChoiceArea;
    @BindView(R.id.play_question1) TextView mQuestion1;
    @BindView(R.id.play_question2) TextView mQuestion2;
    @BindView(R.id.choice1) TextView mChoice1;
    @BindView(R.id.choice2) TextView mChoice2;
    @BindView(R.id.choice3) TextView mChoice3;
    @BindView(R.id.choice4) TextView mChoice4;
    @BindView(R.id.swipe_arrow_1)
    ImageView mSwipeArrow1;
    @BindView(R.id.swipe_arrow_2)
    ImageView mSwipeArrow2;
    @BindView(R.id.swipe_arrow_3)
    ImageView mSwipeArrow3;
    @BindView(R.id.swipe_arrow_4)
    ImageView mSwipeArrow4;
    @BindView(R.id.question_border_1) View mBorder1;
    @BindView(R.id.question_border_2) View mBorder2;
    @BindView(R.id.question_border_3) View mBorder3;
    @BindView(R.id.question_border_4) View mBorder4;
    @BindView(R.id.play_pause_button)
    ImageButton mPauseButton;
    @BindView(R.id.circleCountdown)
    ContinuableCircleCountDownView mCircleCountdown;

    private static final String REMAINING_QUESTIONS_KEY = "remainingList";
    private static final String CORRECTIONS_KEY = "correctionsList";
    private static final String CURRENT_QUESTION = "currentQuestion";
    private static final int DELAY = 500;
    private static final int CORRECTIONS_DELAY = 1000;
    private ArrayList<Integer> mRemainingQuestionsIDs;
    private ArrayList<String> mCorrections;
    private String mSelectedAsset;
    private int mQuestionID;
    private int mSelectedAmount;
    private int mCorrectAnswer;
    private int mCorrectTextViewID;
    private boolean mIsCorrection;
    private boolean mIsPractice;
    private boolean mNoTimer;
    private boolean mSwiped;
    private boolean mDelay;
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
                Intent intent = new Intent(getActivity(), PlayPauseActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for textViews
        mChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mSwiped = true;
                if(!mDelay) {
                    nextQuestion(mChoicesIDs, 0, Integer.valueOf(mChoice1.getText().toString()), false);
                }

            }
        });
        mChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mSwiped = true;
                if(!mDelay) {
                    nextQuestion(mChoicesIDs, 1, Integer.valueOf(mChoice2.getText().toString()), false);
                }
            }
        });
        mChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mSwiped = true;
                if(!mDelay) {
                    nextQuestion(mChoicesIDs, 2, Integer.valueOf(mChoice3.getText().toString()), false);
                }
            }
        });
        mChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mSwiped = true;
                if(!mDelay) {
                    nextQuestion(mChoicesIDs, 3, Integer.valueOf(mChoice4.getText().toString()), false);
                }
            }
        });

        // Set swipe listener
        mChoiceArea.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
//                mSwiped = true;
                if(!mDelay) {
                    nextQuestion(mChoicesIDs, 1, Integer.valueOf(mChoice2.getText().toString()), false);
                }
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
//                mSwiped = true;
                if(!mDelay) {
                    nextQuestion(mChoicesIDs, 2, Integer.valueOf(mChoice3.getText().toString()), false);
                }
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
//                mSwiped = true;
                if(!mDelay) {
                    nextQuestion(mChoicesIDs, 0, Integer.valueOf(mChoice1.getText().toString()), false);
                }
            }

            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
//                mSwiped = true;
                if(!mDelay) {
                    nextQuestion(mChoicesIDs, 3, Integer.valueOf(mChoice4.getText().toString()), false);
                }
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
        // Check selected amount
        mSelectedAmount = PrefUtility.getSelectedAmount(getContext());
        // Check if practice mode
        mIsPractice = PrefUtility.getIsPractice(getContext());

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
    public void nextQuestion(final TextView[] choicesTV, final int choiceTVID, int userAnswer, boolean timerEnd) {

        // Get current score and remaining question
        boolean correct = PrefUtility.userCorrect(mCorrectAnswer, userAnswer);
        int mCurrentScore = PrefUtility.getCurrentScore(getContext());
        int mRemainingQuestions = PrefUtility.getRemainingQuestions(getContext());
        int delay;
        mDelay = true;

        // If user chose correct answer, increase score by 1,
        // else add questionId to correctionsList
        if (correct) {
            mCurrentScore++;
            changeSelectedColor(choicesTV, choiceTVID, ContextCompat.getColor(getContext(), R.color.correct), ContextCompat.getColor(getContext(), R.color.correct));
            delay = DELAY;
        } else if (noTimer()) {
            mCorrections.add(Integer.toString(mQuestionID));
            changeSelectedColor(choicesTV, choiceTVID, ContextCompat.getColor(getContext(), R.color.wrong), ContextCompat.getColor(getContext(), R.color.wrong));
            choicesTV[mCorrectTextViewID].setTextColor(ContextCompat.getColor(getContext(), R.color.correct));
            delay = CORRECTIONS_DELAY;
        } else {
            mCorrections.add(Integer.toString(mQuestionID));
            changeSelectedColor(choicesTV, choiceTVID, ContextCompat.getColor(getContext(), R.color.wrong),
                    ContextCompat.getColor(getContext(), R.color.wrong));
            delay = DELAY;
        }

        // Reduce remaining question by 1
        mRemainingQuestions--;
        if (!noTimer()) {mCircleCountdown.stop();}



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
                    if(!noTimer()) {cancelTimer();}
                    ((PlayActivity) getActivity()).playFinished();
                    CorrectionsUtil.editCorrectionsList(getContext(), mCorrections);
                    Intent intent = new Intent(getContext(), PlayResultActivity.class);
                    ComponentName cn = intent.getComponent();
                    Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                    startActivity(mainIntent);
                } else {
                    // Generate new question
                    if(!noTimer()) {cancelTimer();}
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

        mSwiped = false;
        mDelay = false;

        if(!noTimer()) {startTimer();}
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
     * Change question border color
     */
    private void changeSelectedColor(TextView[] choicesTV, int choiceTVID, int choiceColor, int borderColor) {

        if (choiceTVID != 5) {
            TextView choiceID = choicesTV[choiceTVID];
            choiceID.setTextColor(choiceColor);
        }

        mCircleCountdown.setOUTER_COLOR(borderColor);
    }

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.grow);
        viewToAnimate.startAnimation(animation);
    }

    private void startTimer() {

        final TextView[] mChoicesIDs = {mChoice1, mChoice2, mChoice3, mChoice4};

        mCircleCountdown.setTimer(timerDuration());

        mCircleCountdown.setListener(new ContinuableCircleCountDownView.OnCountDownCompletedListener() {
            @Override
            public void onTick(long passedMillis) {}

            @Override
            public void onCompleted() {
                if(!mSwiped){
                nextQuestion(mChoicesIDs, 5, -1, true);
                }
            }
        });
        mCircleCountdown.start();
    }

    public void pauseTimer() {
        if(mCircleCountdown.isStarted()){mCircleCountdown.stop();}
    }

    public void resumeTimer() {
        if(mCircleCountdown.isStopped()) {mCircleCountdown.continueE();}
    }

    public void cancelTimer() {
        if (mCircleCountdown.isStarted() || mCircleCountdown.isStopped() || mCircleCountdown.isFinished()) {
            mCircleCountdown.cancel();
        }
    }

    public boolean noTimer(){
        if (mIsCorrection){
            mNoTimer = true;
        } else if (mIsPractice){
            mNoTimer = true;
        } else {
            mNoTimer = false;
        }

        return mNoTimer;
    }

    public long timerDuration(){
        long duration = 0;
        switch (mSelectedAmount){
            case 24:
                duration = 5000;
                break;
            case 36:
                duration = 4000;
                break;
            case 48:
                duration = 3000;
                break;
            default:
                Log.d("DURATION", "No duration");
        }
//        switch (mSelectedAmount){
//            case 5:
//                duration = 5000;
//                break;
//            case 6:
//                duration = 4000;
//                break;
//            case 7:
//                duration = 3000;
//                break;
//            default:
//                Log.d("DURATION", "No duration");
//        }
        return duration;
    }
}
