package com.swipeacademy.multiplicationtableswipe;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.db.chart.view.LineChartView;
import com.swipeacademy.multiplicationtableswipe.data.TableContract;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tonyn on 6/12/2017.
 *
 */

public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.chart_card)
    CardView mCardView;
    @BindView(R.id.history_lineChart)
    LineChartView mLineChartView;
    @BindView(R.id.chart_empty_textView)
    TextView mEmptyTextView;

    private static final int HISTORY_LOADER = 0;
    private String mTable;
    private Unbinder unbinder;

    private static final String[] HISTORY_COLUMNS = {
            TableContract.ResultsEntry.TABLE_NAME + " . " + TableContract.ResultsEntry._ID,
            TableContract.ResultsEntry.COLUMN_DATE,
            TableContract.ResultsEntry.COLUMN_TOTAL_RIGHT,
            TableContract.ResultsEntry.COLUMN_TIME
    };

    static final int COL_ROW_TOTAL_RIGHT = 2;

    public static HistoryFragment newInstance(String table) {
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

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(HISTORY_LOADER, null, this);
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

        int axisValue = Integer.parseInt(mTable);
        String emptyView = "Play " + mTable + " three times to view history.";

        if (data.getCount() > 2) {
            mEmptyTextView.setVisibility(View.INVISIBLE);
            new HistoryLineChart(mCardView, getContext(), data, axisValue).show();
        } else {
            mEmptyTextView.setText(emptyView);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}




