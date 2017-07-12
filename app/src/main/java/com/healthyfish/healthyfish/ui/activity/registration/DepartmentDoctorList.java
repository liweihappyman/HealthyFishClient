package com.healthyfish.healthyfish.ui.activity.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanDepartmentDoctor;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChoiceDoctorLvAdapter;
import com.healthyfish.healthyfish.adapter.DepartmentDoctorLvAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：挂号模块科室医生列表页面
 * 作者：LYQ on 2017/7/10.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class DepartmentDoctorList extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_department_doctor_list)
    ListView lvDepartmentDoctorList;

    private String departmentName = "选择医生";

    private Context mContext;
    private ChoiceDoctorLvAdapter mChoiceDoctorLvAdapter;
    private List<BeanDepartmentDoctor> mDoctorInfos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_doctor_list);
        mContext = this;
        ButterKnife.bind(this);
        getDepartmentName();
        initToolBar(toolbar,toolbarTitle,departmentName);
        initData();
        initListView();
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        DepartmentDoctorLvAdapter adapter = new DepartmentDoctorLvAdapter(mContext, mDoctorInfos);
        lvDepartmentDoctorList.setAdapter(adapter);
        lvDepartmentDoctorList.setVerticalScrollBarEnabled(false);
        lvDepartmentDoctorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入到医生详情页面，进行预约时间的选择
                MyToast.showToast(DepartmentDoctorList.this,mDoctorInfos.get(position).getName());
                Intent test = new Intent(DepartmentDoctorList.this,DoctorDetail.class);
                startActivity(test);
            }
        });
    }

    /**
     * 获取上一页面传过来的科室名
     */
    private void getDepartmentName() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            departmentName = bundle.get("DepartmentName").toString();
        }
    }

    /**
     * 初始化科室医生数据
     */
    private void initData() {
        for (int i = 0; i < 5; i++) {
            BeanDepartmentDoctor data = new BeanDepartmentDoctor();
            data.setImgUrl("http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg");
            data.setName("医生" + i);
            data.setDepartment("中医科");
            data.setDuties("主任医师");
            data.setHospital("柳州市中医院");
            data.setIntroduce("擅长：消化系统疾病。。。。。。。。。。。。。。。。。。。。。。。。。。");
            data.setAvailable(true);
            mDoctorInfos.add(data);
        }
        for (int i = 0; i < 5; i++) {
            BeanDepartmentDoctor data = new BeanDepartmentDoctor();
            data.setImgUrl("http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg");
            data.setName("医生" + i);
            data.setDepartment("中医科");
            data.setDuties("主任医师");
            data.setHospital("柳州市中医院");
            data.setIntroduce("擅长：消化系统疾病。。。。。。。。。。。。。。。。。。。。。。。。。。");
            data.setAvailable(false);
            mDoctorInfos.add(data);
        }
    }
}
