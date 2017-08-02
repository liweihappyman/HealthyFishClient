package com.healthyfish.healthyfish.ui.activity.Inspection_report;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.POJO.BeanInspectionReport;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.InspectionReportAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.AutoLogin;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    List<BeanInspectionReport> mList = new ArrayList<>();
    boolean hasNewData = false;
    InspectionReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_report);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "我的化验单");
        search.setOnClickListener(this);
        initFromDB();
        //AutoLogin.autoLogin();
        RequestForNetWorkData();
    }

    /**
     * 从数据库加载数据
     */
    private void initFromDB() {
        mList.clear();
        mList = DataSupport.findAll(BeanInspectionReport.class);
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(lmg);
        adapter = new InspectionReportAdapter(this, mList);
        recyclerview.setAdapter(adapter);
    }

    private void RequestForNetWorkData() {
        BeanUserListValueReq userListValueReq = new BeanUserListValueReq();
        userListValueReq.setPrefix("rept_");
        userListValueReq.setFrom(0);
        userListValueReq.setNum(-1);
        userListValueReq.setTo(-1);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(userListValueReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                if (hasNewData) {
                    initFromDB();
                    hasNewData = false;
                }
            }

            @Override
            public void onError(Throwable e) {
                if (hasNewData) {
                    initFromDB();
                    hasNewData = false;
                }
                Log.i("检查报告", "出错啦" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String str = responseBody.string();
                    Log.i("检查报告", "数据" + str);
                    if (!TextUtils.isEmpty(str)) {
                        List<String> listObjectStr = JSONArray.parseObject(str, List.class);
                        BeanInspectionReport beanInspectionReport;
                        for (String objectStr : listObjectStr) {
                                beanInspectionReport = JSON.parseObject(objectStr, BeanInspectionReport.class);
                                if (DataSupport.select("key").where("key = ? ", beanInspectionReport.getKey()).find(BeanInspectionReport.class).isEmpty()) {
                                    beanInspectionReport.setSPECIMEN(JSON.toJSONString(beanInspectionReport.getTestList()));
                                    beanInspectionReport.save();
                                    hasNewData = true;
                                }
                            }
                        }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MyPrescription.class);
        startActivity(intent);
    }
}
