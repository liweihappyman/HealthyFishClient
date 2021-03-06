package com.healthyfish.healthyfish.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.eventbus.NoticeMessage;
import com.healthyfish.healthyfish.ui.activity.healthy_management.SinglePlanDetail;
import com.healthyfish.healthyfish.utils.Utils1;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class HealthPlanItemDetailFragment extends Fragment {
    BeanHotPlanItem.TodoListBean todo;
    int position;//记录页面的位置
    int id;//该计划所在的数据表的位置
    BeanHealthPlanCommendContent beanHealthPlanCommendContent;
    BeanHotPlanItem beanHotPlanItem;
    List<String> calendarDate = new ArrayList<>();//字符串的日期2017年10月20日
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.btn_go_to_single_scheme1)
    Button btnGoToSingleScheme1;
    Unbinder unbinder;

    public HealthPlanItemDetailFragment(BeanHotPlanItem.TodoListBean todo, int id, int position, List<String> calendarDate) {//todo没用到了
        this.todo = todo;
        this.position = position;
        this.id = id;
        this.calendarDate = calendarDate;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_plan_item_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        initUI();
        return view;
    }

    private void initUI() {
        //一层层解析出来，获取具体的数据
        beanHealthPlanCommendContent = DataSupport.find(BeanHealthPlanCommendContent.class,id);
        beanHotPlanItem = JSON.parseObject(beanHealthPlanCommendContent.getMyHealthyPlanItemJsonStr(), BeanHotPlanItem.class);
        todo = beanHotPlanItem.getTodoList().get(position);
        if (!todo.getProgress().equals("nothing")) {
            type.setText(todo.getTodo());
            if (todo.isDone()) {
                btnGoToSingleScheme1.setText("已完成");
                btnGoToSingleScheme1.setTextColor(0xffcfcfcf);
                btnGoToSingleScheme1.setBackgroundResource(R.drawable.gray_rounded_rectangle);
            }else {
                //int i = Utils1.compare_date(calendarDate.get(position),"2017年8月8日") ;//测试用
                int i = Utils1.compare_date(calendarDate.get(position), Utils1.getTime());
                //Log.i("比较大小",""+i);
                switch (i) {
                    case 1://该位置的日期比当日期大（将来的日期）
                        break;
                    case -1://该位置的日期比当日期小（过期的日期）
                        btnGoToSingleScheme1.setText("未完成");
                        btnGoToSingleScheme1.setTextColor(0xffcfcfcf);
                        btnGoToSingleScheme1.setBackgroundResource(R.drawable.gray_rounded_rectangle);
                        break;
                    case 0://该位置的日期与当日期一样（即今天的日期）
                        btnGoToSingleScheme1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //refreshUiAndSave();
                                Intent intent = new Intent(MyApplication.getContetxt(), SinglePlanDetail.class);
                                startActivityForResult(intent,110);
                            }
                        });
                        break;
                }
            }
        } else {
            type.setText("没有计划哟！");
            btnGoToSingleScheme1.setVisibility(View.GONE);
        }
    }

    private void refreshUiAndSave() {
        btnGoToSingleScheme1.setText("已完成");
        btnGoToSingleScheme1.setTextColor(0xffcfcfcf);
        btnGoToSingleScheme1.setBackgroundResource(R.drawable.gray_rounded_rectangle);
        //一层层包装回去，最后保存
        todo.setDone(true);
        List<BeanHotPlanItem.TodoListBean> tempTodoList = new ArrayList<BeanHotPlanItem.TodoListBean>();
        for (int i = 0; i < beanHotPlanItem.getTodoList().size(); i++) {
            if (i == position) {
                tempTodoList.add(todo);
            } else {
                tempTodoList.add(beanHotPlanItem.getTodoList().get(i));
            }
        }
        beanHotPlanItem.setTodoList(tempTodoList);
        beanHealthPlanCommendContent.setMyHealthyPlanItemJsonStr(JSON.toJSONString(beanHotPlanItem));
        beanHealthPlanCommendContent.save();
        EventBus.getDefault().post(new NoticeMessage(1));//通知更新MyHealthyScheme的进度
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==110){
            refreshUiAndSave();
        }
    }

}
