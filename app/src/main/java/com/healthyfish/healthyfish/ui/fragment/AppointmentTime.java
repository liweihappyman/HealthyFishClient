package com.healthyfish.healthyfish.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanWeekAndDate;
import com.healthyfish.healthyfish.POJO.Test;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.WeekGridAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentTime extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.week_gridview)
    GridView weekGridview;

    private View rootView;

    private Test test;

    public AppointmentTime() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_appointment_time, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Bundle bundle=this.getArguments();
        test = (Test) bundle.getSerializable("data");
        Log.i("ttttttt","测试+"+test.getList().get(0).getDate());



        init();
        return rootView;
    }

    private void init() {
//        List<BeanWeekAndDate> list = new ArrayList<>();
//        BeanWeekAndDate b = new BeanWeekAndDate("周一", "12-27","约满","下午");
//        BeanWeekAndDate b1 = new BeanWeekAndDate("周二","12-27","上午"," ");
//        BeanWeekAndDate b2 = new BeanWeekAndDate("周三","12-27","约满","  ");
//        BeanWeekAndDate b3 = new BeanWeekAndDate("周四", "12-27"," ","约满");
//        BeanWeekAndDate b4 = new BeanWeekAndDate("周五", "12-27","上午","下午");
//        BeanWeekAndDate b5 = new BeanWeekAndDate("周六", "12-27","约满","下午");
//        BeanWeekAndDate b6 = new BeanWeekAndDate("周日", "12-27","  ","  ");
//        list.add(b);
//        list.add(b1);
//        list.add(b2);
//        list.add(b3);
//        list.add(b4);
//        list.add(b5);
//        list.add(b6);
        List<BeanWeekAndDate> list = new ArrayList<>();
        list = test.getList();
        WeekGridAdapter weekAdapter = new WeekGridAdapter(list,getActivity());
        weekGridview.setAdapter(weekAdapter);




    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
