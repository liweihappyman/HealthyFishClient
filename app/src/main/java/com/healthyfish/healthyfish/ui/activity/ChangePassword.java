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
import com.healthyfish.healthyfish.POJO.BeanUserRegisterResp;
import com.healthyfish.healthyfish.POJO.BeanUserSmsAuthReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.eventbus.EmptyMessage;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
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
        initToolBar(toolbar,toolbarTitle,"修改密码");

    }

    @OnClick(R.id.bt_done)
    public void onViewClicked() {
        //需要判断两次输入的密码是否一致，并上传服务器
        if (!etInputPassword.getText().toString().equals(etVerifyPassword.getText().toString())){
            Toast.makeText(ChangePassword.this,"输入的密码不相同",Toast.LENGTH_LONG).show();
        }else {
            BeanUserRegisterReq beanUserRegisterReq = (BeanUserRegisterReq) getIntent().getSerializableExtra("find_password");
            beanUserRegisterReq.setPwdSHA256(Sha256.getSha256(etInputPassword.getText().toString()));
            beanUserRegisterReq.setAct(BeanUserRegisterReq.class.getSimpleName());

            BeanUserLoginReq beanUserLoginReq = new BeanUserLoginReq();
            beanUserLoginReq.setMobileNo(beanUserRegisterReq.getMobileNo());
            beanUserLoginReq.setPwdSHA256(Sha256.getSha256(etInputPassword.getText().toString()));
            final String user = JSON.toJSONString(beanUserLoginReq);

            RetrofitManagerUtils.getInstance(this,null)
                    .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserRegisterReq), new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                        }
                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(ChangePassword.this,"请检查网络环境",Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onNext(ResponseBody responseBody) {
                            String str = null;
                            try {
                                str = responseBody.string();
                                BeanBaseResp beanBaseResp = JSON.parseObject(str,BeanBaseResp.class);
                                int code = beanBaseResp.getCode();
                                if (code >= 0){
                                    Toast.makeText(ChangePassword.this,"修改成功",Toast.LENGTH_LONG).show();
                                    MySharedPrefUtil.saveKeyValue("user",user);
                                    EventBus.getDefault().post(new EmptyMessage());
                                    Intent intent = new Intent(ChangePassword.this, ChangePasswordSuccess.class);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(ChangePassword.this,"修改失败",Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


//    /**
//     *     登录成功由shareprefrence保存
//     */
//    private void saveUserBean(String user) {
//        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.putString("user",user);
//        editor.commit();
//    }
}
