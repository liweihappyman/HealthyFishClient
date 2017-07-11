package com.healthyfish.healthyfish.ui.view.register;

/**
 * 描述：注册交互接口
 * 作者：Wayne on 2017/6/28 11:22
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public interface IRegisterView {

    void showProgressBar();

    void hideProgressBar();

    String getPhoneNumber();

    String getVerificationCode();

    void toActivity();

    void ReadingClauseAndProtocol();

    void showFailedError();
}
