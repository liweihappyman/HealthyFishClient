package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthyfish.healthyfish.MainActivity;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.presenter.login.LoginPresenter;
import com.healthyfish.healthyfish.ui.view.login.ILoginView;
import com.healthyfish.healthyfish.utils.MyToast;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：登录Activity
 * 作者：Wayne on 2017/6/28 16:11
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void showProgressBar() {
        if (loginProgressBar.getVisibility() == View.GONE){
            loginProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (loginProgressBar.getVisibility() == View.VISIBLE){
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
        MyToast.showToast(this,"登录失败");
    }

    @OnClick({R.id.btn_login, R.id.tv_forgot_password, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mLoginPresenter.login();
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
     * 跳转到注册界面
     * */
    private void toRegisterActivity() {
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }

    /**
     * 跳转到重置密码界面
     * */
    private void toResetPasswordActivity() {
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }
}
