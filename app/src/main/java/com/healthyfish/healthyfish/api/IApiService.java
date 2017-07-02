package com.healthyfish.healthyfish.api;

import com.healthyfish.healthyfish.POJO.BeanBaseReq;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述：创建API接口
 * 　　在retrofit中通过一个Java接口作为http请求的api接口。
 * 作者：Wayne on 2017/6/26 19:59
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public interface IApiService {

    @POST("demo/TestServlet")
    Observable<ResponseBody> getHealthyInfoByRetrofit(@Body RequestBody requestBody);

}
