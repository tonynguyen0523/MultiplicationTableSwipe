package com.swipeacademy.multiplicationtableswipe;

import android.content.Context;

import com.swipeacademy.multiplicationtableswipe.Util.CorrectionsUtil;

/**
 * Created by tonyn on 7/9/2017.
 */

public class PlayUtility {

    /**
     * Reset everything back to default
     * */
    public static void resetPlay(Context context){

        PrefUtility.setCurrentScore(context, 0);
        CorrectionsUtil.clearCorrections(context);
        PrefUtility.setIsCorrections(context, false);
        PrefUtility.setIsPractice(context,false);
    }

    /**
     * Start Lets Play option
     * */
    public static void startPlay(Context context, int amount){

        PrefUtility.setSelectedAmount(context, amount);
        PrefUtility.setRemainingQuestions(context, amount);
        PrefUtility.setSelectedTable(context, Integer.toString(amount));
        PrefUtility.setSelectedAsset(context, context.getString(R.string.letsplay_json));
    }
}
