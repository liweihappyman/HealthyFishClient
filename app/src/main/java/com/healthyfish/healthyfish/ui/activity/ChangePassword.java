package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.MainActivity;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanPersonalInformation;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.POJO.BeanUserRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanUserRegisterResp;
import com.healthyfish.healthyfish.POJO.BeanUserSmsAuthReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.eventbus.EmptyMessage;
import com.healthyfish.healthyfish.eventbus.InitAllMessage;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.Sha256;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：修改密码页面
 * 作者：LYQ on 2017/7/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChangePassword extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.et_verify_password)
    EditText etVerifyPassword;
    @BindView(R.id.bt_done)
    Button btDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "修改密码");

    }

    @OnClick(R.id.bt_done)
    public void onViewClicked() {
        //需要判断两次输入的密码是否一致，并上传服务器
        if (!etInputPassword.getText().toString().equals(etVerifyPassword.getText().toString())) {
            Toast.makeText(ChangePassword.this, "输入的密码不相同", Toast.LENGTH_LONG).show();
        } else {
            final BeanUserRegisterReq beanUserRegisterReq = (BeanUserRegisterReq) getIntent().getSerializableExtra("find_password");
            beanUserRegisterReq.setPwdSHA256(Sha256.getSha256(etInputPassword.getText().toString()));
            beanUserRegisterReq.setAct(BeanUserRegisterReq.class.getSimpleName());

            BeanUserLoginReq beanUserLoginReq = new BeanUserLoginReq();
            beanUserLoginReq.setMobileNo(beanUserRegisterReq.getMobileNo());
            beanUserLoginReq.setPwdSHA256(Sha256.getSha256(etInputPassword.getText().toString()));
            final String user = JSON.toJSONString(beanUserLoginReq);

            RetrofitManagerUtils.getInstance(this, null)
                    .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserRegisterReq), new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(ChangePassword.this, "请检查网络环境", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            String str = null;
                            try {
                                str = responseBody.string();
                                BeanBaseResp beanBaseResp = JSON.parseObject(str, BeanBaseResp.class);
                                Log.i("LYQ", "修改响应:" + str);
                                int code = beanBaseResp.getCode();
                                if (code >= 0) {
                                    Toast.makeText(ChangePassword.this, "修改成功", Toast.LENGTH_LONG).show();
                                    MySharedPrefUtil.saveKeyValue("user", user);
                                    autoLogin(beanUserRegisterReq.getMobileNo(), Sha256.getSha256(etInputPassword.getText().toString()));
                                    Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                                    startActivity(intent);
                                    EventBus.getDefault().post(new InitAllMessage());//通知首页初始化
                                    finish();
                                } else {
                                    Toast.makeText(ChangePassword.this, "修改失败", Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * 用于修改密码后自动登录
     *
     * @param mobileNo       手机号
     * @param sha256Password 加密后的密码
     */
    private void autoLogin(final String mobileNo, String sha256Password) {

        BeanUserLoginReq beanUserLoginReq = new BeanUserLoginReq();
        beanUserLoginReq.setMobileNo(mobileNo);//号码
        beanUserLoginReq.setPwdSHA256(sha256Password);//密码

        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserLoginReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String str = null;
                        try {
                            str = responseBody.string();
                            Log.i("LYQ", "登录响应:" + str);
                            BeanBaseResp beanBaseResp = JSON.parseObject(str, BeanBaseResp.class);
                            int code = beanBaseResp.getCode();
                            if (code >= 0) {
                                Toast.makeText(ChangePassword.this, "自动登录成功", Toast.LENGTH_LONG).show();
                                MyApplication.uid = mobileNo;
                                upDatePersonalInformation(mobileNo);

                            } else if (code == -1) {
                                //Toast.makeText(ChangePassword.this, "用户不存在", Toast.LENGTH_LONG).show();
                            } else if (code == -2 || code == -5 || code == -3) {
                                //Toast.makeText(ChangePassword.this, "密码错误", Toast.LENGTH_LONG).show();
                            } else if (code == -10) {
                                //Toast.makeText(ChangePassword.this, "操作次数过多", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ChangePassword.this, "自动登录失败", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 从网络获取个人信息
     */
    private void upDatePersonalInformation(String uid) {
        final String key = "info_" + uid;
        BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
        beanBaseKeyGetReq.setKey(key);
        RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {
            String resp = null;

            @Override
            public void onCompleted() {
                EventBus.getDefault().post(new BeanPersonalInformation(true));//发送消息提醒刷新个人中心的个人信息
            }

            @Override
            public void onError(Throwable e) {
                EventBus.getDefault().post(new BeanPersonalInformation(true));
            }

            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    resp = responseBody.string();
                    Log.i("LYQ", "个人信息：" + resp);
                    if (!TextUtils.isEmpty(resp)) {
                        BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(resp, BeanBaseKeyGetResp.class);
                        if (beanBaseKeyGetResp.getCode() == 0) {
                            MyApplication.isIsFirstUpdatePersonalInfo = false;
                            String strJsonBeanPersonalInformation = beanBaseKeyGetResp.getValue();
                            if (!TextUtils.isEmpty(strJsonBeanPersonalInformation)) {
                                BeanPersonalInformation beanPersonalInformation = JSON.parseObject(strJsonBeanPersonalInformation, BeanPersonalInformation.class);
                                beanPersonalInformation.saveOrUpdate("key = ?", key);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
