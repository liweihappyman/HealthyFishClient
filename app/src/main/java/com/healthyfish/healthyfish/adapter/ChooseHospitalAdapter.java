package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * 描述：医院列表适配器
 * 作者：WKJ on 2017/7/10.
 * 邮箱：
 * 编辑：WKJ
 */

public class ChooseHospitalAdapter extends RecyclerView.Adapter<ChooseHospitalAdapter.ViewHolder>{
    private List<String> imgUrls;//测试数据list
    private Context mContext;

    public ChooseHospitalAdapter(List<String> imgUrls, Context mContext) {
        this.imgUrls = imgUrls;
        this.mContext = mContext;
    }

     @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_choose_hospital,parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = imgUrls.get(position);
        Glide.with(mContext).load(url).placeholder(R.mipmap.placeholder)
                .error(R.mipmap.error).centerCrop().into(holder.hospital);
    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView hospital;
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            hospital = (ImageView) itemView.findViewById(R.id.hospital);
        }
    }
}
