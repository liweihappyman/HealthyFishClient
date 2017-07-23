package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanHospCardAuthReq;
import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanKeyValue;
import com.healthyfish.healthyfish.POJO.BeanWeekAndDate;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

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

    private String sick_id = null;
    private BeanWeekAndDate beanWeekAndDate;
    private String jsonStr = null;
    private String key = null;
    private String value = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_information);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "确认信息");
        iniData();
    }

    /**
     * 获取数据
     */
    private void iniData() {
        beanWeekAndDate = (BeanWeekAndDate) getIntent().getSerializableExtra("BeanWeekAndDate");
        tvHospital.setText("医院：" + beanWeekAndDate.getBeanHospRegisterReq().getHospTxt());
        tvDepartment.setText("科室：" + beanWeekAndDate.getBeanHospRegisterReq().getDeptTxt());
        tvAppointmentTime.setText("预约时间：" + beanWeekAndDate.getDate() + " " + beanWeekAndDate.getTime());
        tvDoctorName.setText("医生姓名：" + beanWeekAndDate.getBeanHospDeptDoctListRespItem().getDOCTOR_NAME());
        tvOutpatientType.setText("问诊类型：" + beanWeekAndDate.getBeanHospDeptDoctListRespItem().getREISTER_NAME());
        setTextColorAndSize(String.valueOf(beanWeekAndDate.getBeanHospDeptDoctListRespItem().getPRICE()));
    }

    @OnClick({R.id.bt_replace, R.id.bt_confirm_registration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_replace:
                //更换就诊人
                Intent intent = new Intent(this, ChangeVisitingPerson.class);
                startActivityForResult(intent, 10012);
                break;
            case R.id.bt_confirm_registration:
                //确认挂号，首先需要验证就诊卡
                if (CertifiedVisitingCard()) {
                    //挂号请求
                    if (RegistrationRequest()) {
                        MyToast.showToast(this,"挂号成功");
                    } else {
                        MyToast.showToast(this,"挂号失败，请重试");
                    }
                }
                break;
        }
    }

    /**
     * 设置字体颜色和大小
     */
    private void setTextColorAndSize(String price) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("挂号费：" + price + " 元");
        //设置字体大小
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(100);
        spannableString.setSpan(absoluteSizeSpan, 4, 4 + price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_secondary));
        spannableString.setSpan(colorSpan, 4, 4 + price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegistrationFee.setText(spannableString);
    }

    /**
     * 挂号请求
     */
    private boolean RegistrationRequest() {
        final BeanHospRegisterReq beanHospRegisterReq = beanWeekAndDate.getBeanHospRegisterReq();
        beanHospRegisterReq.setName(etVisitingPersonName.getText().toString().trim());
        beanHospRegisterReq.setSickId(sick_id);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospRegisterReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("LYQ", e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                //返回BeanKeyValue
                //成功的key："reg_"+uid+"_"+doctBean.getHosp()+"_"+DateTimeUtils.getTime()，value为""  可用于将来查询已挂号的信息。
                //失败的key:"failed"，value为失败的原因
                try {
                    jsonStr = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BeanKeyValue beanKeyValue = JSON.parseObject(jsonStr, BeanKeyValue.class);
                key = beanKeyValue.getKey();
                value = beanKeyValue.getValue();
                Log.e("LYQ", "挂号：" + value);
            }
        });
        if (key.equals("failed")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证就诊卡
     *
     * @return 是否验证成功
     */
    private boolean CertifiedVisitingCard() {
        if (etVisitingPersonName.getText().toString().length() > 0 && etVisitingCardNumber.getText().toString().length() > 0) {
            BeanHospCardAuthReq beanHospCardAuthReq = new BeanHospCardAuthReq();
            beanHospCardAuthReq.setHosp("柳州市中医院");
            beanHospCardAuthReq.setCardId(etVisitingCardNumber.getText().toString().trim());
            beanHospCardAuthReq.setName(etVisitingPersonName.getText().toString().trim());
            RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospCardAuthReq), new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    Log.e("LYQ", e.toString());
                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    try {
                        sick_id = responseBody.string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("LYQ", "验证就诊卡：" + sick_id);
                }
            });
            if (sick_id.length() > 0) {
                return true;
            } else {
                MyToast.showToast(ConfirmReservationInformation.this, "验证就诊卡失败，请确认您的信息重新验证");
                return false;
            }
        } else {
            MyToast.showToast(this, "请填写相关信息");
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
