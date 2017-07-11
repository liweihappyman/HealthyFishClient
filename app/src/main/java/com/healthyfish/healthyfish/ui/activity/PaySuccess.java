package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：送心意支付成功页面
 * 作者：LYQ on 2017/7/5.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PaySuccess extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_pay_success)
    Button btnPaySuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        ButterKnife.bind(this);
        initToolBar(toolbar,tvTitle,"支付成功");
    }

    @OnClick(R.id.btn_pay_success)
    public void onViewClicked() {
        Intent intent = new Intent(this,InterrogationService.class);
        startActivity(intent);
    }

}
