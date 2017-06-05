package com.swipeacademy.multiplicationtableswipe;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipeacademy.multiplicationtableswipe.Util.CursorRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonyn on 5/27/2017.
 */

public class HistoryCursorRecyclerAdapter extends CursorRecyclerViewAdapter<HistoryCursorRecyclerAdapter.ViewHolder> {

    private Context mContext;

    public HistoryCursorRecyclerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.history_correct)TextView mCorrectTV;
        @BindView(R.id.history_time)TextView mTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_list_item,parent,false);

        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

        int correct = cursor.getInt(HistoryListFragment.COL_ROW_TOTAL_RIGHT);
        int time = cursor.getInt(HistoryListFragment.COL_ROW_TIME);

        viewHolder.mCorrectTV.setText(Integer.toString(correct));
        viewHolder.mTimeTV.setText(Integer.toString(time));
    }
}
