package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.healthyfish.healthyfish.POJO.BeanMedRec;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MedRecLvAdapter;
import com.healthyfish.healthyfish.constant.constants;
import com.healthyfish.healthyfish.utils.ComparatorDate;
import com.zhy.autolayout.AutoLinearLayout;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.healthyfish.healthyfish.ui.activity.NewMedRec.ALL_MED_REC_RESULT;

/**
 * 描述：电子病历
 * 作者：WKJ on 2017/6/30.
 * 邮箱：
 * 编辑：WKJ
 */


public class AllMedRec extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int TO_NEW_MED_REC = 38;//进入NewMedRec页面的请求标志
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.med_rec_all)
    ListView medRecAll;
    @BindView(R.id.new_med_rec)
    AutoLinearLayout newMedRec;
    private List<BeanMedRec> listMecRec = new ArrayList<>();

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
        newMedRec.setOnClickListener(this);
        medRecAll.setOnItemClickListener(this);
        init();//初始化布局
    }

    //初始化列表
    private void init() {
//        BeanMedRec beanMedRec = new BeanMedRec();
//        beanMedRec.setName("隔壁老王");
//        beanMedRec.setGender("男");
//        beanMedRec.setDiseaseInfo("慢性疾病");
//        beanMedRec.setClinicalTime("2017年6月15日");
//        beanMedRec.setDiagnosis("就诊");
//        beanMedRec.setState(true);
//        BeanMedRec beanMedRec2 = new BeanMedRec();
//        beanMedRec2.setName("哈士奇");
//        beanMedRec2.setGender("男");
//        beanMedRec2.setDiseaseInfo("肠胃不适");
//        beanMedRec2.setClinicalTime("2016年6月15日");
//        beanMedRec2.setDiagnosis("就诊");
//        beanMedRec2.setState(false);
//        BeanMedRec beanMedRec3 = new BeanMedRec();
//        beanMedRec3.setName("哈士奇");
//        beanMedRec3.setGender("男");
//        beanMedRec3.setDiseaseInfo("肠胃不适");
//        beanMedRec3.setClinicalTime("2017年8月16日");
//        beanMedRec3.setDiagnosis("就诊");
//        beanMedRec3.setState(false);
//        BeanMedRec beanMedRec4 = new BeanMedRec();
//        beanMedRec4.setName("哈士奇");
//        beanMedRec4.setGender("男");
//        beanMedRec4.setDiseaseInfo("肠胃不适");
//        beanMedRec4.setClinicalTime("2010年8月16日");
//        beanMedRec4.setDiagnosis("就诊");
//        beanMedRec4.setState(false);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec2);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec3);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec2);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec2);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec2);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec3);
//        listMecRec.add(beanMedRec3);
//        listMecRec.add(beanMedRec3);
//        listMecRec.add(beanMedRec3);
//        listMecRec.add(beanMedRec2);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec2);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec2);
//        listMecRec.add(beanMedRec);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec4);
//        listMecRec.add(beanMedRec3);
//        listMecRec.add(beanMedRec3);
//        listMecRec.add(beanMedRec3);
//        listMecRec.add(beanMedRec3);

        //对list的日期进行的进行排序，降序
        //SQLiteDatabase db = Connector.getDatabase();
        listMecRec = DataSupport.findAll(BeanMedRec.class);
        if (listMecRec.size() == 0) {
            initNullLV();
        }
        if (listMecRec.size() > 0) {
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

    //初始化空的ListView,提示列表为空（没用到）
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
                constants.POSITION_MED_REC = -1;
                Intent intent = new Intent(this, NewMedRec.class);
                startActivityForResult(intent, TO_NEW_MED_REC);
                startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        constants.POSITION_MED_REC = position;
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
}
