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

import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：获取整体计划
 * 作者：Wayne on 2017/7/10 9:46
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */
public class GetRecommendedHealthyScheme extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_trad_chinese_scheme)
    Button btnTradChineseScheme;
    @BindView(R.id.btn_chronic_disease_scheme)
    Button btnChronicDiseaseScheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_recommended_healthy_scheme);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @OnClick({R.id.btn_trad_chinese_scheme, R.id.btn_chronic_disease_scheme})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_trad_chinese_scheme:
                Intent intent = new Intent(GetRecommendedHealthyScheme.this, SelectHealthyScheme.class);
                startActivity(intent);
                break;
            case R.id.btn_chronic_disease_scheme:
                break;
        }
    }
}
