package com.swipeacademy.multiplicationtableswipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.swipeacademy.multiplicationtableswipe.data.TableContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends AppCompatActivity {

    @BindView(R.id.history_viewPager)ViewPager mViewPager;
    @BindView(R.id.history_toolbar)Toolbar mToolbar;

    FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_history_icon:
                Intent refresh = new Intent(this, HistoryActivity.class);
                getContentResolver().delete(TableContract.TableEntry.CONTENT_URI,null,null);
                startActivity(refresh);
                this.finish();
                Toast.makeText(this,"Delete pressed",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter{

        private static int NUM_ITEMS = 3;
        private String[] tabs = {"24","48","72"};


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
                    return HistoryFragment.newInstance("24");
                case 1:
                    return HistoryFragment.newInstance("48");
                case 2:
                    return HistoryFragment.newInstance("72");
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
