package com.healthyfish.healthyfish.ui.activity.appointment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanAppointmentDates;
import com.healthyfish.healthyfish.POJO.BeanBaseKeySetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanConcernList;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.POJO.BeanHospDoctMoreSchdReq;
import com.healthyfish.healthyfish.POJO.BeanKeyValue;
import com.healthyfish.healthyfish.POJO.BeanListKeyValueResp;
import com.healthyfish.healthyfish.POJO.BeanUserListReq;
import com.healthyfish.healthyfish.POJO.BeanWeekAndDate;
import com.healthyfish.healthyfish.POJO.Test;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MainVpAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.ui.fragment.AppointmentTime;
import com.healthyfish.healthyfish.ui.fragment.AppointmentTime2;
import com.healthyfish.healthyfish.ui.fragment.AppointmentTime3;
import com.healthyfish.healthyfish.utils.FixedSpeedScroller;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.Utils1;
import com.nostra13.universalimageloader.utils.L;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;

/**
 * 描述：医生详情页面
 * 作者：WKJ on 2017/7/10.
 * 邮箱：
 * 编辑：WKJ
 */
public class DoctorDetail extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.civ_doctor)
    CircleImageView civDoctor;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_DepartmentAndTitle)
    TextView tvDepartmentAndTitle;
    @BindView(R.id.tv_doctorCompany)
    TextView tvDoctorCompany;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.btn_sendTheMind)
    Button btnSendTheMind;
    @BindView(R.id.appointment_time_vp)
    ViewPager appointmentTimeVp;
    @BindView(R.id.left)
    ImageView left;
    @BindView(R.id.duration)
    TextView duration;
    @BindView(R.id.right)
    ImageView right;
    @BindView(R.id.first)
    ImageView first;
    @BindView(R.id.second)
    ImageView second;
    @BindView(R.id.third)
    ImageView third;
    @BindView(R.id.doctorInfo)
    TextView doctorInfo;

    private int mPosition = 0;//记录选择预约时间页面的位置
    private FixedSpeedScroller mScroller;

    private FragmentManager fm;
    private FragmentTransaction ft;

    private BeanDoctorInfo beanDoctorInfo = new BeanDoctorInfo();

    private boolean isAttention = false;//是否已经关注该医生
    private String uid;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        ButterKnife.bind(this);
        if (getIntent().getSerializableExtra("BeanDoctorInfo") != null) {
            beanDoctorInfo = (BeanDoctorInfo) getIntent().getSerializableExtra("BeanDoctorInfo");
        }
        initToolBar(toolbar, toolbarTitle, beanDoctorInfo.getName() + "医生");
        uid = MyApplication.uid;
        fm = this.getSupportFragmentManager();
        ft = fm.beginTransaction();
        hospDoctMoreSchdReq();//正在测试的接口，用来获取该医生在其他医院的出诊时间
        tvAttentionListener();//关注操作
        initData();//展示医生信息
        try {
            initApointmentTime();//初始化预约时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pagechange();//页面改动监听
    }

    /**
     * 初始化显示数据
     */
    private void initData() {
        Glide.with(this).load(HttpHealthyFishyUrl + beanDoctorInfo.getImgUrl()).into(civDoctor);
        tvName.setText(beanDoctorInfo.getName());
        tvDepartmentAndTitle.setText("诊室：" + beanDoctorInfo.getCLINIQUE_CODE() + "   " + beanDoctorInfo.getDuties());
        tvDoctorCompany.setText(beanDoctorInfo.getHospital());
        doctorInfo.setText(beanDoctorInfo.getIntroduce());
    }

    /**
     * 初始化预约时间
     */
    private void initApointmentTime() throws ParseException {
        List<BeanWeekAndDate> mList = new ArrayList<>();
        List<String> schdList = beanDoctorInfo.getSchdList();
        if (schdList.isEmpty()) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置想要的日期格式
        Calendar calendar = Calendar.getInstance();//获取日历实例
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuffer stringBuffer = new StringBuffer().append(year).append("-").append(month).append("-").append(day);
        String date = stringBuffer.toString();
        Date Today = dateFormat.parse(date);//必须使用上述方法获取当天日期（保证得到的当前日期不能包含具体时间点）
        calendar.setTime(Today);

        int size_1 = schdList.size() - 1;
        for (int i = 0; i < schdList.size(); i++) {
            String strDateFirst = schdList.get(i).split("_")[0];
            Date dateFirst = dateFormat.parse(strDateFirst);
            //返回的数据会出现日期小于当天日期的情况,必须做此判断，否则会陷入死循环
            if (Today.getTime() <= dateFirst.getTime()) {
                //如果当前时间段不是排班时间的最后一个
                if (i < size_1) {
                    String strDate1 = schdList.get(i).split("_")[0];
                    String strDate2 = schdList.get(i + 1).split("_")[0];
                    String strToday = dateFormat.format(Today);
                    //如果当前时间段的日期刚好属于当天
                    if (strToday.equals(strDate1)) {
                        //对比当前时间段和下一个时间段是否是同一天，如果是同一天则当天上午下午的时间段都显示为可以预约
                        if (strDate1.equals(strDate2)) {
                            mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate1, "上午", "下午"));
                            i++;
                            //如果不是同一天则判断当前时间段是上午还是下午，并将对应的时间段显示为可以预约
                        } else {
                            if (schdList.get(i).split("_")[1].equals("1")) {
                                mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate1, "上午", "1"));
                            } else {
                                mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate1, "1", "下午"));
                            }
                        }
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Today = calendar.getTime();
                        //如果当前时间段的日期不属于当天，则将当前时间段的日期前的部分全部显示为不可预约
                    } else {
                        mList.add(new BeanWeekAndDate(beanDoctorInfo, strToday, "1", "1"));
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Today = calendar.getTime();
                        boolean flag = true;
                        while (flag) {
                            String strToday2 = dateFormat.format(Today);
                            if (strToday2.equals(strDate1)) {
                                if (strDate1.equals(strDate2)) {
                                    mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate1, "上午", "下午"));
                                    i++;
                                } else {
                                    if (schdList.get(i).split("_")[1].equals("1")) {
                                        mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate1, "上午", "1"));
                                    } else {
                                        mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate1, "1", "下午"));
                                    }
                                }
                                calendar.add(Calendar.DAY_OF_YEAR, 1);
                                Today = calendar.getTime();
                                flag = false;
                            } else {
                                mList.add(new BeanWeekAndDate(beanDoctorInfo, strToday2, "1", "1"));
                                calendar.add(Calendar.DAY_OF_YEAR, 1);
                                Today = calendar.getTime();
                            }
                        }
                    }
                    //如果当前时间段是排班时间的最后一个
                } else {
                    String strDate = schdList.get(i).split("_")[0];
                    String strToday = dateFormat.format(Today);
                    if (strToday.equals(strDate)) {
                        if (schdList.get(i).split("_")[1].equals("1")) {
                            mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate, "上午", "1"));
                        } else {
                            mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate, "1", "下午"));
                        }
                        calendar.add(Calendar.DAY_OF_YEAR, 1);//将日期加一天以便后面填充没有预约时间的日期
                    } else {
                        mList.add(new BeanWeekAndDate(beanDoctorInfo, dateFormat.format(Today), "1", "1"));
                        calendar.add(Calendar.DAY_OF_YEAR, 1);
                        Today = calendar.getTime();
                        boolean flag = true;
                        while (flag) {
                            String strToday2 = dateFormat.format(Today);
                            if (strToday2.equals(strDate)) {
                                if (schdList.get(i).split("_")[1].equals("1")) {
                                    mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate, "上午", "1"));
                                } else {
                                    mList.add(new BeanWeekAndDate(beanDoctorInfo, strDate, "1", "下午"));
                                }
                                calendar.add(Calendar.DAY_OF_YEAR, 1);
                                Today = calendar.getTime();
                                flag = false;
                            } else {
                                mList.add(new BeanWeekAndDate(beanDoctorInfo, dateFormat.format(Today), "1", "1"));
                                calendar.add(Calendar.DAY_OF_YEAR, 1);
                                Today = calendar.getTime();
                            }
                        }

                    }
                }
            }
        }

        String today = Utils1.getWeekFromStr(mList.get(0).getDate());
        //判断第一个数据是一周的哪一天，在前面补相应的空位
        //确定是具体的星期几之后：
        // 1.先补相应的空位；
        // 2.以星期天为结尾分成3个list分别放到三个fragment初始化视图
        int position = 0;//分配list的时候，记录已经分到哪个；
        int firstSize = 0;//第一个list真实数据的size
        List<BeanWeekAndDate> list1 = new ArrayList<>();
        switch (today) {
            case "星期一":
                firstSize = 7;
                int num1 = 21 - mList.size();
                mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                for (int i = 0; i < num1 - 1; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                }
                initVeiwpageFragment(mList, list1, firstSize);

                break;
            case "星期二":
                firstSize = 6;
                list1.add(new BeanWeekAndDate("星期一", null, "1", "1", true));//设置占位用的
                int num2 = 20 - mList.size();
                mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                for (int i = 0; i < num2 - 1; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                }
                initVeiwpageFragment(mList, list1, firstSize);

                break;
            case "星期三":
                //假设是
                firstSize = 5;
                list1.add(new BeanWeekAndDate("星期一", null, "1", "1", true));//设置占位用的
                list1.add(new BeanWeekAndDate("星期二", null, "1", "1", true));
                int num3 = 19 - mList.size();
                mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                for (int i = 0; i < num3 - 1; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                }
                initVeiwpageFragment(mList, list1, firstSize);

                break;
            case "星期四":
                firstSize = 4;
                list1.add(new BeanWeekAndDate("星期一", null, "1", "1", true));//设置占位用的
                list1.add(new BeanWeekAndDate("星期二", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期三", null, "1", "1", true));
                int num4 = 18 - mList.size();
                mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                for (int i = 0; i < num4 - 1; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                }
                initVeiwpageFragment(mList, list1, firstSize);

                break;
            case "星期五":
                firstSize = 3;
                list1.add(new BeanWeekAndDate("星期一", null, "1", "1", true));//设置占位用的
                list1.add(new BeanWeekAndDate("星期二", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期三", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期四", null, "1", "1", true));
                int num5 = 17 - mList.size();
                mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                for (int i = 0; i < num5 - 1; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                }
                initVeiwpageFragment(mList, list1, firstSize);

                break;
            case "星期六":
                firstSize = 2;
                list1.add(new BeanWeekAndDate("星期一", null, "1", "1", true));//设置占位用的
                list1.add(new BeanWeekAndDate("星期二", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期三", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期四", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期五", null, "1", "1", true));
                int num6 = 16 - mList.size();
                mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                for (int i = 0; i < num6 - 1; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                }
                initVeiwpageFragment(mList, list1, firstSize);

                break;
            case "星期日":
                firstSize = 1;
                list1.add(new BeanWeekAndDate("星期一", null, "1", "1", true));//设置占位用的
                list1.add(new BeanWeekAndDate("星期二", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期三", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期四", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期五", null, "1", "1", true));
                list1.add(new BeanWeekAndDate("星期六", null, "1", "1", true));
                int num7 = 15 - mList.size();
                mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                for (int i = 0; i < num7 - 1; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    mList.add(new BeanWeekAndDate(dateFormat.format(calendar.getTime()), "1", "1"));
                }
                initVeiwpageFragment(mList, list1, firstSize);

                break;
        }
        //设置选择预约时间小模块的头部显示的时间范围
        SimpleDateFormat dft = new SimpleDateFormat("MM月dd日");
        Date date1 = dateFormat.parse(mList.get(0).getDate(), new ParsePosition(0));
        Date date2 = dateFormat.parse(mList.get(mList.size() - 1).getDate(), new ParsePosition(0));
        duration.setText(dft.format(date1).toString() + "-" + dft.format(date2).toString());

    }

    private void initVeiwpageFragment(List<BeanWeekAndDate> mList, List<BeanWeekAndDate> list1, int firstSize) {
        int position;
        for (position = 0; position < firstSize; position++) {
            list1.add(mList.get(position));
        }
        List<BeanWeekAndDate> list2 = new ArrayList<>();
        for (int i = position; i < (position + 7); i++) {
            list2.add(mList.get(i));
            //position = i+1;
        }
        List<BeanWeekAndDate> list3 = new ArrayList<>();
        for (int j = (position + 7); j < (position + 14); j++) {
            list3.add(mList.get(j));
        }
        BeanAppointmentDates beanAppointmentDates = new BeanAppointmentDates();
        beanAppointmentDates.setList(list1);
        BeanAppointmentDates beanAppointmentDates2 = new BeanAppointmentDates();
        beanAppointmentDates2.setList(list2);
        BeanAppointmentDates beanAppointmentDates3 = new BeanAppointmentDates();
        beanAppointmentDates3.setList(list3);
        List<Fragment> fragments = new ArrayList<>();
        AppointmentTime fragment = new AppointmentTime();
        AppointmentTime2 fragment2 = new AppointmentTime2();
        AppointmentTime3 fragment3 = new AppointmentTime3();

        Bundle bundle1 = new Bundle();     //创建bundle来封装传递给fragment的参数
        bundle1.putSerializable("data", beanAppointmentDates);
        fragment.setArguments(bundle1);         //设置传递的对象

        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("data2", beanAppointmentDates2);
        fragment2.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("data3", beanAppointmentDates3);
        fragment3.setArguments(bundle3);

        fragments.add(fragment);
        fragments.add(fragment2);
        fragments.add(fragment3);
        appointmentTimeVp.setOffscreenPageLimit(0);
        MainVpAdapter vpAdapter = new MainVpAdapter(getSupportFragmentManager(), fragments);
        appointmentTimeVp.setAdapter(vpAdapter);
        setDirectionIndicator();
    }

    //页面改变是重新设置圆点状态
    private void pagechange() {
        //setViewPagerSwitchingTime();
        appointmentTimeVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = appointmentTimeVp.getCurrentItem();
                setDirectionIndicator();
                reSet();
                switch (position) {
                    case 0:
                        first.setBackgroundResource(R.drawable.green_point);
                        break;
                    case 1:
                        second.setBackgroundResource(R.drawable.green_point);
                        break;
                    case 2:
                        third.setBackgroundResource(R.drawable.green_point);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //设置viewpager的切换时间
    public void setViewPagerSwitchingTime() {
        Field mField = null;
        try {
            mField = ViewPager.class.getDeclaredField("mScroller");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mField.setAccessible(true);
        mScroller = new FixedSpeedScroller(appointmentTimeVp.getContext(), new AccelerateInterpolator());
        try {
            mField.set(appointmentTimeVp, mScroller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //重置圆点状态
    private void reSet() {
        first.setBackgroundResource(R.drawable.white_point_stroke);
        second.setBackgroundResource(R.drawable.white_point_stroke);
        third.setBackgroundResource(R.drawable.white_point_stroke);
    }

    @OnClick({R.id.left, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left:
                if (mPosition > 0) {
                    mPosition--;
                    appointmentTimeVp.setCurrentItem(mPosition);
                    setDirectionIndicator();
                    //mScroller.setmDuration(4* 100);

                }
                break;
            case R.id.right:
                if (mPosition < 2) {
                    mPosition++;
                    appointmentTimeVp.setCurrentItem(mPosition);
                    setDirectionIndicator();
                    //mScroller.setmDuration(4* 100);

                }
                break;
        }
    }

    //设置方向指示器可见与不可见状态
    private void setDirectionIndicator() {
        switch (mPosition) {
            case 0:
                left.setVisibility(View.INVISIBLE);
                break;
            case 1:
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
                break;
            case 2:
                right.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 设置是否关注按钮的监听
     */
    private void tvAttentionListener() {
        if (!TextUtils.isEmpty(uid)) {
            if (MyApplication.isFirstUpdateMyConcern) {
                upDateMyConcern(uid);
            } else {
                getMyConcernFromDB(uid);
            }
        }

        tvAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(uid)) {
                    if (tvAttention.getText().toString().equals("+关注")) {
                        addConcern();//加关注
                    }
                } else {
                    MyToast.showToast(DoctorDetail.this, "您还没有登录呦！请先登录再关注");
                }
            }
        });

    }

    /**
     * 查找数据库用户是否关注该医生
     *
     * @param uid
     */
    private void getMyConcernFromDB(String uid) {
        if (!TextUtils.isEmpty(uid)) {
            key = "care_" + uid + "_" + beanDoctorInfo.getHosp() + "_" + beanDoctorInfo.getDept() + "_" + String.valueOf(beanDoctorInfo.getSTAFF_NO());
            if (!DataSupport.where("key = ?", key).find(BeanConcernList.class).isEmpty()) {
                isAttention = true;
                tvAttention.setClickable(false);
            } else {
                isAttention = false;
            }
        }
        //判断是否已经关注该医生
        if (isAttention) {
            tvAttention.setText("已关注");
            tvAttention.setBackgroundResource(R.drawable.concern);
            tvAttention.setTextColor(getResources().getColor(R.color.color_white));
            tvAttention.setClickable(false);
        } else {
            tvAttention.setText("+关注");
            tvAttention.setBackgroundResource(R.drawable.concern_not);
            tvAttention.setTextColor(getResources().getColor(R.color.color_primary));
            tvAttention.setClickable(true);
        }
    }

    /**
     * 加关注操作
     */
    private void addConcern() {
        //Log.i("LYQ", key);

        final String strJson = JSON.toJSONString(beanDoctorInfo);
        Log.i("LYQ", "strJsonBeanDoctorInfo:" + strJson);
        //关注列表的key的具体形式
        //key = "care_" + uid + "_" + Hosp + "_" + Dept + "_" + STAFF_NO;
        BeanBaseKeySetReq beanBaseKeySetReq = new BeanBaseKeySetReq();
        beanBaseKeySetReq.setKey(key);
        beanBaseKeySetReq.setValue(strJson);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeySetReq), new Subscriber<ResponseBody>() {
            String strJson = "";

            @Override
            public void onCompleted() {
                BeanBaseResp beanBaseResp = JSON.parseObject(strJson, BeanBaseResp.class);
                if (beanBaseResp.getCode() >= 0) {
                    tvAttention.setText("已关注");
                    tvAttention.setBackgroundResource(R.drawable.concern);
                    tvAttention.setTextColor(getResources().getColor(R.color.color_white));
                    tvAttention.setClickable(false);
                    isAttention = true;
                    BeanConcernList beanConcernList = new BeanConcernList();
                    beanConcernList.setKey(key);
                    if (!beanConcernList.saveOrUpdate("key = ?", key)) {
                        beanConcernList.saveOrUpdate("key = ?", key);
                    }
                } else {
                    MyToast.showToast(DoctorDetail.this, "关注失败，请重试");
                    tvAttention.setText("+关注");
                    tvAttention.setBackgroundResource(R.drawable.concern_not);
                    tvAttention.setTextColor(getResources().getColor(R.color.color_primary));
                    tvAttention.setClickable(true);
                    isAttention = false;
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strJson = responseBody.string();
                    Log.i("LYQ", strJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 更新用户的关注列表并保存到数据库
     */
    private void upDateMyConcern(final String uid) {
        BeanUserListReq beanUserListReq = new BeanUserListReq();
        beanUserListReq.setPrefix("care_" + uid);
        beanUserListReq.setFrom(0);
        beanUserListReq.setTo(-1);
        beanUserListReq.setNum(-1);

        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListReq), new Subscriber<ResponseBody>() {

                    String jsonStr = null;

                    @Override
                    public void onCompleted() {
                        if (jsonStr.substring(0, 1).equals("[")) {
                            MyApplication.isFirstUpdateMyConcern = false;
                            DataSupport.deleteAll(BeanConcernList.class);
                            List<String> concerns = JSONArray.parseObject(jsonStr, List.class);
                            if (!concerns.isEmpty()) {
                                for (String str : concerns) {
                                    BeanConcernList beanConcernList = new BeanConcernList();
                                    beanConcernList.setKey(str);
                                    boolean isSave = beanConcernList.save();
                                    if (!isSave) {
                                        beanConcernList.save();
                                    }
                                }
                            }
                            getMyConcernFromDB(uid);

                        } else {
                            MyToast.showToast(DoctorDetail.this, "更新关注信息出错啦");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("LYQ", "挂号upDateMyConcern_onError:" + e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            jsonStr = responseBody.string();
                            Log.i("LYQ", "挂号关注列表响应：" + jsonStr);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

    }

    /**
     * 获取该医生在其他医院的出诊时间
     */
    private void hospDoctMoreSchdReq() {
        final BeanHospDoctMoreSchdReq beanHospDoctMoreSchdReq = new BeanHospDoctMoreSchdReq();
        beanHospDoctMoreSchdReq.setHosp(beanDoctorInfo.getHosp());
        beanHospDoctMoreSchdReq.setDept(beanDoctorInfo.getDept());

        beanHospDoctMoreSchdReq.setStaffNo(String.valueOf(beanDoctorInfo.getSTAFF_NO()));
        String strJson = JSON.toJSONString(beanHospDoctMoreSchdReq);
        Log.i("LYQ", "BeanHospDoctMoreSchdReq请求：" + strJson);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDoctMoreSchdReq), new Subscriber<ResponseBody>() {
            String strJson = "";

            @Override
            public void onCompleted() {
                List<BeanKeyValue> beanKeyValueList = JSONArray.parseArray(strJson,BeanKeyValue.class);
                for (BeanKeyValue beanKeyValue : beanKeyValueList) {
                    String key = beanKeyValue.getKey();
                    String value = beanKeyValue.getValue();
                    Log.i("LYQ", "排班时间：" + key + "  :  " + value);
                }

            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "DoctorDetail-hospDoctMoreSchdReq()-onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strJson = responseBody.string();
                    //[{"key":"hosp_lzzyy_ds_186_27000","value":"[\\\"2017-09-29_1\\\"]"}]
                    Log.i("LYQ", "hospDoctMoreSchdReq()响应：" + strJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
