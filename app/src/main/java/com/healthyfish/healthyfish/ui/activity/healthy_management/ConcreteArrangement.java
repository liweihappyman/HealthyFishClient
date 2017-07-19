package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.healthyfish.healthyfish.POJO.BeanCustomPlan;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ConcreteArrangementAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.NestingUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        initToolBar(toolbar,toolbarTitle,"具体安排");
        initListView();
    }

    private void initListView() {
        List<BeanCustomPlan> customPlan = new ArrayList<>();

        List<String> plan1 = new ArrayList<>();
        plan1.add("艾灸");
        plan1.add("推拿");
        BeanCustomPlan bean1 = new BeanCustomPlan();
        bean1.setDateAndWeek("7月17日 星期一");
        bean1.setTime("9:00");
        bean1.setIndividualPlan(plan1);

        List<String> plan2 = new ArrayList<>();
        plan2.add("艾灸");
        plan2.add("推拿");
        plan2.add("桑拿");
        BeanCustomPlan bean2 = new BeanCustomPlan();
        bean2.setDateAndWeek("7月18日 星期二");
        bean2.setTime("11:00");
        bean2.setIndividualPlan(plan2);

        List<String> plan3 = new ArrayList<>();
        plan3.add("艾灸");
        plan3.add("推拿");
        plan3.add("针灸");
        BeanCustomPlan bean3 = new BeanCustomPlan();
        bean3.setDateAndWeek("7月18日 星期二");
        bean3.setTime("11:00");
        bean3.setIndividualPlan(plan3);

        customPlan.add(bean1);
        customPlan.add(bean2);
        customPlan.add(bean3);


        ConcreteArrangementAdapter adapter = new ConcreteArrangementAdapter(this, customPlan);
        lvConcreteArrangement.setAdapter(adapter);
    }

    @OnClick(R.id.bt_next)
    public void onViewClicked() {
        //点击下一步
    }
}
