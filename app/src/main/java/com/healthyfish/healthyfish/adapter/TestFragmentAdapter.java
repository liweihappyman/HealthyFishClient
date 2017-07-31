package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthyfish.healthyfish.POJO.Test;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：WKJ on 2017/7/30.
 * 邮箱：
 * 编辑：WKJ
 */

public class TestFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Test> list = new ArrayList<>();
    private Context mContext;

    public TestFragmentAdapter(List<Test> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item1, parent, false);
            return new ViewHolder(view);
        } else if (viewType == 2) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item2, parent, false);
            return new ViewHolder2(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item3, parent, false);
            return new ViewHolder3(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Test test = list.get(position);
        if (test.getType() == 1) {


        } else if (test.getType() == 2) {


        } else {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        Test test = list.get(position);
        if (test.getType() == 1) {
            return 1;

        } else if (test.getType() == 2) {
            return 2;

        } else if (test.getType() ==3){
            return 3;
        }else {
            return super.getItemViewType(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        public ViewHolder2(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
        }
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder {
        public ViewHolder3(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
        }
    }

}
