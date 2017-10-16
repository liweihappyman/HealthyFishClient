package com.healthyfish.healthyfish.eventbus;

/**
 * 描述：EventBus异步提醒接收到医生端发送过来更新病历夹的系统消息
 * 作者：Wayne on 2017/9/27 22:37
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class WeChatReceiveSysMdrMsg {
    private long time;

    public WeChatReceiveSysMdrMsg(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
