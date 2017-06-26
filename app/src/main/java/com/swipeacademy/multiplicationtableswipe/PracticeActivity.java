package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.swipeacademy.multiplicationtableswipe.Util.GridSpacingItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PracticeActivity extends AppCompatActivity {

    @BindView(R.id.practice_recycler_view)RecyclerView mRecyclerView;
    @BindView(R.id.practice_adView)AdView mAdView;
    @BindView(R.id.practice_toolbar)Toolbar mToolbar;
    private String[] tableSets = {"1","2","3","4","5","6","7","8","9","10","11","12"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        PracticeAdapter practiceAdapter = new PracticeAdapter(this,tableSets);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(this, 2),true));
        mRecyclerView.setAdapter(practiceAdapter);

        practiceAdapter.setOnItemClickListener(new PracticeAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                String assetSelected = getResources().getStringArray(R.array.assets)[position];
                Utility.setSelectedAsset(getApplicationContext(),assetSelected);
                Utility.setRemainingQuestions(getApplicationContext(),12);
                Utility.setSelectedAmount(getApplicationContext(),12);
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(intent);
            }
        });


    }


}
