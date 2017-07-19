package com.healthyfish.healthyfish.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.healthyfish.healthyfish.POJO.BeanSearchReq;
import com.healthyfish.healthyfish.POJO.BeanSearchResp;
import com.healthyfish.healthyfish.POJO.BeanSearchRespItem;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.utils.OkHttpUtils;
import com.healthyfish.healthyfish.utils.RetrofitManagerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * 描述：显示搜索结果页面
 * 作者：LYQ on 2017/7/19.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class SearchResult extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_search_result)
    ListView lvSearchResult;

    private String searchKey = null;
    private List<Map<String,String>> searchRespList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        initToolBar(toolbar,toolbarTitle,"搜索结果");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            searchKey = bundle.get("SEARCH_KEY").toString();
        }
        initSearchResult(searchKey);
    }

    /**
     * 搜索
     */
    private void initSearchResult(String searchKey) {
        BeanSearchReq beanSearchReq = new BeanSearchReq();
        //beanSearchReq.setType("hosp");
        beanSearchReq.setKeyword(searchKey);

        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanSearchReq), new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

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
                        //Log.e("LYQ", jsonStr);
                        BeanSearchResp searchResp = JSON.parseObject(jsonStr, BeanSearchResp.class);
                        for (BeanSearchRespItem searchRespItem : searchResp.getResultList()) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("title", searchRespItem.getTitle());
                            map.put("content", searchRespItem.getValue());
                            searchRespList.add(map);
                        }

                    }
                });

        String[] from = new String[]{"title", "content"};
        int[] to = new int[]{R.id.tv_title,R.id.tv_content};

        SimpleAdapter adapter = new SimpleAdapter(this, searchRespList, R.layout.item_search_result_lv, from, to);
        lvSearchResult.setAdapter(adapter);
    }
}

