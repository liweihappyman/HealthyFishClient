package com.healthyfish.healthyfish.eventbus;

import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;

/**
 * 描述：自动登录后用来通知初始化与用户相关的信息
 * 作者：LYQ on 2017/8/2.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class InitAllMessage {

    private BeanUserLoginReq beanUserLoginReq;

    public InitAllMessage() {
    }

    public InitAllMessage(BeanUserLoginReq beanUserLoginReq) {
        this.beanUserLoginReq = beanUserLoginReq;
    }

    public BeanUserLoginReq getBeanUserLoginReq() {
        return beanUserLoginReq;
    }

    public void setBeanUserLoginReq(BeanUserLoginReq beanUserLoginReq) {
        this.beanUserLoginReq = beanUserLoginReq;
    }
}
