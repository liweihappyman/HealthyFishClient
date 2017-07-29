package com.healthyfish.healthyfish.ui.activity.personal_center;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：个人中心个人信息页面
 * 作者：LYQ on 2017/7/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PersonalInformation extends BaseActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.civ_head_portrait)
    CircleImageView civHeadPortrait;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_birthDate)
    TextView tvBirthDate;
    @BindView(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_idCard)
    TextView tvIdCard;
    @BindView(R.id.iv_go)
    ImageView ivGo;
    @BindView(R.id.lly_id_card)
    AutoLinearLayout llyIdCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "个人信息");
    }

    @OnClick({R.id.tv_change, R.id.iv_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change:
                Intent intent = new Intent(this, ChangePersonalInformation.class);
                startActivity(intent);
                break;
            case R.id.iv_go:
                break;
        }
    }
}
