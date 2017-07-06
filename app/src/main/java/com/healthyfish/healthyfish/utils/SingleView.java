package com.healthyfish.healthyfish.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：具有单选功能的单选框
 * 作者：LYQ on 2017/7/6.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SingleView extends LinearLayout implements Checkable {

    @BindView(R.id.tv_name_gender_age)
    TextView tvNameGenderAge;
    @BindView(R.id.cb_select)
    CheckBox cbSelect;

    public SingleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public SingleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SingleView(Context context) {
        super(context);
        initView(context);
    }

    public View initView(Context context) {
        // 填充布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_choice_document, this, true);
        ButterKnife.bind(this, view);
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
        tvNameGenderAge.setText(text);
    }
}
