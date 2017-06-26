package com.swipeacademy.multiplicationtableswipe;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tonyn on 6/11/2017.
 */

public class ResultsDialog extends DialogFragment {

    @BindView(R.id.play_final_score)TextView mFinalScore;
    @BindView(R.id.play_final_time)TextView mFinalTime;
    @BindView(R.id.results_header)TextView mResultHeader;
    @BindView(R.id.results_home_button)ImageButton mHomeButton;
    @BindView(R.id.results_replay_button)ImageButton mReplayButton;
    @BindView(R.id.correction_button)Button mCorrectionButton;

    private Unbinder unbinder;

    public ResultsDialog(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_play_end,null);
        unbinder = ButterKnife.bind(this, rootView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);

        int mCurrentScore = Utility.getCurrentScore(getContext());
        final int selectedAmount = Utility.getSelectedAmount(getContext());
        double resultPercentage = (double)mCurrentScore/selectedAmount * 100;
        long mFinishedTime = Utility.getFinishedTime(getContext());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(mFinishedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(mFinishedTime);
        boolean isCorrection = Utility.getIsCorrections(getContext());

        String resultHeader;
        if(resultPercentage == 100) {
            resultHeader = "Perfect!";
        } else if (resultPercentage > 90){
            resultHeader = "Awesome Job!";
        } else if (resultPercentage > 70){
            resultHeader = "Good Job!";
        } else {
            resultHeader = "Try Again!";
        }

        mResultHeader.setText(resultHeader);
        mFinalScore.setText(getString(R.string.final_score,mCurrentScore,selectedAmount));
        mFinalTime.setText(getString(R.string.final_time,minutes,seconds));

        // Display correction button if correction are available &
        // is not already doing corrections
        if(!CorrectionsUtil.getCorrections(getContext()).isEmpty() && !isCorrection){
            mCorrectionButton.setVisibility(View.VISIBLE);
        } else if (isCorrection) {
            mCorrectionButton.setVisibility(View.GONE);
            mFinalTime.setVisibility(View.GONE);
        } else {
            mCorrectionButton.setVisibility(View.GONE);
        }

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        mCorrectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                Utility.setIsCorrections(getContext(),true);
                Utility.setCurrentScore(getActivity(),0);
                Utility.setRemainingQuestions(getActivity(), CorrectionsUtil.getCorrections(getContext()).size());
                Utility.setSelectedAmount(getContext(),CorrectionsUtil.getCorrections(getContext()).size());
                startActivity(intent);
            }
        });

        mReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                Utility.setIsCorrections(getContext(),false);
                Utility.setCurrentScore(getActivity(),0);
                Utility.setRemainingQuestions(getActivity(), selectedAmount);
                CorrectionsUtil.clearCorrections(getActivity());
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return alertDialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
