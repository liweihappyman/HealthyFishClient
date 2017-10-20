package com.healthyfish.healthyfish.adapter;


import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.R;

import com.healthyfish.healthyfish.utils.NestingUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：医生他院出诊时间ListView适配器
 * 作者：LYQ on 2017/10/17.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class MoreSchedulingAdapter extends BaseAdapter {

    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private List<BeanDoctorInfo> mList = new ArrayList<>();

    public MoreSchedulingAdapter(Activity mActivity, List<BeanDoctorInfo> mList) {
        this.mActivity = mActivity;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_more_scheduling_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText("其他出诊：" + mList.get(position).getHospital() + "-" + mList.get(position).getDepartment());
        holder.lvScheduling.setAdapter(new SchedulingAdapter(mActivity,mList.get(position)));
        NestingUtils.setListViewHeightBasedOnChildren(holder.lvScheduling);
        holder.lvScheduling.setVerticalScrollBarEnabled(true);
        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.lv_scheduling)
        ListView lvScheduling;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
