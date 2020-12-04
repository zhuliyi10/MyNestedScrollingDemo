package com.leory.mynestedscrollingdemo;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * Description : BaseAdapter
 * Author : Leory
 * Time : 2020-11-2
 */

public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public void clear() {
        getData().clear();
        notifyDataSetChanged();
    }
}

