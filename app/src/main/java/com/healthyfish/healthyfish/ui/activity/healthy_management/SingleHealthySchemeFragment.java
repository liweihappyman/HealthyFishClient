package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthyfish.healthyfish.POJO.BeanHealthyScheme;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.HealthySchemeAdapter;

import java.util.List;

/**
 * 描述：选择ViewPage计划中的具体项
 * 作者：Wayne on 2017/7/13 21:42
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */
@SuppressLint("ValidFragment")
public class SingleHealthySchemeFragment extends Fragment {
    Context mContext;
    private RecyclerView healthySchemeRecyclerview;
    private List<BeanHealthyScheme> listHealthyScheme;

    public SingleHealthySchemeFragment(Context mContext, List<BeanHealthyScheme> listHealthyScheme) {
        this.mContext = mContext;
        this.listHealthyScheme = listHealthyScheme;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_healthy_scheme, null);
        healthySchemeRecyclerview = (RecyclerView) v.findViewById(R.id.healthy_scheme_recyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        healthySchemeRecyclerview.setLayoutManager(layoutManager);
        HealthySchemeAdapter adapter = new HealthySchemeAdapter(mContext, listHealthyScheme);
        healthySchemeRecyclerview.setAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
