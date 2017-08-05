package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanSelectDate;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.CustomSchemeSelectDateGvAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：自定义计划
 * 作者：Wayne on 2017/7/10 9:46
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class GetUserCustomScheme extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gv_week_title)
    GridView gvWeekTitle;
    @BindView(R.id.gv_date)
    GridView gvDate;
    @BindView(R.id.tv_selected_date_number)
    TextView tvSelectedDateNumber;
    @BindView(R.id.bt_next)
    Button btNext;

    public final int mesFlag = 102;
    private CustomSchemeSelectDateGvAdapter mSelectDateAdapter;
    private int selectedNumber = 0;

    /**
     * 显示已选日期数
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case mesFlag:
                    setSelectedDateNumber(msg.arg1);
                    selectedNumber = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_custom_scheme);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "选择养生日");
        initGvDateTitle();
        initGvSelectDate();
        setSelectedDateNumber(0);
    }

    /**
     * 初始化选择日期的头部
     */
    private void initGvDateTitle() {
        String[] strTitle = new String[]{"一", "二", "三", "四", "五", "六", "天"};
        String[] from = new String[]{"title"};
        int[] to = new int[]{R.id.tv_week_title};
        List<Map<String,String>> titleList = new ArrayList<>();
        for (int i = 0; i < strTitle.length; i++) {
            Map<String, String> titleMap = new HashMap<>();
            titleMap.put("title", strTitle[i]);
            titleList.add(titleMap);
        }
        gvWeekTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        gvWeekTitle.setAdapter(new SimpleAdapter(this, titleList, R.layout.item_week_title_gv,from,to));
    }

    /**
     * 初始化选择日期
     */
    private void initGvSelectDate() {
        mSelectDateAdapter = new CustomSchemeSelectDateGvAdapter(this,setDate(),handler);
        gvDate.setAdapter(mSelectDateAdapter);
    }

    /**
     * 计算日期
     */
    private List<BeanSelectDate> setDate() {
        int days = 7;//可选天数
        int number;//日期对应星期的位置（0表示星期一，以此类推，6表示星期天）
        List<BeanSelectDate> dateList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");//设置想要的日期格式
        Calendar calendar = Calendar.getInstance();//获取日历实例
        Date currentDate = calendar.getTime();//获取当前的时间
        calendar.setTime(currentDate);//将日历设置成当前的日期
        int week = calendar.get(Calendar.DAY_OF_WEEK);//星期天是1，星期一是2，以此类推，星期三是4，星期六是7
        if (week == 1) {
            number = 6;
        } else {
            number = week - 2;
        }
        for (int i = 0; i < days + number; i++) {//days + number表示总共的item数，即可选天数+占空的位置数
            if (i < number) {//如果当天不是星期一，则当前星期之前的位置置空（number+1表示当前星期）
                dateList.add(new BeanSelectDate("",false));
            } else if (i == number) {
                String strDate = dateFormat.format(currentDate);
                dateList.add(new BeanSelectDate(strDate,false));
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date date = calendar.getTime();
                String strDate = dateFormat.format(date);
                dateList.add(new BeanSelectDate(strDate,false));
            }
        }
        return dateList;
    }

    /**
     * 改变已选择的日期数目
     * @param selectedDateNumber
     */
    public void setSelectedDateNumber(int selectedDateNumber){
        SpannableString spannableString = new SpannableString("已选择 "+selectedDateNumber+" 个养生日");
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(55);
        spannableString.setSpan(absoluteSizeSpan, 4, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);//粗体
        spannableString.setSpan(styleSpan, 4, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvSelectedDateNumber.setText(spannableString);
    }

    @OnClick(R.id.bt_next)
    public void onViewClicked() {
        //点击下一步按钮
        if (selectedNumber > 0) {
            List<BeanSelectDate> dateList = mSelectDateAdapter.getList();
            List<String> selectDateList = new ArrayList<String>();
            for (BeanSelectDate beanSelectDate : dateList) {
                if (beanSelectDate.isCheck()) {
                    selectDateList.add(beanSelectDate.getDate());
                }
            }
            Intent intent = new Intent(this, ConcreteArrangement.class);
            intent.putStringArrayListExtra("selectDateList", (ArrayList<String>) selectDateList);
            startActivity(intent);
        } else {
            MyToast.showToast(this,"至少选择一天进行养生的日子噢!");
        }

    }

}
