package com.healthyfish.healthyfish.ui.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();

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
        SimpleAdapter simpleAdapter = new SimpleAdapter(SelectDepartments.this,data_list,R.layout.layout_choice_department,from,to);
        gvSelectDepartments.setAdapter(simpleAdapter);
        gvSelectDepartments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //访问服务器获取该科室医生列表，并跳转到医生列表页面
                Intent intent = new Intent(SelectDepartments.this,DepartmentDoctorList.class);
                Bundle bundle = new Bundle();
                bundle.putString("DepartmentName",data_list.get(position).get("text").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void getData(){
        int[] icon = { R.mipmap.ic_chinese_medicine,R.mipmap.ic_chinese_medicine,R.mipmap.ic_chinese_medicine,R.mipmap.ic_chinese_medicine};
        String[] iconName = { "中医科", "儿科", "脾胃病科", "骨科"};
        for(int i=0;i<3;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
    }

}
