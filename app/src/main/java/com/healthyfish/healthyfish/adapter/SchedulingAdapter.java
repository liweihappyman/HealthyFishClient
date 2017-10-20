package com.healthyfish.healthyfish.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.POJO.BeanHospRegNumListReq;
import com.healthyfish.healthyfish.POJO.BeanHospRegNumListRespItem;
import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanWeekAndDate;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.appointment.ConfirmReservationInformation;
import com.healthyfish.healthyfish.utils.LoadingDialog;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：医生他院出诊时间ListView适配器
 * 作者：LYQ on 2017/10/17.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SchedulingAdapter extends BaseAdapter {

    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private BeanDoctorInfo beanDoctorInfo;
    private List<String> dateList = new ArrayList<>();

    private PopupWindow mPopWindow = null;
    private boolean isShow;
    private TextView cancel;
    private TextView confirm;
    private TextView today;
    private ListView listView;
    private View rootView;
    private TextView parentTextView;

    private SelectTimeAdapter selectTimeAdapter;
    private List<String> timeList = new ArrayList<>();
    private List<BeanHospRegNumListRespItem> HospRegNumList = new ArrayList<>();

    private LoadingDialog loadingDialog = null;

    public SchedulingAdapter(Activity mActivity, BeanDoctorInfo beanDoctorInfo) {
        this.mActivity = mActivity;
        this.beanDoctorInfo = beanDoctorInfo;
        this.dateList = beanDoctorInfo.getSchdList();
        mLayoutInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public Object getItem(int position) {
        return dateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_scheduling_lv, parent, false);
            holder = new ViewHolder(convertView);
            parentTextView = holder.tvTime;
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (dateList.get(position).endsWith("1")) {
            holder.tvTime.setText(dateList.get(position).replace("_1", " 上午"));
        } else {
            holder.tvTime.setText(dateList.get(position).replace("_2", " 下午"));
        }
        holder.btSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShow) {
                    loadingDialog = new LoadingDialog(mActivity, "加载中，请稍后...");
                    loadingDialog.setCanceledOnTouchOutside(false);
                    if (dateList.get(position).endsWith("1")) {
                        initData(dateList.get(position).split("_")[0], "1", "上午");
                    } else {
                        initData(dateList.get(position).split("_")[0], "2", "下午");
                    }
                }
            }
        });
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.bt_select_time)
        Button btSelectTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 加载具体的预约时间点
     *
     * @param date
     * @param Type
     * @param AmOrPm
     */

    private void initData(final String date, String Type, final String AmOrPm) {
        BeanHospRegNumListReq beanHospRegNumListReq = new BeanHospRegNumListReq();
        //beanHospRegNumListReq.setHosp("lzzyy");
        beanHospRegNumListReq.setHosp(beanDoctorInfo.getHosp());
        beanHospRegNumListReq.setDept(beanDoctorInfo.getDept());
        beanHospRegNumListReq.setDoct(beanDoctorInfo.getDOCTOR());
        beanHospRegNumListReq.setDate(date);
        beanHospRegNumListReq.setType(Type);

        String jsonStr = JSON.toJSONString(beanHospRegNumListReq);
        Log.e("LYQ", "jsonStr:" + jsonStr);

        RetrofitManagerUtils.getInstance(mActivity, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospRegNumListReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                if (timeList.isEmpty()) {
                    loadingDialog.dismiss();
                    MyToast.showToast(mActivity, "该时间段已经没有号源啦！");
                } else {
                    loadingDialog.dismiss();
                    showOptions(date);
                    isShow = true;
                }
            }

            @Override
            public void onError(Throwable e) {
                loadingDialog.dismiss();
                MyToast.showToast(mActivity, "请求预约时间失败，请检查您的网络状态");
                Log.e("LYQ", e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String str = null;
                try {
                    str = responseBody.string();
                    List<BeanHospRegNumListRespItem> beanHospRegNumListRespItemList = JSONArray.parseArray(str, BeanHospRegNumListRespItem.class);
                    for (BeanHospRegNumListRespItem beanHospRegNumListRespItem : beanHospRegNumListRespItemList) {
                        timeList.add(AmOrPm + "：" + beanHospRegNumListRespItem.getHZS().substring(11, 16));
                        HospRegNumList.add(beanHospRegNumListRespItem);
                    }
                    Log.e("LYQ", " str: " + str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 显示具体的可预约时间点
     */
    private void showOptions(final String date) {
        if (mPopWindow == null) {
            rootView = LayoutInflater.from(mActivity).inflate(R.layout.popupwind_choice_appointment_time, null);
            mPopWindow = new PopupWindow(rootView);
            mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopWindow.setTouchable(true);
            mPopWindow.setFocusable(true);
            mPopWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopWindow.setOutsideTouchable(true);
            mPopWindow.setAnimationStyle(R.style.AnimBottom);

            cancel = (TextView) rootView.findViewById(R.id.cancel);
            today = (TextView) rootView.findViewById(R.id.today);
            confirm = (TextView) rootView.findViewById(R.id.confirm);
            listView = (ListView) rootView.findViewById(R.id.choose_time_listview);
        }

        if (!isShow) {
            //设置窗口背景
            WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
            lp.alpha = 0.7f;
            mActivity.getWindow().setAttributes(lp);
            selectTimeAdapter = new SelectTimeAdapter(mActivity, timeList);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(selectTimeAdapter);
            mPopWindow.showAtLocation(parentTextView, Gravity.BOTTOM, 0, 0);
        }

        today.setText(date);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });

        final ListView finalListView = listView;

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到下一页面
                int position = finalListView.getCheckedItemPosition();
                if (finalListView.INVALID_POSITION != position) {

                    BeanHospRegisterReq beanHospRegisterReq = new BeanHospRegisterReq();
                    beanHospRegisterReq.setDate(HospRegNumList.get(position).getHID());
                    beanHospRegisterReq.setDateTxt(HospRegNumList.get(position).getHZS());

                    BeanWeekAndDate beanWeekAndDate = new BeanWeekAndDate();
                    beanWeekAndDate.setDate(date);
                    beanWeekAndDate.setTime(timeList.get(position));
                    beanWeekAndDate.setBeanDoctorInfo(beanDoctorInfo);
                    beanWeekAndDate.setBeanHospRegisterReq(beanHospRegisterReq);

                    Intent intent = new Intent(mActivity, ConfirmReservationInformation.class);
                    intent.putExtra("BeanWeekAndDate", beanWeekAndDate);
                    mActivity.startActivity(intent);
                    mPopWindow.dismiss();
                } else {
                    MyToast.showToast(mActivity, "请选择预约时间！");
                }
            }
        });
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
                timeList.clear();
                HospRegNumList.clear();
                isShow = false;
            }
        });
    }

}
