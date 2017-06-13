package com.swipeacademy.multiplicationtableswipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.history_viewPager)ViewPager mViewPager;

    FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter{

        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return HistoryFragment.newInstance("5");
                case 1:
                    return HistoryFragment.newInstance("10");
                case 2:
                    return HistoryFragment.newInstance("15");
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "Page" + position;
        }
    }
}
