package com.swipeacademy.multiplicationtableswipe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


/**
 * Created by tonyn on 6/1/2017.
 */

public class QuestionAmountDialog extends DialogFragment {


    public QuestionAmountDialog(){}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.amount_dialog_title)
                .setItems(R.array.questions_amount, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String amountSelected = getResources().getStringArray(R.array.questions_amount)[which];
                        int intAmount = Integer.parseInt(amountSelected);

                        Intent intent = new Intent(getActivity(), PlayActivity.class);
                        Utility.setSelectedAmount(getContext(),intAmount);
                        Utility.setRemainingQuestions(getContext(),intAmount);
                        Utility.setSelectedTable(getContext(),amountSelected);
                        Utility.setSelectedAsset(getContext(),getString(R.string.letsplay_json));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.cancel_dialog_option), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
