package com.healthyfish.healthyfish.utils;

import android.support.annotation.NonNull;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.MyApplication;

import com.healthyfish.healthyfish.POJO.BeanBaseReq;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.platform.Platform;

/**
 * 描述：OkHttpUtils初始化工具
 * 作者：Wayne on 2017/6/27 14:49
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private static OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient)
    {
        if (okHttpClient == null)
        {
            mOkHttpClient = new OkHttpClient();
        } else
        {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient)
    {
        if (mInstance == null)
        {
            synchronized (OkHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance()
    {
        return initClient(null);
    }

    public static OkHttpClient getOkHttpClient()
    {
        return mOkHttpClient;
    }

    public static class METHOD
    {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }

    @NonNull
    public static RequestBody getRequestBody(BeanBaseReq beanBaseReq) {
        String jsonStr = JSON.toJSONString(beanBaseReq);

        Log.i("请求信息"," "+jsonStr);

        MediaType MJSON = MediaType.parse("application/json; charset=utf-8");
        return RequestBody.create(MJSON,jsonStr);
    }


    /**
     * 图片文件上传时的MultipartBody构造
     * @param files  图片文件
     * @return
     */
    public static MultipartBody filesToMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        for (File file : files) {
            // 这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
            builder.addFormDataPart("file", file.getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }


}
