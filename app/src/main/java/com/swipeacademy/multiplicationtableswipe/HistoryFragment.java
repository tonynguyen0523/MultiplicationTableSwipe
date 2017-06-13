package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.swipeacademy.multiplicationtableswipe.data.TableContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tonyn on 6/12/2017.
 */

public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.delete_history_button)Button mDeleteButton;
    @BindView(R.id.history_list_recycler_view)RecyclerView mRecyclerView;

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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
    }
}




