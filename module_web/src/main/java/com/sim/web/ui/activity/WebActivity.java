package com.sim.web.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.sim.basicres.base.BaseActivity;
import com.sim.web.R;
import com.sim.web.ui.view.WebDialogFg;
import com.sim.web.ui.view.WebLayout;

public class WebActivity extends BaseActivity {

    private TextView tvTitle;
    private LinearLayout parent;
    private RelativeLayout rlMore;
    private RelativeLayout rlClose;

    private String title;
    private String webUrl;

    @Override
    protected int getLayoutRes() {
        return R.layout.web_activity;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        webUrl = getIntent().getStringExtra("webUrl");
        title = getIntent().getStringExtra("title");

        tvTitle = findViewById(R.id.tvTitle);
        if (!title.isEmpty()) {
            tvTitle.setText(title);
        }

        parent = findViewById(R.id.parent);
        rlMore = findViewById(R.id.rlMore);
        rlClose = findViewById(R.id.rlClose);
        setViewClick(rlMore, rlClose);
    }

    @Override
    protected void initView() {
        AgentWeb.with(this)
                .setAgentWebParent(parent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setWebLayout(new WebLayout(this))
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(getUrl());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == rlMore) {
            WebDialogFg.newInstance(webUrl).show(getSupportFragmentManager(), "webDialog");
        } else if (view == rlClose) {
            finish();
        } else {
            super.onMultiClick(view);
        }
    }

    private String getUrl() {
        return webUrl;
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }
    };

}