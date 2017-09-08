package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.eventbus.NoticeMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SinglePlanDetail extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.plan_type)
    TextView planType;
    @BindView(R.id.tv_complete_times)
    TextView tvCompleteTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_plan_detail);
        ButterKnife.bind(this);
        intiToolbarView();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    // 初始化toolbar
    private void intiToolbarView() {
        toolbarTitle.setText("单项养生计划");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        planType.setText(MyHealthyScheme.mTitle+"养生计划");
        tvCompleteTimes.setText("已完成"+MyHealthyScheme.mCurrent+"次");
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new NoticeMessage(1));
                setResult(110);
                finish();
            }
        });
    }
}
