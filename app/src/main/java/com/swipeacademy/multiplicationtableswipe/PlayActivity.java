package com.swipeacademy.multiplicationtableswipe;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayActivity extends AppCompatActivity {

    @BindView(R.id.current_score)TextView mCurrentScoreTV;
    @BindView(R.id.remaining_questions)TextView mRemainingQuestionTV;

    private int mCurrentScore;
    private int mRemainingQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        mCurrentScore = Utility.getCurrentScore(this);
        mRemainingQuestion = Utility.getRemainingQuestions(this);

        mCurrentScoreTV.setText(getString(R.string.current_score,mCurrentScore,10));
        mRemainingQuestionTV.setText(getString(R.string.remaining_questions,mRemainingQuestion));

//        PlayFragment playFragment = new PlayFragment();
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.play_container,playFragment)
//                .commit();
    }

    public void updateNumbers(int currentScore, int remainingQuestions){
        mCurrentScoreTV.setText(getString(R.string.current_score,currentScore,12));
        mRemainingQuestionTV.setText(getString(R.string.remaining_questions,remainingQuestions));
    }
}
