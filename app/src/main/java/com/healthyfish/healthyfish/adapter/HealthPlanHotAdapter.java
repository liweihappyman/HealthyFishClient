package com.healthyfish.healthyfish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.healthy_management.MyHealthyScheme;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：
 * 作者：WKJ on 2017/8/3.
 * 邮箱：
 * 编辑：WKJ
 */

public class HealthPlanHotAdapter extends RecyclerView.Adapter<HealthPlanHotAdapter.ViewHolder> {
    private Activity activity;
    private Context mContext;
    private List<BeanHotPlanItem> mList = new ArrayList<>();

    public HealthPlanHotAdapter(Activity  activity, List<BeanHotPlanItem> mList) {
        this.mContext = activity;
        this.mList = mList;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hot_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BeanHotPlanItem beanHotPlanItem = mList.get(position);
        holder.title.setText(beanHotPlanItem.getTitle()+"养生计划");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,MyHealthyScheme.class);
                //intent.putExtra("plan",beanHotPlanItem);
                intent.putExtra("position",position);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.layout)
        AutoLinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            ButterKnife.bind(this,itemView);
        }
    }




}
