package com.healthyfish.healthyfish.eventbus;

import com.healthyfish.healthyfish.POJO.BeanMyAppointmentItem;

/**
 * 描述：
 * 作者：LYQ on 2017/10/20.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class DeleteAppointmentMsg {
    private BeanMyAppointmentItem beanMyAppointmentItem;

    public DeleteAppointmentMsg(BeanMyAppointmentItem beanMyAppointmentItem) {
        this.beanMyAppointmentItem = beanMyAppointmentItem;
    }

    public BeanMyAppointmentItem getBeanMyAppointmentItem() {
        return beanMyAppointmentItem;
    }

    public void setBeanMyAppointmentItem(BeanMyAppointmentItem beanMyAppointmentItem) {
        this.beanMyAppointmentItem = beanMyAppointmentItem;
    }
}
