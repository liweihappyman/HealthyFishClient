package com.healthyfish.healthyfish.ui.presenter.login;

import android.os.Handler;

import com.healthyfish.healthyfish.model.login.ILoginModel;
import com.healthyfish.healthyfish.model.login.LoginModel;
import com.healthyfish.healthyfish.model.login.OnLoginListener;
import com.healthyfish.healthyfish.ui.view.login.ILoginView;

/**
 * 描述：登录Presenter
 * 作者：Wayne on 2017/6/28 11:16
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */


// TODO: 2017/6/28 实现登录Presenter
public class LoginPresenter {

    private ILoginView loginView;
    private ILoginModel loginModel;
    private Handler mHandler = new Handler();

    public LoginPresenter(ILoginView loginView){
        this.loginView = loginView;
        this.loginModel = new LoginModel();
    }

    public void login(){
        loginView.showProgressBar();
        loginModel.Login(loginView.getUserName(), loginView.getPassword(), new OnLoginListener() {
            @Override
            public void LoginSucess() {
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loginView.hideProgressBar();
                        loginView.toActivity();
                    }
                });
            }

            @Override
            public void LoginFailed() {
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loginView.hideProgressBar();
                        loginView.showFailedError();
                    }
                });
            }
        });
    }
}
