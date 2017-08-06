package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：预览健康计划的列表适配器
 * 作者：WKJ on 2017/8/5.
 * 邮箱：
 * 编辑：WKJ
 */

public class PreviewHealthySchemeAdapter extends RecyclerView.Adapter<PreviewHealthySchemeAdapter.ViewHolder> {
    private Context mContext;
    private List<BeanHotPlanItem> hotPlanItem;
    private List<String> week;
    private List<String> calendarDate;
    List<BeanHotPlanItem.TodoListBean> list1 = new ArrayList<>();
    List<BeanHotPlanItem.TodoListBean> list2 = new ArrayList<>();


    public PreviewHealthySchemeAdapter(Context mContext, List<BeanHotPlanItem> hotPlanItem, List<String> week, List<String> calendarDate) {
        this.mContext = mContext;
        this.hotPlanItem = hotPlanItem;
        this.week = week;
        this.calendarDate = calendarDate;
        //因为实现知道这里只有两个计划
        list1 = hotPlanItem.get(0).getTodoList();
        list2 = hotPlanItem.get(1).getTodoList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_preview_my_healthy_scheme, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.type1Layout.setVisibility(View.GONE);
        holder.type2Layout.setVisibility(View.GONE);
        holder.date.setText(calendarDate.get(position) + "  " + "星期" + week.get(position));
        if (!list1.get(position).getProgress().equals("nothing")) {
            holder.type1Layout.setVisibility(View.VISIBLE);
            holder.type1.setText(list1.get(position).getTodo());
        } else {
            //holder.type1.setText("休息");
        }
        if (!list2.get(position).getProgress().equals("nothing")) {
            holder.type2Layout.setVisibility(View.VISIBLE);
            holder.type2.setText(list2.get(position).getTodo());
        } else {
            //holder.type2.setText("休息");
        }
    }

    @Override
    public int getItemCount() {
        return week.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.type1)
        TextView type1;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.type1_layout)
        AutoLinearLayout type1Layout;
        @BindView(R.id.type2)
        TextView type2;
        @BindView(R.id.type2_layout)
        AutoLinearLayout type2Layout;
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
