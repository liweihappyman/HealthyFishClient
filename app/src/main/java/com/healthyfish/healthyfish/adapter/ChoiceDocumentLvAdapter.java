package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthyfish.healthyfish.utils.SingleView;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：选择档案页面的适配器，具有单选功能，并添加了动态添加数据的方法
 * 作者：LYQ on 2017/7/6.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChoiceDocumentLvAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mData = new ArrayList<>();

    public ChoiceDocumentLvAdapter(Context mContext, List<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mInflater = LayoutInflater.from(mContext);
    }

    public void addDataToAdapter(String data) {
        this.mData.add(data);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SingleView singleView = new SingleView(mContext);
        singleView.setTitle(mData.get(position));
        return singleView;
    }
}
