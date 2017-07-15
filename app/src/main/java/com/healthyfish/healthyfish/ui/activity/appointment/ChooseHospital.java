package com.healthyfish.healthyfish.ui.activity.appointment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChooseHospitalAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 描述：选择医院列表页面
 * 作者：WKJ on 2017/7/10.
 * 邮箱：
 * 编辑：WKJ
 */
public class ChooseHospital extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.choose_hospital_recyclerview)
    RecyclerView chooseHospitalRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_hospital);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbarTitle.setText("选择医院");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        initData();
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");

        LinearLayoutManager lmg = new LinearLayoutManager(this);
        chooseHospitalRecyclerview.setLayoutManager(lmg);
        ChooseHospitalAdapter adapter = new ChooseHospitalAdapter(list,this);
        chooseHospitalRecyclerview.setAdapter(adapter);



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
}
