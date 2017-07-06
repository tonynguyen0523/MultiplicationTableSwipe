package com.swipeacademy.multiplicationtableswipe;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.swipeacademy.multiplicationtableswipe.Util.Circle;
import com.swipeacademy.multiplicationtableswipe.Util.CircleAngleAnimation;
import com.swipeacademy.multiplicationtableswipe.dialog.PlayBackPressDialogFragment;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayActivity extends AppCompatActivity {

    @BindView(R.id.remaining_questions)TextView mRemainingQuestionTV;
    @BindView(R.id.current_time)Chronometer mChronometer;
    @BindView(R.id.adView)AdView mAdView;
    @BindView(R.id.play_home_button)ImageButton mHomeButton;
    @BindView(R.id.remaining_circle)Circle mCountdownCircle;

    private static final String TIME_KEY = "time_key";
    private static final String TIME = "time";
    private static final String CIRCLE_ANIMATION_ANGLE = "cirgle_angle";
    private CircleAngleAnimation angleAnimation;
    private long mTime;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);


        // Get remaining questions
        int mRemainingQuestion = Utility.getRemainingQuestions(this);
        date = DateFormat.getDateTimeInstance().format(new Date());

            if (savedInstanceState == null) {
                mTime = 0;
                startTimer();
            }
            else {
                mTime = savedInstanceState.getLong(TIME);
                mChronometer.setBase(savedInstanceState.getLong(TIME_KEY));
                mChronometer.start();
            }

        mRemainingQuestionTV.setText(getString(R.string.remaining_questions, mRemainingQuestion));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        pauseTimer();
        showAlertDialog();
        if(getResources().getBoolean(R.bool.is_portrait)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(TIME_KEY, mChronometer.getBase());
        outState.putLong(TIME, mTime);
        super.onSaveInstanceState(outState);
    }

    public void updateNumbers(int remainingQuestions){
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
        mRemainingQuestionTV.setText(getString(R.string.remaining_questions,0));
        mTime = SystemClock.elapsedRealtime();
        Utility.setFinishedTime(this,mChronometer.getBase());

        String asset = Utility.getSelectedAsset(getApplicationContext());
        boolean corrections = Utility.getIsCorrections(getApplicationContext());

        if(asset.contains("letsplay") && !corrections) {
            int correct = Utility.getCurrentScore(this);
            String selectedMode = Utility.getSelectedTable(this);
            Utility.saveResults(this, selectedMode, date, correct, mChronometer.getBase());
        }
    }

    public void showAlertDialog(){
        DialogFragment alertDialog = new PlayBackPressDialogFragment();
        showChronometer(false);
        alertDialog.show(getSupportFragmentManager(),"backPress");
    }

    public void showChronometer(boolean show){
        if(show) {
            mChronometer.setVisibility(View.VISIBLE);
        } else {
            mChronometer.setVisibility(View.INVISIBLE);
        }
    }

    public void countdownCircle(float angle, long duration){
        angleAnimation = new CircleAngleAnimation(mCountdownCircle,365);
        angleAnimation.setDuration(duration);
        mCountdownCircle.setAngle(angle);
        mCountdownCircle.startAnimation(angleAnimation);
    }

    public float getCountdownCircleAngle(){
        return  mCountdownCircle.getAngle();
    }

    public long getCountDownCircleDuration(){
        return  angleAnimation.getDuration();
    }
}
