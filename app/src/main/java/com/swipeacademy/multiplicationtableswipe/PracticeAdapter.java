package com.swipeacademy.multiplicationtableswipe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonyn on 6/23/2017.
 */

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder> {

    private String[] mDataSet;
    private ItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.practice_recycler_item_textView)TextView mTableTV;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClicked(v,getAdapterPosition());
        }
    }

    public void setOnItemClickListener(final ItemClickListener listener){
        this.listener = listener;
    }

    public interface ItemClickListener{
        void onItemClicked(View view, int position);
    }

    public PracticeAdapter(String[] dataSet){
        mDataSet = dataSet;
    }

    @Override
    public PracticeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_practice_recycler_item, parent, false);
        final ViewHolder vh = new ViewHolder(item);
        return vh;
    }

    @Override
    public void onBindViewHolder(PracticeAdapter.ViewHolder holder, int position) {

        holder.mTableTV.setText(mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
