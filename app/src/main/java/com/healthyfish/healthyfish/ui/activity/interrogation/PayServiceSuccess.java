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
import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeySetReq;
import com.healthyfish.healthyfish.POJO.BeanDoctorChatInfo;
import com.healthyfish.healthyfish.POJO.BeanInterrogationServiceDoctorList;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.POJO.ImMsgBean;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.service.WeChatUploadImage;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.AutoLogin;
import com.healthyfish.healthyfish.utils.DateTimeUtil;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.mqtt_utils.MqttUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

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
    private String topic;
    private String sender;
    private BeanUserLoginReq beanUserLoginReq;

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
        beanUserLoginReq = JSON.parseObject(MySharedPrefUtil.getValue("user"), BeanUserLoginReq.class);
        topic = "d" + beanDoctorChatInfo.getPhone();
        //topic = "d" + "13977211042";
        sender = "u" + beanUserLoginReq.getMobileNo();
    }

    /**
     * 添加用户已购买服务的医生列表
     */
    private void addPictureConsultServiceDoctorList() {
        beanInterrogationServiceDoctorList = new BeanInterrogationServiceDoctorList();
        beanInterrogationServiceDoctorList.setDoctorNumber(beanDoctorChatInfo.getPhone());
        beanInterrogationServiceDoctorList.setDoctorName(beanDoctorChatInfo.getName());
        beanInterrogationServiceDoctorList.setDoctorPortrait(beanDoctorChatInfo.getImgUrl());
        // TODO: 2017/8/7 医院信息
        beanInterrogationServiceDoctorList.setDoctorHostipal("柳州市中医院");
        if (DataSupport.where("DoctorNumber = ?", beanDoctorChatInfo.getPhone()).find(BeanInterrogationServiceDoctorList.class).isEmpty()) {
            beanInterrogationServiceDoctorList.save();
        }
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

            // 发送系统消息，建立与医生的会话
            sendSystemInfoToConnectWithDoctor();
            startActivity(intent);

        }

    }

    // 发送系统消息，建立与医生的会话
    private void sendSystemInfoToConnectWithDoctor() {

        ImMsgBean bean = new ImMsgBean();
        bean.setName(sender);
        bean.setSender(true);// 是否是发送者
        bean.setTime(DateTimeUtil.getLongMs());// 发送时间
        bean.setContent("[sys]");
        bean.setTopic(topic);

        bean.setType("$");// 类型：文字

        // MQTT发送数据
        MqttUtil.sendTxt(bean);

        // 保存与医生建立的会话关系
        saveChatInfoWithDoctor();

    }

    // 保存与医生建立的会话关系
    private void saveChatInfoWithDoctor() {
        BeanBaseKeySetReq beanBaseKeySetReq = new BeanBaseKeySetReq();
        beanBaseKeySetReq.setKey("chan_" + topic.substring(1) + "_" + sender);
        beanBaseKeySetReq.setValue(sender);
        RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeySetReq), new Subscriber<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Log.e("返回数据", responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
