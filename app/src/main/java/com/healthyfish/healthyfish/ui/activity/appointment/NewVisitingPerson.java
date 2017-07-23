package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.healthyfish.healthyfish.POJO.BeanHospCardAuthReq;
import com.healthyfish.healthyfish.POJO.BeanHospRegNumListRespItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

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

    private String sick_id = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visiting_person);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "新建就诊人");
    }

    @OnClick(R.id.bt_confirm_registration)
    public void onViewClicked() {
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
                    Log.e("LYQ", "验证就诊卡："+sick_id);
                    if (sick_id.length() > 0) {
                        //Intent intent = new Intent(NewVisitingPerson.this, ChangeVisitingPerson.class);
                        MyToast.showToast(NewVisitingPerson.this, "验证就诊卡成功");
                    } else {
                        MyToast.showToast(NewVisitingPerson.this, "验证就诊卡失败，请确认您的信息重新验证");
                    }
                }
            });
        } else {
            MyToast.showToast(this, "请填写相关信息");
        }
    }
}
