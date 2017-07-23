package com.healthyfish.healthyfish.ui.activity.Inspection_report;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanPrescription;
import com.healthyfish.healthyfish.POJO.BeanUserRetrPresReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.PrescriptionRvAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class MyPrescription extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_prescription)
    RecyclerView rvPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prescription);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "我的处方");
        initData();
        requestForPrescription();

    }

    private void requestForPrescription() {
        BeanUserRetrPresReq userRetrPresReq = new BeanUserRetrPresReq();
        userRetrPresReq.setSickId("0000281122");
        userRetrPresReq.setUser("邹玉贵");
        userRetrPresReq.setHosp("lzzyy");
        userRetrPresReq.setAct(BeanUserRetrPresReq.class.getSimpleName());

        RetrofitManagerUtils.getInstance(this,null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(userRetrPresReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.i("电子处方","网络错误" + e.toString());
            }
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String str = responseBody.string();
                    Log.i("电子处方","数据" + str);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.med_rec, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.del:

                break;
        }
        return true;
    }
    private void initData() {
        List<BeanPrescription> list = new ArrayList<>();
        BeanPrescription b1 = new BeanPrescription();
        b1.setTitle("高血压（慢）");
        b1.setRef("2017-12-01");
        b1.setName("隔壁老王");
        b1.setSymptom("男");
        b1.setAbbr("66岁");
        b1.setAttending("柳州市中医院-心病科门诊-洋洋");
        b1.setComp("挂号");
        b1.setEffect("瑞舒伐他汀钙片");
        BeanPrescription b2 = new BeanPrescription();
        b2.setTitle("高血压（慢）");
        b2.setRef("2017-12-01");
        b2.setName("隔壁老王");
        b2.setSymptom("男");
        b2.setAbbr("66岁");
        b2.setAttending("柳州市中医院-心病科门诊-洋洋");
        b2.setComp("挂号");
        b2.setEffect("瑞舒伐他汀钙片");
        BeanPrescription b3 = new BeanPrescription();
        b3.setTitle("高血压（慢）");
        b3.setRef("2017-12-01");
        b3.setName("隔壁老王");
        b3.setSymptom("男");
        b3.setAbbr("66岁");
        b3.setAttending("柳州市中医院-心病科门诊-洋洋");
        b3.setComp("挂号");
        b3.setEffect("瑞舒伐他汀钙片");
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b1);
        list.add(b3);
        list.add(b2);
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        rvPrescription.setLayoutManager(lmg);
        PrescriptionRvAdapter adapter = new PrescriptionRvAdapter(this,list,toolbar);
        rvPrescription.setAdapter(adapter);
    }
}
