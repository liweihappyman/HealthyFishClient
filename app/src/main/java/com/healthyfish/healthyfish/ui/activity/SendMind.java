package com.healthyfish.healthyfish.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：送心意页面
 * 作者：LYQ on 2017/7/3.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SendMind extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.civ_doctor_img)
    CircleImageView civDoctorImg;
    @BindView(R.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R.id.rbt_five_yuan)
    RadioButton rbtFiveYuan;
    @BindView(R.id.rbt_ten_yuan)
    RadioButton rbtTenYuan;
    @BindView(R.id.rbt_fifteen_yuan)
    RadioButton rbtFifteenYuan;
    @BindView(R.id.rbt_twenty_yuan)
    RadioButton rbtTwentyYuan;
    @BindView(R.id.rbt_more_price)
    RadioButton rbtMorePrice;
    @BindView(R.id.rgp_choice_figure)
    RadioGroup rgpChoiceFigure;
    @BindView(R.id.et_thinks)
    EditText etThinks;
    @BindView(R.id.bt_commit)
    Button btCommit;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mind);
        ButterKnife.bind(this);
        initToolBar();
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        toolbar.setTitle("");//设置不显示应用名
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
    }

    /**
     * 返回按钮的监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick({R.id.rbt_more_price, R.id.bt_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rbt_more_price:
                break;
            case R.id.bt_commit:
                break;
        }
    }
}
