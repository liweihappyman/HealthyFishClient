package com.healthyfish.healthyfish.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListReq;
import com.healthyfish.healthyfish.POJO.BeanHospDeptListRespItem;
import com.healthyfish.healthyfish.ui.activity.SearchResult;
import com.healthyfish.healthyfish.utils.DividerGridItemDecoration;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.InterrogationRvAdapter;
import com.healthyfish.healthyfish.utils.MyRecyclerViewOnItemListener;
import com.healthyfish.healthyfish.ui.activity.interrogation.ChoiceDoctor;
import com.healthyfish.healthyfish.utils.MyToast;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterrogationFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_choice_department)
    RecyclerView rvChoiceDepartment;
    Unbinder unbinder;

    private Context mContext;
    View rootView;

    private InterrogationRvAdapter mRvAdapter;
    private List<BeanHospDeptListRespItem> DeptList = new ArrayList<>();

    public InterrogationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_interrogation, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initRecycleView();
        rvListener();
        searchListener();
        return rootView;
    }

    /**
     * 搜索功能
     */
    private void searchListener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(getActivity(), SearchResult.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SEARCH_KEY", etSearch.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    /**
     * RecyclerView的监听
     */
    private void rvListener() {
        rvChoiceDepartment.addOnItemTouchListener(new MyRecyclerViewOnItemListener(mContext, rvChoiceDepartment, new MyRecyclerViewOnItemListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到该科室的医生列表，需要发送科室信息到后台获取科室医生列表信息，传入下一个页面
                Intent intent = new Intent(mContext, ChoiceDoctor.class);
                Bundle bundle = new Bundle();
                bundle.putString("DepartmentName", DeptList.get(position).getDEPT_NAME());
                bundle.putString("DepartmentCode", DeptList.get(position).getDEPT_CODE());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //MyToast.showToast(mContext,"长按"+String.valueOf(position));
            }
        }));


    }

    /**
     * 初始化科室数据
     */
    private void initRecycleView() {
        final List<String> mDepartments = new ArrayList<>();
        final List<Integer> mDepartmentIcons = new ArrayList<>();
        final int[] icons = new int[]{R.mipmap.ic_chinese_medicine};

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        rvChoiceDepartment.setLayoutManager(gridLayoutManager);
        mRvAdapter = new InterrogationRvAdapter(mContext, mDepartments, mDepartmentIcons);
        rvChoiceDepartment.setAdapter(mRvAdapter);
        rvChoiceDepartment.addItemDecoration(new DividerGridItemDecoration(mContext));

        BeanHospDeptListReq beanHospDeptListReq = new BeanHospDeptListReq();
        beanHospDeptListReq.setHosp("lzzyy");

        RetrofitManagerUtils.getInstance(getActivity(), null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanHospDeptListReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        mRvAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String jsonStr = null;
                        try {
                            jsonStr = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        List<JSONObject> beanHospDeptListResp = JSONArray.parseObject(jsonStr, List.class);
                        for (JSONObject object : beanHospDeptListResp) {
                            String jsonString = object.toJSONString();
                            BeanHospDeptListRespItem beanHospDeptListRespItem = JSON.parseObject(jsonString, BeanHospDeptListRespItem.class);
                            DeptList.add(beanHospDeptListRespItem);
                            mDepartments.add(beanHospDeptListRespItem.getDEPT_NAME());
                            mDepartmentIcons.add(icons[0]);
                        }
                        mRvAdapter.notifyDataSetChanged();
                    }
                });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
