package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.CreateCourseGridAdapter;
import com.healthyfish.healthyfish.adapter.PhyGvAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.ui.activity.healthy_circle.HealthyCirclePosting;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 描述：红外皮温测试
 * 作者：Wayne on 2017/7/9 17:13
 * 邮箱：liwei_happyman@qq.com
 * 编辑：
 */
// FIXME: 2017/7/9 完成红外皮温上传
public class PhyIdeInfraredTest extends BaseActivity implements AdapterView.OnItemClickListener{

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_select_phy_title)
    TextView tvSelectPhyTitle;
    @BindView(R.id.gv_select_phy)
    GridView gvSelectPhy;
    @BindView(R.id.gv_select_img)
    GridView gvSelectImg;
    @BindView(R.id.bt_complete)
    Button btComplete;

    private CreateCourseGridAdapter gridAdapter;//图片选择器适配器
    private List<String> imagePaths = new ArrayList<>();//（后面用的）图片的路径list
    private ImageCaptureManager captureManager; // 相机拍照处理类
    public static final int REQUEST_CAMERA_CODE = 11;//摄像头拍照的标志
    public static final int REQUEST_PREVIEW_CODE = 12;//预览的标志

    private PhyGvAdapter adapter;//选择体质适配器
    String[] phyNames = new String[]{"平和质","气虚质","阳虚质","阴虚质","痰湿质","湿热质","血瘀质","气郁质","特秉质"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phy_ide_infrared_test);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "红外皮温");
        setTextColorAndSize();
        initGvSelectPhy();
        refreshAdpater(imagePaths);
        gvSelectImg.setOnItemClickListener(this);
    }



    @OnClick(R.id.bt_complete)
    public void onViewClicked() {
        //点击完成按钮,将图片上传，选择的体质上传
        String phy1 = null,phy2 = null,phy3 = null;
        for (int i=0;i<phyNames.length;i++) {
            if (adapter.getTvOrderList().get(i).getText().toString().equals("1")) {
                phy1 = phyNames[i];
            } else if (adapter.getTvOrderList().get(i).getText().toString().equals("2")) {
                phy2 = phyNames[i];
            } else if (adapter.getTvOrderList().get(i).getText().toString().equals("3")) {
                phy3 = phyNames[i];
            }
        }

        if (phy1 != null) {
            String[] selectedPhy;
            if (phy3 != null) {
                selectedPhy = new String[]{phy1,phy2,phy3};
            } else if (phy2 != null) {
                selectedPhy = new String[]{phy1, phy2};
            } else {
                selectedPhy = new String[]{phy1};
            }
            Intent intent = new Intent(this, PhyIdeReport.class);
            intent.putExtra("PHY_NAME", selectedPhy);
            startActivity(intent);
        } else {
            MyToast.showToast(this,"请至少选择一种体质噢");
        }
    }

    /**
     * 初始化选择体质的GridView
     */
    private void initGvSelectPhy() {
        List<String> list = new ArrayList<>();
        for (int i=0; i<phyNames.length; i++){
            list.add(phyNames[i]);
        }
        adapter = new PhyGvAdapter(this, list,3);  //设置适配器并设置最大可选体质数为3
        gvSelectPhy.setAdapter(adapter);
    }

    /**
     * 设置字体颜色和大小
     */
    private void setTextColorAndSize() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("根据红外热像仪热图报告单选择您的体质");
        //设置字体大小
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(50);
        spannableString.setSpan(absoluteSizeSpan, 2, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_secondary));
        spannableString.setSpan(colorSpan, 2, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSelectPhyTitle.setText(spannableString);
    }

    /**
     * 根据页面传回来的数据进行判断处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择图片的返回结果
        if (resultCode == HealthyCirclePosting.RESULT_OK) {
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

    /**
     * 选择图片，设置选择图片的张数
     */
    protected void selectImg() {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照， 默认false
        intent.setMaxTotal(8); // 最多选择照片数量，默认为8
        intent.setSelectedPaths((ArrayList) imagePaths); // 已选中的照片地址， 用于回显选中状态
        // intent.setImageConfig(config);
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    /**
     * 刷新适配器
     * @param paths
     */
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
            gvSelectImg.setAdapter(gridAdapter);
            gridAdapter.notifyDataSetChanged();
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    /**
     * GridView的点击监听
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
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
}
