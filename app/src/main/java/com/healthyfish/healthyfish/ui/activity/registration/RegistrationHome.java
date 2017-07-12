package com.healthyfish.healthyfish.ui.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 描述：挂号首页
 * 作者：WKJ on 2017/7/10.
 * 邮箱：
 * 编辑：WKJ
 */


public class RegistrationHome extends AppCompatActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.choose_hospital)
    Button chooseHospital;
    @BindView(R.id.choose_department)
    Button chooseDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_home);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbarTitle.setText("挂号");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
// 测试获取后面的日期是星期几
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");//设置想要的日期格式
//        String [] str = {"","星期日","星期一","星期二","星期三","星期四","星期五","星期六",};
//        Calendar calendar = Calendar.getInstance();//获取日历实例
//        calendar.getTime();
//        Log.i("testdate",str[calendar.get(Calendar.DAY_OF_WEEK)]);
//        calendar.add(Calendar.DATE, 1);
//        Log.i("testdate",str[calendar.get(Calendar.DAY_OF_WEEK)]);
//        calendar.add(Calendar.DATE, 1);
//        Log.i("testdate",str[calendar.get(Calendar.DAY_OF_WEEK)]);
//        calendar.add(Calendar.DATE, 1);
//        Log.i("testdate",str[calendar.get(Calendar.DAY_OF_WEEK)]);
//        calendar.add(Calendar.DATE, 1);
//        Log.i("testdate",str[calendar.get(Calendar.DAY_OF_WEEK)]);
//        calendar.add(Calendar.DATE, 1);
//        Log.i("testdate",str[calendar.get(Calendar.DAY_OF_WEEK)]);
//        calendar.add(Calendar.DATE, 1);
//        Log.i("testdate",str[calendar.get(Calendar.DAY_OF_WEEK)]);
//
//        String date = dateFormat.format(calendar.getTime());//获取日期格式；
//        Log.i("dateTest",date);
    }
    //直接通过日期获取是星期几
    private String getWeekFromDate(){
        Calendar calendar = Calendar.getInstance();//获取日历实例
        Date date1 = calendar.getTime();//获取当前的时间
        calendar.setTime(date1);//将获取的日期设置成当前的日期
        int number = calendar.get(Calendar.DAY_OF_WEEK);
        /*
        *获取当前日期是星期几
        * 经测试，星期天是1，星期三是4，星期六是7
        */
        String [] str = {"","星期日","星期一","星期二","星期三","星期四","星期五","星期六",};
        Log.i("testdate",str[number]);
        return str[number];
    }
    //将字符串的日期转化为真正的日期格式，获取该日期是星期几,格式如 ： 2017年7月30日
    private String getWeekFromStr(String dateStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");//设置想要的日期格式
        Calendar calendar = Calendar.getInstance();//获取日历实例
        Date date = dateFormat.parse(dateStr, new ParsePosition(0));//反向操作，将字符格式的转化为日期格式
        calendar.setTime(date);//将转化回来的日期设置成当前的日期
        int number = calendar.get(Calendar.DAY_OF_WEEK);
        /*
        *获取当前日期是星期几
        * 经测试，number显示： 星期天是1，星期三是4，星期六是7
        */
        String [] str = {"","星期日","星期一","星期二","星期三","星期四","星期五","星期六",};
        Log.i("testdate",str[number]);
        return str[number];
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
    @OnClick({R.id.choose_hospital, R.id.choose_department})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_hospital:
                Intent toChooseHospital = new Intent(this,ChooseHospital.class);
                startActivity(toChooseHospital);
                break;
            case R.id.choose_department:
                Intent test = new Intent(this,SelectDepartments.class);
                startActivity(test);
                break;
        }
    }
}

