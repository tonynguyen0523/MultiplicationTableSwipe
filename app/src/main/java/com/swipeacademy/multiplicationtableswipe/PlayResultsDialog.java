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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tonyn on 6/11/2017.
 *
 */

public class PlayResultsDialog extends DialogFragment {

    @BindView(R.id.play_final_score)TextView mFinalScore;
    @BindView(R.id.results_header)TextView mResultHeader;
    @BindView(R.id.results_home_button)ImageButton mHomeButton;
    @BindView(R.id.results_replay_button)ImageButton mReplayButton;
    @BindView(R.id.correction_button)Button mCorrectionButton;

    private Unbinder unbinder;

    public PlayResultsDialog(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_play_end,null);
        unbinder = ButterKnife.bind(this, rootView);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView);

        int mCurrentScore = PrefUtility.getCurrentScore(getContext());
        final int selectedAmount = PrefUtility.getSelectedAmount(getContext());
        double resultPercentage = (double)mCurrentScore/selectedAmount * 100;
        boolean isCorrection = PrefUtility.getIsCorrections(getContext());

        String resultHeader;
        if(resultPercentage == 100) {
            resultHeader = getString(R.string.result100);
        } else if (resultPercentage > 90){
            resultHeader = getString(R.string.result90);
        } else if (resultPercentage > 70){
            resultHeader = getString(R.string.result70);
        } else {
            resultHeader = getString(R.string.result_fail);
        }

        mResultHeader.setText(resultHeader);
        mFinalScore.setText(getString(R.string.final_score,mCurrentScore,selectedAmount));

        // Display correction button if correction are available &
        // is not already doing corrections
        if(!CorrectionsUtil.getCorrections(getContext()).isEmpty() && !isCorrection){
            mCorrectionButton.setVisibility(View.VISIBLE);
        } else if (isCorrection) {
            mCorrectionButton.setVisibility(View.GONE);
            mReplayButton.setVisibility(View.GONE);
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
                // Prep for correction mode
                PrefUtility.setIsCorrections(getContext(),true);
                PrefUtility.setCurrentScore(getActivity(),0);
                PrefUtility.setRemainingQuestions(getActivity(), CorrectionsUtil.getCorrections(getContext()).size());
                PrefUtility.setSelectedAmount(getContext(),CorrectionsUtil.getCorrections(getContext()).size());
                getActivity().finish();
                startActivity(intent);
            }
        });

        mReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                // Make sure results are cleared for fresh replay
                PrefUtility.setIsCorrections(getContext(),false);
                PrefUtility.setCurrentScore(getActivity(),0);
                PrefUtility.setRemainingQuestions(getActivity(), selectedAmount);
                CorrectionsUtil.clearCorrections(getActivity());
                getActivity().finish();
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
