package com.swipeacademy.multiplicationtableswipe.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.swipeacademy.multiplicationtableswipe.HomeActivity;
import com.swipeacademy.multiplicationtableswipe.PlayActivity;

/**
 * Created by tonyn on 5/16/2017.
 */

public class PlayBackPressDialogFragment extends DialogFragment {

    public PlayBackPressDialogFragment() {
        // Empty constructor required for DialogFragment

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Go Home?");
        alertDialogBuilder.setMessage("Current results will reset.");
        alertDialogBuilder.setPositiveButton("Go home", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((PlayActivity) getActivity()).startTimer();
                dialog.dismiss();
            }
        });

        return  alertDialogBuilder.create();
    }


}
