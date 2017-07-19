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

import static com.healthyfish.healthyfish.constant.constants.HttpHealthyFishyUrl;

/**
 * 描述：
 * 作者： TMXK on 2017/6/30.
 */

public class HomePageHealthInfoAadpter extends RecyclerView.Adapter<HomePageHealthInfoAadpter.ViewHolder> {

    private View headView;
    private View footView;
    public static final int HEAD = 1;
    public static final int NORMAL = 2;
    public static final int FOOT = 3;

    private Context mContext;
    //private int mPosition;
    private List<BeanItemNewsAbstract> listNews;

    public HomePageHealthInfoAadpter(Context mContext, List<BeanItemNewsAbstract> listNews) {
        this.mContext = mContext;
        this.listNews = listNews;
    }

    public void addData(BeanItemNewsAbstract data) {
        listNews.add(data);

    }

    public void removeData(int position) {
        listNews.remove(position);
    }

    public void addHeadView(View headView) {
        this.headView = headView;
    }

    public void addFootView(View footView) {
        this.footView = footView;
    }

    public int getHeadViewCount() {
        return headView == null ? 0 : 1;
    }

    public int getFootViewCount() {
        return footView == null ? 0 : 1;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == HEAD) {
            return new ViewHolder(headView);
        }
        if (viewType == FOOT) {
            return new ViewHolder(footView);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_page_health_info_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //mPosition = position;
        if (getItemViewType(position) == HEAD) {
            return;
        }
        if (getItemViewType(position) == FOOT) {
            return;
        }
        BeanItemNewsAbstract beanItemNews = listNews.get(position-getHeadViewCount());
        holder.title.setText(beanItemNews.getTitle());
        holder.category.setText(beanItemNews.getCategory());
        holder.timestamp.setText(beanItemNews.getTimestamp().substring(0, 10));

            Glide.with(mContext)
                    .load(HttpHealthyFishyUrl+beanItemNews.getImg())
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.error)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return listNews.size() + getHeadViewCount() + getFootViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeadViewCount()) {
            return HEAD;
        }
        if (position >= listNews.size() + getHeadViewCount()) {
            return FOOT;
        }
        return NORMAL;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img; //标题图片
        private TextView title; //标题
        private TextView category;//分类
        private TextView timestamp; //时间
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            if (itemView == headView) {
                return;
            }
            if (itemView == footView) {
                return;
            }
            img = (ImageView) itemView.findViewById(R.id.item_health_news_img);
            title = (TextView) itemView.findViewById(R.id.item_health_news_title);
            category = (TextView) itemView.findViewById(R.id.item_health_news_category);
            timestamp = (TextView) itemView.findViewById(R.id.item_health_news_timestamp);
        }
    }
}
