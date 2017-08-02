package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanPhyQuestionnaireTest;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserPhyIdReq;
import com.healthyfish.healthyfish.POJO.BeanUserPhysical;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.AutoLogin;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：健康管理模块体质确认，体质报告页面
 * 作者：LYQ on 2017/7/11.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class PhyIdeReport extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_main_physique)
    TextView tvMainPhysique;
    @BindView(R.id.tv_both_phy_title)
    TextView tvBothPhyTitle;
    @BindView(R.id.tv_both_physique)
    TextView tvBothPhysique;
    @BindView(R.id.tv_introduction_title)
    TextView tvIntroductionTitle;
    @BindView(R.id.tv_describe_title)
    TextView tvDescribeTitle;
    @BindView(R.id.tv_describe_details)
    TextView tvDescribeDetails;
    @BindView(R.id.tv_suggest_title)
    TextView tvSuggestTitle;
    @BindView(R.id.tv_suggest_details)
    TextView tvSuggestDetails;
    @BindView(R.id.bt_save)
    Button btSave;

    private String uid;
    private final String phyNames[] = {"气虚质", "阳虚质", "阴虚质", "痰湿质", "湿热质", "血淤质", "气郁质", "特禀质", "平和质"};//0-8

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phy_report);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"体质报告");
        uid = MyApplication.uid;
        initData();
    }

    /**
     * 初始化显示数据
     */
    private void initData() {
        if (getIntent().getBooleanExtra("IS_TESTED", false)) {//由健康管理首页跳转过来
            btSave.setText("重新测试");
        } else {//由测试页面跳转过来
            btSave.setText("保存");

            phyReportReq(uid);

//            if (getIntent().getStringArrayExtra("PHY_NAME") != null) {
//                String[] phy = getIntent().getStringArrayExtra("PHY_NAME");
//                tvMainPhysique.setText(phy[0]);
//                if (phy.length >= 3) {
//                    tvBothPhysique.setText(phy[1]+"、"+phy[2]);
//                } else if (phy.length == 2) {
//                    tvBothPhysique.setText(phy[1]);
//                } else {
//                    tvBothPhyTitle.setVisibility(View.GONE);
//                    tvBothPhysique.setVisibility(View.GONE);
//                }
//                tvIntroductionTitle.setText(phy[0]+"简介");
//            }
        }
    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        //点击保存按钮
        if (btSave.getText().toString().equals("重新测试")) {
            Intent intent = new Intent(this, IndexPhysicalIdentification.class);
            startActivity(intent);
        } else if (btSave.getText().toString().equals("保存")) {
            //保存操作
            MyToast.showToast(this, "保存成功");
            btSave.setText("重新测试");//保存成功后的操作
        }
    }

    /**
     * 获取体质报告
     * @param uid
     */
    private void phyReportReq(String uid) {

        BeanUserListValueReq beanUserListReq = new BeanUserListValueReq();
        beanUserListReq.setPrefix("phyad_" + uid);
        beanUserListReq.setFrom(0);
        beanUserListReq.setTo(-1);
        beanUserListReq.setNum(-1);
        Log.i("LYQ", "beanUserListReq参数json:"+ JSON.toJSONString(beanUserListReq));

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanUserListReq), new Subscriber<ResponseBody>() {

            String strResp = "";

            @Override
            public void onCompleted() {
                if (!TextUtils.isEmpty(strResp)) {
                    BeanUserPhysical beanUserPhysical = JSON.parseObject(strResp, BeanUserPhysical.class);
                    tvIntroductionTitle.setText(beanUserPhysical.getTitle() + "简介");
                    tvDescribeDetails.setText(beanUserPhysical.getDesc());
                    tvSuggestDetails.setText(beanUserPhysical.getSuggest());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "体质onError：" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    strResp = responseBody.string();
                    Log.i("LYQ", "体质报告：" + strResp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
