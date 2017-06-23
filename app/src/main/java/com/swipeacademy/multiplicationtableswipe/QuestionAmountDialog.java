package com.swipeacademy.multiplicationtableswipe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.swipeacademy.multiplicationtableswipe.PlayActivity;
import com.swipeacademy.multiplicationtableswipe.R;
import com.swipeacademy.multiplicationtableswipe.Utility;


/**
 * Created by tonyn on 6/1/2017.
 */

public class QuestionAmountDialog extends DialogFragment {


    public QuestionAmountDialog(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a mode!")
                .setItems(R.array.questions_amount, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String amountSelected = getResources().getStringArray(R.array.questions_amount)[which];
                        int intAmount = Integer.parseInt(amountSelected);
                        Utility.setSelectedAmount(getContext(),intAmount);
                        Utility.setRemainingQuestions(getContext(),intAmount);
                        Utility.setSelectedTable(getContext(),amountSelected);
                        Utility.setSelectedAsset(getContext(),"letsplay.json");

                        Intent intent = new Intent(getActivity(), PlayActivity.class);
                        startActivity(intent);
                        Log.d("AMOUNT", getResources().getStringArray(R.array.questions_amount)[which]);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
