package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanHealthyScheme;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 描述：选择单项、整体养生计划Adapter
 * 作者：Wayne on 2017/7/13 21:56
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class HealthySchemeAdapter extends RecyclerView.Adapter<HealthySchemeAdapter.ViewHolder> {

    Context mContext;
    private List<BeanHealthyScheme> listHealthyScheme;

    public HealthySchemeAdapter(Context mContext, List<BeanHealthyScheme> listHealthyScheme) {
        this.mContext = mContext;
        this.listHealthyScheme = listHealthyScheme;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivBackgroundScheme;
        TextView tvTitleScheme;
        TextView tvContentScheme;
        TextView tvParticipiantScheme;
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            ivBackgroundScheme = (ImageView) itemView.findViewById(R.id.iv_background_healthy_scheme);
            tvTitleScheme = (TextView) itemView.findViewById(R.id.tv_title_healthy_scheme);
            tvContentScheme = (TextView) itemView.findViewById(R.id.tv_content_healthy_scheme);
            tvParticipiantScheme = (TextView) itemView.findViewById(R.id.tv_participant_healthy_scheme);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_healthy_scheme, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BeanHealthyScheme bean = listHealthyScheme.get(position);
        holder.tvTitleScheme.setText(bean.getHealthySchemeTitle());
        holder.tvContentScheme.setText(bean.getHealthySchemeTitle());
        holder.tvTitleScheme.setText(bean.getHealthySchemeContent());
        holder.tvParticipiantScheme.setText(""+ bean.getHealthySchemeParticipant());

        Glide.with(mContext)
                .load(R.mipmap.background_phyide_scheme)
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.error)
                .centerCrop()
                .dontAnimate()
                .into(holder.ivBackgroundScheme);
    }

    @Override
    public int getItemCount() {
        return listHealthyScheme.size();
    }
}
