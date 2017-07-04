package com.healthyfish.healthyfish.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChoiceDoctorLvAdapter;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：按科室分类选择医生
 * 作者：LYQ on 2017/7/2.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChoiceDoctor extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_choice_doctor)
    ListView lvChoiceDoctor;

    private Context mContext;
    private ChoiceDoctorLvAdapter mChoiceDoctorLvAdapter;
    private List<BeanDoctorInfo> mDoctorInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_doctor);
        ButterKnife.bind(this);
        mContext = this;
        initToolBar();
        initData();
        initListView();
        lvListener();
    }

    /**
     * ListView的监听
     */
    private void lvListener() {
        lvChoiceDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到该医生的服务页面,需要传递该医生的唯一标识去服务器访问医生的服务详情
                MyToast.showToast(mContext,mDoctorInfos.get(position).getName());
                Intent intent = new Intent(mContext,ChoiceService.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        mChoiceDoctorLvAdapter = new ChoiceDoctorLvAdapter(mContext, mDoctorInfos);
        lvChoiceDoctor.setAdapter(mChoiceDoctorLvAdapter);
        lvChoiceDoctor.setVerticalScrollBarEnabled(false);//隐藏滚动条，看实际需求
    }

    /**
     * 初始化科室医生数据
     */
    private void initData() {
        for (int i = 0; i < 10; i++) {
            BeanDoctorInfo data = new BeanDoctorInfo();
            data.setImgUrl("http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg");
            data.setName("医生" + i);
            data.setDepartment("中医科");
            data.setDuties("主任医师");
            data.setHospital("柳州市中医院");
            data.setIntroduce("擅长：消化系统疾病。。。。。。。。。。。。。。。。。。。。。。。。。。");
            data.setPrice("10元起");
            mDoctorInfos.add(data);
        }
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
}
