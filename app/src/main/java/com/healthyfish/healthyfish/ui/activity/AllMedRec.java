package com.healthyfish.healthyfish.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.healthyfish.healthyfish.POJO.BeanMedRec;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MedRecLvAdapter;
import com.healthyfish.healthyfish.utils.ComparatorDate;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：病历夹列表
 * 作者：TMXK
 * 编辑：wkj
 */

public class AllMedRec extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.new_med_rec)
    AutoLinearLayout newMedRec;
    @BindView(R.id.med_rec_all)
    ListView medRecAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_rec_all);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        //toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.three_points));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        init();//初始化布局

    }

    //测试初始化列表
    private void init() {
        List<BeanMedRec> listMecRec = new ArrayList<>();
        BeanMedRec beanMedRec = new BeanMedRec();
        beanMedRec.setName("隔壁老王");
        beanMedRec.setGender("男");
        beanMedRec.setDiseaseInfor("慢性疾病");
        beanMedRec.setCliniTime("2017年6月15日");
        beanMedRec.setDiagnose("就诊");
        beanMedRec.setState(true);
        BeanMedRec beanMedRec2 = new BeanMedRec();
        beanMedRec2.setName("哈士奇");
        beanMedRec2.setGender("男");
        beanMedRec2.setDiseaseInfor("肠胃不适");
        beanMedRec2.setCliniTime("2016年6月15日");
        beanMedRec2.setDiagnose("就诊");
        beanMedRec2.setState(false);
        BeanMedRec beanMedRec3 = new BeanMedRec();
        beanMedRec3.setName("哈士奇");
        beanMedRec3.setGender("男");
        beanMedRec3.setDiseaseInfor("肠胃不适");
        beanMedRec3.setCliniTime("2017年8月16日");
        beanMedRec3.setDiagnose("就诊");
        beanMedRec3.setState(false);
        BeanMedRec beanMedRec4 = new BeanMedRec();
        beanMedRec4.setName("哈士奇");
        beanMedRec4.setGender("男");
        beanMedRec4.setDiseaseInfor("肠胃不适");
        beanMedRec4.setCliniTime("2010年8月16日");
        beanMedRec4.setDiagnose("就诊");
        beanMedRec4.setState(false);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec2);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec3);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec2);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec2);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec2);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec3);
        listMecRec.add(beanMedRec3);
        listMecRec.add(beanMedRec3);
        listMecRec.add(beanMedRec3);
        listMecRec.add(beanMedRec2);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec2);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec2);
        listMecRec.add(beanMedRec);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec4);
        listMecRec.add(beanMedRec3);
        listMecRec.add(beanMedRec3);
        listMecRec.add(beanMedRec3);
        listMecRec.add(beanMedRec3);

        //对list的日期进行的进行排序，降序
        ComparatorDate c = new ComparatorDate();
        Collections.sort(listMecRec, c);
        //遍历出日期，格式为：       2017年10月
        List<String> listDate = new ArrayList<>();
        for (int i = 0; i < listMecRec.size(); i++) {
            String date = listMecRec.get(i).getCliniTime();
            date = date.substring(0, date.indexOf("月") + 1);
            listDate.add(date);
        }
        MedRecLvAdapter medRecLvAdapter = new MedRecLvAdapter(this, listMecRec, listDate);
        medRecAll.setAdapter(medRecLvAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.share:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.med_rec, menu);
        return true;
    }
}