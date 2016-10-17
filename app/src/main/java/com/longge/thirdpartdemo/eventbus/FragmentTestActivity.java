package com.longge.thirdpartdemo.eventbus;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.longge.thirdpartdemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTestActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        ButterKnife.bind(this);
        initPagerAdapter();
    }

    private void initPagerAdapter() {
        for (int i = 0; i < 2; i++) {
            mFragments.add(new TestFragment());
        }

        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),mFragments);

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(myViewPagerAdapter);

    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> list;

        public MyViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "fragment: " + position;
        }
    }



}
