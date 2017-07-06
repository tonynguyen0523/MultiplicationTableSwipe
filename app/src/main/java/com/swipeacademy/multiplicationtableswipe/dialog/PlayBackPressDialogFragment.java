package com.swipeacademy.multiplicationtableswipe.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.swipeacademy.multiplicationtableswipe.HomeActivity;
import com.swipeacademy.multiplicationtableswipe.PlayActivity;
import com.swipeacademy.multiplicationtableswipe.PlayFragment;
import com.swipeacademy.multiplicationtableswipe.R;

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
        alertDialogBuilder.setTitle(R.string.go_home_alert);
        alertDialogBuilder.setMessage(R.string.back_press_alert_message);
        alertDialogBuilder.setPositiveButton(R.string.go_home_option, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel_dialog_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        });

        return  alertDialogBuilder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
