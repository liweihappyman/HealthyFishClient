package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.healthyfish.healthyfish.POJO.BeanCourseOfDisease;
import com.healthyfish.healthyfish.POJO.BeanMedRec;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.SelectMedRecAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectMedRec extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name_doctor)
    TextView nameDoctor;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.name_hospital)
    TextView nameHospital;
    @BindView(R.id.skill)
    TextView skill;
    @BindView(R.id.hide_icon)
    ImageView hideIcon;
    @BindView(R.id.hide_info)
    ToggleButton hideInfo;
    @BindView(R.id.allow)
    ToggleButton allow;
    @BindView(R.id.all_select_cb)
    CheckBox allSelectCb;
    @BindView(R.id.select_med_rec_lv)
    ListView selectMedRecLv;
    @BindView(R.id.share_tv)
    TextView shareTv;
    private List<BeanMedRec>  list = new ArrayList<>();
    private SelectMedRecAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_med_rec);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbarTitle.setText("选择病历");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        initData();
        initListener();
    }

    private void initData() {
        list = DataSupport.findAll(BeanMedRec.class);
        adapter = new SelectMedRecAdapter(this,list);
        selectMedRecLv.setAdapter(adapter);

    }

    private void initListener() {
        shareTv.setOnClickListener(this);
        hideIcon.setOnClickListener(this);
        hideInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(SelectMedRec.this,"选中",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SelectMedRec.this,"未选中",Toast.LENGTH_SHORT).show();
                }
            }
        });

        allSelectCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if (isCheck){
                    for (int i=0;i<list.size();i++){
                        list.get(i).setSelect(true);
                    }

                }else {
                    for (int i=0;i<list.size();i++){
                        list.get(i).setSelect(false);
                    }
                }
                adapter.notifyDataSetChanged();

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_tv:
                Intent share = new Intent(SelectMedRec.this,ShareSuccess.class);
                startActivity(share);
                break;
        }
    }

}
