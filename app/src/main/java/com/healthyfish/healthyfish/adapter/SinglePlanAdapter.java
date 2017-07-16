package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanHealthPlanItemTest;
import com.healthyfish.healthyfish.POJO.BeanSinglePlan;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;


/**
 * 描述：单项计划
 * 作者：WKJ on 2017/7/16.
 * 邮箱：
 * 编辑：WKJ
 */

public class SinglePlanAdapter extends RecyclerView.Adapter<SinglePlanAdapter.ViewHolder>{
    private Context mContext;
    private List<BeanSinglePlan> mList;

    public SinglePlanAdapter(Context mContext, List<BeanSinglePlan> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_single_plan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BeanSinglePlan item = mList.get(position);
        holder.type.setText(item.getType());
        holder.title.setText(item.getTitle());
        holder.progress.setText("已完成"+item.getProgress()+"次");
        if (item.isTodo()){
            holder.todo.setText("已完成");
            holder.todo.setTextColor(0xffcfcfcf);
            holder.todo.setBackgroundResource(R.drawable.gray_rounded_rectangle);
        }else {
            holder.todo.setText("去完成");
            //holder.todo.setTextColor(0xffffffff);
            holder.todo.setBackgroundResource(R.drawable.selector_4dp_btn_color_from_priamry_to_white);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView type;
        private TextView title;
        private TextView progress;
        private Button todo;
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            type = (TextView) itemView.findViewById(R.id.tv_type);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            progress = (TextView) itemView.findViewById(R.id.tv_progress);
            todo = (Button) itemView.findViewById(R.id.btn_todo);
        }
    }
}
