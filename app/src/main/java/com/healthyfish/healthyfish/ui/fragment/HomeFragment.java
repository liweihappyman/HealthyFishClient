package com.healthyfish.healthyfish.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanItemTest;
import com.healthyfish.healthyfish.POJO.BeanItemNewsAbstract;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.HealthInfoAadpter;
import com.healthyfish.healthyfish.adapter.HealthPlanAdapter;
import com.healthyfish.healthyfish.ui.activity.AllMedRec;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.health_news_recyclerview)
    RecyclerView healthNewsRecyclerview;
    @BindView(R.id.health_plan_recyclerview)
    RecyclerView healthPlanRecyclerview;
    private Context mContext;
    private View rootView;


    @BindView(R.id.topbar_scan)
    ImageView topbarScan;
    @BindView(R.id.topbar_search_iv)
    ImageView topbarSearchIv;
    @BindView(R.id.topbar_search_et)
    EditText topbarSearchEt;
    @BindView(R.id.topbar_info)
    AutoRelativeLayout topbarInfo;
    @BindView(R.id.banner_guide_content)
    BGABanner bannerGuideContent;
    Unbinder unbinder;
    @BindView(R.id.fm_interrogation2)
    ImageView fmInterrogation2;
    @BindView(R.id.fm_registration)
    ImageView fmRegistration;
    @BindView(R.id.fm_fm_inspection_report)
    ImageView fmFmInspectionReport;
    @BindView(R.id.fm_med_rec)
    ImageView fmMedRec;
    @BindView(R.id.fm_health_management)
    ImageView fmHealthManagement;
    @BindView(R.id.fm_remote_monitoring)
    ImageView fmRemoteMonitoring;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initAll();
        return rootView;

    }

    private void initAll() {
        initInfoPrmopt("9");//测试消息提示文本
        initFunctionMenu();//初始化菜单监听
        initHealthNews();//初始化健康资讯
        initHealthPlan();//初始化养生计划

        //轮播图
        bannerGuideContent.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(getActivity())
                        .load(model)
                        .placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.error)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });

        bannerGuideContent.setData(Arrays.asList("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg",
                "http://github.com/panxw/android-image-indicator/blob/master/screenshot/guider_00.jpg",
                "http://github.com/panxw/android-image-indicator/blob/master/screenshot/guider_01.jpg"),
                Arrays.asList("提示文字1", "提示文字2", "提示文字3"));


//加载本地资源
//        List<View> views = new ArrayList<>();
//        views.add(BGABannerUtil.getItemImageView(getActivity(), R.mipmap.placeholder));
//        views.add(BGABannerUtil.getItemImageView(getActivity(), R.mipmap.healthy_circle));
//        views.add(BGABannerUtil.getItemImageView(getActivity(), R.mipmap.health_workshop));
//        bannerGuideContent.setData(views);
    }

    //测试养生计划
    private void initHealthPlan() {
        List<BeanHealthPlanItemTest> listHealthPlan = new ArrayList<>();
        BeanHealthPlanItemTest healthPlan = new BeanHealthPlanItemTest();
        healthPlan.setDone(false);
        healthPlan.setProgress("5/10");
        healthPlan.setTitle("中医体质养生计划#1");
        healthPlan.setTodo("中医院外治" + "  " + "益阳罐");
        BeanHealthPlanItemTest healthPlan2 = new BeanHealthPlanItemTest();
        healthPlan2.setDone(false);
        healthPlan2.setProgress("5/20");
        healthPlan2.setTitle("中医体质养生计划#1");
        healthPlan2.setTodo("中医院外治" + "  " + "益阳罐");
        BeanHealthPlanItemTest healthPlan3 = new BeanHealthPlanItemTest();
        healthPlan3.setDone(true);
        healthPlan3.setProgress("6/10");
        healthPlan3.setTitle("慢性疾病康复计划#1");
        healthPlan3.setTodo("健身" + "  " + "跑步3KM");
        listHealthPlan.add(healthPlan);
        listHealthPlan.add(healthPlan2);
        listHealthPlan.add(healthPlan3);
        LinearLayoutManager lmg = new LinearLayoutManager(mContext);
        healthPlanRecyclerview.setLayoutManager(lmg);
        HealthPlanAdapter healthPlanAdapter = new HealthPlanAdapter(mContext, listHealthPlan);
        healthPlanRecyclerview.setAdapter(healthPlanAdapter);

    }

    //测试消息提示
    private void initInfoPrmopt(String string) {
        Badge badge = new QBadgeView(mContext).bindTarget(topbarInfo);
        badge.setBadgeBackgroundColor(0xFFF70909);
        badge.setBadgeTextColor(0xffffffFF);
        badge.setBadgeGravity(Gravity.CENTER | Gravity.TOP);
        badge.setBadgeTextSize(12, true);
        badge.setBadgePadding(3, true);
        badge.setBadgeText(string);
        badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {

                }
            }
        });
    }

    //测试健康资讯列表
    private void initHealthNews() {
        List<BeanItemNewsAbstract> listNews = new ArrayList<>();
        BeanItemNewsAbstract itemNews1 = new BeanItemNewsAbstract();
        itemNews1.setTitle("早睡早起对养生的好处");
        itemNews1.setCategory("资讯");
        itemNews1.setTimestamp("2017年7月1日");
        BeanItemNewsAbstract itemNews2 = new BeanItemNewsAbstract();
        itemNews2.setTitle("枸杞的清肝明目的作用");
        itemNews2.setCategory("资讯");
        itemNews2.setTimestamp("2017年7月1日");
        BeanItemNewsAbstract itemNews3 = new BeanItemNewsAbstract();
        itemNews3.setTitle("健身操的好处");
        itemNews3.setCategory("视频");
        itemNews3.setTimestamp("2017年7月1日");
        listNews.add(itemNews1);
        listNews.add(itemNews2);
        listNews.add(itemNews3);
        listNews.add(itemNews1);
        LinearLayoutManager lmg = new LinearLayoutManager(mContext);
        healthNewsRecyclerview.setLayoutManager(lmg);
        HealthInfoAadpter healthInfoAdapter = new HealthInfoAadpter(mContext, listNews);
        healthNewsRecyclerview.setAdapter(healthInfoAdapter);

    }

    //初始化菜单监听
    private void initFunctionMenu() {
        fmInterrogation2.setOnClickListener(this);
        fmRegistration.setOnClickListener(this);
        fmFmInspectionReport.setOnClickListener(this);
        fmMedRec.setOnClickListener(this);
        fmHealthManagement.setOnClickListener(this);
        fmRemoteMonitoring.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fm_med_rec:
                Intent  intent = new Intent(mContext, AllMedRec.class);
                startActivity(intent);
        }

    }
}

