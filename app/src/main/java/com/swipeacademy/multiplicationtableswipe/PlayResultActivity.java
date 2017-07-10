package com.swipeacademy.multiplicationtableswipe;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayResultActivity extends AppCompatActivity {

    @BindView(R.id.results_comment)TextView mResultComment;
    @BindView(R.id.results_score)TextView mFinalScore;
//    @BindView(R.id.results_replay)Button mReplayButton;
    @BindView(R.id.results_home)Button mHomeButton;
    @BindView(R.id.do_corrections)Button mCorrectionsButton;
    @BindView(R.id.result_fragment_container)FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_result);
        ButterKnife.bind(this);

        int mCurrentScore = PrefUtility.getCurrentScore(this);
        final int selectedAmount = PrefUtility.getSelectedAmount(this);
        double resultPercentage = (double) mCurrentScore / selectedAmount * 100;
        boolean isCorrection = PrefUtility.getIsCorrections(this);

        String resultsComment;
        if (resultPercentage == 100) {
            resultsComment = getString(R.string.result100);
        } else if (resultPercentage > 90) {
            resultsComment = getString(R.string.result90);
        } else if (resultPercentage > 70) {
            resultsComment = getString(R.string.result70);
        } else {
            resultsComment = getString(R.string.result_fail);
        }

        mResultComment.setText(resultsComment);
        mFinalScore.setText(getString(R.string.final_score, mCurrentScore, selectedAmount));

        // Display correction button if correction are available &
        // is not already doing corrections
        if (!CorrectionsUtil.getCorrections(this).isEmpty() && !isCorrection) {
            mCorrectionsButton.setVisibility(View.VISIBLE);
        } else if (isCorrection) {
            mCorrectionsButton.setVisibility(View.GONE);
//            mReplayButton.setVisibility(View.GONE);
        } else {
            mCorrectionsButton.setVisibility(View.GONE);
        }

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                startActivity(mainIntent);
            }
        });

        mCorrectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                // Prep for correction mode
                PrefUtility.setIsCorrections(getApplicationContext(), true);
                PrefUtility.setCurrentScore(getApplicationContext(), 0);
                PrefUtility.setRemainingQuestions(getApplicationContext(), CorrectionsUtil.getCorrections(getApplicationContext()).size());
                PrefUtility.setSelectedAmount(getApplicationContext(), CorrectionsUtil.getCorrections(getApplicationContext()).size());
                finish();
                startActivity(intent);
            }
        });

//        mReplayButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
//                // Make sure results are cleared for fresh replay
//                PrefUtility.setIsCorrections(getApplicationContext(), false);
//                PrefUtility.setCurrentScore(getApplicationContext(), 0);
//                PrefUtility.setRemainingQuestions(getApplicationContext(), selectedAmount);
//                PlayUtility.resetPlay(getApplicationContext());
//                PlayUtility.startPlay(getApplicationContext(),selectedAmount);
//                CorrectionsUtil.clearCorrections(getApplicationContext());
//                ComponentName cn = intent.getComponent();
//                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
//                startActivity(mainIntent);
//            }
//        });

        if(!isCorrection) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.result_fragment_container, HistoryFragment.newInstance(Integer.toString(selectedAmount)))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
