package com.healthyfish.healthyfish.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanCustomPlan;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.utils.NestingUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：健康管理自定义计划具体安排页面ListView适配器
 * 作者：LYQ on 2017/7/17.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ConcreteArrangementAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BeanCustomPlan> mList = new ArrayList<>();

    public ConcreteArrangementAdapter(Context mContext, List<BeanCustomPlan> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext);
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
            convertView = mLayoutInflater.inflate(R.layout.item_concrete_arrangement_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvDateAndWeek.setText(mList.get(position).getDateAndWeek());
        holder.tvTime.setText(mList.get(position).getTime());
        holder.lvIndividualHealthPlan.setAdapter(new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,mList.get(position).getIndividualPlan()));
        NestingUtils.setListViewHeightBasedOnChildren(holder.lvIndividualHealthPlan);
        //选择具体时间点
        final ViewHolder finalHolder = holder;
        holder.ivSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finalHolder.tvTime.setText("");
                SelectTimeDialog selectTimeDialog = new SelectTimeDialog(mContext);
                selectTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                selectTimeDialog.showDialog();
            }
        });
        //添加单项养生计划
        holder.tvAddIndividualHealthPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_date_and_week)
        TextView tvDateAndWeek;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_select_time)
        ImageView ivSelectTime;
        @BindView(R.id.lv_individual_health_plan)
        ListView lvIndividualHealthPlan;
        @BindView(R.id.tv_add_individual_health_plan)
        TextView tvAddIndividualHealthPlan;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class SelectTimeDialog extends Dialog {

        public SelectTimeDialog(@NonNull Context context) {
            super(context);
        }

        public void showDialog() {
            setContentView(R.layout.layout_select_time);//设置对话框的布局
            show();//显示对话框
        }
    }
}
