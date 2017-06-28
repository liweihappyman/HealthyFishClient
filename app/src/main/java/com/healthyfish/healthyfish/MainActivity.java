package com.healthyfish.healthyfish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.healthyfish.healthyfish.POJO.BeanBaseKeyAddReq;
import com.healthyfish.healthyfish.POJO.BeanBaseReq;
import com.healthyfish.healthyfish.POJO.BeanDoctorListReq;
import com.healthyfish.healthyfish.api.IApiService;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.healthyfish.healthyfish.constant.constants.CONNECT_TIMEOUT;
import static com.healthyfish.healthyfish.constant.constants.HttpHealthyFishyUrl;
import static com.healthyfish.healthyfish.constant.constants.HttpsHealthyFishyUrl;
import static com.healthyfish.healthyfish.constant.constants.READ_TIMEOUT;
import static com.healthyfish.healthyfish.constant.constants.WRITE_TIMEOUT;

/**
 * 描述：MyApplication初始化参数
 * 作者：Wayne on 2017/6/26 14:51
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


}
