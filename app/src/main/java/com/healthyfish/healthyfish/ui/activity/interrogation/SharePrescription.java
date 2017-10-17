package com.healthyfish.healthyfish.ui.activity.interrogation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanDoctorChatInfo;
import com.healthyfish.healthyfish.POJO.BeanInspectionReport;
import com.healthyfish.healthyfish.POJO.BeanPrescriptiom;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.SelectInspectionReportAdapter;
import com.healthyfish.healthyfish.adapter.SharePrescriptionRvAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SharePrescription extends AppCompatActivity {
    private String phone;//手机号码
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.doctor_portrait)
    CircleImageView doctorPortrait;
    @BindView(R.id.name_doctor)
    TextView nameDoctor;
    @BindView(R.id.prescription_rv)
    RecyclerView prescriptionRv;
    @BindView(R.id.share_tv)
    TextView shareTv;
    private BeanDoctorChatInfo beanDoctorChatInfo;//医生信息
    // 医生头像
    private String mDoctorPortrait;
    // 医生姓名
    private String doctorName;
    List<BeanPrescriptiom> mList = new ArrayList<>();
    SharePrescriptionRvAdapter adapter;
    private List<String> mListKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_prescription);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbarTitle.setText("选择处方");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        initChatInfo();
        initData();
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


    // 初始化聊天信息
    private void initChatInfo() {
        beanDoctorChatInfo = (BeanDoctorChatInfo) getIntent().getSerializableExtra("BeanDoctorChatInfo");
        mDoctorPortrait = beanDoctorChatInfo.getImgUrl();
        doctorName = beanDoctorChatInfo.getName();
    }


    private void initData() {
        //医生信息
        Glide.with(SharePrescription.this).load(mDoctorPortrait).centerCrop().into(doctorPortrait);
        nameDoctor.setText(doctorName);
        phone = beanDoctorChatInfo.getPhone();
        mList = DataSupport.findAll(BeanPrescriptiom.class);

        if (mList.size()>0) {
            Collections.reverse(mList);//倒序
            List<Map<String,Boolean>> listIsSelect = new ArrayList<>();//是否选中标志位
            for (int i = 0; i < mList.size(); i++) {
                Map<String,Boolean> isSelect = new HashMap<>();
                isSelect.put("isSelect",false);
                listIsSelect.add(isSelect);
            }
            adapter = new SharePrescriptionRvAdapter(this, mList, listIsSelect, toolbar, new SharePrescriptionRvAdapter.SelPrescriptionListener() {
                @Override
                public void getSelKeys(List<String> listKeys) {
                    mListKeys = listKeys;
                    Log.i("获取的key", listKeys.size()+"");
                }
            });
            LinearLayoutManager lmg = new LinearLayoutManager(this);
            prescriptionRv.setLayoutManager(lmg);
            prescriptionRv.setAdapter(adapter);
        }

        shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListKeys.size()>0) {
                    // 发送病历
                    Intent share = new Intent(SharePrescription.this, HealthyChat.class);
                    Log.e("获取的key", ""+mListKeys.size());
                    share.putStringArrayListExtra("prescriptionKeyList", (ArrayList<String>) mListKeys);
                    setResult(RESULT_OK,share);//通知发送病历夹后更聊天界面的UI
                    finish();
                }else {
                    Toast.makeText(SharePrescription.this,"还没有选择处方哟",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
