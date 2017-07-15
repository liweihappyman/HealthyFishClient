package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChangeVisitingPersonAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：挂号更换就诊人页面
 * 作者：LYQ on 2017/7/10.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChangeVisitingPerson extends BaseActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_change_visiting_person)
    ListView lvChangeVisitingPerson;
    @BindView(R.id.bt_complete)
    Button btComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_visiting_person);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "更换就诊人");
        initListView();
    }


    @OnClick({R.id.tv_new, R.id.bt_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_new:
                //新建就诊人
                Intent intent = new Intent(this, NewVisitingPerson.class);
                startActivity(intent);
                break;
            case R.id.bt_complete:
                //完成就诊人的选择
                break;
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        ChangeVisitingPersonAdapter adapter = new ChangeVisitingPersonAdapter(this, getData());
        lvChangeVisitingPerson.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvChangeVisitingPerson.setAdapter(adapter);
        lvChangeVisitingPerson.setVerticalScrollBarEnabled(false);
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add("就诊人" + i + "    " + 2 + i + "岁");
        }
        return list;
    }

}
