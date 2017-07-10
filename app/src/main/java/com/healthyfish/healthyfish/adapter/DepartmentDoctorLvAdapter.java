package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanDepartmentDoctor;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：挂号模块科室医生列表ListView适配器
 * 作者：LYQ on 2017/7/2.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class DepartmentDoctorLvAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BeanDepartmentDoctor> doctorInfos;

    public DepartmentDoctorLvAdapter(Context mContext, List<BeanDepartmentDoctor> doctorInfos) {
        this.mContext = mContext;
        this.doctorInfos = doctorInfos;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return doctorInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_department_doctor_lv, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            //对于listView，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(doctorInfos.get(position).getImgUrl()).error(R.mipmap.error).into(holder.civDoctorImg);
        holder.tvDoctorName.setText(doctorInfos.get(position).getName());
        holder.tvDoctorDepartment.setText(doctorInfos.get(position).getDepartment());
        holder.tvDoctorTitle.setText(doctorInfos.get(position).getDuties());
        holder.tvHospital.setText(doctorInfos.get(position).getHospital());
        holder.tvDoctorInfo.setText(doctorInfos.get(position).getIntroduce());
        if (doctorInfos.get(position).getAvailable()){
            holder.tvIsAvailable.setText("近期有号");
            holder.tvIsAvailable.setBackgroundResource(R.drawable.rounded_rectangle_secondary);
        }else {
            holder.tvIsAvailable.setText("近期无号");
            holder.tvIsAvailable.setBackgroundResource(R.drawable.rounded_rectangle_gray);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.civ_doctor_img)
        CircleImageView civDoctorImg;//医生头像
        @BindView(R.id.tv_doctorName)
        TextView tvDoctorName;//医生姓名
        @BindView(R.id.tv_doctorDepartment)
        TextView tvDoctorDepartment;//医生所在科室
        @BindView(R.id.tv_doctorTitle)
        TextView tvDoctorTitle;//医生职务
        @BindView(R.id.tv_hospital)
        TextView tvHospital;//医生所在医院
        @BindView(R.id.tv_doctorInfo)
        TextView tvDoctorInfo;//医生简介
        @BindView(R.id.tv_is_available)
        TextView tvIsAvailable;//医生近期是否有号

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
