package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.healthyfish.healthyfish.POJO.BeanHealthyCircleItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.utils.MyToast;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：健康圈我关注的社区页面的适配器
 * 作者：LYQ on 2017/7/9.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class MyCommunityAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BeanHealthyCircleItem> mList = new ArrayList<>();

    public MyCommunityAdapter(Context mContext, List<BeanHealthyCircleItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    public MyCommunityAdapter() {
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sly_my_community;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.item_my_community, null);
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final Holder holder = new Holder(convertView);
        Glide.with(mContext).load(mList.get(position).getImgUrl()).into(holder.civCommunityImg);
        holder.tvCommunityName.setText(mList.get(position).getTitle());
        holder.tvCommunityContent.setText(mList.get(position).getContent());

        holder.slyMyCommunity.addSwipeListener(new SimpleSwipeListener(){
            @Override
            public void onOpen(SwipeLayout layout) {
                super.onOpen(layout);
                holder.slyMyCommunity.setClickToClose(true);
            }
        });

        holder.rlyMyCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.showToast(mContext,mList.get(position).getTitle());
            }
        });

        holder.rlyMyCommunity.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.slyMyCommunity.open();
                return true;
            }
        });

        holder.tvCancelAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.slyMyCommunity.close();
                mList.remove(position);
                MyCommunityAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {

        @BindView(R.id.tv_cancel_attention)
        TextView tvCancelAttention;
        @BindView(R.id.civ_community_img)
        CircleImageView civCommunityImg;
        @BindView(R.id.tv_community_name)
        TextView tvCommunityName;
        @BindView(R.id.tv_community_content)
        TextView tvCommunityContent;
        @BindView(R.id.rly_my_community)
        RelativeLayout rlyMyCommunity;
        @BindView(R.id.sly_my_community)
        SwipeLayout slyMyCommunity;

        public Holder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
