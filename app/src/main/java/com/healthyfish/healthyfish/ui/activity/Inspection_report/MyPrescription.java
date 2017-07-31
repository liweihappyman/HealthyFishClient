package com.healthyfish.healthyfish.ui.activity.Inspection_report;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.POJO.BeanPrescriptiom;
import com.healthyfish.healthyfish.POJO.BeanPrescription;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.POJO.BeanUserRetrPresReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.PrescriptionRvAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
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

public class MyPrescription extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_prescription)
    RecyclerView rvPrescription;
    private PrescriptionRvAdapter adapter;
    private boolean hasNewData = false;//访问网络后是否有新数据
    private List<BeanPrescriptiom> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prescription);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "我的处方");
        initDataFromDB();
        requestForPrescription();
    }

    /**
     * 网络访问，获取所有的电子处方
     */
    private void requestForPrescription() {
        String userStr = MySharedPrefUtil.getValue("_user");
        BeanUserLoginReq beanUserLogin = JSON.parseObject(userStr, BeanUserLoginReq.class);
        StringBuilder prefix = new StringBuilder("pres_");
        prefix.append(beanUserLogin.getMobileNo());//获取当前用户的手机号


        BeanUserListValueReq userListValueReq = new BeanUserListValueReq();
        userListValueReq.setPrefix(prefix.toString());
        userListValueReq.setFrom(0);
        userListValueReq.setNum(-1);
        userListValueReq.setTo(-1);
        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(userListValueReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                initData(hasNewData);//如果 有新数据则通知更新列表

            }
            @Override
            public void onError(Throwable e) {
                Toast.makeText(MyPrescription.this, "出错啦", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String str = responseBody.string();
                    //Log.i("电子处方", "数据" + str);
                    if (!TextUtils.isEmpty(str)) {
                        saveNewData2DB(str);//保存新数据到本地数据库
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 对比本地数据库的key，如果不存在，则保存到数据库中
     * @param str 请求相应的数据responseBody.string()
     */
    private void saveNewData2DB(String str) {
        List<String> strJsonList = JSONArray.parseObject(str, List.class);
        for (String strJson : strJsonList) {
            BeanPrescriptiom beanPrescriptiom = JSON.parseObject(strJson, BeanPrescriptiom.class);
            //判断当前请求回来的key是否存在，不存在的话保存到数据库中
            if (DataSupport.select("key").where("key = ? ", beanPrescriptiom.getKey()).find(BeanPrescriptiom.class).isEmpty()) {
                list.add(beanPrescriptiom);
//                Log.i("电子处方详细数据", " " + beanPrescriptiom.toString());
//                        Log.i("电子处方详细数据"," "+beanPrescriptiom.getAGE()+beanPrescriptiom.getAPPLY_DEPT()+beanPrescriptiom.getKey());
//                        Log.i("电子处方详细数据","----------------------------------------------------------------------------");
//                        Log.i("电子处方详细数据PresList()"," "+beanPrescriptiom.getPresList());
                List<BeanPrescriptiom.PresListBean> preslist = beanPrescriptiom.getPresList();
                for (BeanPrescriptiom.PresListBean presBean : preslist) {//目前只有一个药，所以执行一遍直接break掉
                    //presBean打包成JsonStr，set到ITEM_CLASS（因为这个数据项（ITEM_CLASS）没有用到，所以用来存放presBean打包成的JsonStr）
                    beanPrescriptiom.setITEM_CLASS(JSON.toJSONString(presBean));
                    //表示有新数据
                    hasNewData = true;
                    break;
                }//保存到数据库
                beanPrescriptiom.save();
            }
        }
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

    /**
     * 从数据库加载
     */
    private void initDataFromDB() {
        list = DataSupport.findAll(BeanPrescriptiom.class);
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        rvPrescription.setLayoutManager(lmg);
        adapter = new PrescriptionRvAdapter(this, list, toolbar);
        rvPrescription.setAdapter(adapter);
    }

    /**
     * 访问网络后，有新数据通知更新列表
     */
    private void initData(boolean hasNewData) {
        if (hasNewData) {
            adapter.notifyDataSetChanged();
        }

    }
}
