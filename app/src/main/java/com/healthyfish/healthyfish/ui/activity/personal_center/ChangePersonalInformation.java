package com.healthyfish.healthyfish.ui.activity.personal_center;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：个人中心>个人信息>修改个人信息页面
 * 作者：LYQ on 2017/7/29.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChangePersonalInformation extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_personal_information);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"修改个人信息");
    }

    @OnClick(R.id.tv_save)
    public void onViewClicked() {
    }
}
