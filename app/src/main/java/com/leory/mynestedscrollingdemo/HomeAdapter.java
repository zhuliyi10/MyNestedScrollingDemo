package com.leory.mynestedscrollingdemo;

import android.util.Log;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeAdapter extends BaseAdapter<String> {
    private static final String TAG=HomeAdapter.class.getSimpleName();
    public HomeAdapter(@Nullable List<String> data) {
        super(R.layout.item_home_list, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {
        holder.setText(R.id.txt, s);
        Log.d(TAG, "convert: "+s);
    }
}
