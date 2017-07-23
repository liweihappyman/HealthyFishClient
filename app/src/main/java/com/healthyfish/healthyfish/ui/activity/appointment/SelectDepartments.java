package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListRespItem;
import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：挂号模块选择科室页面
 * 作者：LYQ on 2017/7/10.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SelectDepartments extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gv_select_departments)
    GridView gvSelectDepartments;

    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
    private List<BeanHospDeptListRespItem> HospDeptList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_departments);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"选择科室");
        getData();
        initGridView();
    }

    /**
     * 初始化GridView
     */
    private void initGridView() {
        String [] from ={"image","text"};
        int [] to = {R.id.iv_department_img,R.id.tv_department_name};
        simpleAdapter = new SimpleAdapter(SelectDepartments.this,data_list,R.layout.layout_choice_department,from,to);
        gvSelectDepartments.setAdapter(simpleAdapter);
        gvSelectDepartments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //访问服务器获取该科室医生列表，并跳转到医生列表页面
                Intent intent = new Intent(SelectDepartments.this,DepartmentDoctorList.class);
                BeanHospRegisterReq beanHospRegisterReq = new BeanHospRegisterReq();
                beanHospRegisterReq.setHosp("lzzyy");
                beanHospRegisterReq.setHospTxt("柳州市中医院");
                beanHospRegisterReq.setDept(HospDeptList.get(position).getDEPT_CODE());
                beanHospRegisterReq.setDeptTxt(data_list.get(position).get("text").toString());
                intent.putExtra("BeanHospRegisterReq", beanHospRegisterReq);
                startActivity(intent);
            }
        });
    }

    public void getData(){
        BeanHospDeptListReq beanHospDeptListReq = new BeanHospDeptListReq();
        beanHospDeptListReq.setHosp("lzzyy");

        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDeptListReq), new Subscriber<ResponseBody>() {
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<JSONObject> beanHospDeptListResp = JSONArray.parseObject(jsonStr,List.class);
                        for (JSONObject  object :beanHospDeptListResp){
                            String jsonString = object.toJSONString();
                            BeanHospDeptListRespItem beanHospDeptListRespItem = JSON.parseObject(jsonString,BeanHospDeptListRespItem.class);
                            Map<String, Object> map = new HashMap<String, Object>();
                            HospDeptList.add(beanHospDeptListRespItem);
                            map.put("text", beanHospDeptListRespItem.getDEPT_NAME());
                            data_list.add(map);
                        }
                        simpleAdapter.notifyDataSetChanged();
                    }
                });

    }
}
