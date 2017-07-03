package com.healthyfish.healthyfish;

import android.app.Application;
import android.content.Context;

import com.zhy.autolayout.config.AutoLayoutConifg;


/**
 * 描述：MyApplication初始化参数
 * 作者：Wayne on 2017/6/26 16:53
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class MyApplication extends Application{
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //自适应平屏幕
        AutoLayoutConifg.getInstance().useDeviceSize();

        applicationContext = getApplicationContext();

    }

    /**
     * 获取全局context方法
     *
     * @return contetxt
     */
    public static Context getContetxt() {
        return applicationContext;
    }
}
