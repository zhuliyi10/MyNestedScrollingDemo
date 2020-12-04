package com.leory.mynestedscrollingdemo.nested_scrolling;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.leory.mynestedscrollingdemo.R;
import com.leory.mynestedscrollingdemo.RcvNestedAdapter;
import com.leory.mynestedscrollingdemo.bean.HeadBean;
import com.leory.mynestedscrollingdemo.bean.ViewPagerBean;

import java.util.ArrayList;

/**
 * @Description: 处理RecyclerView 套viewPager， viewPager内的fragment中 也有RecyclerView，处理外层、内层 RecyclerView的嵌套滑动问题
 * 类似淘宝、京东首页
 * @Author: leory
 * @Time: 2020/12/4
 */
public class NestedHomeView extends NestedScrollingParent2Layout {
    private final String TAG = this.getClass().getSimpleName();
    private View mLastItemView;
    private RecyclerView mRcvParent;
    private RcvNestedAdapter mRcvNestedAdapter;

    public NestedHomeView(Context context) {
        this(context, null);
    }

    public NestedHomeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedHomeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    private void initView() {
        mRcvParent = findViewById(R.id.rcv_parent);
        mRcvParent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvNestedAdapter = new RcvNestedAdapter();
        mRcvNestedAdapter.setNestedParentLayout(this);
        mRcvParent.setAdapter(mRcvNestedAdapter);
        ArrayList<MultiItemEntity> list = new ArrayList<>();
        list.add(new HeadBean());
        list.add(new ViewPagerBean());
        mRcvNestedAdapter.setNewInstance(list);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int nestedScrollAxes, int type) {
        //自己处理逻辑
        //这里处理是接受 竖向的 嵌套滑动
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {


        int lastItemTop = mLastItemView.getTop();
        if (target == mRcvParent) {
            handleParentRecyclerViewScroll(lastItemTop, dy, consumed);
        }
    }

    /**
     * 外层RecyclerView的最后一个item，即：tab + viewPager
     * 用于判断 滑动 临界位置
     *
     * @param lastItemView
     */
    public void setLastItem(View lastItemView) {
        mLastItemView = lastItemView;
    }

    public RecyclerView getCurrentChildRecyclerView() {
        if (mRcvNestedAdapter == null) {
            return null;
        }
        return mRcvNestedAdapter.getCurrentRecyclerView();
    }


    /**
     * 滑动外层RecyclerView时，的处理
     *
     * @param lastItemTop tab到屏幕顶部的距离，是0就代表到顶了
     * @param dy          目标滑动距离， dy>0 代表向上滑
     * @param consumed
     */
    private void handleParentRecyclerViewScroll(int lastItemTop, int dy, int[] consumed) {

        RecyclerView rcvChild = getCurrentChildRecyclerView();
        if (rcvChild == null) return;
        //tab上边没到顶
        if (lastItemTop != 0) {
            if (dy > 0) {//向上滑
                if (lastItemTop > dy) {
                    //tab的top>想要滑动的dy,就让外部RecyclerView自行处理
                } else {
                    consumed[1] = dy;
                    mRcvParent.scrollBy(0, lastItemTop);
                    rcvChild.scrollBy(0, dy - lastItemTop);
                }

            } else {//向下滑，就让外部RecyclerView自行处理

            }
        } else {//tab上边到顶了
            if (dy > 0) {
                //向上，内层直接消费掉
                rcvChild.scrollBy(0, dy);
                consumed[1] = dy;
            } else {
                int childScrolledY = rcvChild.computeVerticalScrollOffset();
                if (childScrolledY > Math.abs(dy)) {
                    //内层已滚动的距离，大于想要滚动的距离，内层直接消费掉
                    rcvChild.scrollBy(0, dy);
                    consumed[1] = dy;
                } else {
                    //内层已滚动的距离，小于想要滚动的距离，那么内层消费一部分，到顶后，剩的还给外层自行滑动
                    rcvChild.scrollBy(0, childScrolledY);
                    consumed[1] = childScrolledY - Math.abs(dy);
                }
            }
        }
    }
}
