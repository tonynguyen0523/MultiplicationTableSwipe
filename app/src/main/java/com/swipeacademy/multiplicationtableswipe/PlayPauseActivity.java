package com.swipeacademy.multiplicationtableswipe;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayPauseActivity extends AppCompatActivity {

    @BindView(R.id.pause_toolbar)Toolbar mToolbar;
    @BindView(R.id.pause_toolbar_text_view)TextView mToolbarTitle;
    @BindView(R.id.pause_replay_button)Button mReplay;
    @BindView(R.id.pause_resume_button)Button mResume;
    @BindView(R.id.pause_home_button)Button mHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pause);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final int gameMode = PrefUtility.getSelectedAmount(this);
        boolean isCorrections = PrefUtility.getIsCorrections(this);

        mToolbarTitle.setText(Integer.toString(gameMode));


        if(isCorrections){
            mReplay.setVisibility(View.GONE);
        }

        mReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                // Make sure results are cleared for fresh replay
                PrefUtility.setCurrentScore(PlayPauseActivity.this, 0);
                PrefUtility.setRemainingQuestions(PlayPauseActivity.this, gameMode);
                CorrectionsUtil.clearCorrections(PlayPauseActivity.this);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                finish();
                startActivity(mainIntent);
            }
        });

        mResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                finish();
                startActivity(mainIntent);
            }
        });

    }
}
