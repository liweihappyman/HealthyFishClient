package com.healthyfish.healthyfish.ui.activity.medicalrecord;

import android.content.Intent;

import android.graphics.Color;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyRemReq;
import com.healthyfish.healthyfish.POJO.BeanCourseOfDisease;
import com.healthyfish.healthyfish.POJO.BeanMedRec;
import com.healthyfish.healthyfish.POJO.BeanUserListReq;
import com.healthyfish.healthyfish.POJO.BeanUserLoginReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MedRecLvAdapter;
import com.healthyfish.healthyfish.constant.Constants;
import com.healthyfish.healthyfish.utils.ComparatorDate;
import com.healthyfish.healthyfish.utils.MySharedPrefUtil;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.ui.activity.medicalrecord.NewMedRec.ALL_MED_REC_RESULT;

/**
 * 描述：电子病历
 * 作者：WKJ on 2017/6/30.
 * 邮箱：
 * 编辑：WKJ
 */


public class AllMedRec extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int TO_NEW_MED_REC = 38;//进入NewMedRec页面的请求标志
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private List<String> nullValueKey = new ArrayList<>();//存放value值为空的key；
    boolean hasNullValueKey = false;//标志空值key
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.med_rec_all)
    ListView medRecAll;
    @BindView(R.id.new_med_rec)
    AutoLinearLayout newMedRec;
    private boolean hasNewData = false;
    private List<BeanMedRec> listMecRec = new ArrayList<>();
    private int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_rec_all);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbarTitle.setText("全部病历");
        //toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.three_points));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        newMedRec.setOnClickListener(this);
        medRecAll.setOnItemClickListener(this);
        init();//先获取数据库的数据初始化列表
        initRefresh();
    }

    private void initRefresh() {
        swipeRefresh.setColorSchemeColors(Color.parseColor("#019b79"));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reqForNetworkData(true);//然后加载网络数据更新列表
            }
        });
    }


    /**
     * 思路:先加载本地数据库的内容，异步获取网络的数据，通过对比key，如果没有则添加到本地数据库，最后更新列表
     */
    private void init() {
        listMecRec.clear();
        listMecRec = DataSupport.findAll(BeanMedRec.class);
        if (listMecRec.size() == 0) {
            //initNullLV();
            reqForNetworkData(false);//如果本地数据为空，则从网上加载，否则要刷新数据，只有下拉刷新
        } else {
            //将日期按时间先后排序
            ComparatorDate c = new ComparatorDate();
            Collections.sort(listMecRec, c);
            //遍历出日期，格式为：       2017年10月
            List<String> listDate = new ArrayList<>();
            for (int i = 0; i < listMecRec.size(); i++) {
                String date = listMecRec.get(i).getClinicalTime();
                date = date.substring(0, date.indexOf("月") + 1);
                listDate.add(date);
            }
            MedRecLvAdapter medRecLvAdapter = new MedRecLvAdapter(this, listMecRec, listDate);
            medRecAll.setAdapter(medRecLvAdapter);
        }
    }

    /**
     * 访问网络数据
     */
    private void reqForNetworkData(final boolean refresh) {
        final List<String> keysOutDB = new ArrayList<>();//存放数据库没有的key
        String userStr = MySharedPrefUtil.getValue("user");
        BeanUserLoginReq beanUserLogin = JSON.parseObject(userStr, BeanUserLoginReq.class);
        StringBuilder prefix = new StringBuilder("medRec_");
        prefix.append(beanUserLogin.getMobileNo());//获取当前用户的手机号

        BeanUserListReq beanUserListReq = new BeanUserListReq();
        beanUserListReq.setPrefix(prefix.toString());
        beanUserListReq.setFrom(0);
        beanUserListReq.setNum(-1);
        beanUserListReq.setTo(-1);
        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(AllMedRec.this, "出错啦，请检查网络环境", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String str = responseBody.string();
                    //Log.i("所有病历", "数据" + str);
                    if (!TextUtils.isEmpty(str)) {
                        List<String> keys = JSONArray.parseObject(str, List.class);
                        if (keys.size() > 0) {
                            for (final String key : keys) {
                                //Log.i("medReckey", "key" + key);
                                if (DataSupport.select("key").where("key = ? ", key).find(BeanMedRec.class).isEmpty()) {
                                    keysOutDB.add(key);
                                    hasNewData = true;
                                }
                            }
                            if (hasNewData) {//如果 有新数据则通知更新列表
                                new Thread() {
                                    @Override
                                    public void run() {
                                        keyGet(keysOutDB,refresh);
                                    }
                                }.start();
                            } else {
                                swipeRefresh.setRefreshing(false);
                                Toast.makeText(AllMedRec.this, "已经是最新数据了", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            swipeRefresh.setRefreshing(false);
                            Toast.makeText(AllMedRec.this, "没有可加载的数据哦", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * ----------------------------------------------------------------------------------------------------------------------
     * 删除网络数据空值key，造成空值key的原因还不清楚，出现过
     */
    private void networkReqDelMedRec(String key) {
        BeanBaseKeyRemReq baseKeyRemReq = new BeanBaseKeyRemReq();//删除单个
        baseKeyRemReq.setKey(key);
        RetrofitManagerUtils.getInstance(AllMedRec.this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(baseKeyRemReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ResponseBody responseBody) {

            }
        });

    }

    /**
     * 根据返回的key逐个获取数据r
     */

    private void keyGet(final List<String> keysOutDB, final boolean refresh) {
        for (final String key : keysOutDB) {
            size++;
            final BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
            beanBaseKeyGetReq.setKey(key);
            RetrofitManagerUtils.getInstance(AllMedRec.this, null).getMedRecByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {
                    if (size == keysOutDB.size()) {
                        size = 0;
                        handler.sendEmptyMessage(0x11);
                        if (refresh) {
                            swipeRefresh.setRefreshing(false);
                            Toast.makeText(AllMedRec.this, "更新成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(AllMedRec.this, "出错啦", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    try {
                        String rspv = responseBody.string();
                        if (rspv != null) {
                            BeanBaseKeyGetResp object = JSON.parseObject(rspv, BeanBaseKeyGetResp.class);
                            if (object.getValue() != null) {
                                BeanMedRec beanMedRec = JSON.parseObject(object.getValue(), BeanMedRec.class);
                                beanMedRec.setKey(key);
                                beanMedRec.save();
                                List<BeanCourseOfDisease> courseOfDiseaseList = beanMedRec.getListCourseOfDisease();
                                for (BeanCourseOfDisease courseOfDisease : courseOfDiseaseList) {
                                    courseOfDisease.setBeanMedRec(beanMedRec);
                                    courseOfDisease.save();
                                }
                            } else {
                                nullValueKey.add(key);
                                hasNullValueKey = true;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 初始化空的ListView,提示列表为空
     */

    private void initNullLV() {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setVisibility(View.GONE);
        ((ViewGroup) medRecAll.getParent()).addView(imageView);
        medRecAll.setEmptyView(imageView);
        imageView.setImageResource(R.mipmap.personal_center);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.share:
                Intent share = new Intent(this, SelectMedRec.class);
                AllMedRec.this.startActivity(share);
                break;
            case R.id.del:
                Intent selectDoctor = new Intent(this, SelectDoctor.class);
                AllMedRec.this.startActivity(selectDoctor);
                break;
            case R.id.test:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.med_rec, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_med_rec://新建病历
                Constants.POSITION_MED_REC = -1;
                Intent intent = new Intent(this, NewMedRec.class);
                startActivityForResult(intent, TO_NEW_MED_REC);
                startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Constants.POSITION_MED_REC = position;
        Intent intent = new Intent(AllMedRec.this, NewMedRec.class);
        //将选中的病历的id穿到NewMedRec活动
        intent.putExtra("id", listMecRec.get(position).getId());
        startActivityForResult(intent, TO_NEW_MED_REC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ALL_MED_REC_RESULT:
                listMecRec.clear();
                init();
                break;

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                init();//更新列表
                hasNewData=false;
                if (hasNullValueKey) {
                    for (String key : nullValueKey)
                        networkReqDelMedRec(key);//删除空值key
                }
            }
        }
    };

}
