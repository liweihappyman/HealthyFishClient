package com.healthyfish.healthyfish.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanPrescription;
import com.healthyfish.healthyfish.R;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 描述：健康计划
 * 作者：WKJ on 2017/7/20.
 * 邮箱：
 * 编辑：WKJ
 */

public class PrescriptionRvAdapter extends RecyclerView.Adapter<PrescriptionRvAdapter.ViewHolder> {


    private Context mContext;
    private List<BeanPrescription> list;
    private Toolbar toolbar;

    public PrescriptionRvAdapter(Context mContext, List<BeanPrescription> list, Toolbar toolbar) {
        this.mContext = mContext;
        this.list = list;
        this.toolbar = toolbar;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_prescription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BeanPrescription item = list.get(position);
        holder.ipresDiagnosisName.setText(item.getTitle());
        holder.ipresWriteTime.setText(item.getRef());
        holder.ipresSickName.setText(item.getName());
        holder.ipresSex.setText(item.getSymptom());
        holder.ipresAge.setText(item.getAbbr());
        holder.ipresDeptOperator.setText(item.getAttending());
        holder.ipresPerscribeStatus.setText(item.getComp());
        holder.ipresPhysicName.setText(item.getEffect());
        holder.drugNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ipres_diagnosis_name)
        TextView ipresDiagnosisName;
        @BindView(R.id.ipres_write_time)
        TextView ipresWriteTime;
        @BindView(R.id.ipres_sick_name)
        TextView ipresSickName;
        @BindView(R.id.ipres_sex)
        TextView ipresSex;
        @BindView(R.id.ipres_age)
        TextView ipresAge;
        @BindView(R.id.ipres_dept_operator)
        TextView ipresDeptOperator;
        @BindView(R.id.ipres_perscribe_status)
        TextView ipresPerscribeStatus;
        @BindView(R.id.ipres_physic_name)
        TextView ipresPhysicName;
        @BindView(R.id.drug_name_layout)
        AutoLinearLayout drugNameLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.auto(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private void showOptions() {
        TextView close;
        View rootView;
        rootView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_drug_instructions,
                null);
        final PopupWindow mPopWindow = new PopupWindow(rootView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setAnimationStyle(R.style.PopupRightAnimation);
        mPopWindow.showAsDropDown(toolbar, 380, 0);
        close = (TextView) rootView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
    }

}
