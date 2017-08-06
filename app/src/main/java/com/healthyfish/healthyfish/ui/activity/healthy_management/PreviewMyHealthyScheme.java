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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.POJO.BeanBaseKeysGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeysGetResp;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.POJO.BeanHotPlan;
import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.POJO.BeanListReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.PreviewHealthySchemeAdapter;
import com.healthyfish.healthyfish.eventbus.NoticeMessage;
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

public class PreviewMyHealthyScheme extends AppCompatActivity {
    BeanHealthPlanCommendContent beanHealthPlanCommendContent;
    List<String> HotPlanListStr;
    BeanHotPlanItem beanHotPlanItem;
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
        if (beanHealthPlanCommendContent!=null) {
            beanHealthPlanCommendContent.save();
            EventBus.getDefault().post(new NoticeMessage(1));
            Intent intent = new Intent(PreviewMyHealthyScheme.this, MainIndexHealthyManagement.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this,"还没有加载计划哟",Toast.LENGTH_SHORT).show();
        }
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
        if (DataSupport.findLast(BeanHealthPlanCommendContent.class) == null) {
            requestForRecommendPlan();
        }
    }

    /**
     * 通过请求回来的key获取具体的计划
     *
     * @param keys
     */
    private void requestForRecommendPlanByKeyGet(final List<String> keys) {
        BeanBaseKeysGetReq beanBaseKeysGet = new BeanBaseKeysGetReq();
        beanBaseKeysGet.setKeyList(keys);
        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeysGet), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                //initUI();
                createPlan();
                initTodoData();
            }

            @Override
            public void onError(Throwable e) {
                Log.i("healthPlan", "keysGetItem" + "出错");
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String str = responseBody.string();
                    Log.i("healthPlan", "keyGet" + str);
                    if (!TextUtils.isEmpty(str)) {
                        BeanBaseKeysGetResp keysGet = JSON.parseObject(str, BeanBaseKeysGetResp.class);
                        //Log.i("healthPlan","keysGetlist" +keysGet.getValueList());
                        if (keysGet.getValueList().size() > 0) {
                            beanHealthPlanCommendContent = new BeanHealthPlanCommendContent();
                            beanHealthPlanCommendContent.setHotPlanListJsonStr(JSON.toJSONString(keysGet.getValueList()));
                            beanHealthPlanCommendContent.setCalendarDateJsonStr("还没有计划");
//                            mList = new ArrayList<BeanHotPlanItem>();
//                            for (String hotPlanItemString : keysGet.getValueList()) {
//                                BeanHotPlanItem hotPlanItem = JSON.parseObject(hotPlanItemString, BeanHotPlanItem.class);
//                                mList.add(hotPlanItem);
//                                hotPlanItem.setDescription(JSON.toJSONString(hotPlanItem.getTodoList()));
//                                hotPlanItem.save();
//                                Log.i("healthPlan","keysGetItem" +hotPlanItem.getTitle());
//                                for (BeanHotPlanItem.TodoListBean todoList:hotPlanItem.getTodoList()){
//                                    Log.i("healthPlan","keysGetItemTodo" +todoList.getTodo());
//                                }
//                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 网络请求推荐的计划key
     */
    private void requestForRecommendPlan() {
        final List<String> keys = new ArrayList<>();
        BeanListReq beanListReq = new BeanListReq();
        beanListReq.setPrefix("hpc_");
        beanListReq.setFrom(0);
        beanListReq.setTo(-1);
        beanListReq.setNum(-1);

        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanListReq), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                requestForRecommendPlanByKeyGet(keys);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String str = responseBody.string();
                    Log.i("healthPlan", "" + str);
                    List<String> listBeanString = JSONArray.parseObject(str, List.class);
                    for (String beanString : listBeanString) {
                        BeanHotPlan hotPlan = JSON.parseObject(beanString, BeanHotPlan.class);
                        keys.add(hotPlan.getUrl().substring(13));
                        //Log.i("healthPlan", hotPlan.getUrl().substring(13));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 该方法只有计划创建的时候调用，生成星期（一、二）和号数（7,8），日期（2017年8月20日）
     */
    private void createPlan() {
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
        HotPlanListStr = JSON.parseObject(beanHealthPlanCommendContent.getHotPlanListJsonStr(), List.class);
        List<BeanHotPlanItem.TodoListBean> todoList = new ArrayList<>();
        List<String> rusult = new ArrayList<>();
        for (int k = 0; k < HotPlanListStr.size(); k++) {
            todoList.clear();
            beanHotPlanItem = JSON.parseObject(HotPlanListStr.get(k), BeanHotPlanItem.class);
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
            rusult.add(JSON.toJSONString(beanHotPlanItem));//保证顺序

        }
        beanHealthPlanCommendContent.setHotPlanListJsonStr(JSON.toJSONString(rusult));
        Log.i("todosize",JSON.toJSONString(rusult));
        //beanHealthPlanCommendContent.save();
        initUI(rusult);
    }

    private void initUI(List<String> rusult) {
        List<BeanHotPlanItem>  hotPlanItemList = new ArrayList<>();
        for (int i = 0 ; i< rusult.size();i++) {
            BeanHotPlanItem beanHotPlanItem = JSON.parseObject(rusult.get(i), BeanHotPlanItem.class);
            hotPlanItemList.add(beanHotPlanItem);
        }
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(lmg);
        PreviewHealthySchemeAdapter previewHealthySchemeAdapter = new PreviewHealthySchemeAdapter(this, hotPlanItemList, week, calendarDate);
        recyclerview.setAdapter(previewHealthySchemeAdapter);
    }
}
    
            
