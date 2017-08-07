package com.healthyfish.healthyfish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.healthy_management.MyHealthyScheme;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：
 * 作者：WKJ on 2017/8/6.
 * 邮箱：
 * 编辑：WKJ
 */

public class WholeSchemeAdapter extends RecyclerView.Adapter<WholeSchemeAdapter.ViewHolder> {
    private Activity activity;
    private Context mContext;
    private List<BeanHealthPlanCommendContent> listHealthPlanCommendContent;

    public WholeSchemeAdapter(Activity activity, List<BeanHealthPlanCommendContent> listHealthPlanCommendContent) {
        this.activity = activity;
        this.mContext = activity;
        this.listHealthPlanCommendContent = listHealthPlanCommendContent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.master_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BeanHotPlanItem beanHotPlanItem = JSON.parseObject(listHealthPlanCommendContent.get(position).getMyHealthyPlanItemJsonStr(),BeanHotPlanItem.class);
        holder.tvDetail.setText("全时段"+"  "+beanHotPlanItem.getTitle());
        //統計進度
        int count = 0;
        int current = 0;
        for (int i = 0; i < beanHotPlanItem.getTodoList().size(); i++) {
            if (beanHotPlanItem.getTodoList().get(i).isDone()) {
                current++;
            }
            if (!beanHotPlanItem.getTodoList().get(i).getProgress().equals("nothing")) {
                count++;
            }
        }
        holder.progressbar.setMax(count);
        holder.progressbar.setProgress(current);
        holder.tvProgress.setText("已完成" + current + "/" + count);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyHealthyScheme.class);
                intent.putExtra("id",listHealthPlanCommendContent.get(position).getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listHealthPlanCommendContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_detail)
        TextView tvDetail;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.progressbar)
        ProgressBar progressbar;
        @BindView(R.id.layout)
        AutoLinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
