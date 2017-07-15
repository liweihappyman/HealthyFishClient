package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanHealthyCircleItem;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：健康圈所有社区列表适配器
 * 作者：LYQ on 2017/7/9.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class AllCommunityAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BeanHealthyCircleItem> mList = new ArrayList();

    public AllCommunityAdapter(Context mContext, List<BeanHealthyCircleItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.item_healthy_community, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(mList.get(position).getImgUrl()).into(holder.civCommunityImg);
        holder.tvCommunityName.setText(mList.get(position).getTitle());
        holder.tvCommunityContent.setText(mList.get(position).getContent());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.civ_community_img)
        CircleImageView civCommunityImg;
        @BindView(R.id.tv_community_name)
        TextView tvCommunityName;
        @BindView(R.id.tv_community_content)
        TextView tvCommunityContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
