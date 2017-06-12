package com.swipeacademy.multiplicationtableswipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tonyn on 6/12/2017.
 */

public class HistoryFragment extends Fragment {

    private String mTable;

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
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
