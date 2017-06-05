package com.swipeacademy.multiplicationtableswipe;

import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Chronometer;
import android.widget.TextView;

import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;
import com.swipeacademy.multiplicationtableswipe.dialog.PlayBackPressDialogFragment;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayActivity extends AppCompatActivity {

    @BindView(R.id.current_score)TextView mCurrentScoreTV;
    @BindView(R.id.remaining_questions)TextView mRemainingQuestionTV;
    @BindView(R.id.current_time)Chronometer mChronometer;

    private long mTime = 0;
    private int correct = 0;
    private int mSelectedAmount = 0;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        int mCurrentScore = Utility.getCurrentScore(this);
        int mRemainingQuestion = Utility.getRemainingQuestions(this);

        mSelectedAmount = Utility.getSelectedAmount(this);
        date = DateFormat.getDateTimeInstance().format(new Date());

        startTimer();

        mCurrentScoreTV.setText(getString(R.string.current_score, mCurrentScore,mSelectedAmount));
        mRemainingQuestionTV.setText(getString(R.string.remaining_questions, mRemainingQuestion));
    }

    @Override
    public void onBackPressed() {
        pauseTimer();
        showAlertDialog();
    }

    public void updateNumbers(int currentScore, int remainingQuestions){
        mCurrentScoreTV.setText(getString(R.string.current_score,currentScore,mSelectedAmount));
        mRemainingQuestionTV.setText(getString(R.string.remaining_questions,remainingQuestions));
    }

    public void startTimer(){

        if (mTime == 0){
            mChronometer.setBase(SystemClock.elapsedRealtime());
        } else {
            long intervalOnPause = (SystemClock.elapsedRealtime() - mTime);
            mChronometer.setBase(mChronometer.getBase() + intervalOnPause);
        }
        mChronometer.start();
    }

    private void pauseTimer(){
        mChronometer.stop();
        mTime = SystemClock.elapsedRealtime();
    }

    public void stopTimer(){

        mChronometer.stop();
        mTime = SystemClock.elapsedRealtime();
        Utility.setFinishedTime(this,mTime);

        correct = Utility.getCurrentScore(this);
        Utility.saveResults(this,"letsplay",date,correct,mTime);
    }

    private void showAlertDialog(){
        DialogFragment alertDialog = new PlayBackPressDialogFragment();
        alertDialog.show(getSupportFragmentManager(),"backPress");
    }
}
