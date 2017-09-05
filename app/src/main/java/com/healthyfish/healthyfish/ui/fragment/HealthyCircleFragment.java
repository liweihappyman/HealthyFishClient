package com.healthyfish.healthyfish.ui.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.healthy_circle.HealthyCirclePosting;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述：健康圈首页
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class HealthyCircleFragment extends Fragment implements View.OnClickListener {
    private FragmentManager fragmentManager;
    public HealthyCircleFragment(FragmentManager fragmentManager){
            this.fragmentManager = fragmentManager;
    }

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_posting)
    ImageView ivPosting;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_healthy_circle)
    ViewPager vpHealthyCircle;
    Unbinder unbinder;
    private Context mContext;
    private View rootView;
    private int mPosition = 0;
    private Activity activity;
    private String[] mTitles = {
            "我关注的", "所有社区", "今日头条"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_healthy_circle, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initTabLayout();
        ivPosting.setOnClickListener(this);
        return rootView;
    }

    private void initTabLayout() {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        mFragments.add(new MyCommunityFragment());
        mFragments.add(new AllCommunityFragment());
        mFragments.add(new TestFragment());
        /*for (int i = 0; i < mTitles.length-3; i++) {
            mFragments.add(new PersonalCenterFragment());
        }*/
        vpHealthyCircle.setAdapter(new PagerAdapter(fragmentManager,mFragments));
        tabLayout.setupWithViewPager(vpHealthyCircle);
        vpHealthyCircle.setCurrentItem(mPosition);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(),HealthyCirclePosting.class);
        startActivity(intent);
    }


    private class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mListFragment;
        public PagerAdapter(FragmentManager fm, List<Fragment> mListFragment) {
            super(fm);
            this.mListFragment = mListFragment;
        }

        @Override
        public int getCount() {
            return mListFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mListFragment.get(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
