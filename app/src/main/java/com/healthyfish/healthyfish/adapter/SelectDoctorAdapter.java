package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：
 * 作者：WKJ on 2017/7/6.
 * 邮箱：
 * 编辑：WKJ
 */

public class SelectDoctorAdapter extends RecyclerView.Adapter<SelectDoctorAdapter.ViewHolder> {

    private Context mContext;
    private List<BeanDoctorInfo> list;

    public SelectDoctorAdapter(Context mContext, List<BeanDoctorInfo> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_select_doctor, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BeanDoctorInfo beanDoctorInfo = list.get(position);
        holder.name.setText(beanDoctorInfo.getName());
        holder.doctorType.setText(beanDoctorInfo.getIntroduce());
        holder.type.setText(beanDoctorInfo.getDepartment());
        holder.hospital.setText(beanDoctorInfo.getHospital());
        holder.date.setText("2017年10月20日");

            Glide.with(mContext)
                    .load(beanDoctorInfo.getImgUrl())
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(holder.headPortrait);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView headPortrait;
        private TextView name;
        private TextView doctorType;
        private TextView type;
        private TextView hospital;
        private TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            headPortrait = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.name);
            doctorType = (TextView) itemView.findViewById(R.id.type_doctor);
            type = (TextView) itemView.findViewById(R.id.type);
            hospital = (TextView) itemView.findViewById(R.id.hospital);
            date = (TextView) itemView.findViewById(R.id.date);

        }
    }
}
