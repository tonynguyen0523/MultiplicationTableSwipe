package com.swipeacademy.multiplicationtableswipe;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.swipeacademy.multiplicationtableswipe.Util.Analytics;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.home_adView)
    AdView mAdView;
    @BindView(R.id.home_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.home_play_card_container)
    FrameLayout mPlayCardContainer;
    @BindView(R.id.home_practice_card_view)
    CardView mPracticeCard;
    @BindView(R.id.home_history_card_view)
    CardView mHistoryCard;

    private boolean mShowingBack = false;
    private int mGradeChoice = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState == null) {
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

        // Make sure prefs are cleared
        PlayUtility.resetPlay(this);

        // Load ad
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_user_profile_icon:
                showUserSchoolGradeDialog();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
    }

    // Flip play card animation
    private void flipCard() {
        if (mShowingBack) {
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

    // Dialog for users to selected their school grade
    private void showUserSchoolGradeDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.school_grade_dialog_title)
                .setSingleChoiceItems(
                        R.array.user_school_grade,
                        PrefUtility.getSchoolGrade(this),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mGradeChoice = which;
                            }
                        }
                )
                .setPositiveButton(R.string.school_grade_dialog_pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mGradeChoice == -1) {
                            return;
                        }
                        PrefUtility.setSchoolGrade(HomeActivity.this, mGradeChoice);
                        Analytics.setUserSchoolGrade(HomeActivity.this, mGradeChoice);
                    }
                })
                .setNegativeButton(R.string.cancel_dialog_option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * Fragment for front of card
     */
    public static class PlayCardFrontFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.home_play_card_front, container, false);

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

    /**
     * Fragment for back of card
     */
    public static class PlayCardBackFragment extends Fragment {

        private int amount24 = 24;
        private int amount48 = 36;
        private int amount72 = 48;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.home_play_card_back, container, false);

            CardView cardView24 = (CardView) view.findViewById(R.id.play_24_option_cardView);
            CardView cardView48 = (CardView) view.findViewById(R.id.play_48_option_cardView);
            CardView cardView72 = (CardView) view.findViewById(R.id.play_72_option_cardView);

            cardView24.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    PlayUtility.startPlay(getActivity(),amount24);
                    getActivity().finish();
                    startActivity(intent);

                }
            });

            cardView48.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    PlayUtility.startPlay(getActivity(),amount48);
                    getActivity().finish();
                    startActivity(intent);
                }
            });

            cardView72.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PlayActivity.class);
                    PlayUtility.startPlay(getActivity(),amount72);
                    getActivity().finish();
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
