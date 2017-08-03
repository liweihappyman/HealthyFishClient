package com.healthyfish.healthyfish.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 描述：
 * 作者：WKJ on 2017/7/24.
 * 邮箱：
 * 编辑：WKJ
 */

public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;
    SharedPreferences sharedPreferences;

    public ReceivedCookiesInterceptor(Context context) {
        super();
        this.context = context;
        sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain == null)
            Log.d("httpCookid", "Receivedchain == null");
        Response originalResponse = chain.proceed(chain.request());

        Log.d("httpCookid", "originalResponse" + originalResponse.toString());
        if (!originalResponse.headers("set-cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            Observable.from(originalResponse.headers("set-cookie"))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            cookieBuffer.append(cookie).append(";");
                        }
                    });
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookie", cookieBuffer.toString());
            Log.i("httpCookid", "收到的cookie" + cookieBuffer.toString());
            editor.commit();
            log(cookieBuffer.toString());
        }

        return originalResponse;
    }
    private void log(String cookie){
        Log.i("httpCookid", "接收到的cookie" + cookie);
    }

}
