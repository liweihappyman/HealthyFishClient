package com.healthyfish.healthyfish.ui.activity.appointment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.healthyfish.healthyfish.POJO.BeanBaseKeysGetReq;
import com.healthyfish.healthyfish.POJO.BeanDoctAuthReq;
import com.healthyfish.healthyfish.POJO.BeanDoctorListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctInfoReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptDoctListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListReq;
import com.healthyfish.healthyfish.POJO.BeanHospitalListReq;
import com.healthyfish.healthyfish.POJO.BeanListReq;
import com.healthyfish.healthyfish.POJO.BeanSearchReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.ChooseHospitalAdapter;
import com.healthyfish.healthyfish.ui.activity.BaseActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_hospital);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"选择医院");
        initData();
    }

    private void initData() {
        List<String> list = new ArrayList<>();
//        BeanListReq beanListReq = new BeanListReq();
//        beanListReq.setPrefix("hpc_");
//        beanListReq.setFrom(0);
//        beanListReq.setTo(20);
//        beanListReq.setNum(21);
//
//        BeanSearchReq beanSearchReq = new BeanSearchReq();
//        beanSearchReq.setType("hosp");
//        beanSearchReq.setKeyword("中医院");
//
//        RetrofitManagerUtils.getInstance(this, null)
//                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanListReq), new Subscriber<ResponseBody>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.e("LYQ", "onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("LYQ", "onError");
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody responseBody) {
//                        Log.e("LYQ", "onNext");
//                        String jsonStr = null;
//                        try {
//                            jsonStr = responseBody.string();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        Log.e("LYQ", jsonStr);
//                    }
//                });
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
        list.add("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");

        LinearLayoutManager lmg = new LinearLayoutManager(this);
        chooseHospitalRecyclerview.setLayoutManager(lmg);
        ChooseHospitalAdapter adapter = new ChooseHospitalAdapter(list, this);
        chooseHospitalRecyclerview.setAdapter(adapter);


    }

}
