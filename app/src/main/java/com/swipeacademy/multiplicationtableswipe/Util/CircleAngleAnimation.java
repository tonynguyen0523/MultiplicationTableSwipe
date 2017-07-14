package com.swipeacademy.multiplicationtableswipe.Util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.swipeacademy.multiplicationtableswipe.PlayActivity;
import com.swipeacademy.multiplicationtableswipe.PlayFragment;
import com.swipeacademy.multiplicationtableswipe.R;

/**
 * Created by tonynguyen on 6/25/17.
 */

public class CircleAngleAnimation extends Animation {

    private Circle circle;
    private float oldAngle;
    private float newAngle;


    public CircleAngleAnimation(Circle circle, int newAngle) {
        this.oldAngle = circle.getAngle();
        this.newAngle = newAngle;
        this.circle = circle;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);

        circle.setAngle(angle);
        circle.requestLayout();
    }

}