package com.swipeacademy.multiplicationtableswipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

/**
 * Created by tonyn on 7/9/2017.
 */

public class PlayUtility {

    public static void resetPlay(Context context){

        PrefUtility.setCurrentScore(context, 0);
        CorrectionsUtil.clearCorrections(context);
        PrefUtility.setIsCorrections(context, false);
    }

    public static void startPlay(Context context, int amount){

        PrefUtility.setSelectedAmount(context, amount);
        PrefUtility.setRemainingQuestions(context, amount);
        PrefUtility.setSelectedTable(context, Integer.toString(amount));
        PrefUtility.setSelectedAsset(context, context.getString(R.string.letsplay_json));
    }
}
