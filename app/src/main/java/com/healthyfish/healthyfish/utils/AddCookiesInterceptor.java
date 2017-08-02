package com.healthyfish.healthyfish.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * 描述：
 * 作者：WKJ on 2017/7/24.
 * 邮箱：
 * 编辑：WKJ
 */

public class AddCookiesInterceptor implements Interceptor {
    private Context context;
    private String lang;

    public AddCookiesInterceptor(Context context, String lang) {
        super();
        this.context = context;
        this.lang = lang;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain == null)
            Log.d("httpCookid", "Addchain == null");
        final Request.Builder builder = chain.request().newBuilder();
        //SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        //Observable.just(sharedPreferences.getString("cookie", ""))
        String cookie = MySharedPrefUtil.getValue("sid");
        if (!TextUtils.isEmpty(cookie)) {
            cookie = "JSESSIONID=" + cookie.substring(4);
            Observable.just(cookie)
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            if (cookie.contains("lang=ch")) {
                                cookie = cookie.replace("lang=ch", "lang=" + lang);
                            }
                            if (cookie.contains("lang=en")) {
                                cookie = cookie.replace("lang=en", "lang=" + lang);
                            }
                            //添加cookie
//                        Log.d("http", "AddCookiesInterceptor"+cookie);
                            builder.addHeader("cookie", cookie);
                            Log.i("httpCookid", "给请求头添加的cookie：" + cookie);
                        }
                    });
        }
        return chain.proceed(builder.build());
    }
}
