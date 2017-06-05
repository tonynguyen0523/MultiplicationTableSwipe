package com.swipeacademy.multiplicationtableswipe;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.swipeacademy.multiplicationtableswipe.data.TableContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.swipeacademy.multiplicationtableswipe.data.TableContract.*;

/**
 * Created by tonyn on 5/24/2017.
 */

public class HistoryListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.history_list_recycler_view)RecyclerView mRecyclerView;
    @BindView(R.id.delete_history_button)Button mDeleteHistoryButton;

    private static final int HISTORY_LOADER = 0;
    private Unbinder unbinder;
    private HistoryAdapter adapter;

    private static final String[] HISTORY_COLUMNS = {
            ResultsEntry.TABLE_NAME + " . " + ResultsEntry._ID,
            ResultsEntry.COLUMN_DATE,
            ResultsEntry.COLUMN_TOTAL_RIGHT,
            ResultsEntry.COLUMN_TIME
    };

    static final int COL_ROW_ID = 0;
    static final int COL_ROW_DATE = 1;
    static final int COL_ROW_TOTAL_RIGHT = 2;
    static final int COL_ROW_TIME = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list,container,false);
        unbinder = ButterKnife.bind(this, view);

        adapter = new HistoryAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        mDeleteHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getContentResolver().delete(TableEntry.CONTENT_URI,null,null);
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

        String table = "letsplay";
        Uri tableUri = ResultsEntry.buildTablesResults(table);

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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
    }
}
