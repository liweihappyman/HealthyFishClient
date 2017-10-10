package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctInfoReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListRespItem;
import com.healthyfish.healthyfish.POJO.BeanSearchReq;
import com.healthyfish.healthyfish.POJO.BeanSearchResp;
import com.healthyfish.healthyfish.POJO.BeanSearchRespItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChoiceDoctorLvAdapter;
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

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;

/**
 * 描述：挂号搜索医生页面
 * 作者：LYQ on 2017/10/9.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SearchDoctor extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.lv_search_doctor)
    ListView lvSearchDoctor;

    private ChoiceDoctorLvAdapter adapter;
    private BeanDoctorInfo beanDoctorInfo;
    private List<BeanDoctorInfo> mDoctorInfo = new ArrayList<>();
    private List<BeanSearchRespItem> searchRespItemList = new ArrayList<>();
    private List<BeanHospDeptDoctListRespItem> doctorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"搜索医生");
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   initSearchResult(etSearch.getText().toString().trim());
                }
                return true;
            }
        });
        initListView();
    }

    /**
     * 搜索
     */
    private void initSearchResult(String searchKey) {
        BeanSearchReq beanSearchReq = new BeanSearchReq();
        beanSearchReq.setType("hosp");
        beanSearchReq.setKeyword(searchKey);
        String jsonStr = JSON.toJSONString(beanSearchReq);
        Log.i("LYQ", "搜索请求体：" + jsonStr);
        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanSearchReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        adapter.notifyDataSetChanged();
                        getDoctorInfo();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e("LYQ", "搜索医生结果：" + jsonStr);
                        BeanSearchResp searchResp = JSON.parseObject(jsonStr, BeanSearchResp.class);
                        for (BeanSearchRespItem searchRespItem : searchResp.getResultList()) {
                            if (searchRespItem.getValue().substring(0, 13).equals("...CTOR_NAME=")) {
                                searchRespItemList.add(searchRespItem);
                            }
                        }
                    }
                });
    }

    /**
     * 根据搜素的结果获取搜索出的医生的信息
     */
    private void getDoctorInfo() {
        if (!searchRespItemList.isEmpty()) {
            BeanHospDeptDoctInfoReq beanHospDeptDoctInfoReq = new BeanHospDeptDoctInfoReq();
            beanHospDeptDoctInfoReq.setHosp(searchRespItemList.get(0).getKey().split("_")[1]);
            beanHospDeptDoctInfoReq.setDept(searchRespItemList.get(0).getKey().split("_")[2]);
            beanHospDeptDoctInfoReq.setStaffNo(searchRespItemList.get(0).getKey().split("_")[3]);

            String jsonStr = JSON.toJSONString(beanHospDeptDoctInfoReq);
            Log.i("LYQ", "获取医生信息请求体：" + jsonStr);

            RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDeptDoctInfoReq), new Subscriber<ResponseBody>() {
                String doctorInfoResp = null;

                @Override
                public void onCompleted() {
                    BeanHospDeptDoctListRespItem beanHospDeptListRespItem = JSON.parseObject(doctorInfoResp, BeanHospDeptDoctListRespItem.class);
                    doctorList.add(beanHospDeptListRespItem); //用于传递数据到下一页面用的list
                    BeanDoctorInfo data = new BeanDoctorInfo();
                    data.setHosp(searchRespItemList.get(0).getKey().split("_")[1]);
                    data.setHospital(searchRespItemList.get(0).getTitle().split("-")[0]);
                    data.setDept(searchRespItemList.get(0).getKey().split("_")[2]);
                    data.setDepartment(searchRespItemList.get(0).getTitle().split("-")[1]);
                    data.setImgUrl(HttpHealthyFishyUrl + beanHospDeptListRespItem.getZHAOPIAN());
                    data.setName(beanHospDeptListRespItem.getDOCTOR_NAME());
                    data.setDuties(beanHospDeptListRespItem.getREISTER_NAME().substring(0, beanHospDeptListRespItem.getREISTER_NAME().length() - 1));
                    data.setIntroduce(beanHospDeptListRespItem.getWEB_INTRODUCE());
                    data.setPrice(beanHospDeptListRespItem.getPRICE() + "元起");
                    mDoctorInfo.add(data);///用于展示医生信息用的list
                    searchRespItemList.remove(0);
                    getDoctorInfo();
                    adapter.notifyDataSetChanged();
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        adapter = new ChoiceDoctorLvAdapter(this, mDoctorInfo);
        lvSearchDoctor.setAdapter(adapter);
        lvSearchDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入到医生详情页面，进行预约时间的选择
                beanDoctorInfo = new BeanDoctorInfo();
                beanDoctorInfo.setHosp(mDoctorInfo.get(position).getHosp());
                beanDoctorInfo.setHospital(mDoctorInfo.get(position).getHospital());
                beanDoctorInfo.setDept(mDoctorInfo.get(position).getDept());
                beanDoctorInfo.setDepartment(mDoctorInfo.get(position).getDepartment());
                beanDoctorInfo.setName(doctorList.get(position).getDOCTOR_NAME());
                beanDoctorInfo.setDOCTOR(doctorList.get(position).getDOCTOR());
                beanDoctorInfo.setIntroduce(doctorList.get(position).getWEB_INTRODUCE());
                beanDoctorInfo.setWORK_TYPE(doctorList.get(position).getWORK_TYPE());
                beanDoctorInfo.setSTAFF_NO(doctorList.get(position).getSTAFF_NO());
                beanDoctorInfo.setCLINIQUE_CODE(doctorList.get(position).getCLINIQUE_CODE());
                beanDoctorInfo.setDuties(doctorList.get(position).getREISTER_NAME());
                beanDoctorInfo.setImgUrl(doctorList.get(position).getZHAOPIAN());
                beanDoctorInfo.setPRE_ALLOW(doctorList.get(position).getPRE_ALLOW());
                beanDoctorInfo.setPrice(String.valueOf(doctorList.get(position).getPRICE()));
                beanDoctorInfo.setSchdList(doctorList.get(position).getSchdList());

                Intent intent = new Intent(SearchDoctor.this, DoctorDetail.class);
                intent.putExtra("BeanDoctorInfo", beanDoctorInfo); //将医生信息传递到下一页面
                startActivity(intent);
            }
        });
    }

}
