package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.home_adView)AdView mAdView;
    @BindView(R.id.home_toolbar)Toolbar mToolbar;
    @BindView(R.id.included_home_play_card)CardView mIncludedPlayCard;
    @BindView(R.id.home_practice_card_view)CardView mPracticeCard;
    @BindView(R.id.home_history_card_view)CardView mHistoryCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mIncludedPlayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment =  new QuestionAmountDialog();
                dialogFragment.show(getSupportFragmentManager(), "dialog");
            }
        });

        mPracticeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PracticeActivity.class);
                startActivity(intent);
            }
        });

        mHistoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });

        CorrectionsUtil.clearCorrections(this);
        Utility.setCurrentScore(this, 0);
        Utility.setIsCorrections(this, false);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    static class IncludedLayout{
        @BindView(R.id.home_play_card_view)CardView playCardView;
        @BindView(R.id.home_practice_card_view)CardView practiceCardView;
        @BindView(R.id.home_history_card_view)CardView historyCardView;
    }


//    public void letsPlay(View view){
//
//        DialogFragment dialogFragment =  new QuestionAmountDialog();
//        dialogFragment.show(getSupportFragmentManager(), "dialog");
//    }
//
//    public void myHistory(View view){
//        Intent intent = new Intent(this, HistoryActivity.class);
//        startActivity(intent);
//    }
//
//    public void practice(View view){
//        Intent intent = new Intent(this, PracticeActivity.class);
//        startActivity(intent);
//    }
}
