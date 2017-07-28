package com.healthyfish.healthyfish;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.healthyfish.healthyfish.POJO.BeanConcernList;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListRespItem;
import com.healthyfish.healthyfish.POJO.BeanMyConcernItem;
import com.healthyfish.healthyfish.POJO.BeanUserListReq;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;


/**
 * 描述：MyApplication初始化参数
 * 作者：Wayne on 2017/6/26 16:53
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class MyApplication extends Application{
    private static Context applicationContext;
    public static String uid = "15278898523";

    @Override
    public void onCreate() {
        super.onCreate();
        //自适应平屏幕
        AutoLayoutConifg.getInstance().useDeviceSize();

        applicationContext = getApplicationContext();
        LitePal.initialize(getApplicationContext());//初始化数据库

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
