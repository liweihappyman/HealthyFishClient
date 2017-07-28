package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanHospCardAuthReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListReq;
import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanKeyValue;
import com.healthyfish.healthyfish.POJO.BeanVisitingPerson;
import com.healthyfish.healthyfish.POJO.BeanWeekAndDate;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

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

    private String sick_id = "";
    private BeanWeekAndDate beanWeekAndDate;
    private BeanKeyValue beanKeyValue;
    private String jsonStr = null;
    private String key = "";
    private String value = "";

    private final int mRequestCode = 13302;
    private BeanVisitingPerson visitingPerson;

    private String id = MyApplication.uid;
    private String hospital = "柳州市中医院";
    private String name;
    private String idCard;
    private String visitingCard;
    private String phoneNumber;

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
        tvHospital.setText("医院：" + beanWeekAndDate.getBeanDoctorInfo().getHospital());
        tvDepartment.setText("科室：" + beanWeekAndDate.getBeanDoctorInfo().getDepartment());
        tvAppointmentTime.setText("预约时间：" + beanWeekAndDate.getDate() + " " + beanWeekAndDate.getTime());
        tvDoctorName.setText("医生姓名：" + beanWeekAndDate.getBeanDoctorInfo().getName());
        tvOutpatientType.setText("问诊类型：" + beanWeekAndDate.getBeanDoctorInfo().getDuties());
        setTextColorAndSize(beanWeekAndDate.getBeanDoctorInfo().getPrice());

