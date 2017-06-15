package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.swipeacademy.multiplicationtableswipe.data.TableContract;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tonyn on 6/12/2017.
 */

public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.delete_history_button)Button mDeleteButton;
    @BindView(R.id.history_list_recycler_view)RecyclerView mRecyclerView;
    @BindView(R.id.history_lineChart)LineChart mLineChart;

    private String mTable;
    private static final int HISTORY_LOADER = 0;
    private Unbinder unbinder;
    private HistoryAdapter adapter;

    private static final String[] HISTORY_COLUMNS = {
            TableContract.ResultsEntry.TABLE_NAME + " . " + TableContract.ResultsEntry._ID,
            TableContract.ResultsEntry.COLUMN_DATE,
            TableContract.ResultsEntry.COLUMN_TOTAL_RIGHT,
            TableContract.ResultsEntry.COLUMN_TIME
    };

    static final int COL_ROW_ID = 0;
    static final int COL_ROW_DATE = 1;
    static final int COL_ROW_TOTAL_RIGHT = 2;
    static final int COL_ROW_TIME = 3;


    public static HistoryFragment newInstance(String table){
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString("tableSelected", table);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTable = getArguments().getString("tableSelected");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history_list,container,false);
        unbinder = ButterKnife.bind(this, view);

        adapter = new HistoryAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HistoryActivity.class);
                getActivity().getContentResolver().delete(TableContract.TableEntry.CONTENT_URI,null,null);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(HISTORY_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String table = mTable;
        Uri tableUri = TableContract.ResultsEntry.buildTablesResults(table);

        return new CursorLoader(getContext(),
                tableUri,
                HISTORY_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.setCursor(data);
        setLineChartData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
    }

    private void setLineChartData(Cursor data){

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        final ArrayList<String> xVals = new ArrayList<>();
        Integer[] userCorrectHistory = getUserCorrectHistory(data);

        for(int i = 0; i < userCorrectHistory.length; i++){
            yVals.add(new Entry(i,userCorrectHistory[i]));
            xVals.add(Integer.toString(i + 1));
        }

        LineDataSet set1;

        if(mLineChart.getData()!= null &&
                mLineChart.getData().getDataSetCount() > 0){
            set1 = (LineDataSet)mLineChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals, "DataSet 1");

            set1.setMode(LineDataSet.Mode.LINEAR);
//            set1.setCubicIntensity(0.0f);
            //set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.WHITE);
            set1.setFillColor(Color.WHITE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            final XAxis xAxis = mLineChart.getXAxis();
            xAxis.setGranularityEnabled(true);
            xAxis.setGranularity(0.1f);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return xVals.get((int)value);
                }
            });

            LineData lineData = new LineData(set1);
            lineData.setValueTextSize(9f);
            lineData.setDrawValues(false);

            mLineChart.setData(lineData);
        }
    }

    private Integer[] getUserCorrectHistory(Cursor data){

        ArrayList<Integer> correctHistory = new ArrayList<>();
        data.moveToFirst();
        while(!data.isAfterLast()){
            correctHistory.add(data.getInt(HistoryFragment.COL_ROW_TOTAL_RIGHT));
            data.moveToNext();
        }
        return correctHistory.toArray(new Integer[correctHistory.size()]);
    }

}




