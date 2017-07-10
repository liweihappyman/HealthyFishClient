package com.healthyfish.healthyfish.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_visiting_person);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"新建就诊人");
    }

    @OnClick(R.id.bt_confirm_registration)
    public void onViewClicked() {

    }
}
