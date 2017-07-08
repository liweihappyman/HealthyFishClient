package com.healthyfish.healthyfish.ui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.healthyfish.healthyfish.POJO.BeanCourseOfDisease;
import com.healthyfish.healthyfish.POJO.BeanMedRec;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.CreateCourseGridAdapter;
import com.healthyfish.healthyfish.constant.constants;
import com.healthyfish.healthyfish.ui.widget.DatePickerDialog;
import com.healthyfish.healthyfish.utils.Utils1;
import com.zhy.autolayout.AutoLinearLayout;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.healthyfish.healthyfish.constant.constants.FLAG_POSITION;

public class CreateCourse extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int FOR_COURSE_OF_DISEASE = 33;//添加病程请求返回标志
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.type_ly)
    AutoLinearLayout typeLy;
    @BindView(R.id.rec_patient_info)
    EditText recPatientInfo;
    @BindView(R.id.create_course_img_gridview)
    GridView createCourseImgGridview;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.save)
    TextView save;
    private CreateCourseGridAdapter gridAdapter;//适配器
    private List<String> imagePaths = new ArrayList<>();//（后面用的）图片的路径list
    private ImageCaptureManager captureManager; // 相机拍照处理类
    public static final int REQUEST_CAMERA_CODE = 11;//摄像头拍照的标志
    public static final int REQUEST_PREVIEW_CODE = 12;//预览的标志
    private BeanMedRec medRec;
    private BeanCourseOfDisease courseOfDisease = new BeanCourseOfDisease();
    private List<BeanCourseOfDisease> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        ButterKnife.bind(this);
        toolbar.setTitle("");
        toolbarTitle.setText("创建病程");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }

        initListener();//初始化监听

        medRec= (BeanMedRec) getIntent().getSerializableExtra("CreateCourse");
        list = medRec.getListCourseOfDisease();
        if (constants.FLAG_STATUE.equals("save"))
        {
            initData1();
        }else {
            initData2();
        }


    }

    private void initData1() {
        date.setText(Utils1.getTime());
        refreshAdpater(imagePaths);
    }
    //如果是点击item进来，那么就要初始化数据，执行这个方法
    private void initData2(){
        BeanCourseOfDisease courseOfDisease = medRec.getListCourseOfDisease().get(FLAG_POSITION);
        type.setText(courseOfDisease.getType());
        date.setText(courseOfDisease.getDate());
        List<String> paths = courseOfDisease.getImgPaths();
        refreshAdpater(paths);
    }





    private void initListener() {
        typeLy.setOnClickListener(this);
        date.setOnClickListener(this);
        createCourseImgGridview.setOnItemClickListener(this);
        save.setOnClickListener(this);
    }
    private void getData(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                selectTime();
                break;
            case R.id.type_ly:
                showOptions();
                break;
            case R.id.save://先获取控件数据
                if (constants.FLAG_STATUE.equals("save"))
                    {
                        BeanCourseOfDisease beanCourseOfDisease = new BeanCourseOfDisease();
                        beanCourseOfDisease.setType(type.getText().toString());
                        beanCourseOfDisease.setDate(date.getText().toString());
                        beanCourseOfDisease.setRecPatientInfo(recPatientInfo.getText().toString());
                        beanCourseOfDisease.setImgPaths(imagePaths);
                        list.add(beanCourseOfDisease);
//                        BeanCourseOfDisease beanCourseOfDisease2 = new BeanCourseOfDisease();
//                        beanCourseOfDisease2.setType(type.getText().toString());
//                        beanCourseOfDisease2.setDate(date.getText().toString());
//                        beanCourseOfDisease2.setImgPaths(imagePaths);
//                        beanCourseOfDisease2.setBeanMedRec(medRec);
//                        beanCourseOfDisease2.save();
//                   测试数据库
//                    List<BeanCourseOfDisease> list = DataSupport.findAll(BeanCourseOfDisease.class);
//                    for (int j = 0 ; j<list.size();j++) {
//                        BeanCourseOfDisease b = list.get(j);
//                        imagePaths = b.getImgPaths();
//                        Log.i("wkjpath",String.valueOf(b.getId()));
//                        for (int i = 0; i < imagePaths.size(); i++) {
//                            String s = imagePaths.get(i);
//                            Log.i("wkjpath", s);
//                    }
//                        }
                    }else {
                    //数据库特性，这个定义的id会在保存的时候被赋值，所以可以根据这个id操作
                    BeanCourseOfDisease courseOfDisease = new BeanCourseOfDisease();
                    courseOfDisease.setType(type.getText().toString());
                    courseOfDisease.setDate(date.getText().toString());
                    courseOfDisease.setRecPatientInfo(recPatientInfo.getText().toString());
                    courseOfDisease.setImgPaths(imagePaths);
                    list.remove(FLAG_POSITION);
                    list.add(courseOfDisease);
                }
                medRec.setListCourseOfDisease(list);
//                List<BeanCourseOfDisease> list = new ArrayList<>();
//                BeanCourseOfDisease beanCourseOfDisease = new BeanCourseOfDisease();
//                beanCourseOfDisease.setImgPaths(imagePaths);
//                list.add(beanCourseOfDisease);
//                medRec = new BeanMedRec();
                //beanMedRec.setListCourseOfDisease(list);
                Intent intent = new Intent(CreateCourse.this, NewMedRec.class);
                intent.putExtra("test",medRec);
                CreateCourse.this.setResult(FOR_COURSE_OF_DISEASE, intent);
                CreateCourse.this.finish();
                break;

        }

    }
    /*
    * 用poppopupwindow+listView+ArrayAdapter实现弹窗选择菜单
    * */
    private void showOptions() {
        View rootView;
        rootView = LayoutInflater.from(CreateCourse.this).inflate(R.layout.options,
                null);
        final PopupWindow mPopWindow = new android.widget.PopupWindow(rootView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setOutsideTouchable(true);
        ListView optionns = (ListView) rootView.findViewById(R.id.options_listview);
        optionns.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("option",parent.getItemAtPosition(position).toString());
                type.setText(parent.getItemAtPosition(position).toString());
                mPopWindow.dismiss();
            }
        });
        mPopWindow.showAsDropDown(typeLy);
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

    //时间选择对话框
    private void selectTime() {
        DatePickerDialog datePicker_dialog = new DatePickerDialog(this, new
                DatePickerDialog.MyListener() {
                    @Override
                    public void refreshUI(String string) {
                        date.setText(string);
                    }
                });
        datePicker_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePicker_dialog.show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 8) {//设置显示图片的数量
            if (imagePaths.size() == position) {
                selectImg();//选择图片
            } else {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(this);
                intent.setCurrentItem(position);
                intent.setPhotoPaths((ArrayList) imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        }
    }

    //选择图片，设置选择图片的张数
    protected void selectImg() {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照， 默认false
        intent.setMaxTotal(8); // 最多选择照片数量，默认为8
        intent.setSelectedPaths((ArrayList) imagePaths); // 已选中的照片地址， 用于回显选中状态
        // intent.setImageConfig(config);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    //刷新适配器
    private void refreshAdpater(List<String> paths) {
        // 处理返回照片地址
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);
        int i = imagePaths.size();
        if (gridAdapter == null) {
            gridAdapter = new CreateCourseGridAdapter(imagePaths, this);
            createCourseImgGridview.setAdapter(gridAdapter);
            gridAdapter.notifyDataSetChanged();
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CreateCourse.RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    refreshAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    refreshAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
                        refreshAdpater(paths);
                    }
                    break;
            }

        }
    }
}
