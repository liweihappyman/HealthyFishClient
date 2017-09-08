package com.healthyfish.healthyfish.ui.activity.healthy_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.PreviewHealthySchemeAdapter;
import com.healthyfish.healthyfish.eventbus.NoticeMessage;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class PreviewMyHealthyScheme extends BaseActivity {
    BeanHealthPlanCommendContent beanHealthPlanCommendContent;
    String[] str = {"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",};
    List<String> week = new ArrayList<>();//星期:   一、二、...
    List<String> date = new ArrayList<>();//号数，如2,3...
    List<String> calendarDate = new ArrayList<>();//字符串的日期2017年10月20日
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_complete_make_scheme)
    Button btnCompleteMakeScheme;
    @BindView(R.id.collapsing_toolbar_preview_toolbar)
    CollapsingToolbarLayout collapsingToolbarPreviewToolbar;
    @BindView(R.id.appbar_preview_scheme)
    AppBarLayout appbarPreviewScheme;
    @BindView(R.id.togglebtn_remind_scheme)
    ToggleButton togglebtnRemindScheme;
    @BindView(R.id.tv_choose_remind_time)
    TextView tvChooseRemindTime;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_type)
    TextView tvType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_my_healthy_scheme);
        ButterKnife.bind(this);
        intiToolbarView();
        init();
    }

    // 初始化toolbar
    private void intiToolbarView() {
        toolbarTitle.setText("查看养生计划");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_icon);
        }
    }

    @OnClick(R.id.btn_complete_make_scheme)
    public void onViewClicked() {
        beanHealthPlanCommendContent.save();
        EventBus.getDefault().post(new NoticeMessage(1));
        Intent intent = new Intent(PreviewMyHealthyScheme.this, MainIndexHealthyManagement.class);
        startActivity(intent);
        finish();
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

    private void init() {
        createPlan();
        initTodoData();
    }


    /**
     * 该方法只有计划创建的时候调用，生成星期（一、二）和号数（7,8），日期（2017年8月20日）
     */
    private void createPlan() {
        beanHealthPlanCommendContent = new BeanHealthPlanCommendContent();
        Calendar calendar = Calendar.getInstance();//获取日历实例
        calendar.getTime();
        for (int i = 0; i < 7; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            StringBuffer stringBuffer = new StringBuffer().append(year).append("年").append(month).append("月").append(day).append("日");
            calendarDate.add(stringBuffer.toString());
            week.add(str[calendar.get(Calendar.DAY_OF_WEEK)].substring(2));
            date.add(String.valueOf(calendar.get(Calendar.DATE)));
            calendar.add(Calendar.DATE, 1);
        }
        beanHealthPlanCommendContent.setCalendarDateJsonStr(JSON.toJSONString(calendarDate));
        beanHealthPlanCommendContent.setWeekJsonStr(JSON.toJSONString(week));
        beanHealthPlanCommendContent.setDateJsonStr(JSON.toJSONString(date));
    }


    /**
     * 初始化TodoList()，将不够7天的补齐，并且根据progress确定计划在TodoList()的位置
     *
     * @return
     */

    private void initTodoData() {
        BeanHotPlanItem beanHotPlanItem = (BeanHotPlanItem) getIntent().getSerializableExtra("plan");
        tvType.setText(beanHotPlanItem.getTitle());
        List<BeanHotPlanItem.TodoListBean> todoList = new ArrayList<>();
        int position = 0;
        for (int i = 1; i < 8; i++) {
            if (position < beanHotPlanItem.getTodoList().size()) {
                if (Integer.valueOf(beanHotPlanItem.getTodoList().get(position).getProgress()) == i) {
                    todoList.add(beanHotPlanItem.getTodoList().get(position));
                    position++;
                } else {
                    BeanHotPlanItem.TodoListBean todo = new BeanHotPlanItem.TodoListBean();
                    todo.setProgress("nothing");
                    todoList.add(todo);
                }
            } else {
                BeanHotPlanItem.TodoListBean todo = new BeanHotPlanItem.TodoListBean();
                todo.setProgress("nothing");
                todoList.add(todo);
            }
        }
        beanHotPlanItem.setTodoList(todoList);
        beanHealthPlanCommendContent.setMyHealthyPlanItemJsonStr(JSON.toJSONString(beanHotPlanItem));
        initUI(beanHotPlanItem);
    }

    private void initUI(BeanHotPlanItem beanHotPlanItem) {
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(lmg);
        PreviewHealthySchemeAdapter previewHealthySchemeAdapter = new PreviewHealthySchemeAdapter(this, beanHotPlanItem, week, calendarDate);
        recyclerview.setAdapter(previewHealthySchemeAdapter);
    }
}
    
            
