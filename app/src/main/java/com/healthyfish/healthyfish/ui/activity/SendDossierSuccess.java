package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.healthyfish.healthyfish.MainActivity;
import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：问诊模块发送病历夹成功页面
 * 作者：LYQ on 2017/7/6.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SendDossierSuccess extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_back_home)
    TextView tvBackHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_medical_success);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"发送成功");
    }

    @OnClick(R.id.tv_back_home)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
