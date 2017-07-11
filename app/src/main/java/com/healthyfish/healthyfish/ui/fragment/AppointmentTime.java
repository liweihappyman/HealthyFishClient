package com.healthyfish.healthyfish.ui.fragment;


import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanWeekAndDate;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.SelectTimeAdapter;
import com.healthyfish.healthyfish.adapter.WeekItemGridAdapter;
import com.healthyfish.healthyfish.adapter.WeekTitleGridAdapter;

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
    @BindView(R.id.title_week_gridview)
    GridView titleWeekGridview;
    @BindView(R.id.item_week_gridview)
    GridView itemWeekGridview;
    private View rootView;

    public AppointmentTime() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_appointment_time, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        List<BeanWeekAndDate> list = new ArrayList<>();
        BeanWeekAndDate b = new BeanWeekAndDate("周一", "  ");
        BeanWeekAndDate b1 = new BeanWeekAndDate("周二", "12-28");
        BeanWeekAndDate b2 = new BeanWeekAndDate("周三", "12-28");
        BeanWeekAndDate b3 = new BeanWeekAndDate("周四", "12-28");
        BeanWeekAndDate b4 = new BeanWeekAndDate("周五", "12-28");
        BeanWeekAndDate b5 = new BeanWeekAndDate("周六", "12-28");
        BeanWeekAndDate b6 = new BeanWeekAndDate("周日", "12-28");
        list.add(b);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        list.add(b5);
        list.add(b6);
        WeekTitleGridAdapter weekAdapter = new WeekTitleGridAdapter(list, getActivity());
        titleWeekGridview.setAdapter(weekAdapter);

        List<String> listStr = new ArrayList<>();
        listStr.add("约满");
        listStr.add("约满");
        listStr.add("约满");
        listStr.add("   ");
        listStr.add("约满");
        listStr.add("约满");
        listStr.add("约满");
        listStr.add("约满");
        listStr.add("   ");
        listStr.add("   ");
        listStr.add("约满");
        listStr.add("约满");
        listStr.add("约满");
        listStr.add("约满");
        WeekItemGridAdapter itemGridAdapter = new WeekItemGridAdapter(listStr, getActivity());
        itemWeekGridview.setAdapter(itemGridAdapter);


        itemWeekGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showOptions();
            }
        });

    }


    private void showOptions() {
        TextView cancel;
        TextView today;
        ListView listView;
        View rootView;
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwind_choice_appointment_time,
                null);
        final PopupWindow mPopWindow = new PopupWindow(rootView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);

        //设置窗口背景
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);

        cancel = (TextView) rootView.findViewById(R.id.cancel);
        listView = (ListView) rootView.findViewById(R.id.choose_time_listview);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("上午 ：08 :10--08:20");
        }

        SelectTimeAdapter selectTimeAdapter = new SelectTimeAdapter(getActivity(), list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(selectTimeAdapter);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        mPopWindow.setAnimationStyle(R.style.AnimBottom);
        mPopWindow.showAtLocation(titleWeekGridview, Gravity.BOTTOM,0,0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
