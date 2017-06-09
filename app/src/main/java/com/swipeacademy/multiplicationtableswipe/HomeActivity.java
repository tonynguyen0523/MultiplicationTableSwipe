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
        Utility.setCurrentScore(this,0);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



//        mPracticeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                practice();
//            }
//        });

    }

    public void letsPlay(View view){

//        Utility.setCurrentScore(this,0);
//        Utility.setRemainingQuestions(this,12);
//
//        Intent playIntent = new Intent(this, PlayActivity.class);
//        startActivity(playIntent);
//        Toast.makeText(this,"LETS PLAY!", Toast.LENGTH_SHORT).show();

        DialogFragment dialogFragment =  new QuestionAmountDialog();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void myHistory(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

//    public void practice(){
//
//        ArrayList<Integer> remainingQuestionsIDs = QuestionSample.getAllQuestionsIDs(this);
//        remainingQuestionsIDs.size();
//        int questionID = Utility.chooseQuestionID(remainingQuestionsIDs);
//        QuestionSample qs = QuestionSample.getQuestionByID(this,questionID);
//        String question = qs.getQuestion();
//        Log.d("LOGTAG", question);
//
//    }
}
