package com.healthyfish.healthyfish.ui.activity.Inspection_report;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserRetrReptReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class InspectionReport extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_report);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "我的化验单");
        search.setOnClickListener(this);
        init();
    }

    private void init() {

        BeanUserRetrReptReq userRetrReptReq = new BeanUserRetrReptReq();
        userRetrReptReq.setSickId("0000281122");
        userRetrReptReq.setUser("13977211042");
        userRetrReptReq.setHosp("lzzyy");

        BeanUserListValueReq userListValueReq = new BeanUserListValueReq();
        userListValueReq.setPrefix("rept_<%= 13977211042 %>_");
        userListValueReq.setFrom(0);
        userListValueReq.setNum(-1);
        userListValueReq.setTo(-1);


        RetrofitManagerUtils.getInstance(this,null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(userRetrReptReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.i("检查报告","网络错误" + e.toString());
            }
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String str = responseBody.string();
                    Log.i("检查报告","数据" + str);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,MyPrescription.class);
        startActivity(intent);
    }
}
