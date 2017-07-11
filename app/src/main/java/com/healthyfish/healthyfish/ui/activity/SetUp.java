package com.healthyfish.healthyfish.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：个人中心设置页面
 * 作者：LYQ on 2017/7/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SetUp extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"设置");
    }
}
