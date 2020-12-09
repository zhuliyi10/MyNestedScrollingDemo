package com.leory.mynestedscrollingdemo;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @Description:切换首页的viewpager
 * @Author: leory
 * @Time: 2020/12/7
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private final String[] mTitles;
    private List<HomeFragment> mFragments;

    public HomePagerAdapter(FragmentManager fm, String[] titles, List<HomeFragment> fragments) {
        super(fm);
        mTitles = titles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}