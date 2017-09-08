package com.healthyfish.healthyfish.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.healthyfish.healthyfish.POJO.BeanPointIncReq;
import com.healthyfish.healthyfish.POJO.BeanPointQueryReq;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：
 * 作者：LYQ on 2017/9/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class IntegralUtils {

    /**
     * 增加积分
     */
    public static void addIntegral(final Context context) {

        RetrofitManagerUtils.getInstance(context, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(new BeanPointIncReq()), new Subscriber<ResponseBody>() {
            String resp = null;

            @Override
            public void onCompleted() {
                int code = Integer.parseInt(resp);
                if (code >= 0) {
                    Toast.makeText(context, "成功获得积分，当前积分：" + code, Toast.LENGTH_SHORT).show();
                }
                else if (code == -1) {
                    Toast.makeText(context,"用户未登录，获取积分失败",Toast.LENGTH_SHORT).show();
                } else if (code == -2) {
                    Toast.makeText(context, "会话id错误", Toast.LENGTH_SHORT).show();
                } else {

                }

            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(context, "增加积分失败");
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    resp = responseBody.string();
                    Log.i("LYQ", "增加积分请求返回：" + resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 增加积分
     */
    public static void queryIntegral(final Context context) {

        RetrofitManagerUtils.getInstance(context, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(new BeanPointQueryReq()), new Subscriber<ResponseBody>() {
            String resp = null;

            @Override
            public void onCompleted() {
                int code = Integer.parseInt(resp);
                if (code >= 0) {
                    Toast.makeText(context, "当前积分：" + code, Toast.LENGTH_SHORT).show();
                }
                else if (code == -1) {
                    Toast.makeText(context,"用户未登录，获取积分失败",Toast.LENGTH_SHORT).show();
                } else {

                }

            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(context, "查询积分失败");
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    resp = responseBody.string();
                    Log.i("LYQ", "查询积分请求返回：" + resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
