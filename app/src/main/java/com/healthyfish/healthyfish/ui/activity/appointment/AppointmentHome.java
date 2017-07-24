package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.healthyfish.healthyfish.POJO.BeanDoctorListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListReq;

import com.healthyfish.healthyfish.POJO.BeanHospDeptListResp;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListRespItem;

import com.healthyfish.healthyfish.POJO.BeanHospitalListReq;
import com.healthyfish.healthyfish.POJO.BeanUserRetrPresReq;
import com.healthyfish.healthyfish.POJO.BeanUserRetrReptReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.widget.AutoVerticalScrollTextView;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：挂号首页
 * 作者：WKJ on 2017/7/10.
 * 邮箱：
 * 编辑：WKJ
 */


public class AppointmentHome extends AppCompatActivity {
    private String[] strings={"我的剑，就是你的剑!","俺也是从石头里蹦出来得!","我用双手成就你的梦想!","人在塔在!","犯我德邦者，虽远必诛!","我会让你看看什么叫残忍!","我的大刀早已饥渴难耐了!"};

    private int number =0;
    private boolean isRunning=true;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.choose_hospital)
    Button chooseHospital;
    @BindView(R.id.choose_department)
    Button chooseDepartment;
    @BindView(R.id.scroll_message)
    AutoVerticalScrollTextView scrollMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_home);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbarTitle.setText("挂号");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        initSrollText();
    }

    private void initSrollText() {
        scrollMessage.setText(strings[0]);
        new Thread(){
            @Override
            public void run() {
                while (isRunning){
                    SystemClock.sleep(3000);
                    handler.sendEmptyMessage(199);
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 199) {
                scrollMessage.next();
                number++;
                scrollMessage.setText(strings[number%strings.length]);
            }

        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                isRunning=false;
                finish();
                break;
        }
        return true;
    }

    @OnClick({R.id.choose_hospital, R.id.choose_department})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_hospital:
                Intent toChooseHospital = new Intent(this, ChooseHospital.class);
                startActivity(toChooseHospital);
                break;
            case R.id.choose_department:
                Intent test = new Intent(this, SelectDepartments.class);
                startActivity(test);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning=false;
    }
}

