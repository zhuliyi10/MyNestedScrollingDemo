package com.leory.mynestedscrollingdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String KEY_PADDING_TOP = "padding_top";
    private WeakReference<RecyclerView> mWeakReference;

    private HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }


    public RecyclerView getRecyclerView() {
        if (mWeakReference != null) {
            return mWeakReference.get();
        }
        return null;
    }

    private void initView(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.rcv);
        mWeakReference = new WeakReference<>(recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("item" + (i + 1));
        }
        HomeAdapter adapter = new HomeAdapter(data);
        recyclerView.setAdapter(adapter);
    }


}
