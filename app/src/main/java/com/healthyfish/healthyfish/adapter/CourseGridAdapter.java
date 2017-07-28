package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：新建病历页面的图片显示适配器
 * 作者：WKJ on 2017/7/5.
 * 邮箱：
 * 编辑：WKJ
 */

public class CourseGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> listPaths;

    public CourseGridAdapter(List<String> listPaths, Context mContext) {
        this.listPaths = listPaths;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return listPaths.size() ;
    }//修改

    @Override
    public String getItem(int position) {
        return listPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, null);
            imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(imageView);
            AutoUtils.auto(convertView);

        } else {
            imageView = (ImageView) convertView.getTag();
        }

        Glide.with(mContext)
                .load(new File(getItem(position)))
                .placeholder(R.mipmap.default_error)
                .error(R.mipmap.default_error)
                .centerCrop()
                .crossFade()
                .into(imageView);


        return convertView;
    }
}
