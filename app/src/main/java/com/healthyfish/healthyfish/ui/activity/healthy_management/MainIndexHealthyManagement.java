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
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.POJO.BeanSinglePlan;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.SinglePlanAdapter;
import com.healthyfish.healthyfish.eventbus.NoticeMessage;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainIndexHealthyManagement extends BaseActivity {
    @BindView(R.id.tv_title1)
    TextView tvTitle1;
    @BindView(R.id.tv_detail1)
    TextView tvDetail1;
    @BindView(R.id.tv_progress1)
    TextView tvProgress1;
    @BindView(R.id.progressbar1)
    ProgressBar progressbar1;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.tv_detail2)
    TextView tvDetail2;
    @BindView(R.id.tv_progress2)
    TextView tvProgress2;
    @BindView(R.id.progressbar2)
    ProgressBar progressbar2;
    @BindView(R.id.layout)
    AutoLinearLayout layout;
    @BindView(R.id.layout1)
    AutoLinearLayout layout1;
    @BindView(R.id.layout2)
    AutoLinearLayout layout2;
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
    private boolean isTested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_index_healthy_management);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar(toolbar, toolbarTitle, "我的健康管理");
        initHealthIdentityView();
        intiTotalHealthyscheme();
        intiSingleHealthyPlan();
        initWholeScheme();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUI(NoticeMessage noticeMessage) {
        if (noticeMessage.getMsg() == 1) {
            initWholeScheme();
            scrollview.smoothScrollTo(0,0);

        }
    }

    private void initWholeScheme() {
        layout.setVisibility(View.GONE);
        BeanHealthPlanCommendContent beanHealthPlanCommendContent;
        List<String> HotPlanListStr;
        BeanHotPlanItem beanHotPlanItem;
        beanHealthPlanCommendContent = DataSupport.findLast(BeanHealthPlanCommendContent.class);
        if (beanHealthPlanCommendContent != null) {
            layout.setVisibility(View.VISIBLE);
            HotPlanListStr = JSON.parseObject(beanHealthPlanCommendContent.getHotPlanListJsonStr(), List.class);
            beanHotPlanItem = JSON.parseObject(HotPlanListStr.get(0), BeanHotPlanItem.class);
            refreshProgressUI(0, beanHotPlanItem);
            beanHotPlanItem = JSON.parseObject(HotPlanListStr.get(1), BeanHotPlanItem.class);
            refreshProgressUI(1, beanHotPlanItem);
            layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainIndexHealthyManagement.this, MyHealthyScheme.class);
                    //intent.putExtra("plan",beanHotPlanItem);
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }
            });
            layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainIndexHealthyManagement.this, MyHealthyScheme.class);
                    //intent.putExtra("plan",beanHotPlanItem);
                    intent.putExtra("position", 1);
                    startActivity(intent);
                }
            });
        }
    }


    /**
     * 更新进度条的进度
     *
     * @param beanHotPlanItem
     */
    private void refreshProgressUI(int position, BeanHotPlanItem beanHotPlanItem) {
        if (position == 0) {
            int count = 0;
            int current = 0;
            for (int i = 0; i < beanHotPlanItem.getTodoList().size(); i++) {
                if (beanHotPlanItem.getTodoList().get(i).isDone()) {
                    current++;
                }
                if (!beanHotPlanItem.getTodoList().get(i).getProgress().equals("nothing")) {
                    count++;
                }
            }
            progressbar1.setMax(count);
            progressbar1.setProgress(current);
            tvProgress1.setText("已完成" + current + "/" + count);
        } else {
            int count = 0;
            int current = 0;
            for (int i = 0; i < beanHotPlanItem.getTodoList().size(); i++) {
                if (beanHotPlanItem.getTodoList().get(i).isDone()) {
                    current++;
                }
                if (!beanHotPlanItem.getTodoList().get(i).getProgress().equals("nothing")) {
                    count++;
                }
            }
            progressbar2.setMax(count);
            progressbar2.setProgress(current);
            tvProgress2.setText("已完成" + current + "/" + count);
        }
    }


    // 初始化体质选项
    private void initHealthIdentityView() {
        healthyIdentication = new SpannableString("体质：阳虚  阴虚  气虚  气郁  血瘀  痰湿  湿热  特禀");
        if (isTested) {//已经测试过体质，则对体质进行标识
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(36);
            healthyIdentication.setSpan(colorSpan, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            healthyIdentication.setSpan(absoluteSizeSpan, 3, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        tvHealthyIdentification.setText(healthyIdentication);

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
}
