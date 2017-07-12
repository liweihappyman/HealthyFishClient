package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 描述：新建病历页面的图片显示适配器
 * 作者：WKJ on 2017/7/11.
 * 邮箱：
 * 编辑：WKJ
 */

public class WeekItemGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> list;

    public WeekItemGridAdapter(List<String> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }//修改

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_week, null);
        AutoUtils.auto(convertView);
        TextView item = (TextView) convertView.findViewById(R.id.time);


        item.setText(list.get(position));
        if (list.get(position)!=null) {
            item.setText(list.get(position));
        }else {
            item.setText("  ");
        }
        return convertView;
    }
}
