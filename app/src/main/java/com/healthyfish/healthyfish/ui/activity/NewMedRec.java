package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanCourseOfDisease;
import com.healthyfish.healthyfish.POJO.BeanMedRec;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.CourseOfDiseaseAdapter;
import com.healthyfish.healthyfish.constant.constants;
import com.healthyfish.healthyfish.ui.widget.DatePickerDialog;
import com.healthyfish.healthyfish.utils.Utils1;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：电子病历
 * 作者：WKJ on 2017/7/4.
 * 邮箱：
 * 编辑：WKJ
 */
public class NewMedRec extends AppCompatActivity implements View.OnClickListener {
    public static final int FOR_ITEM = 38;
    public static final int COURSE_OF_DISEASE = 33;
    public static final int INFO = 34;
    public static final int LABLE = 35;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lable)
    TextView lable;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.patient_info)
    TextView patientInfo;
    @BindView(R.id.clinical_time)
    TextView clinicalTime;
    @BindView(R.id.create_course)
    TextView createCourse;
    @BindView(R.id.course_of_disease)
    RecyclerView courseOfDisease;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.diagnosis)
    EditText diagnosis;
    @BindView(R.id.disease_info)
    EditText diseaseInfo;
    @BindView(R.id.clinical_department)
    EditText clinicalDepartment;

    private CourseOfDiseaseAdapter courseOfDiseaseAdapter;
    private BeanMedRec medRec;
    private List<String> imagePaths = new ArrayList<>();
    private List<BeanCourseOfDisease> listCourseOfDiseases = new ArrayList<BeanCourseOfDisease>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_med_rec);
        ButterKnife.bind(this);
        toolbarTitle.setText("新建病历");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
        initListener();
        if (constants.ALL_FLAG == -1) {
            clinicalTime.setText(Utils1.getTime());
            medRec = new BeanMedRec();
        } else {
            initdata();
        }


    }

    private void initdata() {
        setLableTv();
        setInfo();
        int id = getIntent().getIntExtra("id", 0);
        medRec = DataSupport.find(BeanMedRec.class, id);
        listCourseOfDiseases = medRec.getListCourseOfDisease();
        if (medRec.getDiagnosis() != null) {
            diagnosis.setText(medRec.getDiagnosis());
        }
        if (medRec.getDiseaseInfo() != null) {
            diseaseInfo.setText(medRec.getDiseaseInfo());
        }
        if (medRec.getClinicalDepartement() != null) {
            clinicalDepartment.setText(medRec.getClinicalDepartement());
        }
        clinicalTime.setText(medRec.getClinicalTime());
    }

    //适配器
    private void initList(List<BeanCourseOfDisease> beanCourseOfDiseases) {
        //if (courseOfDiseaseAdapter == null) {
        courseOfDiseaseAdapter = new CourseOfDiseaseAdapter(this, beanCourseOfDiseases);
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        courseOfDisease.setLayoutManager(lmg);
        courseOfDisease.setAdapter(courseOfDiseaseAdapter);
        courseOfDiseaseAdapter.setOnItemClickListener(new CourseOfDiseaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                constants.FLAG_STATUE = "update";
                constants.FLAG_POSITION = position;
                Intent intent = new Intent(NewMedRec.this, CreateCourse.class);
                intent.putExtra("CreateCourse", medRec);
                startActivityForResult(intent, COURSE_OF_DISEASE);
            }
        });
        //}
    }


    /*
    * 初始化监听
    * */
    private void initListener() {
        lable.setOnClickListener(this);
        patientInfo.setOnClickListener(this);
        clinicalTime.setOnClickListener(this);
        createCourse.setOnClickListener(this);
        save.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.lable:
                Intent toLable = new Intent(this, Lable.class);
                toLable.putExtra("lable", medRec);
                startActivityForResult(toLable, LABLE);
                break;
            case R.id.patient_info:
                Intent toPatientInfo = new Intent(this, PatientInfo.class);
                toPatientInfo.putExtra("info", medRec);
                startActivityForResult(toPatientInfo, INFO);
                break;
            case R.id.clinical_time:
                selectTime();
                break;
            case R.id.create_course:
                constants.FLAG_STATUE = "save";
                Intent toCreateCourse = new Intent(this, CreateCourse.class);
                toCreateCourse.putExtra("CreateCourse", medRec);
                startActivityForResult(toCreateCourse, COURSE_OF_DISEASE);
                break;
            case R.id.save:

                medRec.setDiagnosis(diagnosis.getText().toString());
                medRec.setDiseaseInfo(diseaseInfo.getText().toString());
                medRec.setClinicalDepartement(clinicalDepartment.getText().toString());
                medRec.setClinicalTime(clinicalTime.getText().toString());

//                if (constants.beanMedRec.isSaved()) {
//                    constants.beanMedRec.update(constants.beanMedRec.getId());
//                } else {
                BeanMedRec beanMedRec = medRec;
                beanMedRec.saveOrUpdate("id=?", String.valueOf(beanMedRec.getId()));
                //}


//                List<BeanMedRec> l = DataSupport.findAll(BeanMedRec.class,true);
//                List<BeanCourseOfDisease> lista = l.get(0).getListCourseOfDisease();
//                int s = l.size();
//                int a = lista.size();


                Intent intent = new Intent(NewMedRec.this, AllMedRec.class);
                NewMedRec.this.setResult(FOR_ITEM, intent);
                NewMedRec.this.finish();


//                BeanMedRec beanMedRec = new BeanMedRec();
//                BeanCourseOfDisease beanCourseOfDisease = new BeanCourseOfDisease();
//                beanCourseOfDisease.setDate("6666666666YYYYYYYY");
//
//                if(beanCourseOfDisease.save()){
//                    Log.i("cg","成功");
//                }
//                BeanCourseOfDisease beanCourseOfDisease1 = new BeanCourseOfDisease();
//                beanCourseOfDisease1.setDate("6666666666YYYYYYYY");
//                beanCourseOfDisease1.save();
//
//
//                beanMedRec.setName("hahah");
//                beanMedRec.setCliniTime("00000000");
//                beanMedRec.getListCourseOfDisease().add(beanCourseOfDisease);
//                beanMedRec.getListCourseOfDisease().add(beanCourseOfDisease1);
//                beanMedRec.save();
//                BeanMedRec beanMedRec = new BeanMedRec();
//                beanMedRec.setName("hahah");
//                beanMedRec.save();
//                BeanCourseOfDisease beanCourseOfDisease = new BeanCourseOfDisease();
//                beanCourseOfDisease.setDate("6666666666YYYYYYYY");
//                beanCourseOfDisease.setBeanMedRec(beanMedRec);
//                if (beanCourseOfDisease.save()) {
//                    Log.i("cg", "成功");
//                }
//                BeanCourseOfDisease beanCourseOfDisease1 = new BeanCourseOfDisease();
//                beanCourseOfDisease1.setDate("6666666666YYYYYYYY");
//                beanCourseOfDisease1.setBeanMedRec(beanMedRec);
//                beanCourseOfDisease1.save();
//
//                BeanMedRec beanMedRec2 = DataSupport.find(BeanMedRec.class, 1, true);
//                Log.i("haha", beanMedRec2.getName());
//                List<BeanCourseOfDisease> listCourse = beanMedRec2.getListCourseOfDisease();
//                if (listCourse.size() != 0) {
//                    for (int i = 0; i < listCourse.size(); i++) {
//                        String s = listCourse.get(i).getDate();
//                        Log.i("haha", s);
//                    }
//                }


                // DataSupport.deleteAll(BeanMedRec.class);
                break;
        }
    }

    //时间选择对话框
    private void selectTime() {
        DatePickerDialog datePicker_dialog = new DatePickerDialog(this, new
                DatePickerDialog.MyListener() {
                    @Override
                    public void refreshUI(String string) {
                        clinicalTime.setText(string);
                    }
                });
        datePicker_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePicker_dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case COURSE_OF_DISEASE:
                BeanMedRec beanMedRec = (BeanMedRec) data.getSerializableExtra("test");
                medRec = DataSupport.find(BeanMedRec.class, beanMedRec.getId(), true);


                listCourseOfDiseases.clear();
                listCourseOfDiseases = (List<BeanCourseOfDisease>) medRec.getListCourseOfDisease();
                //medRec.setListCourseOfDisease(listCourseOfDiseases);
                //listCourseOfDiseases.add((BeanCourseOfDisease) medRec.getListCourseOfDisease());
                initList(listCourseOfDiseases);
                break;


            case INFO:
                BeanMedRec medRecInfo = (BeanMedRec) data.getSerializableExtra("forInfo");
                medRec.setName(medRecInfo.getName());
                medRec.setGender(medRecInfo.getGender());
                medRec.setBirthday(medRecInfo.getBirthday());
                medRec.setIDno(medRecInfo.getIDno());
                medRec.setOccupation(medRecInfo.getOccupation());
                medRec.setMarital_status(medRecInfo.getMarital_status());
                setInfo();
                break;
            case LABLE:
                BeanMedRec medRecLables = (BeanMedRec) data.getSerializableExtra("forLable");
                medRec.setLables(medRecLables.getLables());
                setLableTv();
                break;
        }
    }

    //改变lable控件的值
    private void setLableTv() {
        List<String> lables = new ArrayList<>();
        lables = (List<String>) medRec.getLables();
        String lableText = "";
        if (lables.size() != 0) {
            for (int i = 0; i < lables.size(); i++) {
                lableText = lableText + lables.get(i) + "  ";
            }
        }
        lable.setText(lableText);
    }


    /*
    * 改变控件name 、patientInfo控件的值
    * */
    private void setInfo() {
        name.setText("姓名： " + medRec.getName());
        patientInfo.setText(medRec.getGender());
    }

}
