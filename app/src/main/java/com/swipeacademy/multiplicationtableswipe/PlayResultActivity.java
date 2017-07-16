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
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayResultActivity extends AppCompatActivity {

    @BindView(R.id.results_comment)
    TextView mResultComment;
    @BindView(R.id.results_score)
    TextView mFinalScore;
    @BindView(R.id.results_home)
    Button mHomeButton;
    @BindView(R.id.do_corrections)
    Button mCorrectionsButton;
    @BindView(R.id.result_fragment_container)
    FrameLayout mContainer;

    private int backPressCount;
    private boolean mIsCorrections;
    private boolean mIsPractice;
    private boolean mNoTimer;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_result);
        ButterKnife.bind(this);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.admob_ad_unit_id_interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice(getString(R.string.admob_test_device_id)).build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                startActivity(mainIntent);
            }
        });


        int mCurrentScore = PrefUtility.getCurrentScore(this);
        final int selectedAmount = PrefUtility.getSelectedAmount(this);
        double resultPercentage = (double) mCurrentScore / selectedAmount * 100;
        mIsCorrections = PrefUtility.getIsCorrections(this);
        mIsPractice = PrefUtility.getIsPractice(this);
        backPressCount = 0;

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
        if (!CorrectionsUtil.getCorrections(this).isEmpty() && !mIsCorrections) {
            mCorrectionsButton.setVisibility(View.VISIBLE);
        } else if (mNoTimer) {
            mCorrectionsButton.setVisibility(View.GONE);
        } else {
            mCorrectionsButton.setVisibility(View.GONE);
        }

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interstitialAd.isLoaded()){
                    interstitialAd.show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    ComponentName cn = intent.getComponent();
                    Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                    startActivity(mainIntent);
                }
            }
        });

        mCorrectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayResultActivity.this, PlayActivity.class);
                // Prep for correction mode
                PrefUtility.setIsCorrections(PlayResultActivity.this, true);
                PrefUtility.setIsPractice(PlayResultActivity.this,true);
                PrefUtility.setCurrentScore(PlayResultActivity.this, 0);
                PrefUtility.setRemainingQuestions(PlayResultActivity.this, CorrectionsUtil.getCorrections(getApplicationContext()).size());
                PrefUtility.setSelectedAmount(PlayResultActivity.this, CorrectionsUtil.getCorrections(getApplicationContext()).size());
                startActivity(intent);
            }
        });

        // Only display graph if not corrections or practice mode
        if (!noTimer()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.result_fragment_container, HistoryFragment.newInstance(Integer.toString(selectedAmount)))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        backPressCount++;
        if (backPressCount == 2) {
            Intent intent = new Intent(PlayResultActivity.this, HomeActivity.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            startActivity(mainIntent);
        } else {
            Toast.makeText(this, "Press back again to go home", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean noTimer(){
        if (mIsCorrections){
            mNoTimer = true;
        } else if (mIsPractice){
            mNoTimer = true;
        } else {
            mNoTimer = false;
        }

        return mNoTimer;
    }
}
