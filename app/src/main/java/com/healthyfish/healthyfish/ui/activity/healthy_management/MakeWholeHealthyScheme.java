package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 描述：定制整体计划
 * 作者：Wayne on 2017/7/9 16:31
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */
public class MakeWholeHealthyScheme extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_get_recommended_scheme)
    Button btnGetRecommendedScheme;
    @BindView(R.id.btn_get_user_custom_scheme)
    Button btnGetUserCustomScheme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_total_healthy_scheme);
        ButterKnife.bind(this);

        intiToolbarView();

    }

    // 初始化toolbar
    private void intiToolbarView() {
        toolbarTitle.setText("定制整体计划");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
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

    @OnClick({R.id.btn_get_recommended_scheme, R.id.btn_get_user_custom_scheme})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_recommended_scheme:
                Intent intentRecommend = new Intent(MakeWholeHealthyScheme.this, GetRecommendedHealthyScheme.class);
                startActivity(intentRecommend);
                break;
            case R.id.btn_get_user_custom_scheme:
                Intent intentCustom = new Intent(MakeWholeHealthyScheme.this, GetUserCustomScheme.class);
                startActivity(intentCustom);
                break;
        }
    }


}
