package com.swipeacademy.multiplicationtableswipe;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonyn on 5/22/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private Cursor cursor;

    HistoryAdapter(Context context){
        this.context = context;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.history_correct)TextView historyCorrect;
        @BindView(R.id.history_time)TextView historyTime;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(context).inflate(R.layout.fragment_history_list_item, parent, false);
        return new HistoryViewHolder(item);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {

        cursor.moveToPosition(position);

        int correct = cursor.getInt(HistoryFragment.COL_ROW_TOTAL_RIGHT);
        int time = cursor.getInt(HistoryFragment.COL_ROW_TIME);

        holder.historyCorrect.setText(Integer.toString(correct));
        holder.historyTime.setText(Integer.toString(time));
    }

    void setCursor(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(cursor != null){
            count = cursor.getCount();
            Log.d("COUNT", Integer.toString(count));
        }
        return count;
    }
}
