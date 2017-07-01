package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanHealthPlanItemTest;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 描述：
 * 作者： TMXK on 2017/6/30.
 */

public class HealthPlanAdapter extends RecyclerView.Adapter<HealthPlanAdapter.ViewHolder>{
    private Context mContext;
    private List<BeanHealthPlanItemTest> listHealthPlan;

    public HealthPlanAdapter(Context mContext, List<BeanHealthPlanItemTest> listHealthPlan) {
        this.mContext = mContext;
        this.listHealthPlan = listHealthPlan;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_health_plan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BeanHealthPlanItemTest healthPlanItem = listHealthPlan.get(position);
        holder.title.setText(healthPlanItem.getTitle());
        holder.progressAndContent.setText("第"+healthPlanItem.getProgress()+"天"
                +"  "+healthPlanItem.getTodo());
        String progress = healthPlanItem.getProgress();
        String currentProgress = progress.substring(0,progress.indexOf("/"));
        String maxProgress = progress.substring(progress.indexOf("/")+1,progress.length()-1);
        holder.progressbar.setMax(Integer.parseInt(currentProgress));
        holder.progressbar.setProgress(Integer.parseInt(maxProgress));
        if (healthPlanItem.isDone()){
            holder.todo.setText("已完成");
            holder.todo.setTextColor(0xffcfcfcf);
            holder.todo.setBackgroundResource(R.drawable.gray_rounded_rectangle);
        }else {
            holder.todo.setText("未完成");
            holder.todo.setTextColor(0xffffffff);
            holder.todo.setBackgroundResource(R.drawable.green_rounded_rectangle);
        }
    }

    @Override
    public int getItemCount() {
        return listHealthPlan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView progressAndContent;
        private ProgressBar progressbar;
        private TextView todo;
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            title = (TextView) itemView.findViewById(R.id.item_health_plan_title);
            progressAndContent = (TextView) itemView.findViewById(R.id.item_health_plan_pro_content);
            progressbar = (ProgressBar) itemView.findViewById(R.id.item_health_plan_progressbar);
            todo = (TextView) itemView.findViewById(R.id.item_health_plan_todo);
        }
    }
}
