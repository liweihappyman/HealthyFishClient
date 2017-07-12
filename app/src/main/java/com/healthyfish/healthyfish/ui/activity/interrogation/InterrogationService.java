package com.healthyfish.healthyfish.ui.activity.interrogation;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MainVpAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.ui.fragment.AllServiceFragment;
import com.healthyfish.healthyfish.ui.fragment.CurrentServiceFragment;
import com.healthyfish.healthyfish.ui.fragment.MyDoctorFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：问诊服务页面
 * 作者：LYQ on 2017/7/5.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class InterrogationService extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_currentService)
    TextView tvCurrentService;
    @BindView(R.id.tv_myDoctor)
    TextView tvMyDoctor;
    @BindView(R.id.tv_allService)
    TextView tvAllService;
    @BindView(R.id.cursor)
    ImageView cursor;
    @BindView(R.id.vp_interrogation_service)
    ViewPager vpInterrogationService;

    private ArrayList fragments;  //Fragment页面数组
    private int offset = 0;  // 动画图片偏移量
    private int currIndex = 0;  // 当前页卡编号
    private int currentItem = 0;  //初始页面
    private Animation animation = null;
    private int one2twoLength;  // 页卡1 -> 页卡2 偏移量
    private int one2threeLength;  // 页卡1 -> 页卡3 偏移量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interrogation_service);
        ButterKnife.bind(this);
        initToolBar(toolbar,tvTitle,"问诊服务");
        InitImageView();
        initPgAdapter();
        vpListener();
    }

    /**
     * 设置ViewPager的选中监听
     */
    private void vpListener() {
        vpInterrogationService.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    //第一个页面当创建后滑动到第二页面不会销毁，滑动到第三页面时会销毁，如果要刷新理论上可以在页面的Fragment中重新加载处理刷新
                    case 0:
                        if (currIndex == 1) {
                            animation = new TranslateAnimation(one2twoLength, 0, 0, 0);
                        } else if (currIndex == 2) {
                            animation = new TranslateAnimation(one2threeLength, 0, 0, 0);
                        }
                        reSet();
                        tvCurrentService.setTextColor(getResources().getColor(R.color.color_primary));
                        break;
                    //中间的页面当创建后不会销毁，即它不会再去执行Fragment中的程序，如果要刷新理论上应该在此处理刷新
                    case 1:
                        if (currIndex == 0) {
                            animation = new TranslateAnimation(offset, one2twoLength, 0, 0);
                        } else if (currIndex == 2) {
                            animation = new TranslateAnimation(one2threeLength, one2twoLength, 0, 0);
                        }
                        reSet();
                        tvMyDoctor.setTextColor(getResources().getColor(R.color.color_primary));
                        break;
                    //第三个页面当创建后滑动到第二页面不会销毁，滑动到第一页面时会销毁，如果要刷新理论上可以在页面的Fragment中重新加载处理刷新
                    case 2:
                        if (currIndex == 0) {
                            animation = new TranslateAnimation(offset, one2threeLength, 0, 0);
                        } else if (currIndex == 1) {
                            animation = new TranslateAnimation(one2twoLength, one2threeLength, 0, 0);
                        }
                        reSet();
                        tvAllService.setTextColor(getResources().getColor(R.color.color_primary));
                        break;
                }
                currIndex = position;
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                cursor.startAnimation(animation);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 初始化ViewPager
     */
    private void initPgAdapter() {
        fragments = new ArrayList<Fragment>();
        Fragment currentService = new CurrentServiceFragment();
        Fragment myDoctor = new MyDoctorFragment();
        Fragment allService = new AllServiceFragment();
        fragments.add(currentService);
        fragments.add(myDoctor);
        fragments.add(allService);
        vpInterrogationService.setAdapter(new MainVpAdapter(getSupportFragmentManager(), fragments));
        vpInterrogationService.setCurrentItem(currentItem);//设置初始页面
        if (currentItem == 1){
            currIndex = 1;
        }else if (currentItem == 2){
            currIndex = 2;
        }
    }

    /**
     * 头部标题的点击监听
     * @param view
     */
    @OnClick({R.id.tv_currentService, R.id.tv_myDoctor, R.id.tv_allService})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_currentService:
                vpInterrogationService.setCurrentItem(0);
                break;
            case R.id.tv_myDoctor:
                vpInterrogationService.setCurrentItem(1);
                break;
            case R.id.tv_allService:
                vpInterrogationService.setCurrentItem(2);
                break;
        }
    }

    /**
     * 重置头部文字颜色
     */
    private void reSet(){
        tvCurrentService.setTextColor(getResources().getColor(R.color.color_content_title));
        tvMyDoctor.setTextColor(getResources().getColor(R.color.color_content_title));
        tvAllService.setTextColor(getResources().getColor(R.color.color_content_title));
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();  //获取屏幕宽度
        int indicatorWidth = screenWidth / 3;//计算卡片宽度
        cursor.getLayoutParams().width  = indicatorWidth;//设置卡片宽度
        cursor.requestLayout();  //通知卡片宽度发生改变
        offset = (screenWidth / 3 - indicatorWidth) / 2;// 计算偏移量
        one2twoLength = offset * 2 + indicatorWidth;// 页卡1 -> 页卡2 偏移量
        one2threeLength = one2twoLength * 2;// 页卡1 -> 页卡3 偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

}
