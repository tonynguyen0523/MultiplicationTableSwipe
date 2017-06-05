package com.swipeacademy.multiplicationtableswipe.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.swipeacademy.multiplicationtableswipe.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tonyn on 5/31/2017.
 */

public final class CorrectionsUtil {

    private CorrectionsUtil(){}

    public static Set<String> getCorrections(Context context){
        String correctionsKey = context.getString(R.string.pref_corrections_key);
        String correctionsInitializedKey = context.getString(R.string.pref_corrections_initialized_key);
        String[] correctionsList = context.getResources().getStringArray(R.array.correctionsID);

        HashSet<String> correctionList = new HashSet<>(Arrays.asList(correctionsList));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean initialized = preferences.getBoolean(correctionsInitializedKey, false);

        if(!initialized){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(correctionsInitializedKey,true);
            editor.putStringSet(correctionsKey,correctionList);
            editor.apply();
            return correctionList;
        }

        return preferences.getStringSet(correctionsKey, new HashSet<String>());
    }

    public static void editCorrectionsList(Context context, ArrayList<String> correctionsList){
        String key = context.getString(R.string.pref_corrections_key);
        Set<String> corrections = getCorrections(context);

        corrections.addAll(correctionsList);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, corrections);
        editor.apply();

        Log.d("CORRECTIONS", Integer.toString(corrections.size()));
    }

    public static void clearCorrections(Context context){
        String key = context.getString(R.string.pref_corrections_key);
        Set<String> corrections = getCorrections(context);

        corrections.clear();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(key, corrections);
        editor.apply();

        Log.d("CORRECTIONS", Integer.toString(corrections.size()));
    }
}