//        tvDoctorName.setText("医生姓名：" + beanWeekAndDate.getBeanHospDeptDoctListRespItem().getDOCTOR_NAME());
//        tvOutpatientType.setText("问诊类型：" + beanWeekAndDate.getBeanHospDeptDoctListRespItem().getREISTER_NAME());
//        setTextColorAndSize(String.valueOf(beanWeekAndDate.getBeanHospDeptDoctListRespItem().getPRICE()));
    }

    @OnClick({R.id.bt_replace, R.id.bt_confirm_registration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_replace:
                //更换就诊人
                Intent intent = new Intent(this, ChangeVisitingPerson.class);
                startActivityForResult(intent, mRequestCode);
                break;
            case R.id.bt_confirm_registration:
                //确认挂号，首先需要验证就诊卡
                name = etVisitingPersonName.getText().toString().trim();
                idCard = etIDNumber.getText().toString().trim();
                visitingCard = etVisitingCardNumber.getText().toString().trim();
                phoneNumber = etPhoneNumber.getText().toString().trim();

                if (!TextUtils.isEmpty(name) &&
                        !TextUtils.isEmpty(idCard) && idCard.length() == 18 &&
                        !TextUtils.isEmpty(visitingCard) &&
                        !TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11) {

                    CertifiedVisitingCard();//验证就诊卡

                } else {
                    MyToast.showToast(this, "请填写相关信息或选择就诊人");
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
    private void RegistrationRequest() {
        final BeanHospRegisterReq beanHospRegisterReq = new BeanHospRegisterReq();
//        beanHospRegisterReq.setHosp(beanWeekAndDate.getBeanHospRegisterReq().getHosp());
//        beanHospRegisterReq.setHospTxt(beanWeekAndDate.getBeanHospRegisterReq().getHospTxt());
//        beanHospRegisterReq.setDept(beanWeekAndDate.getBeanHospRegisterReq().getDept());
//        beanHospRegisterReq.setDeptTxt(beanWeekAndDate.getBeanHospRegisterReq().getDeptTxt());
//        beanHospRegisterReq.setDoct(beanWeekAndDate.getBeanHospRegisterReq().getDoct());
//        beanHospRegisterReq.setDoctTxt(beanWeekAndDate.getBeanHospRegisterReq().getDoctTxt());
//        beanHospRegisterReq.setStaffNo(beanWeekAndDate.getBeanHospRegisterReq().getStaffNo());
//        beanHospRegisterReq.setDate(beanWeekAndDate.getBeanHospRegisterReq().getDate());
//        beanHospRegisterReq.setDateTxt(beanWeekAndDate.getBeanHospRegisterReq().getDateTxt());
        beanHospRegisterReq.setHosp(beanWeekAndDate.getBeanDoctorInfo().getHosp());
        beanHospRegisterReq.setHospTxt(beanWeekAndDate.getBeanDoctorInfo().getHospital());
        beanHospRegisterReq.setDept(beanWeekAndDate.getBeanDoctorInfo().getDept());
        beanHospRegisterReq.setDeptTxt(beanWeekAndDate.getBeanDoctorInfo().getDepartment());
        beanHospRegisterReq.setDoct(beanWeekAndDate.getBeanDoctorInfo().getDOCTOR());
        beanHospRegisterReq.setDoctTxt(beanWeekAndDate.getBeanDoctorInfo().getName());
        beanHospRegisterReq.setStaffNo(String.valueOf(beanWeekAndDate.getBeanDoctorInfo().getSTAFF_NO()));
        beanHospRegisterReq.setDate(beanWeekAndDate.getBeanHospRegisterReq().getDate());
        beanHospRegisterReq.setDateTxt(beanWeekAndDate.getBeanHospRegisterReq().getDateTxt());
        beanHospRegisterReq.setName(name);
        beanHospRegisterReq.setSickId(sick_id);

        Log.e("LYQ", "Hosp:" + beanHospRegisterReq.getHosp() + "--" + "HospTxt:" + beanHospRegisterReq.getHospTxt() + "--"
                + "Dept:" + beanHospRegisterReq.getDept() + "--" + "DeptTxt:" + beanHospRegisterReq.getDeptTxt() + "--"
                + "Doct:" + beanHospRegisterReq.getDoct() + "--" + "DoctTxt:" + beanHospRegisterReq.getDoctTxt() + "--"
                + "StaffNo:" + beanHospRegisterReq.getStaffNo() + "--" + "Date:" + beanHospRegisterReq.getDate() + "--"
                + "DateTxt:" + beanHospRegisterReq.getDateTxt() + "--" + "Name:" + beanHospRegisterReq.getName() + "--"
                + "SickId:" + beanHospRegisterReq.getSickId());
        Log.e("LYQ", JSON.toJSONString(beanHospRegisterReq));

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospRegisterReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                if (beanKeyValue != null) {
                    if (beanKeyValue.getKey().equals("failed")) {
                        MyToast.showToast(ConfirmReservationInformation.this, "挂号失败，" + beanKeyValue.getValue());
                        Log.e("LYQ", "挂号1：" + value);
                    } else {
                        MyToast.showToast(ConfirmReservationInformation.this, "挂号成功");
                        Log.e("LYQ", "挂号2：" + value);
                    }
                } else {
                    MyToast.showToast(ConfirmReservationInformation.this, "挂号失败，请重试");
                }
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
                Log.e("LYQ", "挂号666：" + jsonStr);
                beanKeyValue = JSON.parseObject(jsonStr, BeanKeyValue.class);
                Log.e("LYQ", "挂号：" + value);
            }
        });
    }

    /**
     * 验证就诊卡
     */
    private void CertifiedVisitingCard() {

        BeanHospCardAuthReq beanHospCardAuthReq = new BeanHospCardAuthReq();
        beanHospCardAuthReq.setHosp(hospital);
        beanHospCardAuthReq.setName(name);
        beanHospCardAuthReq.setCardId(visitingCard);
        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospCardAuthReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(sick_id)) {
                    RegistrationRequest();//挂号
                    saveData();
                    Log.e("LYQ", "挂号4：" + key + ":" + value);
                } else {
                    MyToast.showToast(ConfirmReservationInformation.this, "验证就诊卡失败，请确认您的信息重新验证");
                }
            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(ConfirmReservationInformation.this, "验证就诊卡失败，" + e.toString());
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
    }

    /**
     * 保存就诊人数据到数据库
     */
    private void saveData() {
        boolean isSave = false;
        List<BeanVisitingPerson> List = DataSupport.where("phoneID = ? and hospital = ? and visitingPerson = ? and visitingCard = ?", id, hospital, name, visitingCard).find(BeanVisitingPerson.class);
        if (List.size() == 0) {
            visitingPerson = new BeanVisitingPerson();
            visitingPerson.setPhoneId(id);
            visitingPerson.setHospital(hospital);
            visitingPerson.setVisitingPerson(name);
            visitingPerson.setIDCard(idCard);
            visitingPerson.setVisitingCard(visitingCard);
            visitingPerson.setPhoneNumber(phoneNumber);
            visitingPerson.setSick_id(sick_id);
            isSave = visitingPerson.save();
            if (!isSave) {
                MyToast.showToast(ConfirmReservationInformation.this, "保存就诊人信息失败");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode) {
            if (resultCode == ChangeVisitingPerson.mResultCode) {
                visitingPerson = (BeanVisitingPerson) data.getSerializableExtra("BeanVisitingPerson");
                etVisitingPersonName.setText(visitingPerson.getVisitingPerson());
                etIDNumber.setText(visitingPerson.getIDCard());
                etVisitingCardNumber.setText(visitingPerson.getVisitingCard());
                etPhoneNumber.setText(visitingPerson.getPhoneNumber());
            }
        }
    }
}
