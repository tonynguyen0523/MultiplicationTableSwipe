package com.swipeacademy.multiplicationtableswipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonyn on 5/17/2017.
 */

public class PlayResultActivity extends Activity {

    @BindView(R.id.play_final_score)TextView mFinalScore;
    @BindView(R.id.play_final_time)TextView mFinalTime;
    @BindView(R.id.results_home_button)ImageButton mHomeButton;
    @BindView(R.id.results_replay_button)ImageButton mReplayButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogfragment_play_end);
        ButterKnife.bind(this);

        int mCurrentScore = Utility.getCurrentScore(this);
        long mFinishedTime = Utility.getFinishedTime(this);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(mFinishedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(mFinishedTime);

        mFinalScore.setText(getString(R.string.final_score,mCurrentScore,12));
        mFinalTime.setText(getString(R.string.final_time,minutes,seconds));

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayResultActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        mReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayResultActivity.this, PlayActivity.class);
                Utility.setCurrentScore(PlayResultActivity.this,0);
                CorrectionsUtil.clearCorrections(PlayResultActivity.this);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onVisibleBehindCanceled() {
        super.onVisibleBehindCanceled();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
