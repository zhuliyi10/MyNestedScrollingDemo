package com.leory.mynestedscrollingdemo;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.leory.mynestedscrollingdemo.bean.HeadBean;
import com.leory.mynestedscrollingdemo.nested_scrolling.NestedHomeView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:recycerView嵌套viewpager
 * @Author: leory
 * @Time: 2020/12/4
 */
public class RcvNestedAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int NORMAL_TYPE = 1;
    public static final int VIEW_PAGE_TYPE = 2;
    private String[] mTitles = {"头条号","大鱼号","百家号","企鹅号","一点号"};
    private NestedHomeView mHomeView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<HomeFragment>mHomeFragments=new ArrayList<>();

    public RcvNestedAdapter() {
        addItemType(NORMAL_TYPE, R.layout.head_home);
        addItemType(VIEW_PAGE_TYPE, R.layout.item_view_page);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MultiItemEntity multiItemEntity) {
        int itemViewType = baseViewHolder.getItemViewType();
        switch (itemViewType) {
            case NORMAL_TYPE:
                convertNormal(baseViewHolder, (HeadBean) multiItemEntity);
                break;
            case VIEW_PAGE_TYPE:
                convertViewPager(baseViewHolder);
                break;
            default:
                break;
        }

    }

    public void setNestedParentLayout(NestedHomeView homeView) {
        mHomeView = homeView;
    }
    public RecyclerView getCurrentRecyclerView(){
        return mHomeFragments.get(mViewPager.getCurrentItem()).getRecyclerView();
    }

    private void convertNormal(BaseViewHolder baseViewHolder, HeadBean headBean) {
        mHomeView.post(new Runnable() {
            @Override
            public void run() {
                mHomeView.setHeaderView(baseViewHolder.itemView);
            }
        });

    }

    private void convertViewPager(BaseViewHolder baseViewHolder) {
        mViewPager = baseViewHolder.getView(R.id.view_pager);
        mTabLayout = baseViewHolder.getView(R.id.tab_layout);
        for (int i = 0; i < mTitles.length; i++) {
            HomeFragment fragment = HomeFragment.newInstance();
            mHomeFragments.add(fragment);
        }
        HomePagerAdapter adapter = new HomePagerAdapter(((FragmentActivity) getContext()).getSupportFragmentManager(), mTitles, mHomeFragments);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

            mHomeView.post(new Runnable() {
                @Override
                public void run() {
                    mHomeView.setLastItem(baseViewHolder.itemView);
                }
            });


    }


    class HomePagerAdapter extends FragmentStatePagerAdapter {

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


}
