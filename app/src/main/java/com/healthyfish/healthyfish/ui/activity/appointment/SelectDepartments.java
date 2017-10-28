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
import com.healthyfish.healthyfish.POJO.BeanDepartmentInfo;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListRespItem;
import com.healthyfish.healthyfish.POJO.BeanHospRegisterReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListRespItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.UpdateDepartmentInfoUtils;

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
    private BeanHospitalListRespItem beanHospitalListRespItem;
    private String hospID = "lzzyy";//默认
    private String hospName = "柳州市中医院";//默认
    private final int[] icons = new int[]{R.mipmap.ic_chinese_medicine};//填充科室图标

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_departments);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "选择科室");
        getHospInfo(); //从上一个页面（选择医院页面）获取其传过来的医院数据
        getData(); //获取科室数据
        initGridView(); //初始化展示科室信息的GridView
    }

    /**
     * 从上一页面获取医院信息
     */
    private void getHospInfo() {
        //从选择医院页面跳转过来的时候获取
        if (getIntent().getSerializableExtra("BeanHospitalListRespItem") != null) {
            beanHospitalListRespItem = (BeanHospitalListRespItem) getIntent().getSerializableExtra("BeanHospitalListRespItem");
            hospID = beanHospitalListRespItem.getId(); //医院编号id（中医院为lzzyy , 中医院东院为lzzyy1）
            hospName = beanHospitalListRespItem.getName(); //医院名字
        }
    }

    /**
     * 初始化GridView
     */
    private void initGridView() {
        String[] from = {"image", "dept_name"}; //image是用来放科室图片的（目前没有）
        int[] to = {R.id.iv_department_img, R.id.tv_department_name};
        simpleAdapter = new SimpleAdapter(SelectDepartments.this, data_list, R.layout.layout_choice_department, from, to);
        gvSelectDepartments.setAdapter(simpleAdapter);
        gvSelectDepartments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BeanHospRegisterReq beanHospRegisterReq = new BeanHospRegisterReq();
                //如果选择的科室是中医院东院的科室则医院名为柳州市中医院-东院，如果选择的是中医院则为柳州市中医院
                if (data_list.get(position).get("dept_name").toString().substring(0, 2).equals("东院")) {
                    //beanHospRegisterReq.setHosp(hospID);
                    beanHospRegisterReq.setHospTxt("柳州市中医院-东院");
                } else {
                    //beanHospRegisterReq.setHosp(hospID);
                    beanHospRegisterReq.setHospTxt(hospName);
                }
                beanHospRegisterReq.setHosp("lzzyy");//目前只用lzzyy没有启用lzzyy1，所以目前统一用lzzyy进行请求获取数据和挂号用
                beanHospRegisterReq.setDept(data_list.get(position).get("dept").toString());
                beanHospRegisterReq.setDeptTxt(data_list.get(position).get("dept_name").toString());

                Intent intent = new Intent(SelectDepartments.this, DepartmentDoctorList.class);
                intent.putExtra("BeanHospRegisterReq", beanHospRegisterReq); //传递科室编号和科室名到下一个页面用
                startActivity(intent);
            }
        });
    }


    /**
     * 网络请求科室列表
     */
    public void getData() {
        //请求获取医院科室信息用：BeanHospDeptListReq
        final BeanHospDeptListReq beanHospDeptListReq = new BeanHospDeptListReq();
        //beanHospDeptListReq.setHosp(hospID);
        beanHospDeptListReq.setHosp("lzzyy"); //目前没有区分lzzyy和lzzyy1，所以中医院和中医院东院都用lzzyy,后期区分时用上一行代码

        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDeptListReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Map<String, Object> map;
                        if (beanHospitalListRespItem == null) { //如果为空则表明不是通过点击选择医院进来的，所以展示所有的科室信息
                            for (BeanHospDeptListRespItem beanHospDeptListRespItem : HospDeptList) {
                                map = new HashMap<String, Object>();
                                map.put("image", icons[0]);
                                map.put("dept", beanHospDeptListRespItem.getDEPT_CODE());
                                map.put("dept_name", beanHospDeptListRespItem.getDEPT_NAME());
                                data_list.add(map);

                            }
                        } else {
                            //目前用医院编号id来判断是点击了哪个医院，并且通过获取科室名的前两个字符来区分该科室是属于哪个医院
                            if (hospID.equals("lzzyy")) {//柳州市中医院
                                for (BeanHospDeptListRespItem beanHospDeptListRespItem : HospDeptList) {
                                    map = new HashMap<String, Object>();
                                    if (!beanHospDeptListRespItem.getDEPT_NAME().substring(0, 2).equals("东院")) {
                                        map.put("image", icons[0]);
                                        map.put("dept", beanHospDeptListRespItem.getDEPT_CODE());
                                        map.put("dept_name", beanHospDeptListRespItem.getDEPT_NAME());
                                        data_list.add(map);
                                    }
                                }
                            } else if (hospID.equals("lzzyy1")) {//柳州市中医院—东院
                                for (BeanHospDeptListRespItem beanHospDeptListRespItem : HospDeptList) {
                                    map = new HashMap<String, Object>();
                                    if (beanHospDeptListRespItem.getDEPT_NAME().substring(0, 2).equals("东院")) {
                                        map.put("image", icons[0]);
                                        map.put("dept", beanHospDeptListRespItem.getDEPT_CODE());
                                        map.put("dept_name", beanHospDeptListRespItem.getDEPT_NAME());
                                        data_list.add(map);
                                    }
                                }
                            }
                        }

                        simpleAdapter.notifyDataSetChanged(); //加载数据完成后刷新适配器
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                            List<JSONObject> beanHospDeptListResp = JSONArray.parseObject(jsonStr, List.class);
                            for (JSONObject object : beanHospDeptListResp) {
                                String jsonString = object.toJSONString();
                                BeanHospDeptListRespItem beanHospDeptListRespItem = JSON.parseObject(jsonString, BeanHospDeptListRespItem.class);
                                HospDeptList.add(beanHospDeptListRespItem);
                                //将医院科室信息保存到本地数据库,不需在此保存，提前在挂号首页更新保存
//                                BeanDepartmentInfo beanDepartmentInfo = new BeanDepartmentInfo();
//                                beanDepartmentInfo.setKey(hospID + "_" + beanHospDeptListRespItem.getDEPT_NAME());
//                                beanDepartmentInfo.setHospital(hospName);
//                                beanDepartmentInfo.setDepartmentName(beanHospDeptListRespItem.getDEPT_NAME());
//                                UpdateDepartmentInfoUtils.saveDepartmentInfo(beanDepartmentInfo);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
