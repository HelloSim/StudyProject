package com.sim.web.ui.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.just.agentweb.IWebLayout;
import com.sim.web.R;

public class WebLayout implements IWebLayout {

    private WebView mWebView = null;

    public WebLayout(Activity activity) {
        mWebView = (WebView) LayoutInflater.from(activity).inflate(R.layout.web_layout, null);
    }

    @NonNull
    @Override
    public ViewGroup getLayout() {
        return mWebView;
    }

    @Nullable
    @Override
    public WebView getWebView() {
        return mWebView;
    }

}