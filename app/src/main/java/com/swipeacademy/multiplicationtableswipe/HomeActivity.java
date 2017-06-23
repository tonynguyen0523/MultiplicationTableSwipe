package com.swipeacademy.multiplicationtableswipe;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.practice_button)Button mPracticeButton;
    @BindView(R.id.home_adView)AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        CorrectionsUtil.clearCorrections(this);
        Utility.setCurrentScore(this, 0);
        Utility.setIsCorrections(this, false);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void letsPlay(View view){

        DialogFragment dialogFragment =  new QuestionAmountDialog();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void myHistory(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void practice(View view){
        Intent intent = new Intent(this, PracticeActivity.class);
        startActivity(intent);
    }
}
