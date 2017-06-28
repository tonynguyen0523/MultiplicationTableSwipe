package com.swipeacademy.multiplicationtableswipe;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.animation.BounceInterpolator;

import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;

/**
 * Created by tonyn on 6/27/2017.
 */

public class HistoryLineChart {

    private final LineChartView mChart;
    private final Context mContext;
    private String[] mLables;
    private float[][] mValues;

    public HistoryLineChart(CardView card, Context context, String[] lables, float[][] values){
        mContext = context;
        mLables = lables;
        mValues = values;
        mChart = (LineChartView) card.findViewById(R.id.history_lineChart);
    }

    public void show(){
        LineSet dataSet = new LineSet(mLables,mValues[0]);
        dataSet.setColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                .setFill(ContextCompat.getColor(mContext,R.color.colorPrimary))
                .setDotsColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                .setThickness(4);
        mChart.addData(dataSet);

        mChart.setAxisBorderValues(0, 20)
                .setYLabels(AxisRenderer.LabelPosition.NONE)
                .show(new Animation().setInterpolator(new BounceInterpolator())
                        .fromAlpha(0)
                        .withEndAction(chartAction));
    }
}
