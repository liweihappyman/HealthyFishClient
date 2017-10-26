package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanHomeSearchResult;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：首页搜索结果的适配器
 * 作者：LYQ on 2017/10/25.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class HomeSearchResultLvAdapter extends BaseAdapter {

    private Context mContext;
    private List<BeanHomeSearchResult> resultList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public HomeSearchResultLvAdapter(Context mContext, List<BeanHomeSearchResult> resultList) {
        this.mContext = mContext;
        this.resultList = resultList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_home_search_result, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvSrTitle.setText(resultList.get(position).getTitle());
        holder.tvSrContent.setText(resultList.get(position).getValue());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_sr_title)
        TextView tvSrTitle;
        @BindView(R.id.tv_sr_content)
        TextView tvSrContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
