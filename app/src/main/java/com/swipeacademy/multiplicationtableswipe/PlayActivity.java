package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
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
    @BindView(R.id.adView)AdView mAdView;
    @BindView(R.id.play_home_button)ImageButton mHomeButton;
    @BindView(R.id.remaining_circle)Circle mCountdownCircle;

    public static final String ACTION_RECENT_RESULTS_UPDATED =
            "com.example.android.sunshine.app.ACTION_RECENT_RESULTS_UPDATED";
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        // Get remaining questions
        int mRemainingQuestion = Utility.getRemainingQuestions(this);
        date = DateFormat.getDateTimeInstance().format(new Date());

        mRemainingQuestionTV.setText(getString(R.string.remaining_questions, mRemainingQuestion));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        countdownCircle();
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();

        if(getResources().getBoolean(R.bool.is_portrait)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    public void updateNumbers(int remainingQuestions){
        mRemainingQuestionTV.setText(getString(R.string.remaining_questions,remainingQuestions));
    }

    public void playFinished(){

        mRemainingQuestionTV.setText(getString(R.string.remaining_questions,0));

        String asset = Utility.getSelectedAsset(getApplicationContext());
        boolean corrections = Utility.getIsCorrections(getApplicationContext());
        int correct = Utility.getCurrentScore(this);

        if(asset.contains("letsplay") && !corrections) {
            String selectedMode = Utility.getSelectedTable(this);
            Utility.saveResults(this, selectedMode, date, correct);

            switch (Utility.getSelectedTable(this)) {
                case "24":
                    Utility.setRecent24(this, correct);
                    break;
                case "48":
                    Utility.setRecent48(this, correct);
                    break;
                case "72":
                    Utility.setRecent72(this, correct);
                    break;
                default:
                    break;
            }

            Intent recentResultsUpdated = new Intent(ACTION_RECENT_RESULTS_UPDATED)
                    .setPackage(getPackageName());
            this.sendBroadcast(recentResultsUpdated);
        }
    }

    public void showAlertDialog(){
        DialogFragment alertDialog = new PlayBackPressDialogFragment();
        alertDialog.show(getSupportFragmentManager(),"backPress");
    }

    public void countdownCircle(){
        CircleAngleAnimation angleAnimation = new CircleAngleAnimation(mCountdownCircle, 370);
        angleAnimation.setDuration(750);
        mCountdownCircle.setAngle(0);
        mCountdownCircle.startAnimation(angleAnimation);
    }
}
