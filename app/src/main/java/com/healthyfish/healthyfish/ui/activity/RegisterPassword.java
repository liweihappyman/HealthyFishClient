package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.POJO.BeanUserRegisterReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.eventbus.EmptyMessage;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：注册填写昵称的密码页面
 * 作者：LYQ on 2017/7/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class RegisterPassword extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_input_nickname)
    EditText etInputNickname;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.et_verify_password)
    EditText etVerifyPassword;
    @BindView(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_nickname_and_password);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "注册");
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {

        if (!etInputPassword.getText().toString().equals(etVerifyPassword.getText().toString())) {
            Toast.makeText(this, "输入密码不相同", Toast.LENGTH_LONG).show();
        } else {
            //注册请求的bean
            BeanUserRegisterReq beanUserRegisterReq = (BeanUserRegisterReq) getIntent().getSerializableExtra("user");
            beanUserRegisterReq.setAct(BeanUserRegisterReq.class.getSimpleName());
            Log.i("电话号码：", beanUserRegisterReq.getMobileNo());
            beanUserRegisterReq.setPwdSHA256(etInputPassword.getText().toString());
            beanUserRegisterReq.setType(0);//新注册用户
            //待保存的bean，如果登录成功，将会由sharepreference保存如下bean
            BeanUserLoginReq beanUserLoginReq = new BeanUserLoginReq();
            beanUserLoginReq.setMobileNo(beanUserRegisterReq.getMobileNo());
            beanUserLoginReq.setPwdSHA256(beanUserRegisterReq.getPwdSHA256());
            final String user = JSON.toJSONString(beanUserLoginReq);

            RetrofitManagerUtils.getInstance(this, null)
                    .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserRegisterReq), new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(RegisterPassword.this, "注册失败，请检查网络环境", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            String str = null;
                            try {
                                str = responseBody.string();
                                BeanBaseResp beanBaseResp = JSON.parseObject(str, BeanBaseResp.class);
                                int code = beanBaseResp.getCode();
                                if (code >=0) {
                                    Toast.makeText(RegisterPassword.this, "注册成功", Toast.LENGTH_LONG).show();
                                    saveUsrBean(user);
                                    //————————————————————————————————————————
                                    EventBus.getDefault().post(new EmptyMessage());//注册成功通知更新登录状态
                                    Intent intent = new Intent(RegisterPassword.this, RegisterSuccess.class);
                                    startActivity(intent);
                                    //—————————————————————————————————————————————
                                }else {
                                    Toast.makeText(RegisterPassword.this, "注册失败", Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }


    }

    /**
     * 注册成功后保存用户信息
     * @param user
     */
    private void saveUsrBean(String user) {
        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("user",user);
        editor.commit();
    }


}
