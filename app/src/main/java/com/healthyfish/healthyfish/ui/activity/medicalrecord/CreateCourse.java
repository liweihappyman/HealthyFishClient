package com.healthyfish.healthyfish.ui.activity.medicalrecord;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import android.widget.Toast;


import com.healthyfish.healthyfish.POJO.BeanCourseOfDisease;
import com.healthyfish.healthyfish.POJO.BeanMedRec;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.CreateCourseGridAdapter;
import com.healthyfish.healthyfish.constant.constants;
import com.healthyfish.healthyfish.ui.widget.DatePickerDialog;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.Utils1;
import com.lidong.photopicker.ImageCaptureManager;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.zhy.autolayout.AutoLinearLayout;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class CreateCourse extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int CREATE_COURSE_RESULT_UPDATE_OR_DEL = 30;//病程更新返回标志
    public static final int CREATE_COURSE_RESULT_SAVE = 31;//病程保存返回标志
    private CreateCourseGridAdapter gridAdapter;//适配器
    private List<String> imagePaths = new ArrayList<>();//（后面用的）图片的路径list
    private ImageCaptureManager captureManager; // 相机拍照处理类
    public static final int REQUEST_CAMERA_CODE = 11;//摄像头拍照的标志
    public static final int REQUEST_PREVIEW_CODE = 12;//预览的标志
    private BeanCourseOfDisease courseOfDisease = new BeanCourseOfDisease();
    private BeanMedRec medRec = new BeanMedRec();

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
        initListener();
        if (constants.POSITION_COURSE == -1) {
            initData1();
        } else {
            initData2();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.med_rec, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.del:
                delete();
                break;
        }
        return true;
    }

    //删除病程的操作,新建状态提示没有可删除的病程
    private void delete() {
        if (constants.POSITION_COURSE != -1) {
            showDelDialog();// 删除提示对话框
        } else {
            Toast.makeText(this, "没有可删除的病程哦", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 删除提示对话框
     */
    private void showDelDialog() {
        new AlertDialog.Builder(CreateCourse.this).setMessage("是否要删除此病程")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courseOfDisease.delete(BeanCourseOfDisease.class, courseOfDisease.getId());
                        dialog.dismiss();
                        Intent intent = new Intent(CreateCourse.this, NewMedRec.class);
                        setResult(CREATE_COURSE_RESULT_UPDATE_OR_DEL, intent);
                        finish();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    //点击创建病程进来执行的初始化操作
    private void initData1() {
        date.setText(Utils1.getTime());
        refreshAdpater(imagePaths);
    }

    //如果是点击item进来初始化数据，执行这个方法
    //根据ID获取当前编辑的medRec的数据，并做相应的处理
    private void initData2() {
        medRec = DataSupport.find(BeanMedRec.class, NewMedRec.ID, true);
        courseOfDisease = medRec.getListCourseOfDisease().get(constants.POSITION_COURSE);
        type.setText(courseOfDisease.getType());
        date.setText(courseOfDisease.getDate());
        recPatientInfo.setText(courseOfDisease.getRecPatientInfo());
        List<String> paths = courseOfDisease.getImgPaths();
        refreshAdpater(paths);
    }

    //初始化控件的监听
    private void initListener() {
        typeLy.setOnClickListener(this);
        date.setOnClickListener(this);
        createCourseImgGridview.setOnItemClickListener(this);
        save.setOnClickListener(this);
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
                saveOrUpdata();
                break;
        }
    }

    //执行保存或者更新操作，保存操作在NewMedRec活动里执行
    private void saveOrUpdata() {
        getInfo();//获取控件的值
        if (constants.POSITION_COURSE != -1) {//每次更新必须重新关联
            courseOfDisease.setBeanMedRec(medRec);
            //更新操作
            courseOfDisease.update(courseOfDisease.getId());
            uploadImages();

//            Intent intent = new Intent(CreateCourse.this, NewMedRec.class);
//            intent.putExtra("updateCourse", courseOfDisease);
//            setResult(CREATE_COURSE_RESULT_UPDATE_OR_DEL, intent);
//            finish();
        } else {
            //数据库特性，这个定义的id会在保存的时候被赋值，所以可以根据这个id操作
//                    BeanCourseOfDisease courseOfDisease = new BeanCourseOfDisease();
//                    courseOfDisease.setType(type.getText().toString());
//                    courseOfDisease.setDate(date.getText().toString());
//                    courseOfDisease.setRecPatientInfo(recPatientInfo.getText().toString());
//                    courseOfDisease.setImgPaths(imagePaths);
            //将数据返回NewMedRec活动进行保存
            Intent intent = new Intent(CreateCourse.this, NewMedRec.class);
            intent.putExtra("saveCourse", courseOfDisease);
            setResult(CREATE_COURSE_RESULT_SAVE, intent);
            finish();

        }
    }

    private void uploadImages()  {
        if (imagePaths.size()>0){
            //List<File> files = new ArrayList<>();//从路径直接获取到的图片文件
            List<File> compressFiles = new ArrayList<>();//暂时存放压缩后的图片，用来上传
            for (int i = 0;i<imagePaths.size();i++){
                File file = new File(imagePaths.get(i));
                final DecimalFormat df = new DecimalFormat("00.0000");
                try {
                    String size = df.format(((double)(new FileInputStream(file).available())) / 1024 / 1024);
                    Log.i("图片原来的大小"+i,"第  "+i+"  张:  "+size+"   M");
                    if (Float.valueOf(size)<0.6){//如果文件小于600k，就不用压缩了
                        compressFiles.add(file);
                    }else {
                        compressFiles.add(Luban.with(CreateCourse.this).load(file).get());
                        String compressSize = null;
                        try {
                            compressSize = df.format(((double)(new FileInputStream(Luban.with(CreateCourse.this).load(file).get()).available())) / 1024 / 1024);
                            Log.i("压缩后的图片"," "+compressSize+"   M");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                        Observable.just(file)
//                                .subscribeOn(AndroidSchedulers.mainThread())
//                                .map(new Func1<File, File>() {
//                                    @Override
//                                    public File call(File file) {
//                                        File f = null;
//                                        try {
//                                            f = Luban.with(CreateCourse.this).load(file).get();
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                        return f;
//                                    }
//                                })
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Subscriber<File>() {
//                                    @Override
//                                    public void onCompleted() {
//
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        Log.i("压缩图片异常","" +e.toString());
//                                    }
//
//                                    @Override
//                                    public void onNext(File file) {
//                                        try {
//                                            Log.i("压缩后图片的大小",""+df.format((double)(new FileInputStream(file).available()) / 1024 / 1024 )+"M");
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                        compressFiles.add(file);
//                                    }
//                                });
//================================================================
//                        Luban.with(this)
//                                .load(file)                     //传人要压缩的图片
//                                .setCompressListener(new OnCompressListener() { //设置回调
//                                    @Override
//                                    public void onStart() {
//                                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
//                                    }
//                                    @Override
//                                    public void onSuccess(File file) {
//                                        // TODO 压缩成功后调用，返回压缩后的图片文件
//                                        try {
//                                            Log.i("压缩后图片的大小",""+df.format((double)(new FileInputStream(file).available()) / 1024 / 1024 )+"M");
//                                            compressFiles.add(file);
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        // TODO 当压缩过程出现问题时调用
//                                    }
//                                }).launch();    //启动压缩
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for(int k = 0; k<compressFiles.size();k++){
                DecimalFormat df = new DecimalFormat("#.0000");
                String size = null;
                try {
                    size = df.format(((double)(new FileInputStream(compressFiles.get(k)).available())) / 1024 / 1024);
                    Log.i("等待上传的图片的大小"+k,"第  "+k+"  张:  "+size+"   M");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            MultipartBody body = OkHttpUtils.filesToMultipartBody(compressFiles);
            RetrofitManagerUtils.getInstance(this,null).uploadFilesRetrofit(body, new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {
                    Log.i("图片上传","完成");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i("图片上传","错误"+e.toString());
                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    try {
                        String str = responseBody.string();
                        Log.i("图片上传","返回数据"+str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //http://www.kangfish.cn/demo/downloadFile/upload/20170318/19411489838119199.jpg
                }
            });
        }
    }

    //获取控件的值
    private void getInfo() {
        courseOfDisease.setType(type.getText().toString());
        courseOfDisease.setDate(date.getText().toString());
        courseOfDisease.setRecPatientInfo(recPatientInfo.getText().toString());
        courseOfDisease.setImgPaths(imagePaths);
    }

    //用poppopupwindow+listView+ArrayAdapter实现弹窗选择菜单
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
                type.setText(parent.getItemAtPosition(position).toString());
                mPopWindow.dismiss();
            }
        });
        mPopWindow.showAsDropDown(typeLy);
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
        //intent.setImageConfig(config);
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
            gridAdapter = new CreateCourseGridAdapter(imagePaths,this);
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
