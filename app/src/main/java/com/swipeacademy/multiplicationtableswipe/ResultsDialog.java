package com.swipeacademy.multiplicationtableswipe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.swipeacademy.multiplicationtableswipe.HomeActivity;
import com.swipeacademy.multiplicationtableswipe.PlayActivity;
import com.swipeacademy.multiplicationtableswipe.PlayResultActivity;
import com.swipeacademy.multiplicationtableswipe.R;
import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;
import com.swipeacademy.multiplicationtableswipe.Utility;

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
    @BindView(R.id.results_home_button)ImageButton mHomeButton;
    @BindView(R.id.results_replay_button)ImageButton mReplayButton;
    @BindView(R.id.correction_button)Button mCorrectionButton;

    private Unbinder unbinder;

    public ResultsDialog(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_play_end,null);
        unbinder = ButterKnife.bind(this, rootView);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);

        int mCurrentScore = Utility.getCurrentScore(getContext());
        long mFinishedTime = Utility.getFinishedTime(getContext());
        final int selectedAmount = Utility.getSelectedAmount(getContext());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(mFinishedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(mFinishedTime);
        boolean isCorrection = Utility.getIsCorrections(getContext());

        mFinalScore.setText(getString(R.string.final_score,mCurrentScore,selectedAmount));
        mFinalTime.setText(getString(R.string.final_time,minutes,seconds));

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
                Log.d("Corrections List", CorrectionsUtil.getCorrections(getContext()).toString());
                Toast.makeText(getContext(),Integer.toString(CorrectionsUtil.getCorrections(getContext()).size()),Toast.LENGTH_SHORT).show();
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
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
