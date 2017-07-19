package com.healthyfish.healthyfish.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseReq;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanItemTest;
import com.healthyfish.healthyfish.POJO.BeanHealthWorkShop;
import com.healthyfish.healthyfish.POJO.BeanHomeImgSlideReq;
import com.healthyfish.healthyfish.POJO.BeanHomeImgSlideResp;
import com.healthyfish.healthyfish.POJO.BeanHomeImgSlideRespItem;
import com.healthyfish.healthyfish.POJO.BeanItemNewsAbstract;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.HomePageHealthInfoAadpter;
import com.healthyfish.healthyfish.adapter.HomePageHealthPlanAdapter;
import com.healthyfish.healthyfish.adapter.HomePageHealthWorkShopAdapter;
import com.healthyfish.healthyfish.api.IApiService;
import com.healthyfish.healthyfish.ui.activity.appointment.AppointmentHome;
import com.healthyfish.healthyfish.ui.activity.healthy_management.MainIndexHealthyManagement;
import com.healthyfish.healthyfish.ui.activity.medicalrecord.AllMedRec;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.Utils1;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import retrofit2.Retrofit;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.constants.HttpHealthyFishyUrl;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.health_news_recyclerview)
    RecyclerView healthNewsRecyclerview;
    @BindView(R.id.health_plan_recyclerview)
    RecyclerView healthPlanRecyclerview;
    @BindView(R.id.work_shop_recyclerview)
    RecyclerView workShopRecyclerview;
    @BindView(R.id.date)
    TextView date;
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
    private String url2 = "http://219.159.248.209/demo/TestServlet";

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
        initHealthWorkShop();//初始化健康工坊
        initBannerRequest();//网络访问获取轮播图内容

    }
    //网络访问获取轮播图内容
    private void initBannerRequest() {
        final List<String> imgs =new ArrayList<>();//装载图片
        final List<String> desc = new ArrayList<>();//装载描述
        RetrofitManagerUtils.getInstance(getActivity(),null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(new BeanHomeImgSlideReq()), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNext(ResponseBody responseBody) {
                String str = null;
                try {
                    str = responseBody.string();
                    BeanHomeImgSlideResp beanHomeImgSlideResp = JSON.parseObject(str,BeanHomeImgSlideResp.class);
                    for (BeanHomeImgSlideRespItem beanHomeImgSlideRespItem :beanHomeImgSlideResp.getImgList()){
                        imgs.add(HttpHealthyFishyUrl+beanHomeImgSlideRespItem.getImg());
                        Log.i("imgstr",beanHomeImgSlideRespItem.getImg());
                        desc.add(beanHomeImgSlideRespItem.getDesc());
                    }
                    setbanner(imgs, desc);//给轮播图设置图片
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.i("imgstr",str);
            }
        });
    }



    /**
     * 给轮播图设置图片和描述
     * @param imgs  图片地址链接
     * @param desc  描述
     */
    private void setbanner(List<String> imgs, List<String> desc) {
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
        //轮播图设置数据
        bannerGuideContent.setData(imgs,desc);
    }

    //测试养生计划
    private void initHealthPlan() {
        date.setText(Utils1.getTime());//初始化时间
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
        HomePageHealthPlanAdapter homePageHealthPlanAdapter = new HomePageHealthPlanAdapter(mContext, listHealthPlan);
        healthPlanRecyclerview.setAdapter(homePageHealthPlanAdapter);

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
        HomePageHealthInfoAadpter healthInfoAdapter = new HomePageHealthInfoAadpter(mContext, listNews);
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

    // 初始化健康工坊
    private void initHealthWorkShop() {
        List<BeanHealthWorkShop> listHealthWorkShop = new ArrayList<>();
        BeanHealthWorkShop commodity1 = new BeanHealthWorkShop();
        commodity1.setSmallImgCommodity(R.mipmap.image_home_page_work_shop);
        commodity1.setNameCommodity("中医面膜");
        commodity1.setHotSale(true);

        BeanHealthWorkShop commodity2 = new BeanHealthWorkShop();
        commodity2.setSmallImgCommodity(R.mipmap.image_home_page_work_shop);
        commodity2.setNameCommodity("西医面膜");
        commodity2.setHotSale(false);

        listHealthWorkShop.add(commodity1);
        listHealthWorkShop.add(commodity2);
        listHealthWorkShop.add(commodity1);
        listHealthWorkShop.add(commodity2);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        workShopRecyclerview.setLayoutManager(layoutManager);
        HomePageHealthWorkShopAdapter adapter = new HomePageHealthWorkShopAdapter(mContext, listHealthWorkShop);
        workShopRecyclerview.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fm_med_rec:
                Intent intent_med_rec = new Intent(mContext, AllMedRec.class);
                startActivity(intent_med_rec);
                break;
            case R.id.fm_health_management:
                Intent intent = new Intent(mContext, MainIndexHealthyManagement.class);
                startActivity(intent);
                break;
            case R.id.fm_registration:
                Intent toRegistration = new Intent(mContext, AppointmentHome.class);
                startActivity(toRegistration);
                break;
        }

    }
}

