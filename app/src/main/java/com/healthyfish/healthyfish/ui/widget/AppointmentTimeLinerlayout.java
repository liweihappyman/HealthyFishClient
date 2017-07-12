package com.healthyfish.healthyfish.ui.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MainVpAdapter;
import com.healthyfish.healthyfish.ui.fragment.AppointmentTime;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：
 * 作者：WKJ on 2017/7/11.
 * 邮箱：
 * 编辑：WKJ
 */

public class AppointmentTimeLinerlayout extends AutoLinearLayout {

    private Context context;
    @BindView(R.id.left)
    ImageView left;
    @BindView(R.id.duration)
    TextView duration;
    @BindView(R.id.right)
    ImageView right;
    @BindView(R.id.appointment_time_vp)
    ViewPager appointmentTimeVp;
    @BindView(R.id.first)
    ImageView first;
    @BindView(R.id.second)
    ImageView second;
    @BindView(R.id.third)
    ImageView third;

    public AppointmentTimeLinerlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.appointment_timepicker, this);
    }



    
    
    
    @OnClick({R.id.left, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left:
                break;
            case R.id.right:
                break;
        }
    }


}
