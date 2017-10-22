package com.healthyfish.healthyfish.eventbus;

/**
 * 描述：挂号成功后通知我的挂号页面刷新数据
 * 作者：LYQ on 2017/10/20.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class RefreshMyAppointmentMsg {
    private String respKey;//挂号成功返回的key

    public RefreshMyAppointmentMsg(String respKey) {
        this.respKey = respKey;
    }

    public String getRespKey() {
        return respKey;
    }

    public void setRespKey(String respKey) {
        this.respKey = respKey;
    }
}
