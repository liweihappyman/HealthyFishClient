package com.healthyfish.healthyfish.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.MyApplication;
import com.healthyfish.healthyfish.POJO.BeanBaseKeysGetReq;
import com.healthyfish.healthyfish.POJO.BeanBaseKeysGetResp;
import com.healthyfish.healthyfish.POJO.BeanHealthPlanCommendContent;
import com.healthyfish.healthyfish.POJO.BeanHotPlan;
import com.healthyfish.healthyfish.POJO.BeanHotPlanItem;
import com.healthyfish.healthyfish.POJO.BeanListReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.HealthPlanHotAdapter;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;
import com.healthyfish.healthyfish.utils.Utils1;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthyPlanHot extends Fragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;
    LinearLayoutManager lmg;
    private List<BeanHotPlanItem> mList;
    public HealthyPlanHot() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_healthy_plan_hot, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        //initUI();
        requestForRecommendPlan();
        //init();
        return rootView;
    }


    private void requestForRecommendPlanByKeyGet(final List<String> keys) {
        BeanBaseKeysGetReq beanBaseKeysGet = new BeanBaseKeysGetReq();
        beanBaseKeysGet.setKeyList(keys);
        RetrofitManagerUtils.getInstance(getActivity(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanBaseKeysGet), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {
                initUI();
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
//                            BeanHealthPlanCommendContent beanHealthPlanCommendContent = new BeanHealthPlanCommendContent();
//                            beanHealthPlanCommendContent.setHotPlanListJsonStr(JSON.toJSONString(keysGet.getValueList()));
//                            beanHealthPlanCommendContent.setCalendarDateJsonStr("还没有计划");
//                            beanHealthPlanCommendContent.save();
                            mList = new ArrayList<BeanHotPlanItem>();
                            for (String hotPlanItemString : keysGet.getValueList()) {
                                BeanHotPlanItem hotPlanItem = JSON.parseObject(hotPlanItemString, BeanHotPlanItem.class);
                                mList.add(hotPlanItem);
//                                hotPlanItem.setDescription(JSON.toJSONString(hotPlanItem.getTodoList()));
//                                hotPlanItem.save();
//                                Log.i("healthPlan","keysGetItem" +hotPlanItem.getTitle());
//                                for (BeanHotPlanItem.TodoListBean todoList:hotPlanItem.getTodoList()){
//                                    Log.i("healthPlan","keysGetItemTodo" +todoList.getTodo());
//                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void initUI() {
//        BeanHealthPlanCommendContent beanHealthPlanCommendContent = DataSupport.findLast(BeanHealthPlanCommendContent.class);
//        mList = new ArrayList<BeanHotPlanItem>();
//        List<String> listStr = JSON.parseObject(beanHealthPlanCommendContent.getHotPlanListJsonStr(), List.class);
//        for (String hotPlanItemString : listStr) {
//            Log.i("hotPlanItemString",hotPlanItemString);
//            BeanHotPlanItem hotPlanItem = JSON.parseObject(hotPlanItemString, BeanHotPlanItem.class);
//            mList.add(hotPlanItem);
//        }
        lmg = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(lmg);
        HealthPlanHotAdapter adapter = new HealthPlanHotAdapter(getActivity(), mList);
        recyclerview.setAdapter(adapter);
    }

    private void requestForRecommendPlan() {
        final List<String> keys = new ArrayList<>();
        BeanListReq beanListReq = new BeanListReq();
        beanListReq.setPrefix("hpc_");
        beanListReq.setFrom(0);
        beanListReq.setTo(-1);
        beanListReq.setNum(-1);

        RetrofitManagerUtils.getInstance(getActivity(), null).getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanListReq), new Subscriber<ResponseBody>() {
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
                        Log.i("healthPlan", hotPlan.getUrl().substring(13));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
