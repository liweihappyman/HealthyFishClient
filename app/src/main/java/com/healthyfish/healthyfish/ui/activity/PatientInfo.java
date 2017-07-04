package com.healthyfish.healthyfish.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：患者信息页面
 * 作者：WKJ on 2017/7/4.
 * 邮箱：
 * 编辑：WKJ
 */
public class PatientInfo extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.gender)
    EditText gender;
    @BindView(R.id.birthday)
    EditText birthday;
    @BindView(R.id.id_number)
    EditText idNumber;
    @BindView(R.id.occupation)
    EditText occupation;
    @BindView(R.id.unmarried)
    RadioButton unmarried;
    @BindView(R.id.married)
    RadioButton married;
    @BindView(R.id.marital_status)
    RadioGroup maritalStatus;
    @BindView(R.id.save)
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        ButterKnife.bind(this);
        toolbarTitle.setText("患者信息");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
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

}
