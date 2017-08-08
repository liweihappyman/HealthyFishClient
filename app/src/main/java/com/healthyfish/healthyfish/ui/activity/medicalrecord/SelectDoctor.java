package com.healthyfish.healthyfish.ui.activity.medicalrecord;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.SelectDoctorAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectDoctor extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.doctor_list)
    RecyclerView doctorList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbarTitle.setText("选择医生");
        //toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.three_points));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        initData();
    }

    private void initData() {
        List<BeanDoctorInfo> list = new ArrayList<>();
        BeanDoctorInfo b1 = new BeanDoctorInfo();
        b1.setName("隔壁老王");
        b1.setDepartment("中医科");
        b1.setHospital("柳州市人民医院");
        b1.setIntroduce("厉害了");
        b1.setImgUrl("http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg");
        BeanDoctorInfo b2 = new BeanDoctorInfo();
        b2.setName("隔壁老王");
        b2.setDepartment("中医科");
        b2.setHospital("柳州市人民医院");
        b2.setIntroduce("厉害了");
        b2.setImgUrl("http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg");
        BeanDoctorInfo b3 = new BeanDoctorInfo();
        b3.setName("隔壁老王");
        b3.setDepartment("中医科");
        b3.setHospital("柳州市人民医院");
        b3.setIntroduce("厉害了");
        b3.setImgUrl("http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg");
        BeanDoctorInfo b4 = new BeanDoctorInfo();
        b4.setName("隔壁老王");
        b4.setDepartment("中医科");
        b4.setHospital("柳州市人民医院");
        b4.setIntroduce("厉害了");
        b4.setImgUrl("http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg");
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        doctorList.setLayoutManager(lmg);
        SelectDoctorAdapter adapter = new SelectDoctorAdapter(this,list);
        doctorList.setAdapter(adapter);


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
