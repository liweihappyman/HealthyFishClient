package com.healthyfish.healthyfish.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.healthyfish.healthyfish.POJO.BeanHospRegNumListReq;
import com.healthyfish.healthyfish.POJO.BeanHospRegNumListRespItem;
import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListResp;
import com.healthyfish.healthyfish.POJO.BeanHospitalListRespItem;
import com.healthyfish.healthyfish.POJO.BeanUserRetrPresReq;
import com.healthyfish.healthyfish.POJO.BeanWeekAndDate;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.appointment.ConfirmReservationInformation;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.Utils1;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.constants.HttpHealthyFishyUrl;

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

    private List<String> timeList = new ArrayList<>();
    private SelectTimeAdapter selectTimeAdapter;

    private List<BeanHospRegNumListRespItem> HospRegNumList = new ArrayList<>();

    public WeekGridAdapter(List<BeanWeekAndDate> list, Activity activity) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_week_title, parent, false);
        AutoUtils.auto(convertView);
        //头部
        titleWeek = (TextView) convertView.findViewById(R.id.title_week);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        //上午下午
        TextView am = (TextView) convertView.findViewById(R.id.am);
        TextView pm = (TextView) convertView.findViewById(R.id.pm);

        if (list.get(position) != null) {
            if (list.get(position).getDate() != null) {
                String dateStr = list.get(position).getDate();
                String monthAndDay = dateStr.substring(dateStr.indexOf("-") + 1, dateStr.length());//设置星期下面的日期
                titleWeek.setText(Utils1.getWeekFromStr(list.get(position).getDate()));//设置时星期几,格式为：yyyy年MM月dd日
                date.setText(monthAndDay);//日期
            } else {
                titleWeek.setText(list.get(position).getTitleWeek());
                date.setText("  ");
            }

            //上午
            if (list.get(position).getAm().equals("上午")) {
                am.setText("上午");
                am.setBackgroundResource(R.color.color_primary_dark);
            }

            //下午
            if (list.get(position).getPm().equals("下午")) {
                pm.setText("下午");
                pm.setBackgroundResource(R.color.color_primary_dark);
            }

            //监听上午
            if (list.get(position).getAm().equals("上午") && !list.get(position).isOutTime()) {
                am.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData(list.get(position), "1", "上午");
                    }
                });
            }
            //监听下午
            if (list.get(position).getPm().equals("下午") && !list.get(position).isOutTime()) {
                pm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initData(list.get(position), "2", "下午");
                    }
                });
            }

        }

        //根据时间判断给怎样的监听事件
        return convertView;
    }


    private void initData(final BeanWeekAndDate beanWeekAndDate, String Type, final String AmOrPm) {

        BeanHospRegNumListReq beanHospRegNumListReq = new BeanHospRegNumListReq();
        beanHospRegNumListReq.setHosp("lzzyy");
        beanHospRegNumListReq.setDept(beanWeekAndDate.getBeanHospRegisterReq().getDept());
        beanHospRegNumListReq.setDoct(beanWeekAndDate.getBeanHospRegisterReq().getDoct());
        beanHospRegNumListReq.setDate(beanWeekAndDate.getDate());
        beanHospRegNumListReq.setType(Type);


        RetrofitManagerUtils.getInstance(mContext, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospRegNumListReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("LYQ", e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String str = null;
                try {
                    str = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<JSONObject> jsonObjects = JSONArray.parseObject(str, List.class);
                for (JSONObject jsonObject : jsonObjects) {
                    BeanHospRegNumListRespItem beanHospRegNumListRespItem = JSON.parseObject(jsonObject.toJSONString(), BeanHospRegNumListRespItem.class);
                    timeList.add(AmOrPm + "：" + beanHospRegNumListRespItem.getHZS().substring(11, 16));
                    HospRegNumList.add(beanHospRegNumListRespItem);
                }
                if (timeList.isEmpty()) {
                    MyToast.showToast(mContext, "该时间段已经没有号源啦！");
                } else {
                    showOptions(beanWeekAndDate);
                }

                Log.e("LYQ", str + "  ");
            }
        });

    }

    private void showOptions(final BeanWeekAndDate beanWeekAndDate) {
        TextView cancel;
        TextView confirm;
        TextView today;
        final ListView listView;
        View rootView;
        rootView = LayoutInflater.from(mContext).inflate(R.layout.popupwind_choice_appointment_time, null);
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
        today = (TextView) rootView.findViewById(R.id.today);
        confirm = (TextView) rootView.findViewById(R.id.confirm);
        listView = (ListView) rootView.findViewById(R.id.choose_time_listview);

        today.setText(beanWeekAndDate.getDate());

        selectTimeAdapter = new SelectTimeAdapter(mContext, timeList);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(selectTimeAdapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到下一页面
                int position = listView.getCheckedItemPosition();
                if (listView.INVALID_POSITION != position) {
                    BeanHospRegisterReq beanHospRegisterReq = beanWeekAndDate.getBeanHospRegisterReq();
                    beanHospRegisterReq.setDate(HospRegNumList.get(position).getHID());
                    beanHospRegisterReq.setDateTxt(HospRegNumList.get(position).getHZS());
                    beanWeekAndDate.setTime(timeList.get(position));
                    beanWeekAndDate.setBeanHospRegisterReq(beanHospRegisterReq);
                    Intent intent = new Intent(mContext, ConfirmReservationInformation.class);
                    intent.putExtra("BeanWeekAndDate", beanWeekAndDate);
                    mContext.startActivity(intent);
                    mPopWindow.dismiss();
                } else {
                    MyToast.showToast(mContext, "请选择预约时间！");
                }
            }
        });
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
                timeList.clear();
            }
        });
        mPopWindow.setAnimationStyle(R.style.AnimBottom);
        mPopWindow.showAtLocation(titleWeek, Gravity.BOTTOM, 0, 0);
    }


}
