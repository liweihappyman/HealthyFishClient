package com.healthyfish.healthyfish.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanWeekAndDate;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.utils.Utils1;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：预约时间显示适配器
 * 作者：WKJ on 2017/7/11.
 * 邮箱：
 * 编辑：WKJ
 */

public class WeekGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<BeanWeekAndDate> list;
    private Activity activity;
    private TextView titleWeek;

    public WeekGridAdapter(List<BeanWeekAndDate> list,Activity activity) {
        this.list = list;
        this.mContext = activity;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }//修改

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_week_title, parent,false);
        AutoUtils.auto(convertView);
        //头部
        titleWeek = (TextView) convertView.findViewById(R.id.title_week);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        //上午下午
        TextView am = (TextView) convertView.findViewById(R.id.am);
        TextView pm = (TextView) convertView.findViewById(R.id.pm);


        if (list.get(position).getDate()!=null) {
            String dateStr = list.get(position).getDate();
            String monthAndDay = dateStr.substring(dateStr.indexOf("年")+1,dateStr.length());//设置星期下面的日期
            titleWeek.setText( Utils1.getWeekFromStr(list.get(position).getDate()));//设置时星期几,格式为：yyyy年MM月dd日
            date.setText(monthAndDay);//日期
        }else {
            titleWeek.setText(list.get(position).getTitleWeek());
            date.setText("  ");
        }


    //上午
        if (list.get(position).getAm().equals("约满")){
            am.setText(list.get(position).getAm());
            am.setBackgroundResource(R.color.color_titlebar_bg);//设置背景色为灰色
        }else if (list.get(position).getAm().equals("上午")) {
            am.setText("上午");
            am.setBackgroundResource(R.color.color_primary_dark);
        }

    //下午
        if (list.get(position).getPm().equals("约满")){
            pm.setText(list.get(position).getPm());
            pm.setBackgroundResource(R.color.color_titlebar_bg);//设置背景色为灰色
        }else  if (list.get(position).getPm().equals("下午")){
            pm.setText("下午");
            pm.setBackgroundResource(R.color.color_primary_dark);
        }

        //监听
        if (!list.get(position).getPm().equals("约满")&&!list.get(position).isOutTime()) {
            pm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptions();
                }
            });
        }
        if (!list.get(position).getAm().equals("约满")&&!list.get(position).isOutTime()) {
            am.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOptions();
                }
            });
        }

        //根据时间判断给怎样的监听事件
        return convertView;
    }




    private void showOptions() {
        TextView cancel;
        TextView today;
        ListView listView;
        View rootView;
        rootView = LayoutInflater.from(mContext).inflate(R.layout.popupwind_choice_appointment_time,
                null);
        final PopupWindow mPopWindow = new PopupWindow(rootView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);

        //设置窗口背景
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.7f;
        activity.getWindow().setAttributes(lp);
        cancel = (TextView) rootView.findViewById(R.id.cancel);
        listView = (ListView) rootView.findViewById(R.id.choose_time_listview);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("上午 ：08 :10--08:20");
        }

        SelectTimeAdapter selectTimeAdapter = new SelectTimeAdapter(mContext, list);
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
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
        mPopWindow.setAnimationStyle(R.style.AnimBottom);
        mPopWindow.showAtLocation(titleWeek, Gravity.BOTTOM, 0, 0);
    }


















}
