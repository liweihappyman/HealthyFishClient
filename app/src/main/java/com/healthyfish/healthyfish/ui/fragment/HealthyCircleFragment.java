package com.healthyfish.healthyfish.ui.fragment;


import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MainVpAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 描述：健康圈首页
 * A simple {@link Fragment} subclass.
 */
public class HealthyCircleFragment extends Fragment {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_concern_community)
    TextView tvConcernCommunity;
    @BindView(R.id.tv_all_community)
    TextView tvAllCommunity;
    @BindView(R.id.cursor)
    ImageView cursor;
    @BindView(R.id.vp_healthy_circle)
    ViewPager vpHealthyCircle;
    Unbinder unbinder;

    private Context mContext;
    private View rootView;

    private ArrayList fragments;  //Fragment页面数组
    private int offset = 0;  // 动画图片偏移量
    private int currIndex = 0;  // 当前页卡编号
    private int currentItem = 0;  //初始页面
    private Animation animation = null;
    private int one2twoLength;  // 页卡1 -> 页卡2 偏移量

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_healthy_circle, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initImageView();
        initPgAdapter();
        vpListener();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_concern_community, R.id.tv_all_community})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_concern_community:
                vpHealthyCircle.setCurrentItem(0);
                break;
            case R.id.tv_all_community:
                vpHealthyCircle.setCurrentItem(1);
                break;
        }
    }

    /**
     * 设置ViewPager的选中监听
     */
    private void vpListener() {
        vpHealthyCircle.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                        }
                        reSet();
                        tvConcernCommunity.setTextColor(getResources().getColor(R.color.color_secondary));
                        break;
                    //中间的页面当创建后不会销毁，即它不会再去执行Fragment中的程序，如果要刷新理论上应该在此处理刷新
                    case 1:
                        if (currIndex == 0) {
                            animation = new TranslateAnimation(offset, one2twoLength, 0, 0);
                        }
                        reSet();
                        tvAllCommunity.setTextColor(getResources().getColor(R.color.color_secondary));
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
     * 重置头部文字颜色
     */
    private void reSet(){
        tvConcernCommunity.setTextColor(getResources().getColor(R.color.color_general_and_title));
        tvAllCommunity.setTextColor(getResources().getColor(R.color.color_general_and_title));
    }


    /**
     * 初始化ViewPager
     */
    private void initPgAdapter() {
        fragments = new ArrayList<Fragment>();
        Fragment MyCommunity = new MyCommunityFragment();
        Fragment AllCommunity = new AllCommunityFragment();
        fragments.add(MyCommunity);
        fragments.add(AllCommunity);
        vpHealthyCircle.setAdapter(new MainVpAdapter(getActivity().getSupportFragmentManager(), fragments));
        vpHealthyCircle.setCurrentItem(currentItem);//设置初始页面
        if (currentItem == 1){
            currIndex = 1;
        }
    }

    /**
     * 初始化动画
     */
    private void initImageView() {
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();  //获取屏幕宽度
        int indicatorWidth = screenWidth / 2;   //计算卡片宽度
        cursor.getLayoutParams().width  = indicatorWidth;   //设置卡片宽度
        cursor.requestLayout();     //通知卡片宽度发生改变
        offset = (screenWidth / 2 - indicatorWidth) / 2;    // 计算偏移量
        one2twoLength = offset * 2 + indicatorWidth;    // 页卡1 -> 页卡2 偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);  //设置动画初始位置
    }

}
