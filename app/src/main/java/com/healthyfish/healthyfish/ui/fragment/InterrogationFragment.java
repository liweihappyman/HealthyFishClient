package com.healthyfish.healthyfish.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanHospDeptListRespItem;
import com.healthyfish.healthyfish.POJO.BeanMyAppointmentItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.InterrogationRvAdapter;
import com.healthyfish.healthyfish.eventbus.RefreshMyAppointmentMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterrogationFragment extends Fragment {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_interrogation_service)
    ViewPager vpInterrogationService;
    Unbinder unbinder;


    private View rootView;
    private Context mContext;
    private Activity activity;
    private int mPosition = 0;

    private String[] mTitles = {
            "图文咨询", "我的挂号", "全部服务"};


    public InterrogationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_interrogation, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        toolbarTitle.setText("问诊服务");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
       initTabLayout();
        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toMyAppointmentFragment(BeanMyAppointmentItem beanMyAppointmentItem) {
        vpInterrogationService.setCurrentItem(1);//挂号成功后通知跳转到我的挂号页面MyAppointmentFragment

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toMyAppointmentFragment(RefreshMyAppointmentMsg refreshMyAppointmentMsg) {
        vpInterrogationService.setCurrentItem(1);//从我的关注页面进去挂号成功后通知跳转到我的挂号页面MyAppointmentFragment
    }

    private void initTabLayout() {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        Fragment currentService = new CurrentServiceFragment();
        Fragment myAppointment = new MyAppointmentFragment();
        Fragment allService = new AllServiceFragment();
        mFragments.add(currentService);
        mFragments.add(myAppointment);
        mFragments.add(allService);

        vpInterrogationService.setAdapter(new PagerAdapter(getChildFragmentManager(),mFragments));
        tabLayout.setupWithViewPager(vpInterrogationService);
        vpInterrogationService.setCurrentItem(mPosition);
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
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }
}
