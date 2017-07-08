package com.healthyfish.healthyfish.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.healthyfish.healthyfish.POJO.BeanMyConcernDoctorItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.MyConcernRvAdapter;
import com.healthyfish.healthyfish.listener.InterrogationRvlistener;
import com.healthyfish.healthyfish.utils.DividerGridItemDecoration;
import com.healthyfish.healthyfish.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：个人中心我的关注页面
 * 作者：LYQ on 2017/7/7.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class MyConcern extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_my_concern)
    ListView lvMyConcern;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_concern);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "我的关注");
        initRecyclerView();
    }

    /**
     * 初始化ListView
     */
    private void initRecyclerView() {
        final List<BeanMyConcernDoctorItem> mData = new ArrayList<>();
        for (int i=0; i<10; i++){
            mData.add(new BeanMyConcernDoctorItem("非凡"+i,"中医科","主任医师","http://wmtp.net/wp-content/uploads/2017/02/0227_weimei01_1.jpeg","柳州市中医院","擅长：对消化不良的治疗有独特见解"));
        }
        MyConcernRvAdapter adapter = new MyConcernRvAdapter(this,mData);
        adapter.setMode(Attributes.Mode.Single);//只有一个拖拽打开的时候，其他的关闭
        lvMyConcern.setAdapter(adapter);
        lvMyConcern.setVerticalScrollBarEnabled(false);
    }
}
