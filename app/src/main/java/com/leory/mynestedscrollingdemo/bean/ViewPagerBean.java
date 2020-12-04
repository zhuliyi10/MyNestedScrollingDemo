package com.leory.mynestedscrollingdemo.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.leory.mynestedscrollingdemo.RcvNestedAdapter;

/**
 * @Description: viewPager的数据格式
 * @Author: leory
 * @Time: 2020/12/4
 */
public class ViewPagerBean implements MultiItemEntity {
    @Override
    public int getItemType() {
        return RcvNestedAdapter.VIEW_PAGE_TYPE;
    }
}
