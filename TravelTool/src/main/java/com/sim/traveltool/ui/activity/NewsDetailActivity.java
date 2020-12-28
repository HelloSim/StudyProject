package com.sim.traveltool.ui.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.NewsWangYiBean;

/**
 * @Auther Sim
 * @Time 2020/4/28 1:05
 * @Description 显示网易新闻的页面
 */
public class NewsDetailActivity extends BaseActivity {

    ImageView back;
    WebView webView;
    ImageView collect;

    private NewsWangYiBean.ResultBean news;
    private String fileName = "collect";
    private boolean isCollect = false;//是否收藏

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        back = findViewById(R.id.back);
        webView = findViewById(R.id.web_view);
        collect = findViewById(R.id.collect);
        setViewClick(back, collect);
    }

    @Override
    protected void initView() {
        if (isCollect) {
            collect.setImageResource(R.mipmap.ic_collect_yes);
        } else {
            collect.setImageResource(R.mipmap.ic_collect_not);
        }
        if (news != null) {
            //启用支持javascript
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            //优先使用缓存
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });
            WebViewClient mWebviewclient = new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    // TODO Auto-generated method stub
                    super.onReceivedError(view, errorCode, description, failingUrl);
                }
            };
            webView.setWebViewClient(mWebviewclient);
            webView.loadUrl(news.getPath());
        }
    }

    @Override
    protected void initData() {
        news = (NewsWangYiBean.ResultBean) getIntent().getSerializableExtra("news");
        isCollect = SPUtil.contains(this, fileName, news.getTitle());
    }

    @Override
    public void onMultiClick(View view) {
        if (view == back) {
            finish();
        } else if (view == collect) {
            if (isCollect) {
                SPUtil.remove(NewsDetailActivity.this, fileName, news.getTitle());
                collect.setImageResource(R.mipmap.ic_collect_not);
                isCollect = false;
            } else {
                SPUtil.put(NewsDetailActivity.this, fileName, news.getTitle(), new Gson().toJson(news));
                collect.setImageResource(R.mipmap.ic_collect_yes);
                isCollect = true;
            }
        } else {
            super.onMultiClick(view);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
