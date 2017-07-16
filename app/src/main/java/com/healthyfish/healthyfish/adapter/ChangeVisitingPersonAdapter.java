package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：挂号模块更换就诊人页面ListView适配器，具有单选功能
 * 作者：LYQ on 2017/7/10.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChangeVisitingPersonAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mData = new ArrayList<>();

    public ChangeVisitingPersonAdapter(Context mContext, List<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SingleView singleView = new SingleView(mContext);
        singleView.setTitle(mData.get(position).toString());
        return singleView;
    }

    /**
     * 具有点击自动勾选的布局
     */
    public class SingleView extends LinearLayout implements Checkable {

        @BindView(R.id.tv_name_and_age)
        TextView tvNameAndAge;
        @BindView(R.id.cb_select)
        CheckBox cbSelect;

        public SingleView(Context context) {
            super(context);
            initView(context);
        }

        public View initView(Context context) {
            // 填充布局
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_change_visiting_person_lv, this, true);
            ButterKnife.bind(this, view);
            AutoUtils.autoSize(view);
            return view;
        }

        @Override
        public void setChecked(boolean checked) {
            cbSelect.setChecked(checked);
        }

        @Override
        public boolean isChecked() {
            return cbSelect.isChecked();
        }

        @Override
        public void toggle() {
            cbSelect.toggle();
        }

        public void setTitle(String text) {
            tvNameAndAge.setText(text);
        }
    }
}
