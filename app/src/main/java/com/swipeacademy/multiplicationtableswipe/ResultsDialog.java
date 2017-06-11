package com.swipeacademy.multiplicationtableswipe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

        long minutes = TimeUnit.MILLISECONDS.toMinutes(mFinishedTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(mFinishedTime);

        mFinalScore.setText(getString(R.string.final_score,mCurrentScore,12));
        mFinalTime.setText(getString(R.string.final_time,minutes,seconds));

        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        mReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                Utility.setCurrentScore(getActivity(),0);
                CorrectionsUtil.clearCorrections(getActivity());
                startActivity(intent);
            }
        });


        return builder.create();


    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
