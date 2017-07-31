package com.healthyfish.healthyfish.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.healthyfish.healthyfish.POJO.Test;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.TestFragmentAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        List<Test> mList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0 ; i <30 ;i++){
            int type = random.nextInt(3)+1;
            mList.add(new Test(type));
        }
        LinearLayoutManager lmg = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(lmg);
        TestFragmentAdapter adapter = new TestFragmentAdapter(mList,getActivity());
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
