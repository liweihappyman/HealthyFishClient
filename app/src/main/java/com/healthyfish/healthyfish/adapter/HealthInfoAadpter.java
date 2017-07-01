package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanItemNewsAbstract;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;

/**
 * 描述：
 * 作者： TMXK on 2017/6/30.
 */

public class HealthInfoAadpter extends RecyclerView.Adapter<HealthInfoAadpter.ViewHolder> {
    private Context mContext;
    private int mPosition;
    private List<BeanItemNewsAbstract> listNews;

    public HealthInfoAadpter(Context mContext, List<BeanItemNewsAbstract> listNews) {
        this.mContext = mContext;
        this.listNews = listNews;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_health_info_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mPosition = position;
        BeanItemNewsAbstract beanItemNews = listNews.get(position);
        holder.title.setText(beanItemNews.getTitle());
        holder.category.setText(beanItemNews.getCategory());
        holder.timestamp.setText(beanItemNews.getTimestamp());

            Glide.with(mContext)
                    .load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.error)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.img);

        //防止图片加载错位
//        if (holder.img.getTag() == beanItemNews.getImg()){
//            Glide.with(mContext)
//                    .load(beanItemNews.getImg())
//                    .placeholder(R.mipmap.placeholder)
//                    .error(R.mipmap.error)
//                    .centerCrop()
//                    .dontAnimate()
//                    .into(holder.img);
//        }
    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img; //标题图片
        private TextView title; //标题
        private TextView category;//分类
        private TextView timestamp; //时间
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_health_news_img);
            title = (TextView) itemView.findViewById(R.id.item_health_news_title);
            category = (TextView) itemView.findViewById(R.id.item_health_news_category);
            timestamp = (TextView) itemView.findViewById(R.id.item_health_news_timestamp);
            img.setTag(listNews.get(mPosition).getImg());
        }
    }

}
