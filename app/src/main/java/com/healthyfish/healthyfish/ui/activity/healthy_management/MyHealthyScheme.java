package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.POJO.BeanPersonalInformation;
import com.healthyfish.healthyfish.POJO.TabEntity;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.eventbus.NoticeMessage;
import com.healthyfish.healthyfish.ui.activity.medicalrecord.AllMedRec;
import com.healthyfish.healthyfish.ui.activity.medicalrecord.NewMedRec;
import com.healthyfish.healthyfish.ui.fragment.HealthPlanItemDetailFragment;
import com.healthyfish.healthyfish.utils.Utils1;
import com.healthyfish.healthyfish.utils.ViewFindUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 最关键的地方是处理数据对应的关系
 */
public class MyHealthyScheme extends AppCompatActivity {
    @BindView(R.id.iv_banner_my_healthy_scheme)
    AutoLinearLayout ivBannerMyHealthyScheme;
    CommonTabLayout tabDown;
    CommonTabLayout tabUp;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    String[] str = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntitiesWeek = new ArrayList<>();
    List<String> week = new ArrayList<>();//星期:   一、二、...
    List<String> date = new ArrayList<>();//号数，如2,3...
    List<String> calendarDate = new ArrayList<>();//字符串的日期2017年10月20日
    private String[] mTitlesWeek = {"一", "二", "三", "四", "五", "六", "日"};
    private String[] mTitlesDate = {"1", "2", "3", "4", "5", "6", "7"};
    private View mDecorView;
    private int id = 0;//标志点击的item的位置
    BeanHealthPlanCommendContent beanHealthPlanCommendContent = new BeanHealthPlanCommendContent();
    BeanHotPlanItem beanHotPlanItem = new BeanHotPlanItem();

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_healthy_scheme);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        id = getIntent().getIntExtra("id", 0);//获取点击item的位置
        intiToolbarView();
        initAll();
    }

    private void initAll() {
        beanHealthPlanCommendContent = DataSupport.find(BeanHealthPlanCommendContent.class, id);
        beanHotPlanItem = JSON.parseObject(beanHealthPlanCommendContent.getMyHealthyPlanItemJsonStr(),BeanHotPlanItem.class);
        title.setText("一周" + beanHotPlanItem.getTitle() + "计划");
        refreshProgressUI(beanHotPlanItem);
        calendarDate = JSON.parseObject(beanHealthPlanCommendContent.getCalendarDateJsonStr(), List.class);
        week = JSON.parseObject(beanHealthPlanCommendContent.getWeekJsonStr(), List.class);
        date = JSON.parseObject(beanHealthPlanCommendContent.getDateJsonStr(), List.class);
        for (int i = 0; i < beanHotPlanItem.getTodoList().size(); i++) {
            mFragments.add(new HealthPlanItemDetailFragment(beanHotPlanItem.getTodoList().get(i), id, i, calendarDate));
        }
        initViewPager();
        boolean isShowRedPoint = false;//今天之后的有计划的日期显示红点
        for (int k = 0; k < beanHotPlanItem.getTodoList().size(); k++) {
            if (calendarDate.get(k).equals(Utils1.getTime()) || isShowRedPoint) {
                //if (calendarDate.get(i).equals("2017年8月7日") || isShowRedPoint) {//测试用
                if (!beanHotPlanItem.getTodoList().get(k).getProgress().equals("nothing") && !beanHotPlanItem.getTodoList().get(k).isDone()) {
                    tabDown.showDot(k);
                }
                isShowRedPoint = true;
            }
        }
    }


    // 初始化toolbar
    private void intiToolbarView() {
        toolbarTitle.setText("我的养生计划");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.quit_scheme:
                //Toast.makeText(this, "退出计划", Toast.LENGTH_SHORT).show();
                showDelDialog();
                break;
        }
        return true;
    }
    /**
     * 删除提示对话框
     */
    private void showDelDialog() {
        new AlertDialog.Builder(this).setMessage("是否要删除此计划")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DataSupport.delete(BeanHealthPlanCommendContent.class, id);
                        EventBus.getDefault().post(new NoticeMessage(1));
                        Intent intent = new Intent(MyHealthyScheme.this, MainIndexHealthyManagement.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_healthy_scheme, menu);
        return true;
    }

    private void initViewPager() {
        for (int i = 0; i < week.size(); i++) {
            mTabEntitiesWeek.add(new TabEntity(week.get(i), 0, 0));
        }
        for (int i = 0; i < date.size(); i++) {
            mTabEntities.add(new TabEntity(date.get(i), 0, 0));
        }
        mDecorView = getWindow().getDecorView();
        tabUp = ViewFindUtils.find(mDecorView, R.id.tab_up);
        tabDown = ViewFindUtils.find(mDecorView, R.id.tab_down);
        viewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabDown.setTabData(mTabEntities);
        tabUp.setTabData(mTabEntitiesWeek);
        tabDown.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewpager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabDown.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        String currentTime = Utils1.getTime();
        for (int i = 0; i < calendarDate.size(); i++) {
            if (currentTime.equals(calendarDate.get(i)))
                viewpager.setCurrentItem(i);
        }

    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitlesDate[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUI(NoticeMessage noticeMessage) {
        if (noticeMessage.getMsg() == 1) {
            beanHealthPlanCommendContent = DataSupport.find(BeanHealthPlanCommendContent.class,id);
            beanHotPlanItem = JSON.parseObject(beanHealthPlanCommendContent.getMyHealthyPlanItemJsonStr(), BeanHotPlanItem.class);
            refreshProgressUI(beanHotPlanItem);
        }
    }

    /**
     * 更新进度条的进度
     *
     * @param beanHotPlanItem
     */
    private void refreshProgressUI(BeanHotPlanItem beanHotPlanItem) {
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
        progressbar.setMax(count);
        progressbar.setProgress(current);
        tvProgress.setText("已完成" + current + "/" + count);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
