package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：挂号模块确认预约信息页面
 * 作者：LYQ on 2017/7/10.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ConfirmReservationInformation extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.bt_replace)
    Button btReplace;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_appointment_time)
    TextView tvAppointmentTime;
    @BindView(R.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R.id.tv_outpatient_type)
    TextView tvOutpatientType;
    @BindView(R.id.et_visiting_person_name)
    EditText etVisitingPersonName;
    @BindView(R.id.et_ID_number)
    EditText etIDNumber;
    @BindView(R.id.et_visiting_card_number)
    EditText etVisitingCardNumber;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.tv_registration_fee)
    TextView tvRegistrationFee;
    @BindView(R.id.bt_confirm_registration)
    Button btConfirmRegistration;

    private String price = "20";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_information);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"确认信息");
        setTextColorAndSize(price);
    }

    @OnClick({R.id.bt_replace, R.id.bt_confirm_registration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_replace:
                //更换就诊人
                Intent intent = new Intent(this, ChangeVisitingPerson.class);
                startActivity(intent);
                break;
            case R.id.bt_confirm_registration:
                //确认挂号
                break;
        }
    }

    /**
     * 设置字体颜色和大小
     */
    private void setTextColorAndSize(String price) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("挂号费："+price+" 元");
        //设置字体大小
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(100);
        spannableString.setSpan(absoluteSizeSpan, 4, 4+price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_secondary));
        spannableString.setSpan(colorSpan, 4, 4+price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegistrationFee.setText(spannableString);
    }
}
