package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：问诊首页RecyclerView适配器
 * 作者：LYQ on 2017/7/1.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class InterrogationRvAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<String> departments = new ArrayList<>();
    private List<Integer> departmentIcons = new ArrayList<>();

    public InterrogationRvAdapter(Context context, List<String> departmentName, List<Integer> departmentIcon) {
        this.context = context;
        this.departments = departmentName;
        this.departmentIcons = departmentIcon;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_choice_department,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder){
            ((ContentViewHolder)holder).title.setText(departments.get(position));
            Glide.with(context).load(departmentIcons.get(position)).into(((ContentViewHolder) holder).icon);
        }
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public ContentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_department_name);
            icon = (ImageView) itemView.findViewById(R.id.iv_department_img);
            AutoUtils.autoSize(itemView);
        }
    }
}
