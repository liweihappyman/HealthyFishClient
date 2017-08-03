package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.MainActivity;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanConcernList;
import com.healthyfish.healthyfish.POJO.BeanPersonalInformation;
import com.healthyfish.healthyfish.POJO.BeanServiceList;
import com.healthyfish.healthyfish.POJO.BeanUserListReq;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.eventbus.EmptyMessage;
import com.healthyfish.healthyfish.ui.activity.personal_center.PersonalInformation;
import com.healthyfish.healthyfish.ui.fragment.PersonalCenterFragment;
import com.healthyfish.healthyfish.ui.presenter.login.LoginPresenter;
import com.healthyfish.healthyfish.ui.view.login.ILoginView;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.Sha256;
import com.healthyfish.healthyfish.utils.mqtt_utils.MqttUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;

/**
 * 描述：登录Activity
 * 作者：Wayne on 2017/6/28 16:11
 * 邮箱：liwei_happyman@qq.com
 * 编辑：LYQ
 */

public class Login extends AutoLayoutActivity implements ILoginView {

    @BindView(R.id.et_login_user_name)
    EditText etLoginUserName;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.login_progressBar)
    ProgressBar loginProgressBar;

    private LoginPresenter mLoginPresenter = new LoginPresenter(this);

    private BeanPersonalInformation beanPersonalInformation = new BeanPersonalInformation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void showProgressBar() {
        if (loginProgressBar.getVisibility() == View.GONE) {
            loginProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (loginProgressBar.getVisibility() == View.VISIBLE) {
            loginProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public String getUserName() {
        return etLoginUserName.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etLoginPassword.getText().toString().trim();
    }

    @Override
    public void toActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showFailedError() {
        MyToast.showToast(this, "登录失败");
    }

    @OnClick({R.id.btn_login, R.id.tv_forgot_password, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //mLoginPresenter.login();
                login();
                break;
            case R.id.tv_forgot_password:
                toResetPasswordActivity();
                break;
            case R.id.btn_register:
                toRegisterActivity();
                break;
        }
    }


    /**
     * 登录
     */
    private void login() {
        BeanUserLoginReq beanUserLoginReq = new BeanUserLoginReq();
        beanUserLoginReq.setMobileNo(getUserName());//号码
        beanUserLoginReq.setAct(BeanUserLoginReq.class.getSimpleName());//设置操作类型，不然服务器不知道
        beanUserLoginReq.setPwdSHA256(Sha256.getSha256(getPassword()));//密码

        final String user = JSON.toJSONString(beanUserLoginReq);//如果登录成功，将会由sharepreference保存

        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserLoginReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Login.this, "登录失败，请检查网络环境", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String str = null;
                        try {
                            str = responseBody.string();
                            Log.i("LYQ", "登录响应:" + str);
                            BeanBaseResp beanBaseResp = JSON.parseObject(str, BeanBaseResp.class);
                            int code = beanBaseResp.getCode();
                            judgeAndShowToast(code, user);//根据返回码做出相应的提示
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 根据返回码做出相应的提示
     */
    private void judgeAndShowToast(int code, String user) {
        if (code >= 0) {
            Toast.makeText(Login.this, "登录成功", Toast.LENGTH_LONG).show();

            MySharedPrefUtil.saveKeyValue("user", user);  //登录成功由shareprefrence保存
            MyApplication.uid = getUserName();
            upDatePersonalInformation(getUserName());
            upDateServiceListReq(getUserName());
            upDateMyConcern();

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (code == -1) {
            Toast.makeText(Login.this, "用户不存在", Toast.LENGTH_LONG).show();
        } else if (code == -2 || code == -5 || code == -3) {
            Toast.makeText(Login.this, "密码错误", Toast.LENGTH_LONG).show();
        } else if (code == -10) {
            Toast.makeText(Login.this, "操作次数过多", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(Login.this, "登录失败", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 跳转到注册界面
     */
    private void toRegisterActivity() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    /**
     * 跳转到重置密码界面
     */
    private void toResetPasswordActivity() {
        Intent intent = new Intent(this, ForgetPassword.class);
        startActivity(intent);
    }


    /**
     * 将用户的关注列表保存到数据库
     */
    private void upDateMyConcern() {
        final List<BeanConcernList> concernList = new ArrayList<>();

        BeanUserListReq beanUserListReq = new BeanUserListReq();
        beanUserListReq.setPrefix("care_" + getUserName());
        beanUserListReq.setFrom(0);
        beanUserListReq.setTo(-1);
        beanUserListReq.setNum(-1);

        RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (DataSupport.findAll(BeanConcernList.class).isEmpty()) {
                            for (BeanConcernList beanConcernList : concernList) {
                                if (!beanConcernList.save()) {
                                    for (BeanConcernList beanConcernList1 : concernList) {
                                        beanConcernList1.save();
                                    }
                                }
                            }
                        } else {
                            DataSupport.deleteAll(BeanConcernList.class);
                            for (BeanConcernList beanConcernList : concernList) {
                                if (!beanConcernList.save()) {
                                    for (BeanConcernList beanConcernList1 : concernList) {
                                        beanConcernList1.save();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("LYQ", "Login_upDateMyConcern_onError:" + e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<String> concerns = JSONArray.parseObject(jsonStr, List.class);
                        for (String str : concerns) {
                            BeanConcernList beanConcernList = new BeanConcernList();
                            beanConcernList.setKey(str);
                            concernList.add(beanConcernList);

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
        Log.i("LYQ", "KEY:" + key);

        RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {

            String resp = null;

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(resp)) {
                    BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(resp, BeanBaseKeyGetResp.class);

                    if (beanBaseKeyGetResp.getCode() == 0) {
                        //MyToast.showToast(Login.this, "获取个人信息成功");
                        String strJsonBeanPersonalInformation = beanBaseKeyGetResp.getValue();
                        if (!TextUtils.isEmpty(strJsonBeanPersonalInformation)) {
                            BeanPersonalInformation beanPersonalInformation = JSON.parseObject(strJsonBeanPersonalInformation, BeanPersonalInformation.class);

                            boolean isSave = beanPersonalInformation.saveOrUpdate("key = ?", key);
                            if (!isSave) {

                                if (!beanPersonalInformation.saveOrUpdate("key = ?", key)) {
                                    MyToast.showToast(Login.this, "保存个人信息失败");
                                }
                            }
                            beanPersonalInformation.setLogin(true);
                            EventBus.getDefault().post(beanPersonalInformation);//发送消息提醒刷新个人中心的个人信息
                        } else {
                            MyToast.showToast(Login.this, "您还没有填写个人信息，请填写您的个人信息");
                        }
                        EventBus.getDefault().post(new BeanPersonalInformation(true));//发送消息提醒刷新个人中心的个人信息
                    } else {
                        MyToast.showToast(Login.this, "获取个人信息失败");
                        EventBus.getDefault().post(new BeanPersonalInformation(true));//发送消息提醒刷新个人中心的个人信息
                    }
                } else {
                    MyToast.showToast(Login.this, "获取个人信息失败");
                    EventBus.getDefault().post(new BeanPersonalInformation(true));//发送消息提醒刷新个人中心的个人信息

                }
            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(Login.this, "获取个人信息失败,请更新您的个人信息");
                EventBus.getDefault().post(new BeanPersonalInformation(true));//发送消息提醒刷新个人中心的个人信息
                Log.i("LYQ", "获取个人信息失败，" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    resp = responseBody.string();
                    Log.i("LYQ", "个人信息：" + resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 从网络获取已购买的服务列表
     */
    private void upDateServiceListReq(String uid) {

        BeanUserListValueReq beanUserListValueReq = new BeanUserListValueReq();
        beanUserListValueReq.setPrefix("service_" + uid);
        beanUserListValueReq.setFrom(0);
        beanUserListValueReq.setTo(-1);
        beanUserListValueReq.setNum(-1);

        RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListValueReq), new Subscriber<ResponseBody>() {
            String strJson = "";

            @Override
            public void onCompleted() {
                List<String> strServiceList = JSONArray.parseObject(strJson, List.class);
                DataSupport.deleteAll(BeanServiceList.class);//清空数据库中旧的已购买服务列表
                for (String strService : strServiceList) {
                    BeanServiceList beanServiceList = JSON.parseObject(strService, BeanServiceList.class);
                    beanServiceList.save();//更新数据库中已购买服务列表
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "getServiceListReq()_onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strJson = responseBody.string();
                    Log.i("LYQ", "获取已购买服务响应：" + strJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
