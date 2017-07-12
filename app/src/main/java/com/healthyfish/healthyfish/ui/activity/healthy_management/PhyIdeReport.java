package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：健康管理模块体质确认，体质报告页面
 * 作者：LYQ on 2017/7/11.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PhyIdeReport extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_main_physique)
    TextView tvMainPhysique;
    @BindView(R.id.tv_both_phy_title)
    TextView tvBothPhyTitle;
    @BindView(R.id.tv_both_physique)
    TextView tvBothPhysique;
    @BindView(R.id.tv_introduction_title)
    TextView tvIntroductionTitle;
    @BindView(R.id.tv_describe_title)
    TextView tvDescribeTitle;
    @BindView(R.id.tv_describe_details)
    TextView tvDescribeDetails;
    @BindView(R.id.tv_suggest_title)
    TextView tvSuggestTitle;
    @BindView(R.id.tv_suggest_details)
    TextView tvSuggestDetails;
    @BindView(R.id.bt_save)
    Button btSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phy_report);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"体质报告");

    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        //点击保存按钮
    }
}
