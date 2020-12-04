package com.leory.mynestedscrollingdemo.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.leory.mynestedscrollingdemo.RcvNestedAdapter;

/**
 * @Description: 头部数据
 * @Author: leory
 * @Time: 2020/12/4
 */
public class HeadBean implements MultiItemEntity {
    @Override
    public int getItemType() {
        return RcvNestedAdapter.NORMAL_TYPE;
    }
}
