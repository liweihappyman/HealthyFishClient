package com.healthyfish.healthyfish;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthyfish.healthyfish.adapter.MainVpAdapter;
import com.healthyfish.healthyfish.ui.fragment.HealthWorkshopFragment;
import com.healthyfish.healthyfish.ui.fragment.HealthyCircleFragment;
import com.healthyfish.healthyfish.ui.fragment.HomeFragment;
import com.healthyfish.healthyfish.ui.fragment.InterrogationFragment;
import com.healthyfish.healthyfish.ui.fragment.PersonalCenterFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 描述：MyApplication初始化参数
 * 作者：Wayne on 2017/6/26 14:51
 * 邮箱：liwei_happyman@qq.com
 * 编辑：wkj
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.fg_viewpage)
    ViewPager fgViewpage;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.ly_home)
    AutoLinearLayout lyHome;
    @BindView(R.id.iv_interrogation)
    ImageView ivInterrogation;
    @BindView(R.id.tv_interrogation)
    TextView tvInterrogation;
    @BindView(R.id.ly_interrogation)
    AutoLinearLayout lyInterrogation;
    @BindView(R.id.iv_healthy_circle)
    ImageView ivHealthyCircle;
    @BindView(R.id.tv_healthy_circle)
    TextView tvHealthyCircle;
    @BindView(R.id.ly_healthy_circle)
    AutoLinearLayout lyHealthyCircle;
    @BindView(R.id.iv_health_workshop)
    ImageView ivHealthWorkshop;
    @BindView(R.id.tv_health_workshop)
    TextView tvHealthWorkshop;
    @BindView(R.id.ly_health_workshop)
    AutoLinearLayout lyHealthWorkshop;
    @BindView(R.id.iv_personal_center)
    ImageView ivPersonalCenter;
    @BindView(R.id.tv_personal_center)
    TextView tvPersonalCenter;
    @BindView(R.id.ly_personal_center)
    AutoLinearLayout lyPersonalCenter;
    private MainVpAdapter ViewpageAdapter;
    private List<Fragment> fragments;
    private HomeFragment homeFragment;
    private InterrogationFragment interrogationFragment;
    private HealthyCircleFragment healthyCircleFragment;
    private HealthWorkshopFragment healthWorkshopFragment;
    private PersonalCenterFragment personalCenterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //菜单监听
        lyHome.setOnClickListener(this);
        lyInterrogation.setOnClickListener(this);
        lyHealthyCircle.setOnClickListener(this);
        lyHealthWorkshop.setOnClickListener(this);
        lyPersonalCenter.setOnClickListener(this);
        initpgAdapter();//初始化viewpage
        setTab(0);
        fgViewpage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int pos = fgViewpage.getCurrentItem();
                setTab(pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

//菜单点击
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_home:
                setTab(0);
                break;
            case R.id.ly_interrogation:
                setTab(1);
                break;
            case R.id.ly_healthy_circle:
                setTab(2);
                break;
            case R.id.ly_health_workshop:
                setTab(3);
                break;
            case R.id.ly_personal_center:
                setTab(4);
                break;
            default:
        }
    }

    //初始化ViewPage
    private void initpgAdapter() {
        fragments = new ArrayList<>();
        homeFragment = new HomeFragment();
        interrogationFragment = new InterrogationFragment();
        healthyCircleFragment = new HealthyCircleFragment();
        healthWorkshopFragment = new HealthWorkshopFragment();
        personalCenterFragment = new PersonalCenterFragment();
        fragments.add(homeFragment);
        fragments.add(interrogationFragment);
        fragments.add(healthyCircleFragment);
        fragments.add(healthWorkshopFragment);
        fragments.add(personalCenterFragment);
        ViewpageAdapter = new MainVpAdapter(getSupportFragmentManager()
                , fragments);
        fgViewpage.setAdapter(ViewpageAdapter);
    }

    //重置菜单状态
    private void reSet() {
        ivHome.setImageResource(R.mipmap.home_unselected);
        ivInterrogation.setImageResource(R.mipmap.interrogation_unselect);
        ivHealthyCircle.setImageResource(R.mipmap.healthy_circle_unselected);
        ivHealthWorkshop.setImageResource(R.mipmap.health_workshop_unselected);
        ivPersonalCenter.setImageResource(R.mipmap.personal_center_unselect);
        tvHome.setTextColor(getResources().getColor(R.color.color_general_and_titl));
        tvInterrogation.setTextColor(getResources().getColor(R.color.color_general_and_titl));
        tvHealthyCircle.setTextColor(getResources().getColor(R.color.color_general_and_titl));
        tvHealthWorkshop.setTextColor(getResources().getColor(R.color.color_general_and_titl));
        tvPersonalCenter.setTextColor(getResources().getColor(R.color.color_general_and_titl));
    }

    private void setTab(int i) {
        switch (i) {
            case 0:
                reSet();
                ivHome.setImageResource(R.mipmap.home);
                tvHome.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(0);
                break;
            case 1:
                reSet();
                ivInterrogation.setImageResource(R.mipmap.interrogation);
                tvInterrogation.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(1);
                break;
            case 2:
                reSet();
                ivHealthyCircle.setImageResource(R.mipmap.healthy_circle);
                tvHealthyCircle.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(2);
                break;
            case 3:
                reSet();
                ivHealthWorkshop.setImageResource(R.mipmap.health_workshop);
                tvHealthWorkshop.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(3);
                break;
            case 4:
                reSet();
                ivPersonalCenter.setImageResource(R.mipmap.personal_center);
                tvPersonalCenter.setTextColor(getResources().getColor(R.color.color_primary_dark));
                fgViewpage.setCurrentItem(4);
                break;
            default:
        }
    }
}
