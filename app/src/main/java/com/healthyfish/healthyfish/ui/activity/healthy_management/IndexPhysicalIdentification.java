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
 * 描述：体质辨识首页
 * 作者：Wayne on 2017/7/9 14:50
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */
public class IndexPhysicalIdentification extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_questionnaire_begin_test)
    Button btnQuestionnaireBeginTest;
    @BindView(R.id.btn_infrared_begin_test)
    Button btnInfraredBeginTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_physical_identification);
        ButterKnife.bind(this);

        intiToolbarView();
    }

    // 初始化toolbar
    private void intiToolbarView() {
        toolbarTitle.setText("体质辨识");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
    }

    @OnClick({R.id.btn_questionnaire_begin_test, R.id.btn_infrared_begin_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_questionnaire_begin_test:
                Intent intentQuestionnaire = new Intent(IndexPhysicalIdentification.this, PhyIdeQuestionnaireTest.class);
                startActivity(intentQuestionnaire);
                break;
            case R.id.btn_infrared_begin_test:
                Intent intentInfrared = new Intent(IndexPhysicalIdentification.this, PhyIdeInfraredTest.class);
                startActivity(intentInfrared);
                break;
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
}
