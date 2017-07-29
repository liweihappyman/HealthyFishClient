package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanHospCardAuthReq;
import com.healthyfish.healthyfish.POJO.BeanVisitingPerson;
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
 * 描述：挂号新建就诊人页面
 * 作者：LYQ on 2017/7/10.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class NewVisitingPerson extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_visiting_person_name)
    EditText etVisitingPersonName;
    @BindView(R.id.et_ID_number)
    EditText etIDNumber;
    @BindView(R.id.et_visiting_card_number)
    EditText etVisitingCardNumber;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.bt_confirm_registration)
    Button btConfirmRegistration;
    @BindView(R.id.et_hospital)
    EditText etHospital;

    private BeanVisitingPerson visitingPerson;

    private String id = "15278898523";
    private String hospital = "柳州市中医院";
    private String name;
    private String idCard;
    private String visitingCard;
    private String phoneNumber;
    private String sick_id = null;

    public final static int resultCode = 10033;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visiting_person);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "新建就诊人");
        etHospital.setText(hospital);
        etHospital.setEnabled(false);
    }

    @OnClick(R.id.bt_confirm_registration)
    public void onViewClicked() {
        name = etVisitingPersonName.getText().toString().trim();
        idCard = etIDNumber.getText().toString().trim();
        visitingCard = etVisitingCardNumber.getText().toString().trim();
        phoneNumber = etPhoneNumber.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(idCard) && idCard.length() == 18) {
                if (!TextUtils.isEmpty(visitingCard)) {
                    if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11) {

                        CertifiedVisitingCard();//验证就诊人

                    } else {
                        MyToast.showToast(this, "请填写手机号并确认");
                    }
                } else {
                    MyToast.showToast(this, "请填写就诊卡号");
                }
            } else {
                MyToast.showToast(this, "请填写身份证号并确认");
            }
        } else {
            MyToast.showToast(this, "请填写姓名");
        }
    }

    /**
     * 验证就诊人
     */
    private void CertifiedVisitingCard() {
        BeanHospCardAuthReq beanHospCardAuthReq = new BeanHospCardAuthReq();
        beanHospCardAuthReq.setHosp(hospital);
        beanHospCardAuthReq.setCardId(visitingCard);
        beanHospCardAuthReq.setName(name);
        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospCardAuthReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(sick_id)) {
                    saveData();
                } else {
                    MyToast.showToast(NewVisitingPerson.this, "验证就诊卡失败，请确认您的信息重新验证");
                }
            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(NewVisitingPerson.this, "验证就诊卡失败，" + e.toString());
                Log.e("LYQ", "错误：" + sick_id);
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
        if (List.size() > 0) {
            MyToast.showToast(NewVisitingPerson.this, "就诊人已验证,无需再次验证");
            Log.e("LYQ", "已验证");
        } else {
            visitingPerson = new BeanVisitingPerson();
            visitingPerson.setPhoneId(id);
            visitingPerson.setHospital(hospital);
            visitingPerson.setVisitingPerson(name);
            visitingPerson.setIDCard(idCard);
            visitingPerson.setVisitingCard(visitingCard);
            visitingPerson.setPhoneNumber(phoneNumber);
            visitingPerson.setSick_id(sick_id);
            isSave = visitingPerson.save();
            if (isSave) {
                Intent intent = new Intent(NewVisitingPerson.this, ChangeVisitingPerson.class);
                intent.putExtra("BeanVisitingPerson", visitingPerson);
                NewVisitingPerson.this.setResult(resultCode, intent);
                MyToast.showToast(NewVisitingPerson.this, "验证就诊卡成功");
                finish();
            } else {
                MyToast.showToast(NewVisitingPerson.this, "保存就诊人信息失败，请重试");
            }
        }
    }

}
