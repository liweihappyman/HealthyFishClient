package com.healthyfish.healthyfish.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.healthyfish.healthyfish.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：健康新闻详情页面
 * 作者：LYQ on 2017/7/18.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class HealthNews extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wv_health_news)
    WebView wvHealthNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_news);
        ButterKnife.bind(this);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initToolBar(toolbar, toolbarTitle, bundle.get("HEALTH_NEWS_TITLE").toString());
            WebSettings webSettings = wvHealthNews.getSettings();
            webSettings.setJavaScriptEnabled(true);//支持JavaScript
            webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
            webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
            webSettings.setUseWideViewPort(true); //将图片调整到适合WebView的大小
            webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
            wvHealthNews.loadUrl((String) bundle.get("HEALTH_NEWS_URL"));
            wvHealthNews.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    wvHealthNews.loadUrl((String) bundle.get("HEALTH_NEWS_URL"));//防止调用系统浏览器打开网页
                    return true;
                }
            });
        } else {
            initToolBar(toolbar, toolbarTitle, "健康资讯详情");
        }
    }


    /**
     * 销毁WebView
     */
    @Override
    protected void onDestroy() {
        if (wvHealthNews != null) {
            wvHealthNews.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            wvHealthNews.clearHistory();
            ((ViewGroup) wvHealthNews.getParent()).removeView(wvHealthNews);
            wvHealthNews.destroy();
            wvHealthNews = null;
        }
        super.onDestroy();
    }

}
