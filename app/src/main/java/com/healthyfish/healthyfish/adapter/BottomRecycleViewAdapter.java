package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * 描述：
 * 作者：LYQ on 2017/7/1.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public abstract class BottomRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public enum ITEM_TYPE {
        ITEM_TYPE_CONTENT,
        ITEM_TYPE_BOTTOM
    }

    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected int mBottomCount;//底部View个数

    public BottomRecycleViewAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void setBottomCount(int bottomCount) {
        this.mBottomCount = bottomCount;
    }


    public boolean isBottomView(int position) {
        return mBottomCount != 0 && position >= (getContentItemCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal()) {
            return onCreateContentView(parent);
        } else if (viewType == ITEM_TYPE.ITEM_TYPE_BOTTOM.ordinal()) {
            return onCreateBottomView(parent);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mBottomCount != 0 && position >= (dataItemCount)) {//底部View
            return ITEM_TYPE.ITEM_TYPE_BOTTOM.ordinal();
        } else {
            return ITEM_TYPE.ITEM_TYPE_CONTENT.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return getContentItemCount() + mBottomCount;
    }

    public abstract RecyclerView.ViewHolder onCreateContentView(ViewGroup parent);//创建中间内容View

    public abstract RecyclerView.ViewHolder onCreateBottomView(ViewGroup parent);//创建底部View

    public abstract int getContentItemCount();//获取中间内容个数
}
