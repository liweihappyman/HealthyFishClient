package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanHospitalListReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListResp;
import com.healthyfish.healthyfish.POJO.BeanHospitalListRespItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChooseHospitalAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
import com.healthyfish.healthyfish.utils.MyRecyclerViewOnItemListener;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：选择医院列表页面
 * 作者：WKJ on 2017/7/10.
 * 邮箱：
 * 编辑：WKJ
 */
public class ChooseHospital extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.choose_hospital_recyclerview)
    RecyclerView chooseHospitalRecyclerview;

    private ChooseHospitalAdapter adapter;
    private List<BeanHospitalListRespItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_hospital);
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "选择医院");
        initData(); //获取医院列表
        Listener(); //医院列表的点击监听
    }

    /**
     * 医院列表的点击监听
     */
    private void Listener() {
        chooseHospitalRecyclerview.addOnItemTouchListener(new MyRecyclerViewOnItemListener(this, chooseHospitalRecyclerview, new MyRecyclerViewOnItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) { //点击监听
                if (position < 2) {
                    Intent intent = new Intent(ChooseHospital.this, SelectDepartments.class);
                    intent.putExtra("BeanHospitalListRespItem", list.get(position)); //传递医院信息到下一个页面（选择科室页面）
                    startActivity(intent);
                } else {
                    Toast.makeText(ChooseHospital.this, "非常抱歉！该医院暂时没有科室开通网上挂号", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) { //长按监听

            }
        }));
    }

    /**
     * 获取医院列表
     */
    private void initData() {
        //请求医院列表用：BeanHospitalListReq
        //解析用：BeanHospitalListResp
        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(new BeanHospitalListReq()), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                testHospital();//测试数据，作填充列表用
                adapter.notifyDataSetChanged();//加载医院列表信息完成后通知适配器刷新数据
            }

            @Override
            public void onError(Throwable e) {
                Log.e("LYQ", e.toString());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String str = null;
                try {
                    str = responseBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("LYQ", "医院列表响应：" + str);
                BeanHospitalListResp beanHospitalListResp = JSON.parseObject(str, BeanHospitalListResp.class);
                for (BeanHospitalListRespItem beanHospitalListRespItem : beanHospitalListResp.getHospitalList()) {
                    list.add(beanHospitalListRespItem); //将医院信息添加到医院列表里用于展示
                }
            }
        });

        //初始化医院列表RecyclerView
        LinearLayoutManager lmg = new LinearLayoutManager(this);
        chooseHospitalRecyclerview.setLayoutManager(lmg); //设置RecyclerView为线性布局
        adapter = new ChooseHospitalAdapter(list, this);
        chooseHospitalRecyclerview.setAdapter(adapter); //给RecyclerView设置适配器
    }

    private void testHospital() {

        BeanHospitalListRespItem beanHospitalListRespItem1 = new BeanHospitalListRespItem();
        beanHospitalListRespItem1.setName("鹿寨县中医院");
        beanHospitalListRespItem1.setAddress("柳州市鹿寨镇建中北路17号");
        beanHospitalListRespItem1.setImg("/demo/downloadFile/upload/20171016/99711508161604765.png");
        list.add(beanHospitalListRespItem1);

        BeanHospitalListRespItem beanHospitalListRespItem2 = new BeanHospitalListRespItem();
        beanHospitalListRespItem2.setName("三江县中医院 ");
        beanHospitalListRespItem2.setAddress("柳州市三江侗族自治县古宜镇侗乡大道65号");
        beanHospitalListRespItem2.setImg("/demo/downloadFile/upload/20171016/63431508161970414.png");
        list.add(beanHospitalListRespItem2);

        BeanHospitalListRespItem beanHospitalListRespItem = new BeanHospitalListRespItem();
        beanHospitalListRespItem.setName("柳城县中医院");
        beanHospitalListRespItem.setAddress("柳州市柳城县大埔镇胜利东路20号");
        beanHospitalListRespItem.setImg("/demo/downloadFile/upload/20171016/11611508161519518.png");
        list.add(beanHospitalListRespItem);
    }

}
