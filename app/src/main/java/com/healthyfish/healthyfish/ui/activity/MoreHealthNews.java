package com.healthyfish.healthyfish.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.healthyfish.healthyfish.POJO.BeanItemNewsAbstract;
import com.healthyfish.healthyfish.POJO.BeanListReq;
import com.healthyfish.healthyfish.R;
import com.healthyfish.healthyfish.adapter.HomePageHealthInfoAadpter;
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
 * 描述：更多健康咨询页面
 * 作者：LYQ on 2017/7/19.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class MoreHealthNews extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ryv_more_health_news)
    RecyclerView ryvMoreHealthNews;

    private Context mContext;
    private HomePageHealthInfoAadpter healthInfoAdapter;
    final List<BeanItemNewsAbstract> newsList = new ArrayList<>();
    private TextView footTextView;
    private int to = 15;//起始加载的资讯条数
    private boolean isNotMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_health_news);
        mContext = this;
        ButterKnife.bind(this);
        initToolBar(toolbar, toolbarTitle, "健康资讯");
        initListView();
    }

    private void initListView() {
        createRequest(0, to, false);
        LinearLayoutManager lm = new LinearLayoutManager(MoreHealthNews.this);
        ryvMoreHealthNews.setLayoutManager(lm);
        healthInfoAdapter = new HomePageHealthInfoAadpter(mContext, newsList);
        ryvMoreHealthNews.setAdapter(healthInfoAdapter);

        final View footView = LayoutInflater.from(this).inflate(R.layout.layout_load_more, ryvMoreHealthNews, false);
        footTextView = (TextView) footView.findViewById(R.id.tv_load_more);
        healthInfoAdapter.addFootView(footView);

        //Item的点击监听
        ryvMoreHealthNews.addOnItemTouchListener(new MyRecyclerViewOnItemListener(this, ryvMoreHealthNews,
                new MyRecyclerViewOnItemListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position <= newsList.size() - 1) {
                            Intent intent = new Intent(mContext, HealthNews.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("HEALTH_NEWS_URL", newsList.get(position).getUrl());
                            bundle.putString("HEALTH_NEWS_TITLE", newsList.get(position).getTitle());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (position == newsList.size()) {
                            if (!footTextView.getText().toString().equals("到底啦！")) {
                                footTextView.setText("加载中...");
                                createRequest(to + 1, to + 10, true);
                                healthInfoAdapter.notifyDataSetChanged();
                                to = to + 10;
                            }
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
    }

    private void createRequest(int from, int to, final boolean isLoad) {
        BeanListReq beanListReq = new BeanListReq();
        beanListReq.setPrefix("news_");
        beanListReq.setFrom(from);
        beanListReq.setTo(to);
        beanListReq.setNum(to - from + 1);
        RetrofitManagerUtils.getInstance(this, null)
                .getHealthyInfoByRetrofit(OkHttpUtils.getRequestBody(beanListReq),
                        new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {
                                if (isLoad && !isNotMore) {
                                    footTextView.setText("加载更多");
                                } else if (isNotMore) {
                                    footTextView.setText("到底啦！");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                if (responseBody != null) {
                                    String jsonNews = null;
                                    try {
                                        jsonNews = responseBody.string();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    List<String> strJsonNewsList = JSONArray.parseObject(jsonNews, List.class);
                                    for (String strJsonNews : strJsonNewsList) {
                                        BeanItemNewsAbstract bean = JSON.parseObject(strJsonNews, BeanItemNewsAbstract.class);
                                        newsList.add(bean);
                                    }
                                    healthInfoAdapter.notifyDataSetChanged();//由于加载需要时间，故加载完成重新刷新适配器，防止FootView位置出错
                                    if (strJsonNewsList.size() < 9) {
                                        isNotMore = true;
                                    }
                                }
                            }
                        });
    }

}
