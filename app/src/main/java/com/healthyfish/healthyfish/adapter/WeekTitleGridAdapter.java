package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanWeekAndDate;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 描述：新建病历页面的图片显示适配器
 * 作者：WKJ on 2017/7/11.
 * 邮箱：
 * 编辑：WKJ
 */

public class WeekTitleGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<BeanWeekAndDate> list;

    public WeekTitleGridAdapter(List<BeanWeekAndDate> list, Context mContext) {
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_week_title, parent,false);
        AutoUtils.auto(convertView);
        TextView titleWeek = (TextView) convertView.findViewById(R.id.title_week);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        titleWeek.setText(list.get(position).getTitleWeek());
        if (list.get(position).getDate()!=null) {
            date.setText(list.get(position).getDate());
        }else {
            date.setText("  ");
        }
        return convertView;
    }
}
