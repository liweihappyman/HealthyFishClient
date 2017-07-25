package com.healthyfish.healthyfish.ui.activity.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanBaseKeysGetReq;
import com.healthyfish.healthyfish.POJO.BeanDoctAuthReq;
import com.healthyfish.healthyfish.POJO.BeanDoctorListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctInfoReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListRespItem;
import com.healthyfish.healthyfish.POJO.BeanHospRegNumListReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListResp;
import com.healthyfish.healthyfish.POJO.BeanHospitalListRespItem;
import com.healthyfish.healthyfish.POJO.BeanListReq;
import com.healthyfish.healthyfish.POJO.BeanSearchReq;
import com.healthyfish.healthyfish.POJO.BeanUserRetrPresReq;
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

import static com.healthyfish.healthyfish.constant.constants.HttpHealthyFishyUrl;

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
        initData();
        Listener();
    }

    private void Listener() {
        chooseHospitalRecyclerview.addOnItemTouchListener(new MyRecyclerViewOnItemListener(this, chooseHospitalRecyclerview, new MyRecyclerViewOnItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ChooseHospital.this, SelectDepartments.class);
                intent.putExtra("BeanHospitalListRespItem", list.get(position));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private void initData() {


        RetrofitManagerUtils.getInstance(this, null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(new BeanHospitalListReq()), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

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
                BeanHospitalListResp beanHospitalListResp = JSON.parseObject(str, BeanHospitalListResp.class);
                for (BeanHospitalListRespItem beanHospitalListRespItem : beanHospitalListResp.getHospitalList()) {
                    list.add(beanHospitalListRespItem);
                }
                adapter.notifyDataSetChanged();
            }
        });

        LinearLayoutManager lmg = new LinearLayoutManager(this);
        chooseHospitalRecyclerview.setLayoutManager(lmg);
        adapter = new ChooseHospitalAdapter(list, this);
        chooseHospitalRecyclerview.setAdapter(adapter);
    }

}
