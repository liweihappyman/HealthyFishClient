package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanMyAppointmentItem;
import com.healthyfish.healthyfish.POJO.BeanUserRegCancelReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;

/**
 * 描述：我的挂号列表适配器
 * 作者：LYQ on 2017/8/1.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class MyAppointmentLvAdapter extends BaseAdapter {

    private Context mContext;
    private List<BeanMyAppointmentItem> appointmentList = new ArrayList<>();

    public MyAppointmentLvAdapter(Context mContext, List<BeanMyAppointmentItem> appointmentList) {
        this.mContext = mContext;
        this.appointmentList = appointmentList;
    }

    @Override
    public int getCount() {
        return appointmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_appointment, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(HttpHealthyFishyUrl + appointmentList.get(position).getImgUrl()).error(R.mipmap.error).into(holder.civDoctor);
        holder.tvDoctorName.setText(appointmentList.get(position).getDoctorName());
        holder.tvRoomAndDuties.setText("诊室：" + appointmentList.get(position).getConsultationRoom() + "  " + appointmentList.get(position).getDutise());
        holder.tvHospital.setText(appointmentList.get(position).getHospital());
        holder.tvAppointmentTime.setText("预约时间：" + appointmentList.get(position).getAppointmentTime());
        holder.tvVisitingPerson.setText("就诊人：" + appointmentList.get(position).getVisitingPerson());

        holder.btCancelAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelDialog(position);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.civ_doctor)
        CircleImageView civDoctor;
        @BindView(R.id.tv_doctorName)
        TextView tvDoctorName;
        @BindView(R.id.tv_room_and_duties)
        TextView tvRoomAndDuties;
        @BindView(R.id.tv_hospital)
        TextView tvHospital;
        @BindView(R.id.tv_appointment_time)
        TextView tvAppointmentTime;
        @BindView(R.id.tv_visitingPerson)
        TextView tvVisitingPerson;
        @BindView(R.id.bt_cancel_appointment)
        Button btCancelAppointment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 取消挂号提示对话框
     */
    private void showCancelDialog(final int position) {
        new AlertDialog.Builder(mContext).setMessage("您确定要取消挂号吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelAppointmentReq(position);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 取消挂号
     *
     * @param position
     */
    private void cancelAppointmentReq(final int position) {
        String key = appointmentList.get(position).getRespKey();

        BeanUserRegCancelReq beanUserRegCancelReq = new BeanUserRegCancelReq();
        beanUserRegCancelReq.setKey(key);

        RetrofitManagerUtils.getInstance(mContext, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserRegCancelReq), new Subscriber<ResponseBody>() {
            String cancelAppointmentResp = "";

            @Override
            public void onCompleted() {
                //BeanBaseResp beanBaseResp = JSON.parseObject(cancelAppointmentResp, BeanBaseResp.class);
                if (cancelAppointmentResp.equals("success")) {
                    MyToast.showToast(mContext, "成功取消挂号");
                    appointmentList.remove(position);
                    MyAppointmentLvAdapter.this.notifyDataSetChanged();
                } else {
                    MyToast.showToast(mContext, "取消挂号失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(mContext, "取消挂号失败");
                Log.i("LYQ", "cancelAppointmentResp()_onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    cancelAppointmentResp = responseBody.string();
                    Log.i("LYQ", "cancelAppointmentResp:" + cancelAppointmentResp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
