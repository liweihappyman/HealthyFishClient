package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviewMyHealthyScheme extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_banner_healthy_scheme)
    ImageView ivBannerHealthyScheme;
    @BindView(R.id.btn_complete_make_scheme)
    Button btnCompleteMakeScheme;
    @BindView(R.id.collapsing_toolbar_preview_toolbar)
    CollapsingToolbarLayout collapsingToolbarPreviewToolbar;
    @BindView(R.id.appbar_preview_scheme)
    AppBarLayout appbarPreviewScheme;
    @BindView(R.id.togglebtn_remind_scheme)
    ToggleButton togglebtnRemindScheme;
    @BindView(R.id.tv_choose_remind_time)
    TextView tvChooseRemindTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_my_healthy_scheme);
        ButterKnife.bind(this);

        intiToolbarView();
    }

    // 初始化toolbar
    private void intiToolbarView() {
        toolbarTitle.setText("查看养生计划");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
    }

    @OnClick(R.id.btn_complete_make_scheme)
    public void onViewClicked() {
        Intent intent = new Intent(PreviewMyHealthyScheme.this, MyHealthyScheme.class);
        startActivity(intent);
    }
}
