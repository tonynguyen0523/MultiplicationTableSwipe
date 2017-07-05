package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.home_adView)AdView mAdView;
    @BindView(R.id.home_toolbar)Toolbar mToolbar;
    @BindView(R.id.home_play_card_container)FrameLayout mPlayCardContainer;
    @BindView(R.id.home_practice_card_view)CardView mPracticeCard;
    @BindView(R.id.home_history_card_view)CardView mHistoryCard;

    private boolean mShowingBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if(savedInstanceState == null){
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.home_play_card_container, new PlayCardFrontFragment())
                    .commit();
        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }

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

        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
    }

    private void flipCard(){
        if(mShowingBack){
            getFragmentManager().popBackStack();
            return;
        }

        mShowingBack = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.home_play_card_container, new PlayCardBackFragment())
                .addToBackStack(null)
                .commit();
    }

    public static class PlayCardFrontFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.home_play_card_front,container,false);

            CardView cardView = (CardView) view.findViewById(R.id.home_play_card_view);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) getActivity()).flipCard();
                }
            });

            return view;
        }
    }

    public static class PlayCardBackFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.home_play_card_back,container,false);

            CardView cardView24 = (CardView)view.findViewById(R.id.play_24_option_cardView);
            CardView cardView48 = (CardView)view.findViewById(R.id.play_48_option_cardView);
            CardView cardView72 = (CardView)view.findViewById(R.id.play_72_option_cardView);

            cardView24.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    Utility.setSelectedAmount(getActivity(),24);
                    Utility.setRemainingQuestions(getActivity(),24);
                    Utility.setSelectedTable(getActivity(),"24");
                    Utility.setSelectedAsset(getActivity(),getString(R.string.letsplay_json));
                    startActivity(intent);
                }
            });

            cardView48.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    Utility.setSelectedAmount(getActivity(),48);
                    Utility.setRemainingQuestions(getActivity(),48);
                    Utility.setSelectedTable(getActivity(),"48");
                    Utility.setSelectedAsset(getActivity(),getString(R.string.letsplay_json));
                    startActivity(intent);
                }
            });

            cardView72.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    Utility.setSelectedAmount(getActivity(),72);
                    Utility.setRemainingQuestions(getActivity(),72);
                    Utility.setSelectedTable(getActivity(),"72");
                    Utility.setSelectedAsset(getActivity(),getString(R.string.letsplay_json));
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
