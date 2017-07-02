package com.healthyfish.healthyfish.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.healthyfish.healthyfish.utils.DividerGridItemDecoration;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.InterrogationRvAdapter;
import com.healthyfish.healthyfish.listener.InterrogationRvlistener;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterrogationFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_choice_department)
    RecyclerView rvChoiceDepartment;
    Unbinder unbinder;

    private Context mContext;
    private List<String> mDepartments = new ArrayList<>();
    private List<Integer> mDepartmentIcons = new ArrayList<>();
    private InterrogationRvAdapter mRvAdapter;

    public InterrogationFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interrogation, container, false);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initData();
        initRecycleView();
        rvListener();
        return view;
    }

    /**
     * RecyclerView的监听
     */
    private void rvListener() {
        rvChoiceDepartment.addOnItemTouchListener(new InterrogationRvlistener(mContext, rvChoiceDepartment, new InterrogationRvlistener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyToast.showToast(mContext,"点击"+String.valueOf(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                MyToast.showToast(mContext,"长按"+String.valueOf(position));
            }
        }));
    }

    /**
     * 初始化RecycleView
     */
    private void initRecycleView() {
        GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,4);
        rvChoiceDepartment.setLayoutManager(gridLayoutManager);
        mRvAdapter = new InterrogationRvAdapter(mContext,mDepartments,mDepartmentIcons);
        rvChoiceDepartment.setAdapter(mRvAdapter);
        rvChoiceDepartment.addItemDecoration(new DividerGridItemDecoration(mContext));
    }

    /**
     * 初始化科室数据
     */
    private void initData() {
        String[] departments = new String[]{"中医科","儿科","脾胃病科","骨科","中医科","儿科","脾胃病科","骨科","中医科","儿科","儿科","儿科"};
        int[] icons = new int[]{R.mipmap.ic_alipay_pay,R.mipmap.ic_alipay_pay,R.mipmap.ic_alipay_pay,R.mipmap.ic_alipay_pay,
                R.mipmap.ic_alipay_pay,R.mipmap.ic_alipay_pay,R.mipmap.ic_alipay_pay,R.mipmap.ic_alipay_pay,
                R.mipmap.ic_alipay_pay,R.mipmap.ic_alipay_pay,R.mipmap.ic_alipay_pay,R.mipmap.ic_alipay_pay,};
        for (int i = 0;i<departments.length;i++){
            mDepartments.add(departments[i]);
            mDepartmentIcons.add(icons[i]);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
