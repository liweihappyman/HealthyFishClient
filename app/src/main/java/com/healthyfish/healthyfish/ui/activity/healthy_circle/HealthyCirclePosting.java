package com.healthyfish.healthyfish.ui.activity.healthy_circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.CreateCourseGridAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyToast;
import com.lidong.photopicker.ImageCaptureManager;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述：健康圈发帖页面
 * 作者：LYQ on 2017/7/10.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class HealthyCirclePosting extends BaseActivity implements AdapterView.OnItemClickListener{

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_share_health_trends)
    EditText etShareHealthTrends;
    @BindView(R.id.gv_share_health_trends_img)
    GridView gvShareHealthTrendsImg;
    @BindView(R.id.iv_go_select_community)
    ImageView ivGoSelectCommunity;
    @BindView(R.id.lly_select_community)
    AutoLinearLayout llySelectCommunity;
    @BindView(R.id.tv_release)
    TextView tvRelease;
    @BindView(R.id.tv_select_community)
    TextView tvSelectCommunity;

    private static final int mRequestCode = 000;

    private CreateCourseGridAdapter gridAdapter;//适配器
    private List<String> imagePaths = new ArrayList<>();//（后面用的）图片的路径list
    private ImageCaptureManager captureManager; // 相机拍照处理类
    public static final int REQUEST_CAMERA_CODE = 11;//摄像头拍照的标志
    public static final int REQUEST_PREVIEW_CODE = 12;//预览的标志


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_circle_posting);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "健康社区");
        gvShareHealthTrendsImg.setOnItemClickListener(this);
        refreshAdpater(imagePaths);
    }

    @OnClick({R.id.iv_go_select_community, R.id.lly_select_community, R.id.tv_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_select_community:
                //选择社区
                Intent intent = new Intent(this, SelectCommunity.class);
                startActivityForResult(intent, mRequestCode);
                //Intent intent = new Intent(this,SelectDepartments.class);
                //startActivity(intent);
                break;
            case R.id.lly_select_community:
                //选择社区
                break;
            case R.id.tv_release:
                //点击发布
                //Intent intent01 = new Intent(this,ConfirmReservationInformation.class);
                //startActivity(intent01);
                MyToast.showToast(this, "你确定要发布么");
                break;
        }
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
        // 选择社区的返回结果，根据发送过去的请求来区别
        if (requestCode == mRequestCode) {
            String info = data.getStringExtra("Title");
            switch (resultCode) {
                case 0:
                    //返回成功
                    tvSelectCommunity.setText(info);
                    break;
                case 2:
                    //返回失败
                    if(tvSelectCommunity.getText().toString() == "选择社区"){
                        MyToast.showToast(this,"未选择社区");
                    }
                    break;
                default:
                    tvSelectCommunity.setText("选择社区");
                    break;
            }
        }
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
            gridAdapter = new CreateCourseGridAdapter(imagePaths,this);
            gvShareHealthTrendsImg.setAdapter(gridAdapter);
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
