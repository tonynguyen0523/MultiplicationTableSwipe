package com.swipeacademy.multiplicationtableswipe.Util;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.swipeacademy.multiplicationtableswipe.R;

/**
 * Created by tonyn on 7/6/2017.
 */

public class Analytics {

    public static void setUserSchoolGrade(Context context, int gradeIndex){
        String userPropertyKey = context.getString(
                R.string.user_property_key_school_grade);
        String[] userPropertyValues = context.getResources()
                .getStringArray(R.array.user_school_grade);
        FirebaseAnalytics.getInstance(context).setUserProperty(
                userPropertyKey,userPropertyValues[gradeIndex]
        );
    }
}
