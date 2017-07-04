package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.widget.DatePickerDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：电子病历
 * 作者：WKJ on 2017/7/4.
 * 邮箱：
 * 编辑：WKJ
 */
public class NewMedRec extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lable)
    TextView lable;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.patient_info)
    TextView patientInfo;
    @BindView(R.id.clinical_time)
    TextView clinicalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med_rec);
        ButterKnife.bind(this);
        toolbarTitle.setText("新建病历");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        initListener();
        initdata();

    }

    private void initdata() {
        initTime();
    }



    /*
    * 初始化监听
    * */
    private void initListener() {
        lable.setOnClickListener(this);
        patientInfo.setOnClickListener(this);
        clinicalTime.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lable:
                Intent toLable = new Intent(this, Lable.class);
                startActivity(toLable);
                break;
            case R.id.patient_info:
                Intent toPatientInfo = new Intent(this, PatientInfo.class);
                startActivity(toPatientInfo);
                break;
            case R.id.clinical_time:
                selectTime();
                break;
        }
    }
    //时间选择对话框
    private void selectTime() {
        DatePickerDialog datePicker_dialog = new DatePickerDialog(this, new
                DatePickerDialog.MyListener() {
                    @Override
                    public void refreshUI(String string) {
                        clinicalTime.setText(string);
                    }
                });
        datePicker_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePicker_dialog.show();
    }

    private void initTime() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        clinicalTime.setText(year + "年" + (month + 1) + "月" + day + "日");

    }
}
