package com.healthyfish.healthyfish.ui.activity.personal_center;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeyRemReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeySetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanPersonalInformation;
import com.healthyfish.healthyfish.POJO.BeanUploadImagesResp;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.ui.activity.medicalrecord.CreateCourse;
import com.healthyfish.healthyfish.ui.widget.DatePickerDialog;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.zhy.autolayout.AutoLinearLayout;


import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import rx.Subscriber;
import top.zibin.luban.Luban;

import static com.healthyfish.healthyfish.constant.Constants.HttpHealthyFishyUrl;

/**
 * 描述：个人中心>个人信息>修改个人信息页面
 * 作者：LYQ on 2017/7/29.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ChangePersonalInformation extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.civ_head_portrait)
    CircleImageView civHeadPortrait;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_birthDate)
    TextView tvBirthDate;
    @BindView(R.id.et_phoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_idCard)
    EditText etIdCard;
    @BindView(R.id.iv_go)
    ImageView ivGo;
    @BindView(R.id.lly_id_card)
    AutoLinearLayout llyIdCard;

    private String uid = MyApplication.uid;
    private BeanPersonalInformation personalInformation = new BeanPersonalInformation();

    private String phone = "";//手机号
    private String name = "";//姓名
    private String nickname = "";//昵称
    private String imgUrl = "";//头像
    private String gender = "";//性别
    private String birthDate = "";//出生日期
    private String idCard = "";//身份证号

    private final int mResultCode = 10035;

    private List<BeanUploadImagesResp> beanUploadImagesRespList = new ArrayList<>();//上传头像后返回的响应List,放弃本次编辑时用来清除服务器图片
    private ImageCaptureManager captureManager; // 相机拍照处理类
    public static final int REQUEST_CAMERA_CODE = 11;//摄像头拍照的标志
    public static final int REQUEST_PREVIEW_CODE = 12;//预览的标志


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_personal_information);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "修改个人信息");
        initData();
    }

    @OnClick({R.id.tv_save, R.id.civ_head_portrait, R.id.tv_gender, R.id.tv_birthDate, R.id.iv_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                getInputData();
                saveDataToNetwork();
                break;
            case R.id.civ_head_portrait:
                selectImg();
                break;
            case R.id.tv_gender:
                chooseGenderDialog();
                break;
            case R.id.tv_birthDate:
                selectBirthDate();
                break;
            case R.id.iv_go:
                break;
        }
    }


    /**
     * 初始化个人信息数据
     */
    private void initData() {
        BeanPersonalInformation beanPersonalInformation;
        if (getIntent().getSerializableExtra("BeanPersonalInformation") != null) {
            beanPersonalInformation = (BeanPersonalInformation) getIntent().getSerializableExtra("BeanPersonalInformation");
            initWidget(beanPersonalInformation);
        } else {
            String key = "info_" + uid;
            List<BeanPersonalInformation> personalInformationList = DataSupport.where("key = ?", key).find(BeanPersonalInformation.class);
            if (!personalInformationList.isEmpty()) {
                beanPersonalInformation = personalInformationList.get(0);
                initWidget(beanPersonalInformation);
            }
        }
    }

    /**
     * 显示个人信息
     *
     * @param beanPersonalInformation
     */
    private void initWidget(BeanPersonalInformation beanPersonalInformation) {
        if (!TextUtils.isEmpty(beanPersonalInformation.getImgUrl())) {
            Glide.with(this).load(HttpHealthyFishyUrl+beanPersonalInformation.getImgUrl()).error(R.mipmap.error).into(civHeadPortrait);
            imgUrl = beanPersonalInformation.getImgUrl();
        }
        etNickname.setText(beanPersonalInformation.getNickname());
        tvGender.setText(beanPersonalInformation.getGender());
        tvBirthDate.setText(beanPersonalInformation.getBirthDate());
        etPhoneNumber.setText(beanPersonalInformation.getPhone());
        etName.setText(beanPersonalInformation.getName());
        etIdCard.setText(beanPersonalInformation.getIdCard());
    }

    /**
     * 重写返回按钮的监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showBackDialog();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 重写物理返回键的监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showBackDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getInputData() {
        nickname = etNickname.getText().toString().trim();
        gender = tvGender.getText().toString().trim();
        birthDate = tvBirthDate.getText().toString().trim();
        phone = etPhoneNumber.getText().toString().trim();
        name = etName.getText().toString().trim();
        idCard = etIdCard.getText().toString().trim();
    }

    /**
     * 将个人信息同步保存到服务器
     */
    private void saveDataToNetwork() {

        if (!TextUtils.isEmpty(phone)) {
            if (phone.length() != 11) {
                MyToast.showToast(this, "请确认您的手机号是否正确");
                return;
            }
        }
        if (!TextUtils.isEmpty(idCard)) {
            if (idCard.length() != 18) {
                MyToast.showToast(this, "请确认您的身份证号是否正确");
                return;
            }
        }
        final String key = "info_" + uid;
        personalInformation = new BeanPersonalInformation();
        personalInformation.setKey(key);
        personalInformation.setNickname(nickname);
        personalInformation.setGender(gender);
        personalInformation.setPhone(phone);
        personalInformation.setName(name);
        personalInformation.setBirthDate(birthDate);
        personalInformation.setIdCard(idCard);
        personalInformation.setImgUrl(imgUrl);
        String jsonReq = JSON.toJSONString(personalInformation);

        Log.i("LYQ", "jsonReq:" + jsonReq);
        BeanBaseKeySetReq beanBaseKeySetReq = new BeanBaseKeySetReq();
        beanBaseKeySetReq.setKey(key);
        beanBaseKeySetReq.setValue(jsonReq);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeySetReq), new Subscriber<ResponseBody>() {
            String resp = null;

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(resp)) {
                    BeanBaseResp beanBaseResp = JSON.parseObject(resp, BeanBaseResp.class);
                    if (beanBaseResp.getCode() == 0) {
                        MyToast.showToast(ChangePersonalInformation.this, "修改个人信息成功");
                        boolean isSave = personalInformation.saveOrUpdate("key = ?", key);//将个人信息保存到数据库
                        if (isSave) {
                            if (beanUploadImagesRespList.size() > 1) {
                                beanUploadImagesRespList.remove(beanUploadImagesRespList.size() - 1);
                                removeKey();
                            }
                            EventBus.getDefault().post(personalInformation);//发送消息提醒刷新个人中心的个人信息
                            Intent intent = new Intent(ChangePersonalInformation.this, PersonalInformation.class);
                            ChangePersonalInformation.this.setResult(mResultCode, intent);
                            finish();
                        } else {
                            MyToast.showToast(ChangePersonalInformation.this, "保存个人信息失败，请重试");
                        }
                    } else {
                        MyToast.showToast(ChangePersonalInformation.this, "修改个人信息失败，请重试");
                    }
                } else {
                    MyToast.showToast(ChangePersonalInformation.this, "修改个人信息失败，请重试");
                }
                Log.i("LYQ", "上传请求onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                MyToast.showToast(ChangePersonalInformation.this, "修改个人信息失败，" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    resp = responseBody.string();
                    Log.i("LYQ", "上传请求返回：" + resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 性别选择对话框
     */
    private void chooseGenderDialog() {
        final String[] strings = new String[]{"男", "女"};
        new AlertDialog.Builder(ChangePersonalInformation.this)
                .setTitle("请选择性别")
                .setSingleChoiceItems(strings, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                tvGender.setText(strings[which]);
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 出生日期选择对话框
     */
    private void selectBirthDate() {
        DatePickerDialog datePicker_dialog = new DatePickerDialog(this, new
                DatePickerDialog.MyListener() {
                    @Override
                    public void refreshUI(String string) {
                        tvBirthDate.setText(string);
                    }
                });
        datePicker_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        datePicker_dialog.show();
    }

    /**
     * 退出提示对话框
     */
    private void showBackDialog() {
        new AlertDialog.Builder(ChangePersonalInformation.this).setMessage("您确定要放弃本次修改吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeKey();
                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 设置选择图片的张数
     */
    protected void selectImg() {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照， 默认false
        intent.setMaxTotal(1); // 最多选择照片数量，默认为8
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    /**
     * 上传和显示头像
     */
    private void showAndUploadHeadPortrait(List<String> paths) {
        if (!paths.isEmpty()) {
            Glide.with(ChangePersonalInformation.this).load(paths.get(0)).error(R.mipmap.error).into(civHeadPortrait);
            uploadImage(getCompressFiles(paths));
        }
    }

    /**
     * 上传头像
     */
    private void uploadImage(final List<File> compressFiles) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < compressFiles.size(); i++) {
                    final List<File> list = new ArrayList<>();
                    list.clear();
                    list.add(compressFiles.get(i));
                    RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null).uploadFilesRetrofit(list, i, new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("LYQ", "上传图片：" + e.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyToast.showToast(MyApplication.getContetxt(), "头像上传失败" );
                                    Glide.with(ChangePersonalInformation.this).load(imgUrl).error(R.mipmap.ic_logo).into(civHeadPortrait);
                                }
                            });
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                String str = responseBody.string();
                                Log.i("图片上传", "返回数据:" + str);
                                if (str != null) {
                                    final BeanUploadImagesResp beanUploadImagesResp = JSON.parseObject(str, BeanUploadImagesResp.class);
                                    if (beanUploadImagesResp.getCode() == 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                imgUrl = beanUploadImagesResp.getUrl();
                                                if (!TextUtils.isEmpty(imgUrl)) {
                                                    beanUploadImagesRespList.add(beanUploadImagesResp);//后期用来清除服务器图片
                                                    MyToast.showToast(MyApplication.getContetxt(), "头像上传成功");
                                                    Glide.with(ChangePersonalInformation.this).load(HttpHealthyFishyUrl + imgUrl).into(civHeadPortrait);
                                                }
                                            }
                                        });
                                    } else if (beanUploadImagesResp.getCode() < 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                MyToast.showToast(MyApplication.getContetxt(), "头像上传失败，请重新选择");
                                                Glide.with(ChangePersonalInformation.this).load(imgUrl).error(R.mipmap.ic_logo).into(civHeadPortrait);
                                            }
                                        });
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 压缩上传的图片文件
     *
     * @return 压缩后的图片L
     */
    @NonNull
    private List<File> getCompressFiles(List<String> imagePathList) {
        List<File> compressFiles = new ArrayList<>();//暂时存放压缩后的图片，用来上传
        for (int i = 0; i < imagePathList.size(); i++) {
            File file = new File(imagePathList.get(i));
            final DecimalFormat df = new DecimalFormat("00.0000");
            try {
                String size = df.format(((double) (new FileInputStream(file).available())) / 1024 / 1024);
                if (Float.valueOf(size) < 0.6) {//如果文件小于600k，就不用压缩了
                    compressFiles.add(file);
                } else {
                    compressFiles.add(Luban.with(MyApplication.getContetxt()).load(file).get());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return compressFiles;
    }

    /**
     * 清除服务器上的头像
     */
    private void removeKey() {
        for (int i = 0; i < beanUploadImagesRespList.size(); i++) {
            BeanBaseKeyRemReq beanBaseKeyRemReq = new BeanBaseKeyRemReq();
            beanBaseKeyRemReq.setKey(beanUploadImagesRespList.get(i).getBeanKey());

            RetrofitManagerUtils.getInstance(MyApplication.getContetxt(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeyRemReq), new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ResponseBody responseBody) {

                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CreateCourse.RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    showAndUploadHeadPortrait(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
                        showAndUploadHeadPortrait(paths);
                    }
                    break;
            }
        }
    }

}
