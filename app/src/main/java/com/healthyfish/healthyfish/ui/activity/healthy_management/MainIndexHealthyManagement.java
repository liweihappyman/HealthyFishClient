package com.healthyfish.healthyfish.ui.activity.healthy_management;

/**
 * 描述：健康管理首页
 * 作者：Wayne on 2017/7/9 14:46
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.POJO.BeanSinglePlan;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserPhy;
import com.healthyfish.healthyfish.POJO.BeanUserPhyIdResp;
import com.healthyfish.healthyfish.POJO.BeanUserPhysical;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.SinglePlanAdapter;
import com.healthyfish.healthyfish.adapter.WholeSchemeAdapter;
import com.healthyfish.healthyfish.eventbus.NoticeMessage;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class MainIndexHealthyManagement extends BaseActivity {
    @BindView(R.id.whole_scheme)
    TextView wholeScheme;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    // 显示体质状态，一共八种
    @BindView(R.id.tv_healthy_identification)
    TextView tvHealthyIdentification;
    @BindView(R.id.tv_add_more_single_plan)
    TextView tvAddMoreSinglePlan;
    @BindView(R.id.btn_total_healthy_scheme)
    Button btnTotalHealthyScheme;
    @BindView(R.id.rv_single_plan)
    RecyclerView rvSinglePlan;
    SpannableString healthyIdentication;
    @BindView(R.id.whole_scheme_recyclerview)
    RecyclerView wholeSchemeRecyclerview;
    private boolean isTested = false;
    private String uid = "";
    private final String phyNames[] = {"气虚", "阳虚", "阴虚", "痰湿", "湿热", "血淤", "气郁", "特禀", "平和"};//0-8

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_index_healthy_management);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar(toolbar, toolbarTitle, "我的健康管理");
        uid = MyApplication.uid;
        initHealthIdentityView();
        intiTotalHealthyscheme();
        intiSingleHealthyPlan();
        initWholeScheme();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUI(NoticeMessage noticeMessage) {
        if (noticeMessage.getMsg() == 1) {
            initWholeScheme();
            scrollview.smoothScrollTo(0, 0);

        }
    }
    //初始化整体计划界面布局

    private void initWholeScheme() {
        List<BeanHealthPlanCommendContent> listHealthPlanCommendContent = new ArrayList<>();
        listHealthPlanCommendContent = DataSupport.findAll(BeanHealthPlanCommendContent.class);
        if (listHealthPlanCommendContent.size() > 0) {
            wholeScheme.setVisibility(View.VISIBLE);
            wholeSchemeRecyclerview.setVisibility(View.VISIBLE);
            LinearLayoutManager lmg = new LinearLayoutManager(this);
            wholeSchemeRecyclerview.setLayoutManager(lmg);
            WholeSchemeAdapter wholeSchemeAdapter = new WholeSchemeAdapter(this, listHealthPlanCommendContent);
            wholeSchemeRecyclerview.setAdapter(wholeSchemeAdapter);
        }else {
            wholeScheme.setVisibility(View.GONE);
            wholeSchemeRecyclerview.setVisibility(View.GONE);
        }
    }


    // 初始化体质选项
    private void initHealthIdentityView() {
        healthyIdentication = new SpannableString("体质：气虚  阳虚  阴虚  痰湿  湿热  血淤  气郁  特禀  平和");

        if (MyApplication.isFirstUpdateUsrPhy) {
            upDateUserPhyFromNetwork(uid);
        } else {
            getUserPhyFromDB(uid);
        }

        tvHealthyIdentification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTested) {//已经做过体质测试
                    Intent intent = new Intent(MainIndexHealthyManagement.this, PhyIdeReport.class);
                    intent.putExtra("IS_TESTED", isTested);
                    startActivity(intent);
                } else {//未做过体质测试
                    Intent intent = new Intent(MainIndexHealthyManagement.this, IndexPhysicalIdentification.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 从数据库查找用户体质报告
     *
     * @param uid
     */
    private void getUserPhyFromDB(String uid) {
        List<BeanUserPhy> beanUserPhyList = DataSupport.where("uid = ?", uid).find(BeanUserPhy.class);
        if (!beanUserPhyList.isEmpty()) {
            isTested = true;
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(36);
            BeanUserPhyIdResp beanUserPhyIdResp = JSON.parseObject(beanUserPhyList.get(0).getJsonStrPhysicalList(), BeanUserPhyIdResp.class);
            List<BeanUserPhysical> physicals = beanUserPhyIdResp.getPhyList();

            for (int i = 0; i < phyNames.length; i++) {
                if (physicals.get(0).getTitle().equals(phyNames[i])) {
                    healthyIdentication.setSpan(colorSpan, i * 4 + 3, i * 4 + 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    healthyIdentication.setSpan(absoluteSizeSpan, i * 4 + 3, i * 4 + 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                }
            }
        }
        tvHealthyIdentification.setText(healthyIdentication);
    }


    // 初始化整体健康计划
    private void intiTotalHealthyscheme() {

        // 制定整体计划
        btnTotalHealthyScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainIndexHealthyManagement.this, MakeWholeHealthyScheme.class);
                startActivity(intent);
            }
        });
    }

    // 初始化单项健康计划
    private void intiSingleHealthyPlan() {

        List<BeanSinglePlan> list = new ArrayList<>();
        list.add(new BeanSinglePlan("针灸", "中医体质单项养生计划", 10, true));
        list.add(new BeanSinglePlan("艾灸", "中医体质单项养生计划", 8, true));
        list.add(new BeanSinglePlan("针灸", "中医体质单项养生计划", 10, false));
        list.add(new BeanSinglePlan("针灸", "中医体质单项养生计划", 10, true));
        list.add(new BeanSinglePlan("艾灸", "中医体质单项养生计划", 8, true));
        list.add(new BeanSinglePlan("针灸", "中医体质单项养生计划", 10, false));
        SinglePlanAdapter adapter = new SinglePlanAdapter(this, list);
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        rvSinglePlan.setLayoutManager(lmg);
        rvSinglePlan.setAdapter(adapter);
        // 添加更多单项计划
        tvAddMoreSinglePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainIndexHealthyManagement.this, "添加更多单项计划", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 从服务器更新本地数据库的用户体质
     *
     * @param uid
     */
    private void upDateUserPhyFromNetwork(final String uid) {
        BeanUserListValueReq beanUserListReq = new BeanUserListValueReq();
        beanUserListReq.setPrefix("phyad_" + uid);
        beanUserListReq.setFrom(0);
        beanUserListReq.setTo(-1);
        beanUserListReq.setNum(-1);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListReq), new Subscriber<ResponseBody>() {

            String strResp = "";

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(strResp)) {
                    if (strResp.toString().substring(0, 1).equals("[")) {
                        MyApplication.isFirstUpdateUsrPhy = false;
                        DataSupport.deleteAll(BeanUserPhy.class);
                        List<String> strList = JSONArray.parseObject(strResp, List.class);
                        if (!strList.isEmpty()) {
                            for (String str : strList) {
                                BeanUserPhyIdResp beanUserPhyIdResp = JSON.parseObject(str, BeanUserPhyIdResp.class);
                                if (beanUserPhyIdResp.getCode() == 0) {
                                    BeanUserPhy beanuserPhy = new BeanUserPhy();
                                    beanuserPhy.setUid(uid);
                                    beanuserPhy.setJsonStrPhysicalList(str);
                                    boolean isSave = beanuserPhy.saveOrUpdate("uid = ?", uid);
                                    if (!isSave) {
                                        beanuserPhy.saveOrUpdate("uid = ?", uid);
                                    }
                                }
                            }
                        }
                        getUserPhyFromDB(uid);

                    } else {
                        MyToast.showToast(MainIndexHealthyManagement.this, "加载个人体质信息出错啦");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "健康管理体质报告onError：" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strResp = responseBody.string();
                    Log.i("LYQ", "健康管理体质报告：" + strResp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
