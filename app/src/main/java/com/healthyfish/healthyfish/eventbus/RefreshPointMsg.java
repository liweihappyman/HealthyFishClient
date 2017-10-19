package com.healthyfish.healthyfish.eventbus;

/**
 * Created by asus on 2017/10/19.
 */

public class RefreshPointMsg {
    private String point; //积分

    public RefreshPointMsg(String point) {
        this.point = point;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
