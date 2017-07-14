package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    @BindView(R.id.remaining_questions)
    TextView mRemainingQuestionTV;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.play_home_button)
    ImageButton mHomeButton;
//    @BindView(R.id.remaining_circle)
//    Circle mCountdownCircle;
//    CircleAngleAnimation angleAnimation;

    public static final String ACTION_RECENT_RESULTS_UPDATED =
            "com.example.android.sunshine.app.ACTION_RECENT_RESULTS_UPDATED";
    private String date;
    private float angle;
    private boolean mPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        // Get remaining questions and date4
        int mRemainingQuestion = PrefUtility.getRemainingQuestions(this);
        date = DateFormat.getDateTimeInstance().format(new Date());

        // Update remaining question text view
        mRemainingQuestionTV.setText(getString(R.string.remaining_questions, mRemainingQuestion));

        // Load Ad
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
        Intent intent = new Intent(PlayActivity.this,PlayPauseActivity.class);
        startActivity(intent);
    }


    // Update remaining question text view method
    public void updateNumbers(int remainingQuestions) {
        mRemainingQuestionTV.setText(getString(R.string.remaining_questions, remainingQuestions));
    }

    // Method for when game is finished
    public void playFinished() {

        mRemainingQuestionTV.setText(getString(R.string.remaining_questions, 0));

        String asset = PrefUtility.getSelectedAsset(getApplicationContext());
        boolean corrections = PrefUtility.getIsCorrections(getApplicationContext());
        int score = PrefUtility.getCurrentScore(this);

        // Make sure game mode is correct and not corrections
        // before saving results
        if (asset.contains(getString(R.string.letsplay)) && !corrections) {
            String selectedMode = PrefUtility.getSelectedTable(this);
            PrefUtility.saveResults(this, selectedMode, date, score);

            // Update recent results for widget
            switch (PrefUtility.getSelectedTable(this)) {
                case "24":
                    PrefUtility.setRecent24(this, score);
                    break;
                case "48":
                    PrefUtility.setRecent48(this, score);
                    break;
                case "72":
                    PrefUtility.setRecent72(this, score);
                    break;
                default:
                    break;
            }

            // Update widget
            Intent recentResultsUpdated = new Intent(ACTION_RECENT_RESULTS_UPDATED)
                    .setPackage(getPackageName());
            this.sendBroadcast(recentResultsUpdated);
        }
    }

    // AlertDialog for when user presses back or home button
    public void showAlertDialog() {
        DialogFragment alertDialog = new PlayBackPressDialogFragment();
        alertDialog.show(getSupportFragmentManager(), "backPress");
    }

    // Initiate circle animation method
//    public void countdownCircle(long duration) {
//        mCountdownCircle.clearAnimation();
//        mCountdownCircle.setAngle(0);
//        angleAnimation = new CircleAngleAnimation(mCountdownCircle, 365);
//        angleAnimation.setDuration(duration);
//        mCountdownCircle.startAnimation(angleAnimation);
//        mPaused = false;
//    }
//
//    public void pauseCountdownCircle(){
//        angle = mCountdownCircle.getAngle();
//        mCountdownCircle.clearAnimation();
//        mCountdownCircle.setAngle(angle);
//        mPaused = true;
//    }
//
//    public void resumeCountdownCircle(long timeLeft,long duration){
//        if (mPaused) {
//            mCountdownCircle.clearAnimation();
//            mCountdownCircle.setAngle(angle);
//            angleAnimation = new CircleAngleAnimation(mCountdownCircle, 360);
//            angleAnimation.setDuration(timeLeft);
//            mCountdownCircle.startAnimation(angleAnimation);
//            mPaused = false;
//        } else {
//            countdownCircle(duration);
//        }
//    }
}
