package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.healthyfish.healthyfish.POJO.BeanBaseKeyGetReq;
import com.healthyfish.healthyfish.POJO.BeanCustomPlan;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCustomizeReq;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanListReq;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanReq;
import com.healthyfish.healthyfish.POJO.BeanListReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ConcreteArrangementAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.DateUtils;
import com.healthyfish.healthyfish.utils.NestingUtils;
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

import static com.healthyfish.healthyfish.constant.Constants.HttpsHealthyFishyUrl;

/**
 * 描述：健康管理自定义计划具体安排页面
 * 作者：LYQ on 2017/7/17.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class ConcreteArrangement extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.switch_open_reminder)
    ToggleButton switchOpenReminder;
    @BindView(R.id.lv_concrete_arrangement)
    ListView lvConcreteArrangement;
    @BindView(R.id.bt_next)
    Button btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_arrangement);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "具体安排");
        initListView();
    }

    private void initListView() {
        List<BeanCustomPlan> customPlan = new ArrayList<>();
        List<String> selectDateList = new ArrayList<>();
        if (getIntent().getStringArrayListExtra("selectDateList") != null) {
            selectDateList = getIntent().getStringArrayListExtra("selectDateList");
        }
        List<String> plan = new ArrayList<>();
        plan.add("艾灸");
        plan.add("推拿");

        String DateType = "yyyy年MM月dd日";
        for (String str : selectDateList) {
            BeanCustomPlan bean = new BeanCustomPlan();
            bean.setDateAndWeek(str.substring(5, str.length()) + " " + DateUtils.getWeekFromStr(DateType, str));
            bean.setTime("9:00");
            bean.setIndividualPlan(plan);
            customPlan.add(bean);
        }

        ConcreteArrangementAdapter adapter = new ConcreteArrangementAdapter(this, customPlan);
        lvConcreteArrangement.setAdapter(adapter);
    }

    @OnClick(R.id.bt_next)
    public void onViewClicked() {
        //点击下一步
        customSchemeReq();
    }

    private void customSchemeReq() {
        BeanHealthPlanListReq beanHealthPlanListReq = new BeanHealthPlanListReq();

        BeanHealthPlanReq beanHealthPlanReq = new BeanHealthPlanReq();
        beanHealthPlanReq.setKey("hpc_");

        BeanListReq beanListReq = new BeanListReq();
        beanListReq.setPrefix("hpc_");
        beanListReq.setFrom(0);
        beanListReq.setTo(-1);
        beanListReq.setNum(-1);

        BeanBaseKeyGetReq beanBaseKeyGetReq = new BeanBaseKeyGetReq();
        beanBaseKeyGetReq.setKey("hpc_20170516_be36facd-9890-433d-bb21-dc0830bf80b4");


        RetrofitManagerUtils.getInstance(this, HttpsHealthyFishyUrl).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanListReq), new Subscriber<ResponseBody>() {
            String healthPlanListResp = "";

            @Override
            public void onCompleted() {
                Log.i("LYQ", "customSchemeReq()_onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("LYQ", "customSchemeReq()_onError:" + e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    healthPlanListResp = responseBody.string();
                    Log.i("LYQ", "healthPlanListResp:" + healthPlanListResp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
