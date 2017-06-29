package com.swipeacademy.multiplicationtableswipe;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.LineChartView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by tonyn on 6/27/2017.
 */

public class HistoryLineChart {

    private final LineChartView mChart;
    private final Context mContext;
    private Tooltip tooltip;
    private Cursor cursor;

    HistoryLineChart(CardView card, Context context, Cursor data){
        mContext = context;
        cursor = data;
        mChart = (LineChartView) card.findViewById(R.id.history_lineChart);
    }

    private ArrayList<Integer> getValues(Cursor cursor) {

        ArrayList<Integer> valuesList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            valuesList.add(cursor.getInt(HistoryFragment.COL_ROW_TOTAL_RIGHT));
            cursor.moveToNext();
        }
        return valuesList;
    }

    private float[] convertListToFloatArray(ArrayList<Integer> valuesList){

        float[] valueArray = new float[valuesList.size()];
        Iterator<Integer> iterator = valuesList.iterator();
        for(int i = 0; i < valueArray.length; i++){
                valueArray[i] = iterator.next().intValue();
        }
        return valueArray;
    }

    private String[] getLabels(float[] values){

        String[] labels = new String[values.length];
        for (int i = 0; i < values.length; i++){
            labels[i] = Integer.toString(i + 1);
        }
        return labels;
    }

    void show(){

        ArrayList<Integer> values = getValues(cursor);
        final float[] valuesArray = convertListToFloatArray(values);
        final int arrayLength = valuesArray.length;

        tooltip = new Tooltip(mContext,R.layout.line_graph_tooltip, R.id.line_graph_value);
        tooltip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        tooltip.setDimensions((int) Tools.fromDpToPx(30), (int) Tools.fromDpToPx(25));

        tooltip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

        tooltip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

        tooltip.setPivotX(Tools.fromDpToPx(65) / 2);
        tooltip.setPivotY(Tools.fromDpToPx(25));

        Runnable chartAction = new Runnable() {
            @Override
            public void run() {

                tooltip.prepare(mChart.getEntriesArea(0).get(0), valuesArray[0]);
                mChart.showTooltip(tooltip, true);
            }
        };


        LineSet dataSet = new LineSet(getLabels(valuesArray), valuesArray);
        dataSet.setColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                .setFill(ContextCompat.getColor(mContext,R.color.colorPrimaryDark))
                .setDotsColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                .setThickness(4);
        mChart.addData(dataSet);

        mChart.setAxisBorderValues(0, 10)
                .setYLabels(AxisRenderer.LabelPosition.NONE)
                .setTooltips(tooltip)
                .show(new Animation().setInterpolator(new BounceInterpolator())
                        .fromAlpha(0)
                        .withEndAction(chartAction));
    }
}
