package com.healthyfish.healthyfish.POJO;

import java.io.Serializable;

/**
 * 描述：传递到图文咨询聊天页面的所需医生信息
 * 作者：LYQ on 2017/7/31.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class BeanDoctorChatInfo implements Serializable {

    private String name;
    private String phone;
    private String imgUrl;
    // 所选服务类型
    private String serviceType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
