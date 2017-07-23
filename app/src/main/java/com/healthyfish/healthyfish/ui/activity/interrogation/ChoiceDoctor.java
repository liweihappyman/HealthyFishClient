package com.healthyfish.healthyfish.ui.activity.interrogation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.healthyfish.healthyfish.POJO.BeanDepartmentDoctor;
import com.healthyfish.healthyfish.POJO.BeanDoctorInfo;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListRespItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChoiceDoctorLvAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.healthyfish.healthyfish.constant.constants.HttpHealthyFishyUrl;

/**
 * 描述：问诊按科室分类选择医生
 * 作者：LYQ on 2017/7/2.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChoiceDoctor extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_choice_doctor)
    ListView lvChoiceDoctor;
    @BindView(R.id.toolbar_title)
    TextView tvTitle;

    private Context mContext;
    private ChoiceDoctorLvAdapter mChoiceDoctorLvAdapter;
    private List<BeanDoctorInfo> mDoctorInfos = new ArrayList<>();

    private String departmentCode;//部门编号
    private List<BeanHospDeptDoctListRespItem> DeptDoctList = new ArrayList<>();
    private String departmentName;//部门名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_doctor);
        ButterKnife.bind(this);
        mContext = this;
        initToolBar(toolbar,tvTitle,"选择医生");
        getDepartmentName();
        initData();
        initListView();
        lvListener();
    }

    /**
     * ListView的监听
     */
    private void lvListener() {
        lvChoiceDoctor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到该医生的服务页面,需要传递该医生的唯一标识去服务器访问医生的服务详情
                Intent intent = new Intent(mContext, ChoiceService.class);
                intent.putExtra("DepartmentName", departmentName);
                intent.putExtra("DEPT_DOCTOR_INFO", DeptDoctList.get(position));
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        mChoiceDoctorLvAdapter = new ChoiceDoctorLvAdapter(mContext, mDoctorInfos);
        lvChoiceDoctor.setAdapter(mChoiceDoctorLvAdapter);
        lvChoiceDoctor.setVerticalScrollBarEnabled(false);//隐藏滚动条，看实际需求
    }

    /**
     * 获取上一页面传过来的科室编号
     */
    private void getDepartmentName() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            departmentName = bundle.get("DepartmentName").toString();
            departmentCode = bundle.get("DepartmentCode").toString();
        }
    }

    /**
     * 初始化科室医生数据
     */
    private void initData() {
        BeanHospDeptDoctListReq beanHospDeptDoctListReq = new BeanHospDeptDoctListReq();
        beanHospDeptDoctListReq.setHosp("lzzyy");
        beanHospDeptDoctListReq.setDept(departmentCode);

        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDeptDoctListReq), new Subscriber<ResponseBody>() {
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
                        List<JSONObject> doctorList = JSONArray.parseObject(jsonStr,List.class);
                        for (JSONObject  object :doctorList){
                            String jsonString = object.toJSONString();
                            BeanHospDeptDoctListRespItem beanHospDeptListRespItem = JSON.parseObject(jsonString,BeanHospDeptDoctListRespItem.class);
                            DeptDoctList.add(beanHospDeptListRespItem);
                            BeanDoctorInfo data = new BeanDoctorInfo();
                            data.setImgUrl(HttpHealthyFishyUrl+beanHospDeptListRespItem.getZHAOPIAN());
                            data.setName(beanHospDeptListRespItem.getDOCTOR_NAME());
                            data.setDepartment(departmentName);
                            data.setDuties(beanHospDeptListRespItem.getREISTER_NAME());
                            data.setHospital("柳州市中医院");
                            data.setIntroduce(beanHospDeptListRespItem.getWEB_INTRODUCE());
                            //data.setPrice(String.valueOf(beanHospDeptListRespItem.getPRICE()));
                            data.setPrice(beanHospDeptListRespItem.getPRICE()+"元起");
                            mDoctorInfos.add(data);
                        }
                        mChoiceDoctorLvAdapter.notifyDataSetChanged();
                    }
                });
    }

}
