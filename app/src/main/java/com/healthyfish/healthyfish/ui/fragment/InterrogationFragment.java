package com.healthyfish.healthyfish.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.healthyfish.healthyfish.utils.DividerGridItemDecoration;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.InterrogationRvAdapter;
import com.healthyfish.healthyfish.listener.InterrogationRvlistener;
import com.healthyfish.healthyfish.ui.activity.ChoiceDoctor;
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
    private Context mContext;
    private View rootView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_choice_department)
    RecyclerView rvChoiceDepartment;
    Unbinder unbinder;

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
    View rootView;
    private List<String> mDepartments = new ArrayList<>();
    private List<Integer> mDepartmentIcons = new ArrayList<>();
    private InterrogationRvAdapter mRvAdapter;

    private InterrogationRvAdapter mRvAdapter;

    public InterrogationFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                mContext = getActivity();
                rootView= inflater.inflate(R.layout.fragment_interrogation, container, false);
                unbinder = ButterKnife.bind(this, rootView);
                rvListener();
                initRecycleView();
                return rootView;
    }

    /**
     * RecyclerView的监听
     */
    private void rvListener() {
        rvChoiceDepartment.addOnItemTouchListener(new InterrogationRvlistener(mContext, rvChoiceDepartment, new InterrogationRvlistener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到该科室的医生列表，需要发送科室信息到后台获取科室医生列表信息，传入下一个页面
                MyToast.showToast(mContext,"点击"+String.valueOf(position));
                Intent intent = new Intent(mContext,ChoiceDoctor.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                MyToast.showToast(mContext,"长按"+String.valueOf(position));
            }
        }));


    }

    /**
     * 初始化科室数据
     */
    private void initRecycleView() {
        List<String> mDepartments = new ArrayList<>();
        List<Integer> mDepartmentIcons = new ArrayList<>();
        String[] departments = new String[]{"中医科"};
        int[] icons = new int[]{R.mipmap.ic_chinese_medicine};
        for (int i = 0;i<departments.length;i++){
            mDepartments.add(departments[i]);
            mDepartmentIcons.add(icons[i]);
        }
        GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,4);
        rvChoiceDepartment.setLayoutManager(gridLayoutManager);
        mRvAdapter = new InterrogationRvAdapter(mContext,mDepartments,mDepartmentIcons);
        rvChoiceDepartment.setAdapter(mRvAdapter);
        rvChoiceDepartment.addItemDecoration(new DividerGridItemDecoration(mContext));
    }
 

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
