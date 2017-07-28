package com.healthyfish.healthyfish.ui.activity.personal_center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.daimajia.swipe.util.Attributes;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctInfoReq;
import com.healthyfish.healthyfish.POJO.BeanMyConcernItem;
import com.healthyfish.healthyfish.POJO.BeanUserListReq;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MyConcernLvAdapter;
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

/**
 * 描述：个人中心我的关注页面
 * 作者：LYQ on 2017/7/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class MyConcern extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_my_concern)
    ListView lvMyConcern;

    private MyConcernLvAdapter adapter;

    private List<BeanMyConcernItem> mData = new ArrayList<>();

    private String uid = MyApplication.uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_concern);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "我的关注");
        getConcernList();
    }

    /**
     * 初始化ListView
     */
    private void initRecyclerView() {
        adapter = new MyConcernLvAdapter(this, mData);
        adapter.setMode(Attributes.Mode.Single);//只有一个拖拽打开的时候，其他的关闭
        lvMyConcern.setAdapter(adapter);
        lvMyConcern.setVerticalScrollBarEnabled(false);

    }

    private void getConcernList() {

        BeanUserListValueReq beanUserListValueReq = new BeanUserListValueReq();
        beanUserListValueReq.setPrefix("care_" + uid);
        beanUserListValueReq.setFrom(0);
        beanUserListValueReq.setTo(-1);
        beanUserListValueReq.setNum(-1);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListValueReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                initRecyclerView();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "getDoctorList--onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String strJson = responseBody.string();
                    Log.i("LYQ", "MyConcern:" + strJson);

                    List<String> concernList = JSONArray.parseObject(strJson, List.class);

                    for (String strDoctor : concernList) {
                        BeanDoctorInfo beanDoctorInfo = JSON.parseObject(strDoctor, BeanDoctorInfo.class);
                        BeanMyConcernItem beanConcernItem = new BeanMyConcernItem();
                        beanConcernItem.setType("PRIVATE_DOCTOR");
                        beanConcernItem.setBeanDoctorInfo(beanDoctorInfo);
                        mData.add(beanConcernItem);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
