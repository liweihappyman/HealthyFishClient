package com.healthyfish.healthyfish.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanPointIncReq;
import com.healthyfish.healthyfish.POJO.BeanPointQueryReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.eventbus.RefreshPointMsg;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：
 * 作者：LYQ on 2017/9/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PersonalPointUtils {

    private static String returnPoint = "";
    /**
     * 增加积分
     */
    public static void addPoint(final Context context) {

        RetrofitManagerUtils.getInstance(context, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(new BeanPointIncReq()), new Subscriber<ResponseBody>() {
            String resp = null;

            @Override
            public void onCompleted() {

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
                    int code = Integer.parseInt(resp);
                    if (code >= 0) {                        showMyToast(context);
                        //Toast.makeText(context, "成功获得积分，当前积分：" + code, Toast.LENGTH_SHORT).show();
                    }
                    else if (code == -1) {
                        //Toast.makeText(context,"用户未登录，获取积分失败",Toast.LENGTH_SHORT).show();
                    } else if (code == -2) {
                        //Toast.makeText(context, "会话id错误", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 增加积分
     */
    public static String queryPoint(final Context context) {

        RetrofitManagerUtils.getInstance(context, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(new BeanPointQueryReq()), new Subscriber<ResponseBody>() {
            String resp = null;

            @Override
            public void onCompleted() {

                EventBus.getDefault().post(new RefreshPointMsg(returnPoint));

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
                    int code = Integer.parseInt(resp);
                    if (code >= 0) {
                        //Toast.makeText(context, "当前积分：" + code, Toast.LENGTH_SHORT).show();
                        returnPoint = resp;
                    }
                    else if (code == -1) {
                        //Toast.makeText(context,"用户未登录，获取积分失败",Toast.LENGTH_SHORT).show();
                    } else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return returnPoint;
    }

    public static void showMyToast(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_toast,null );
        TextView textView = (TextView) v.findViewById(R.id.tv_toast);
        textView.setText("恭喜您获得鱼丸 +1");
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(outMetrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 120);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(v);
        toast.show();
    }

}
