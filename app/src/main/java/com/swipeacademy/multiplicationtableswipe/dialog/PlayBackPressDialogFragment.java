package com.swipeacademy.multiplicationtableswipe.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.swipeacademy.multiplicationtableswipe.HomeActivity;
import com.swipeacademy.multiplicationtableswipe.PlayFragment;
import com.swipeacademy.multiplicationtableswipe.R;

/**
 * Created by tonyn on 5/16/2017.
 * Dialog for when user presses the home or back button during PlayActivity.
 */

public class PlayBackPressDialogFragment extends DialogFragment {


    private PlayFragment playFragment;

    public PlayBackPressDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        playFragment = (PlayFragment)getFragmentManager().findFragmentById(R.id.play_container);
        playFragment.pauseTimer();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(R.string.go_home_alert);
        alertDialogBuilder.setMessage(R.string.back_press_alert_message);
        alertDialogBuilder.setPositiveButton(R.string.go_home_option, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().finish();
                playFragment.cancelTimer();
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel_dialog_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alertDialogBuilder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        playFragment.resumeTimer();
        super.onDismiss(dialog);
    }
}
