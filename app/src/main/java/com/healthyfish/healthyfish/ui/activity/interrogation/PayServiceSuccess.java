package com.healthyfish.healthyfish.ui.activity.interrogation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanDoctorChatInfo;
import com.healthyfish.healthyfish.POJO.BeanInterrogationServiceDoctorList;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.AutoLogin;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
import com.healthyfish.healthyfish.utils.mqtt_utils.MqttUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;

/**
 * 描述：购买服务支付成功页面
 * 作者：LYQ on 2017/7/5.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PayServiceSuccess extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_name_type)
    TextView tvNameType;
    @BindView(R.id.tv_service_time)
    TextView tvServiceTime;
    @BindView(R.id.btn_next)
    Button btnNext;

    private Intent intent;
    private Bundle bundleShopType;
    private String shopType;
    private String strPayPrice;
    private String doctorName;
    private String serviceFinishTime;

    private BeanDoctorChatInfo beanDoctorChatInfo = new BeanDoctorChatInfo();
    private BeanInterrogationServiceDoctorList beanInterrogationServiceDoctorList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_success);
        ButterKnife.bind(this);
        initToolBar(toolbar, tvTitle, "购买成功");
        initData();
        // 添加用户已购买服务的医生列表
        addPictureConsultServiceDoctorList();
    }

    /**
     * 获取上一页面的参数并初始化数据
     */
    private void initData() {
        intent = this.getIntent();
        if (intent.getExtras() != null) {
            bundleShopType = intent.getExtras();
            shopType = bundleShopType.getString("serviceType");
            doctorName = bundleShopType.getString("name");
            serviceFinishTime = bundleShopType.getString("serviceFinishTime");
            beanDoctorChatInfo = (BeanDoctorChatInfo) bundleShopType.getSerializable("BeanDoctorChatInfo");
        }
        tvNameType.setText(doctorName + "医生" + "-" + shopType);
        if (shopType.equals("私人医生")) {
            tvServiceTime.setText(serviceFinishTime);
            beanDoctorChatInfo.setServiceType("privateDoctor");
            //tvServiceTime.setVisibility(View.GONE);
        } else if (shopType.equals("图文咨询")) {
            tvServiceTime.setText(serviceFinishTime);
            beanDoctorChatInfo.setServiceType("pictureConsulting");
            //tvServiceTime.setVisibility(View.GONE);//目前需求不需要展示服务到期时间
        } else {
            tvServiceTime.setVisibility(View.GONE);
        }
    }

    /**
     * 添加用户已购买服务的医生列表
     */
    private void addPictureConsultServiceDoctorList() {
        beanInterrogationServiceDoctorList = new BeanInterrogationServiceDoctorList();
        beanInterrogationServiceDoctorList.setDoctorNumber(beanDoctorChatInfo.getPhone());
        beanInterrogationServiceDoctorList.setDoctorName(beanDoctorChatInfo.getName());
        beanInterrogationServiceDoctorList.setDoctorPortrait(HttpHealthyFishyUrl + beanDoctorChatInfo.getImgUrl());
        // TODO: 2017/8/7 医院信息
        beanInterrogationServiceDoctorList.setDoctorHostipal("柳州市中医院");

        beanInterrogationServiceDoctorList.save();
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        if (shopType.equals("私人医生")) {
            jumpTo(PerfectArchives.class);
        } else if (shopType.equals("图文咨询")) {

            Log.i("LYQ", beanDoctorChatInfo.getName() + beanDoctorChatInfo.getPhone() + beanDoctorChatInfo.getImgUrl() + beanDoctorChatInfo.getServiceType());
            Intent intent = new Intent(this, HealthyChat.class);
            intent.putExtra("BeanDoctorChatInfo", beanDoctorChatInfo);

            if (!TextUtils.isEmpty(MySharedPrefUtil.getValue("sid"))) {
                AutoLogin.autoLogin();
                MqttUtil.startAsync();
            }
            startActivity(intent);

        }

    }

    /**
     * 页面跳转
     *
     * @param cla
     */
    public void jumpTo(Class<? extends Activity> cla) {
        Intent intent = new Intent(this, cla);
        startActivity(intent);
    }

}
