package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanHealthWorkShop;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/*
 * 描述：首页健康工坊适配器
 * 作者：Wayne on 2017/7/11 11:20
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
*/


public class HomePageHealthWorkShopAdapter extends RecyclerView.Adapter<HomePageHealthWorkShopAdapter.ViewHolder> {

    Context mContext;
    private int mPosition;
    private List<BeanHealthWorkShop> listHealthWorkShop;

    public HomePageHealthWorkShopAdapter(Context mContext, List<BeanHealthWorkShop> listHealthWorkShop) {
        this.mContext = mContext;
        this.listHealthWorkShop = listHealthWorkShop;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageWorkShopCommodity;
        TextView nameWorkShopCommodity;
        ImageView imageHotSale;
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            imageWorkShopCommodity = (ImageView) itemView.findViewById(R.id.image_work_shop_commodity);
            nameWorkShopCommodity = (TextView) itemView.findViewById(R.id.name_work_shop_commodity);
            imageHotSale = (ImageView) itemView.findViewById(R.id.whether_hot_sale);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_page_health_work_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mPosition = position;
        BeanHealthWorkShop bean = listHealthWorkShop.get(position);
        holder.nameWorkShopCommodity.setText(bean.getNameCommodity());
        Glide.with(mContext)
                .load(R.mipmap.image_home_page_work_shop)
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.error)
                .centerCrop()
                .dontAnimate()
                .into(holder.imageWorkShopCommodity);

        if (bean.isHotSale()) {
            holder.imageHotSale.setVisibility(View.VISIBLE);
        } else {
            holder.imageHotSale.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listHealthWorkShop.size();
    }

}
