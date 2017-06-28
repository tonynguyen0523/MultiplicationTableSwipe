package com.swipeacademy.multiplicationtableswipe;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.animation.BounceInterpolator;

import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by tonyn on 6/27/2017.
 */

public class HistoryLineChart {

    private final LineChartView mChart;
    private final Context mContext;
    private Cursor cursor;
    private static float[] mValues;
    private static String[] mStrings = {"1","2","3","4","5","6","7"};

    public HistoryLineChart(CardView card, Context context, Cursor data){
        mContext = context;
        cursor = data;
        mChart = (LineChartView) card.findViewById(R.id.history_lineChart);
    }

    public ArrayList<Integer> getValues(Cursor cursor) {

        ArrayList<Integer> valuesList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            valuesList.add(cursor.getInt(HistoryFragment.COL_ROW_TOTAL_RIGHT));
            cursor.moveToNext();
        }
        return valuesList;
    }

    public float[] convertListToFloatArray(ArrayList<Integer> valuesList){

        float[] valueArray = new float[valuesList.size()];
        Iterator<Integer> iterator = valuesList.iterator();
        for(int i = 0; i < valueArray.length; i++){
                valueArray[i] = iterator.next().intValue();
        }
        return valueArray;
    }

    public String[] getLabels(float[] values){

        String[] labels = new String[values.length];
        for (int i = 0; i < values.length; i++){
            labels[i] = Integer.toString(i + 1);
        }
        return labels;
    }

    public void show(){

        ArrayList<Integer> values = getValues(cursor);
        float[] valuesArray = convertListToFloatArray(values);

        LineSet dataSet = new LineSet(getLabels(valuesArray), valuesArray);
        dataSet.setColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                .setFill(ContextCompat.getColor(mContext,R.color.colorPrimaryDark))
                .setDotsColor(ContextCompat.getColor(mContext,R.color.colorAccent))
                .setThickness(4);
        mChart.addData(dataSet);

        mChart.setAxisBorderValues(0, 20)
                .setYLabels(AxisRenderer.LabelPosition.NONE)
                .show(new Animation().setInterpolator(new BounceInterpolator())
                        .fromAlpha(0));
    }
}
