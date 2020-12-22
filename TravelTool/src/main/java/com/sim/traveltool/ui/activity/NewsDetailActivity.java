package com.sim.traveltool.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.NewsWangYiBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Auther Sim
 * @Time 2020/4/28 1:05
 * @Description 显示网易新闻的页面
 */
public class NewsDetailActivity extends BaseActivity {
    private static final String TAG = "Sim_NewsDetailActivity";

    private Context context;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.collect)
    ImageView collect;

    private NewsWangYiBean.ResultBean news;
    private String fileName = "collect";
    private boolean isCollect = false;//是否收藏

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        context = this;
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("CommitPrefEdits")
    private void initData() {
        news = (NewsWangYiBean.ResultBean) getIntent().getSerializableExtra("news");
        isCollect = SPUtil.contains(context, fileName, news.getTitle());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
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

    @OnClick({R.id.back, R.id.collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.collect:
                if (isCollect) {
                    SPUtil.remove(context, fileName, news.getTitle());
                    collect.setImageResource(R.mipmap.ic_collect_not);
                    isCollect = false;
                } else {
                    SPUtil.put(context, fileName, news.getTitle(), new Gson().toJson(news));
                    collect.setImageResource(R.mipmap.ic_collect_yes);
                    isCollect = true;
                }
                break;
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
