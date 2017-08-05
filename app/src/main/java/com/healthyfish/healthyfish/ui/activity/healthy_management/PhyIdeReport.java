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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseResp;
import com.healthyfish.healthyfish.POJO.BeanPhyQuestionnaireTest;
import com.healthyfish.healthyfish.POJO.BeanUserListValueReq;
import com.healthyfish.healthyfish.POJO.BeanUserPhy;
import com.healthyfish.healthyfish.POJO.BeanUserPhyIdReq;
import com.healthyfish.healthyfish.POJO.BeanUserPhyIdResp;
import com.healthyfish.healthyfish.POJO.BeanUserPhysical;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.AutoLogin;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import org.litepal.crud.DataSupport;

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
    private List<BeanUserPhysical> physicals = new ArrayList<>();
    private String jsonStrPhysicalList = "";

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
            List<BeanUserPhy> beanUserPhyList = DataSupport.where("uid = ?", uid).find(BeanUserPhy.class);
            if (!beanUserPhyList.isEmpty()) {
                BeanUserPhyIdResp beanUserPhyIdResp = JSON.parseObject(beanUserPhyList.get(0).getJsonStrPhysicalList(), BeanUserPhyIdResp.class);
                initWidget(beanUserPhyIdResp.getPhyList());
            }
            btSave.setText("重新测试");
        } else {//由测试页面跳转过来
            btSave.setText("保存");

            phyReportReq(uid);
        }
    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        //点击保存按钮
        if (btSave.getText().toString().equals("重新测试")) {
            Intent intent = new Intent(this, IndexPhysicalIdentification.class);
            startActivity(intent);
            finish();
        } else if (btSave.getText().toString().equals("保存")) {
            //保存操作
            BeanUserPhy beanUserPhy = new BeanUserPhy();
            beanUserPhy.setUid(uid);
            beanUserPhy.setJsonStrPhysicalList(jsonStrPhysicalList);

            boolean isSave = beanUserPhy.saveOrUpdate("uid = ?",uid );
            if (isSave) {
                MyToast.showToast(this, "保存成功");
                btSave.setText("重新测试");//保存成功后的操作
            } else {
                MyToast.showToast(this, "保存失败，请重试");
            }

        }
    }

    /**
     * 获取体质报告
     * @param uid
     */
    private void phyReportReq(final String uid) {

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
                    List<String> strList = JSONArray.parseObject(strResp, List.class);
                    for (String str : strList) {
                        jsonStrPhysicalList = str;
                        BeanUserPhyIdResp beanUserPhyIdResp = JSON.parseObject(str, BeanUserPhyIdResp.class);
                        if (beanUserPhyIdResp.getCode() == 0) {
                            physicals = beanUserPhyIdResp.getPhyList();
                        }
                    }
                    initWidget(physicals);
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
 //["{\"code\":0,\"phyList\":[{\"desc\":\"以机体阴阳平衡，气血调和，充满活力为主要特征。一般来说，平和质的人形体匀称健壮、平素患病较少、性格开朗、面色红润光泽、讲话中气十足、纳寐可、二便调、舌淡红、苔薄白、脉和有神。一派生机勃勃之气。\",\"suggest\":\"\",\"title\":\"平和\"}],\"ver\":\"0.1\"}"]
//["{\"code\":0,\"phyList\":[{\"desc\":\"由于先天禀赋不足和禀赋遗传等因素造成的一种特殊体质，故以先天不足，过敏反应为主要特征。此类人形体或无特殊，或有畸形，或有先天生理缺陷。易患遗传疾病、易过敏。尽管没有感冒也会出现打喷嚏、流清涕、鼻塞等症状。\",\"suggest\":\"中医认为，“肾为先天之本”，特禀质养生时就应以健脾、补肾气为主，以增强卫外功能。 饮食上多吃五谷杂粮，健脾补肾。被褥床单要经常洗晒，可防止对尘螨过敏；不宜养宠物，以免对动物皮毛过敏。运动方面，可选择太极拳，调节调节阴阳平衡，增强体质。\",\"title\":\"特禀\"},{\"desc\":\"阳气不足，故以畏寒怕冷，四肢不温等虚寒表现为主要特征。一般来说，阳虚质的人肌肉松软不实亦或白白胖胖、感邪易从寒化、性格内向、面色白、声低懒言、手足不温、畏寒怕冷、易出汗、大便稀、小便清长、口唇色淡、口淡无味、食欲不振、舌质淡、苔白而润、脉虚弱。一副萎靡之象。\",\"suggest\":\"治以温阳为主。饮食方面，多食温肾壮阳食物，如鸡肉、羊肉、狗肉、韭菜等，即使在盛夏也不要过食寒凉之品。运动方面，可做些舒缓的运动，如打太极，缓缓散步等。\",\"title\":\"阳虚\"},{\"desc\":\"血行不畅，故以肤色晦暗，舌质紫暗等淤血表现为主要特征。此类人常常健忘且性情急躁易怒，易患痛证和血证，常见面色晦黯，甚者可以见肌肤甲错，色素沉着，或有紫斑，易出现黑眼圈，胸闷胸痛，口唇黯淡，可见舌下静脉曲张，女性可出现痛经、闭经。舌质青紫或有瘀点，脉细涩。\",\"suggest\":\"饮食上多吃具有活血、散结、行气、疏肝解郁作用的食物，如：海藻、海带、山楂、醋、玫瑰花、红糖、葡萄酒等。血瘀质的人很适合推拿，拔罐、刮痧、放血疗法。运动上多参与一些促进气血运行的活动，如太极拳、太极剑、传统养生保健操等。若在运动中出现胸闷、呼吸困难、脉搏急促等症状，应给予重视。\",\"title\":\"血淤\"}],\"ver\":\"0.1\"}"]

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 显示体质报告
     */
    private void initWidget(List<BeanUserPhysical> physicals) {
        tvMainPhysique.setText(physicals.get(0).getTitle()+"质");
        tvIntroductionTitle.setText(physicals.get(0).getTitle() + "质简介");
        tvDescribeDetails.setText(physicals.get(0).getDesc());
        tvSuggestDetails.setText(physicals.get(0).getSuggest());
        if (physicals.size() >= 3) {
            tvBothPhysique.setText(physicals.get(1).getTitle()+"质、"+physicals.get(2).getTitle()+"质");
        } else if (physicals.size() == 2) {
            tvBothPhysique.setText(physicals.get(1).getTitle()+"质");
        } else {
            tvBothPhyTitle.setVisibility(View.GONE);
            tvBothPhysique.setVisibility(View.GONE);
        }
    }

}
