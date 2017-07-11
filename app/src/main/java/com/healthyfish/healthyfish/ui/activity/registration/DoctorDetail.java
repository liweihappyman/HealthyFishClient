package com.healthyfish.healthyfish.ui.activity.registration;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MainVpAdapter;
import com.healthyfish.healthyfish.ui.fragment.AppointmentTime;
import com.healthyfish.healthyfish.utils.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：医生详情页面
 * 作者：WKJ on 2017/7/10.
 * 邮箱：
 * 编辑：WKJ
 */
public class DoctorDetail extends AppCompatActivity {
    private int mPosition = 0;//记录选择预约时间页面的位置
    private FixedSpeedScroller  mScroller;
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
    @BindView(R.id.ckb_attention)
    CheckBox ckbAttention;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbarTitle.setText("xx医生");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }

        initPointmentTime();//初始化预约时间
        pagechange();//页面改动监听
    }


    private void initPointmentTime() {


        List<Fragment> fragments = new ArrayList<>();
        AppointmentTime fragment = new AppointmentTime();
        AppointmentTime fragment2 = new AppointmentTime();
        AppointmentTime fragment3 = new AppointmentTime();
        fragments.add(fragment);
        fragments.add(fragment2);
        fragments.add(fragment3);
        MainVpAdapter vpAdapter = new MainVpAdapter(getSupportFragmentManager(), fragments);
        appointmentTimeVp.setAdapter(vpAdapter);
        setDirectionIndicator();
    }


    //页面改变是重新设置圆点状态
    private void pagechange() {
        setViewPagerSwitchingTime();
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
            mField =ViewPager.class.getDeclaredField("mScroller");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        mField.setAccessible(true);
        mScroller = new FixedSpeedScroller(appointmentTimeVp.getContext(),new AccelerateInterpolator());
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
                    mScroller.setmDuration(4* 100);
                    setDirectionIndicator();
                }
                break;
            case R.id.right:
                if (mPosition < 2) {
                    mPosition++;
                    appointmentTimeVp.setCurrentItem(mPosition);
                    mScroller.setmDuration(4* 100);
                    setDirectionIndicator();
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
