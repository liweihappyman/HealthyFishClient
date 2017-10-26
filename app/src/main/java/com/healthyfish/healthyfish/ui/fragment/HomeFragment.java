package com.healthyfish.healthyfish.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.POJO.BeanHealthWorkShop;
import com.healthyfish.healthyfish.POJO.BeanHomeImgSlideReq;
import com.healthyfish.healthyfish.POJO.BeanHomeImgSlideResp;
import com.healthyfish.healthyfish.POJO.BeanHomeImgSlideRespItem;
import com.healthyfish.healthyfish.POJO.BeanItemNewsAbstract;
import com.healthyfish.healthyfish.POJO.BeanListReq;
import com.healthyfish.healthyfish.POJO.BeanSearchReq;
import com.healthyfish.healthyfish.POJO.BeanSearchResp;
import com.healthyfish.healthyfish.POJO.BeanSearchRespItem;
import com.healthyfish.healthyfish.POJO.BeanSessionIdReq;
import com.healthyfish.healthyfish.POJO.BeanSessionIdResp;
import com.healthyfish.healthyfish.POJO.ImMsgBean;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.HomePageHealthInfoAadpter;
import com.healthyfish.healthyfish.adapter.HomePageHealthWorkShopAdapter;
import com.healthyfish.healthyfish.adapter.WholeSchemeAdapter;
import com.healthyfish.healthyfish.constant.Constants;
import com.healthyfish.healthyfish.eventbus.NoticeMessage;
import com.healthyfish.healthyfish.eventbus.RefresHomeMsg;
import com.healthyfish.healthyfish.eventbus.WeChatReceiveSysMdrMsg;
import com.healthyfish.healthyfish.ui.activity.HealthNews;
import com.healthyfish.healthyfish.ui.activity.HomeSearchResult;
import com.healthyfish.healthyfish.ui.activity.Inspection_report.InspectionReport;
import com.healthyfish.healthyfish.ui.activity.MoreHealthNews;
import com.healthyfish.healthyfish.ui.activity.SearchResult;
import com.healthyfish.healthyfish.ui.activity.appointment.AppointmentHome;
import com.healthyfish.healthyfish.ui.activity.healthy_management.MainIndexHealthyManagement;
import com.healthyfish.healthyfish.ui.activity.interrogation.ChoiceDepartment;
import com.healthyfish.healthyfish.ui.activity.medicalrecord.AllMedRec;
import com.healthyfish.healthyfish.ui.activity.personal_center.MyNews;
import com.healthyfish.healthyfish.ui.widget.AutoCardView;
import com.healthyfish.healthyfish.utils.AutoLogin;
import com.healthyfish.healthyfish.utils.MyRecyclerViewOnItemListener;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.NetworkConnectUtils;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.Utils1;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.ResponseBody;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;


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
    @BindView(R.id.tv_add_more_plan)
    TextView tvAddMorePlan;
    @BindView(R.id.tv_add_more_news)
    TextView tvAddMoreNews;
    @BindView(R.id.lly_more_health_news)
    AutoLinearLayout llyMoreHealthNews;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.plan_list)
    AutoCardView planList;
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
    final BeanSessionIdReq beanSessionIdReq = new BeanSessionIdReq();
    private HomePageHealthInfoAadpter healthInfoAdapter;
    private boolean isRefresh = false;//是否已经加载过首页

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        String user = MySharedPrefUtil.getValue("user");
        String sid = MySharedPrefUtil.getValue("sid");
        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(sid)) {
            isRefresh = true;
            AutoLogin.autoLogin();
            initAll();
        }
        topbarSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(getActivity(), HomeSearchResult.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SEARCH_KEY", topbarSearchEt.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return true;
            }
        });
        return rootView;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshHomeFragment(RefresHomeMsg refresHomeMsg) {
        if (!isRefresh) {
            initAll();
        }
    }

    private void initAll() {
        initBannerRequest();//网络访问获取轮播图内容
        if (Constants.NUMBER_SYS_INFO > 0) {
            initInfoPrmopt(String.valueOf(Constants.NUMBER_SYS_INFO));
        }
        initFunctionMenu();//初始化菜单监听

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initHealthNews();//初始化健康资讯
                    }
                });


            }
        }).start();


        initHealthPlan();//初始化养生计划
        initHealthWorkShop();//初始化健康工坊
    }

    //网络访问获取轮播图内容
    private void initBannerRequest() {
        final List<String> imgs = new ArrayList<>();//装载图片
        final List<String> desc = new ArrayList<>();//装载描述
        RetrofitManagerUtils.getInstance(getActivity(), null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(new BeanHomeImgSlideReq()), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String str = null;
                        try {
                            str = responseBody.string();
                            BeanHomeImgSlideResp beanHomeImgSlideResp = JSON.parseObject(str, BeanHomeImgSlideResp.class);
                            for (BeanHomeImgSlideRespItem beanHomeImgSlideRespItem : beanHomeImgSlideResp.getImgList()) {
                                imgs.add(HttpHealthyFishyUrl + beanHomeImgSlideRespItem.getImg());
                                //Log.i("imgstr", beanHomeImgSlideRespItem.getImg());
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
     *
     * @param imgs 图片地址链接
     * @param desc 描述
     */
    private void setbanner(List<String> imgs, List<String> desc) {
        //轮播图
        bannerGuideContent.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(getActivity())
                        .load(model)
                        //.placeholder(R.mipmap.placeholder)
                        .error(R.mipmap.error)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
        //轮播图设置数据
        bannerGuideContent.setData(imgs, desc);
    }

    //养生计划(目前实际数据为健康管理首页的单项列表)
    private void initHealthPlan() {
        //设计图原来样式
//        List<BeanHealthPlanItemTest> listHealthPlan = new ArrayList<>();
//        BeanHealthPlanItemTest healthPlan = new BeanHealthPlanItemTest();
//        healthPlan.setDone(false);
//        healthPlan.setProgress("5/10");
//        healthPlan.setTitle("中医体质养生计划#1");
//        healthPlan.setTodo("中医院外治" + "  " + "益阳罐");
//        BeanHealthPlanItemTest healthPlan2 = new BeanHealthPlanItemTest();
//        healthPlan2.setDone(false);
//        healthPlan2.setProgress("5/20");
//        healthPlan2.setTitle("中医体质养生计划#1");
//        healthPlan2.setTodo("中医院外治" + "  " + "益阳罐");
//        BeanHealthPlanItemTest healthPlan3 = new BeanHealthPlanItemTest();
//        healthPlan3.setDone(true);
//        healthPlan3.setProgress("6/10");
//        healthPlan3.setTitle("慢性疾病康复计划#1");
//        healthPlan3.setTodo("健身" + "  " + "跑步3KM");
//        listHealthPlan.add(healthPlan);
//        listHealthPlan.add(healthPlan2);
//        listHealthPlan.add(healthPlan3);
//        LinearLayoutManager lmg = new LinearLayoutManager(mContext);
//        healthPlanRecyclerview.setLayoutManager(lmg);
//        HomePageHealthPlanAdapter homePageHealthPlanAdapter = new HomePageHealthPlanAdapter(mContext, listHealthPlan);
//        healthPlanRecyclerview.setAdapter(homePageHealthPlanAdapter);

        List<BeanHealthPlanCommendContent> listHealthPlanCommendContent = new ArrayList<>();
        listHealthPlanCommendContent = DataSupport.findAll(BeanHealthPlanCommendContent.class);
        if (listHealthPlanCommendContent.size() > 0) {//读取数据库的计划内容，如果日期是今天的则展示出来
            planList.setVisibility(View.VISIBLE);
            date.setText(Utils1.getTime());//初始化时间
            LinearLayoutManager lmg = new LinearLayoutManager(mContext);
            healthPlanRecyclerview.setLayoutManager(lmg);
            WholeSchemeAdapter wholeSchemeAdapter = new WholeSchemeAdapter(getActivity(), listHealthPlanCommendContent);
            healthPlanRecyclerview.setAdapter(wholeSchemeAdapter);
        } else {
            planList.setVisibility(View.GONE);
        }

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

    //健康资讯列表
    private void initHealthNews() {
        final List<BeanItemNewsAbstract> newsList = new ArrayList<>();
        createRequest(newsList);//健康资讯请求
        //Item的点击监听
        healthNewsRecyclerview.addOnItemTouchListener(new MyRecyclerViewOnItemListener(mContext, healthNewsRecyclerview,
                new MyRecyclerViewOnItemListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(mContext, HealthNews.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("HEALTH_NEWS_URL", newsList.get(position).getUrl());
                        bundle.putString("HEALTH_NEWS_TITLE", newsList.get(position).getTitle());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
        //点击加载更多
        tvAddMoreNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectUtils.isNetworkConnected(mContext)) {
                    if (tvAddMoreNews.getText().toString().equals("点击加载更多")) {
                        if (newsList.isEmpty()) {
                            createRequest(newsList);
                        } else {
                            if (newsList.size() > 4) {
                                for (int i = 4; i < newsList.size(); i++) {
                                    healthInfoAdapter.addData(newsList.get(i));
                                }
                                healthInfoAdapter.notifyDataSetChanged();
                                tvAddMoreNews.setText("收起");
                            } else {
                                MyToast.showToast(mContext, "没有更多啦！");
                            }
                        }
                    } else if (tvAddMoreNews.getText().toString().equals("收起")) {
                        if (newsList.size() > 4) {
                            for (int i = 4; i < newsList.size(); i++) {
                                healthInfoAdapter.removeData(4);
                            }
                            healthInfoAdapter.notifyDataSetChanged();
                            tvAddMoreNews.setText("点击加载更多");
                        }
                    }
                } else {
                    MyToast.showToast(getActivity(), "网络不可用，请检查您的网络连接！");
                }

            }
        });
        //跳转到更多健康资讯页面
        llyMoreHealthNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreHealthNews.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 健康新闻请求
     *
     * @param newsList
     */
    private void createRequest(final List<BeanItemNewsAbstract> newsList) {
        BeanListReq beanListReq = new BeanListReq();
        beanListReq.setPrefix("news_");
        beanListReq.setFrom(0);
        beanListReq.setTo(7);
        beanListReq.setNum(8);
        RetrofitManagerUtils.getInstance(getActivity(), null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanListReq),
                        new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {
                                if (newsList.size() > 0) {
                                    List<BeanItemNewsAbstract> list = new ArrayList<>();
                                    if (newsList.size() >= 4) {
                                        for (int i = 0; i < 4; i++) {
                                            list.add(newsList.get(i));
                                        }
                                    } else {
                                        for (int i = 0; i < newsList.size(); i++) {
                                            list.add(newsList.get(i));
                                        }
                                    }
                                    LinearLayoutManager lmg = new LinearLayoutManager(mContext);
                                    healthNewsRecyclerview.setLayoutManager(lmg);
                                    healthInfoAdapter = new HomePageHealthInfoAadpter(getActivity(), list);
                                    healthNewsRecyclerview.setAdapter(healthInfoAdapter);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                //MyToast.showToast(getActivity(), "加载健康咨询出错");
                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                if (responseBody != null) {
                                    String jsonNews = null;
                                    try {
                                        jsonNews = responseBody.string();
                                        //Log.e("LYQ", "新闻资讯：" + jsonNews);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (!TextUtils.isEmpty(jsonNews)) {
                                        if (jsonNews.substring(0, 1).equals("[")) {
                                            List<String> strJsonNewsList = JSONArray.parseObject(jsonNews, List.class);
                                            for (String strJsonNews : strJsonNewsList) {
                                                BeanItemNewsAbstract bean = JSON.parseObject(strJsonNews, BeanItemNewsAbstract.class);
                                                newsList.add(bean);
                                            }
                                        } else {
                                            //MyToast.showToast(getActivity(), "加载健康咨询出错");
                                        }
                                    } else {
                                        //MyToast.showToast(getActivity(), "加载健康咨询出错");
                                    }
                                }
                            }
                        });
    }


    //初始化菜单监听
    private void initFunctionMenu() {
        fmInterrogation2.setOnClickListener(this);
        fmRegistration.setOnClickListener(this);
        fmFmInspectionReport.setOnClickListener(this);
        fmMedRec.setOnClickListener(this);
        fmHealthManagement.setOnClickListener(this);
        fmRemoteMonitoring.setOnClickListener(this);
        // 信息提示监听
        topbarInfo.setOnClickListener(this);
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

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        workShopRecyclerview.setLayoutManager(layoutManager);
        HomePageHealthWorkShopAdapter adapter = new HomePageHealthWorkShopAdapter(mContext, listHealthWorkShop);
        workShopRecyclerview.setAdapter(adapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_info:
                Constants.NUMBER_SYS_INFO = 0;
                initInfoPrmopt("0");
                startActivity(new Intent(mContext, MyNews.class));
                break;
            case R.id.fm_interrogation2:
                startActivity(new Intent(mContext, ChoiceDepartment.class));
                break;

            case R.id.fm_med_rec:
                if (MySharedPrefUtil.getValue("user") == "") {
                    Toast.makeText(getActivity(), "您还没有登录哟", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent_med_rec = new Intent(mContext, AllMedRec.class);
                    startActivity(intent_med_rec);
                }
                break;
            case R.id.fm_health_management:
                Intent intent = new Intent(mContext, MainIndexHealthyManagement.class);
                startActivity(intent);
                break;
            case R.id.fm_registration:
                Intent toRegistration = new Intent(mContext, AppointmentHome.class);
                startActivity(toRegistration);
                break;
            case R.id.fm_fm_inspection_report:
                Intent toInspectionReport = new Intent(getActivity(), InspectionReport.class);
                toInspectionReport.putExtra("key",Constants.FOR_LIST);
                startActivity(toInspectionReport);
                break;

            case R.id.fm_remote_monitoring:
                List<String> topicList = new ArrayList<>(); // 获取购买过问诊服务的医生列表
                //Cursor cursor = DataSupport.findBySQL("select topic from immsgbean where id in (select min(id) from immsgbean group by topic) ");
                Cursor cursor = DataSupport.findBySQL("select distinct topic from immsgbean");
                if (cursor.moveToFirst()) {
                    do {
                        String peerNumber = cursor.getString(cursor.getColumnIndex("topic")).substring(1);
                        topicList.add(peerNumber);
                    } while (cursor.moveToNext());
                }
                cursor.close();

                /*for (String b : topicList) {
                    Log.e("topic", b);
                }*/
                break;
        }
    }

    /*
    * 更新健康计划UI
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUI(NoticeMessage noticeMessage) {
        if (noticeMessage.getMsg() == 1) {
            initHealthPlan();
        }
    }

    /*
    * 更新病历夹系统消息
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDateMdrSystemInfo(final WeChatReceiveSysMdrMsg msg) {
        Constants.NUMBER_SYS_INFO++;
        initInfoPrmopt(String.valueOf(Constants.NUMBER_SYS_INFO));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // EventBus.getDefault().unregister(this);
    }
}

