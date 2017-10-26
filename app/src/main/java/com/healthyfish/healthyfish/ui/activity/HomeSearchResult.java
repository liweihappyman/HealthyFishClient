package com.healthyfish.healthyfish.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetResp;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.POJO.BeanHomeSearchResult;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctInfoReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListRespItem;
import com.healthyfish.healthyfish.POJO.BeanItemNewsAbstract;
import com.healthyfish.healthyfish.POJO.BeanSearchReq;
import com.healthyfish.healthyfish.POJO.BeanSearchResp;
import com.healthyfish.healthyfish.POJO.BeanSearchRespItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.HomeSearchResultLvAdapter;
import com.healthyfish.healthyfish.ui.activity.appointment.DoctorDetail;
import com.healthyfish.healthyfish.ui.activity.appointment.SearchDoctor;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;

/**
 * 描述：首页搜索结果页面
 * 作者：LYQ on 2017/10/24.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class HomeSearchResult extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.lv_home_search_result)
    ListView lvHomeSearchResult;

    private HomeSearchResultLvAdapter mAdapter;
    private List<BeanHomeSearchResult> resultList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search_result);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "搜索");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String searchKey = bundle.get("SEARCH_KEY").toString();
            initSearch(searchKey);
            etSearch.setText(searchKey);
        }
        //键盘的搜索按钮监听
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    initSearch(etSearch.getText().toString().trim());
                    etSearch.clearFocus();//取消输入框焦点
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);//关闭键盘
                }
                return true;
            }
        });
        initLv();
    }

    /**
     * 初始化搜索结果列表
     */
    private void initLv() {
        mAdapter = new HomeSearchResultLvAdapter(this, resultList);
        lvHomeSearchResult.setAdapter(mAdapter);

        lvHomeSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (resultList.get(position).getType().equals(BeanHomeSearchResult.SEARCH_TYPE_NEWS)) {
                    getNews(resultList.get(position).getKey(), resultList.get(position).getTitle());
                } else if (resultList.get(position).getType().equals(BeanHomeSearchResult.SEARCH_TYPE_HOSP)) {
                    getDoctorInfo(resultList.get(position).getKey(), resultList.get(position).getTitle());
                }
            }
        });
    }


    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        //搜索按钮
        initSearch(etSearch.getText().toString().trim());
        etSearch.clearFocus();//取消输入框焦点
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);//关闭键盘
    }

    /**
     * 搜索
     */
    private void initSearch(String searchKey) {
        resultList.clear();//每次搜索前先将之前的搜索结果清除
        BeanSearchReq beanSearchReq = new BeanSearchReq();
        //beanSearchReq.setType("");
        beanSearchReq.setKeyword(searchKey);
        String jsonStr = JSON.toJSONString(beanSearchReq);
        Log.i("LYQ", "搜索请求体：" + jsonStr);
        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanSearchReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                            Log.e("LYQ", "首页搜索结果：" + jsonStr);
                            BeanSearchResp beanSearchResp = JSON.parseObject(jsonStr, BeanSearchResp.class);
                            if (!beanSearchResp.getResultList().isEmpty()) {
                                for (BeanSearchRespItem beanSearchRespItem : beanSearchResp.getResultList()) {
                                    BeanHomeSearchResult beanHomeSearchResult = new BeanHomeSearchResult();
                                    beanHomeSearchResult.setType(beanSearchRespItem.getType());
                                    beanHomeSearchResult.setKey(beanSearchRespItem.getKey());
                                    beanHomeSearchResult.setTitle(beanSearchRespItem.getTitle());
                                    beanHomeSearchResult.setValue(beanSearchRespItem.getValue().replace("<code>", "").replace("</code>", ""));
                                    resultList.add(beanHomeSearchResult);
                                    mAdapter.notifyDataSetChanged();
                                }
                                lvHomeSearchResult.setSelection(0);//让列表回到顶部
                                Toast.makeText(HomeSearchResult.this,"搜索完毕",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeSearchResult.this,"搜索结果为空",Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


    /**
     * 获取新闻资讯的url
     *
     * @param key
     */
    private void getNews(String key, final String title) {
        BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
        beanBaseKeyGetReq.setKey(key);
        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyGetReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                            BeanBaseKeyGetResp beanBaseKeyGetResp = JSON.parseObject(jsonStr, BeanBaseKeyGetResp.class);
                            BeanItemNewsAbstract beanItemNewsAbstractList = JSON.parseObject(beanBaseKeyGetResp.getValue(), BeanItemNewsAbstract.class);

                            Intent intent = new Intent(HomeSearchResult.this, HealthNews.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("HEALTH_NEWS_URL", beanItemNewsAbstractList.getUrl());
                            bundle.putString("HEALTH_NEWS_TITLE", title);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                });

    }


    /**
     * 获取医生信息
     *
     * @param key
     */
    private void getDoctorInfo(final String key, final String title) {
        BeanHospDeptDoctInfoReq beanHospDeptDoctInfoReq = new BeanHospDeptDoctInfoReq();
        beanHospDeptDoctInfoReq.setHosp(key.split("_")[1]);
        beanHospDeptDoctInfoReq.setDept(key.split("_")[2]);
        beanHospDeptDoctInfoReq.setStaffNo(key.split("_")[3]);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDeptDoctInfoReq), new Subscriber<ResponseBody>() {
            String doctorInfoResp = null;

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("LYQ", "搜索医生信息错误：" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    doctorInfoResp = responseBody.string();
                    Log.e("LYQ", "搜索医生信息结果：" + doctorInfoResp);
                    BeanHospDeptDoctListRespItem beanHospDeptListRespItem = JSON.parseObject(doctorInfoResp, BeanHospDeptDoctListRespItem.class);

                    BeanDoctorInfo beanDoctorInfo = new BeanDoctorInfo();
                    beanDoctorInfo.setHosp(key.split("_")[1]);
                    beanDoctorInfo.setHospital(title.split("-")[0]);
                    beanDoctorInfo.setDept(key.split("_")[2]);
                    beanDoctorInfo.setDepartment(title.split("-")[1]);
                    beanDoctorInfo.setImgUrl(beanHospDeptListRespItem.getZHAOPIAN());
                    beanDoctorInfo.setName(beanHospDeptListRespItem.getDOCTOR_NAME());
                    beanDoctorInfo.setDuties(beanHospDeptListRespItem.getREISTER_NAME());
                    beanDoctorInfo.setIntroduce(beanHospDeptListRespItem.getWEB_INTRODUCE());
                    beanDoctorInfo.setDOCTOR(beanHospDeptListRespItem.getDOCTOR());
                    beanDoctorInfo.setCLINIQUE_CODE(beanHospDeptListRespItem.getCLINIQUE_CODE());
                    beanDoctorInfo.setWORK_TYPE(beanHospDeptListRespItem.getWORK_TYPE());
                    beanDoctorInfo.setPRE_ALLOW(beanHospDeptListRespItem.getPRE_ALLOW());
                    beanDoctorInfo.setSchdList(beanHospDeptListRespItem.getSchdList());
                    beanDoctorInfo.setSTAFF_NO(beanHospDeptListRespItem.getSTAFF_NO());
                    beanDoctorInfo.setPrice(beanHospDeptListRespItem.getPRICE() + "元");

                    Intent intent = new Intent(HomeSearchResult.this, DoctorDetail.class);
                    intent.putExtra("BeanDoctorInfo", beanDoctorInfo); //将医生信息传递到下一页面
                    startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
