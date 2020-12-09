package com.leory.mynestedscrollingdemo.nested_scrolling;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
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
    private HomeHeaderView mHeaderView;
    private RecyclerView mRcvParent;
    private RcvNestedAdapter mRcvNestedAdapter;
    private int mHeaderMinHeight;
    private int mHeaderHeight;
    private int tabHeight;//标签高度


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
        mRcvNestedAdapter = new RcvNestedAdapter((FragmentActivity) getContext());
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

        handleParentRecyclerViewScroll(target, dy, consumed);

    }

    /**
     * 设置头部view
     *
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = (HomeHeaderView) headerView;
        mHeaderHeight = mHeaderView.getMeasuredHeight();
        mHeaderMinHeight = (int) (mHeaderHeight * 0.6);
        mHeaderView.setHeaderMinHeight(mHeaderMinHeight);
    }

    /**
     * 外层RecyclerView的最后一个item，即：tab + viewPager
     * 用于判断 滑动 临界位置
     *
     * @param lastItemView
     */
    public void setLastItem(View lastItemView) {
        mLastItemView = lastItemView;
        View tab = mLastItemView.findViewById(R.id.tab_layout);
        tabHeight = tab.getMeasuredHeight();
        ViewGroup.LayoutParams lp = mLastItemView.getLayoutParams();
        lp.height = getMeasuredHeight() - mHeaderMinHeight + tabHeight;
        mLastItemView.setLayoutParams(lp);

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
     * @param dy       目标滑动距离， dy>0 代表向上滑
     * @param consumed
     */
    private void handleParentRecyclerViewScroll(View target, int dy, int[] consumed) {
        RecyclerView rcvChild = getCurrentChildRecyclerView();
        if (rcvChild == null) return;
        if (target == mRcvParent) {
            int lastItemTop = mLastItemView.getTop() - mHeaderMinHeight;//tab到屏幕最顶部
            int childScrolledY = rcvChild.computeVerticalScrollOffset();
            if (dy > 0) {//向上
                int scrollY = mLastItemView.getScrollY();
                if (scrollY < tabHeight) {//隐藏tab
                    int newScrollY = scrollY + dy / 2;
                    if (newScrollY > tabHeight) {
                        dy = newScrollY - tabHeight;
                        newScrollY = tabHeight;
                    } else {
                        dy = 0;
                    }
                    //根据滚动距离缩放图片和字体
                    float factor = 1f * newScrollY / tabHeight;
                    mHeaderView.setTextAndImageFactor(factor);
                    consumed[1] = newScrollY - scrollY;//给标签上滑消耗
                    mLastItemView.setScrollY(newScrollY);
                }
                if (dy > 0) {

                    if (lastItemTop > dy) {
                        //tab的top>想要滑动的dy,就让外部RecyclerView自行处理
                    } else {
                        mHeaderView.setHeaderShrink();
                        consumed[1] = dy - lastItemTop;//外部默认处理lastItemTop
                        rcvChild.scrollBy(0, dy - lastItemTop);//剩下的交给子rcv处理
                    }
                }

            } else {//向下
                if (childScrolledY > Math.abs(dy)) {
                    rcvChild.scrollBy(0, dy);//全部交给子rcv
                    consumed[1] = dy;
                } else {
                    mHeaderView.setHeaderExpand();
                    rcvChild.scrollBy(0, -childScrolledY);//子rcv滚到最顶
                    dy = childScrolledY + dy;//剩下这么多dy
                    int deltaY = (mHeaderHeight - tabHeight) - mLastItemView.getTop();//到mHeaderHeight-tabHeight这个高度时，开始显示标签
                    if (deltaY < Math.abs(dy)) {
                        int scrollY = mLastItemView.getScrollY();
                        int newScrollY = scrollY + dy / 2;
                        if (newScrollY < 0) {
                            newScrollY = 0;
                        }
                        mLastItemView.setScrollY(newScrollY);
                        consumed[1] = newScrollY - scrollY;//消耗标签向下滑动
                        //根据滚动距离缩放图片和字体
                        float factor = 1f * newScrollY / tabHeight;
                        mHeaderView.setTextAndImageFactor(factor);
                    }

                }
            }

        } else if (target == rcvChild) {
            Log.d(TAG, "handleParentRecyclerViewScroll: rcvChild");
        } else {
            Log.d(TAG, "handleParentRecyclerViewScroll: other");
        }
    }

    private int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, getResources().getDisplayMetrics());
    }


}
