package com.healthyfish.healthyfish.ui.activity.HealthyManagement;

/**
 * 描述：健康管理首页
 * 作者：Wayne on 2017/7/9 14:46
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainIndexHealthyManagement extends AppCompatActivity {

    SpannableString healthyIdentication;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // 显示体质状态，一共八种
    @BindView(R.id.tv_healthy_identification)
    TextView tvHealthyIdentification;
    @BindView(R.id.tv_add_more_single_plan)
    TextView tvAddMoreSinglePlan;
    @BindView(R.id.btn_total_healthy_scheme)
    Button btnTotalHealthyScheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_index_healthy_management);
        ButterKnife.bind(this);

        intiToolbarView();
        initHealthIdentityView();
        intiTotalHealthyscheme();
        intiSingleHealthyPlan();

    }

    // 初始化toolbar
    private void intiToolbarView() {
        toolbarTitle.setText("我的健康管理");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
    }

    // 初始化体质选项
    private void initHealthIdentityView() {
        healthyIdentication = new SpannableString("体质：阳虚  阴虚  气虚  气郁  血瘀  痰湿  湿热  特禀");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(36);
        healthyIdentication.setSpan(colorSpan, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        healthyIdentication.setSpan(absoluteSizeSpan, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvHealthyIdentification.setText(healthyIdentication);

        tvHealthyIdentification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainIndexHealthyManagement.this, IndexPhysicalIdentification.class);
                startActivity(intent);
            }
        });
    }

    // 初始化整体健康计划
    private void intiTotalHealthyscheme() {

        // 制定整体计划
        btnTotalHealthyScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainIndexHealthyManagement.this, "制定整体计划", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 初始化单项健康计划
    private void intiSingleHealthyPlan() {

        // 添加更多单项计划
        tvAddMoreSinglePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainIndexHealthyManagement.this, "添加更多单项计划", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Toolbar按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
