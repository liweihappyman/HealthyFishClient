package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.healthyfish.healthyfish.POJO.BeanHealthyScheme;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.ui.fragment.HealthyPlanHot;
import com.healthyfish.healthyfish.ui.fragment.SingleHealthySchemeFragment;
import com.healthyfish.healthyfish.utils.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：选择养生计划
 * 作者：Wayne on 2017/7/13 21:17
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */
public class SelectHealthyScheme extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "热门", "气虚质", "阳虚质", "痰湿质", "阴虚质"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_healthy_scheme);
        ButterKnife.bind(this);

        initAll();
        initTabLayout();
    }

    private void initAll() {
        initToolBar(toolbar, toolbarTitle, "健康计划");
    }

    private void initTabLayout() {
        // 初始化数据
        List<BeanHealthyScheme> listHealthyScheme = new ArrayList<>();
        BeanHealthyScheme bean = new BeanHealthyScheme();
        bean.setHealthySchemeTitle("秋季养生计划");
        bean.setHealthySchemeContent("秋分 * 气虚质 * 养生");
        bean.setHealthySchemeParticipant("200");
        listHealthyScheme.add(bean);
        mFragments.add(new HealthyPlanHot());
        for (int i = 0; i < mTitles.length-1; i++) {
            mFragments.add(new SingleHealthySchemeFragment(mContext, listHealthyScheme));
        }

        View decorView = getWindow().getDecorView();
        ViewPager viewPage = ViewFindUtils.find(decorView, R.id.vp_healthy_scheme);
        viewPage.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tl_healthy_scheme);

        tabLayout.setViewPager(viewPage);
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
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
