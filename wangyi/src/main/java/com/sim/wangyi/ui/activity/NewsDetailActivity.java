package com.sim.wangyi.ui.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sim.bean.User;
import com.sim.bean.WangyiBean;
import com.sim.common.base.BaseActivity;
import com.sim.common.utils.ToastUtil;
import com.sim.common.views.TitleView;
import com.sim.traveltool.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Sim --- 显示网易新闻的页面
 */
public class NewsDetailActivity extends BaseActivity {

    private TitleView titleView;
    private WebView webView;

    private WangyiBean.NewsBean news;//传进来的news
    private WangyiBean.NewsBean collectionNewsBean;//收藏中的news

    private boolean isCollect = false;//是否收藏

    @Override
    protected int getLayoutRes() {
        return R.layout.wangyi_activity_news_detail;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        webView = findViewById(R.id.web_view);
        titleView.setClickListener(new TitleView.ClickListener() {
            @Override
            public void left(View leftView) {
                finish();
            }

            @Override
            public void right(View right) {
                WangyiBean.NewsBean bean = new WangyiBean.NewsBean();
                if (BmobUser.isLogin()) {
                    if (isCollect && collectionNewsBean != null) {
                        bean.setObjectId(collectionNewsBean.getObjectId());
                        bean.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    collectionNewsBean = null;
                                    titleView.setRightImage(R.mipmap.common_ic_collect_not);
                                    isCollect = false;
                                }
                            }

                        });
                    } else {
                        bean.setUser(BmobUser.getCurrentUser(User.class));
                        bean.setTitle(news.getTitle());
                        bean.setPath(news.getPath());
                        bean.setImage(news.getImage());
                        bean.setPasstime(news.getPasstime());
                        bean.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    collectionNewsBean = bean;
                                    titleView.setRightImage(R.mipmap.common_ic_collect_yes);
                                    isCollect = true;
                                }
                            }
                        });
                    }
                } else {
                    titleView.setRightImage(R.mipmap.common_ic_collect_not);
                    collectionNewsBean = null;
                    isCollect = false;
                    ToastUtil.toast(NewsDetailActivity.this, "未登录！");
                }
            }
        });
    }

    @Override
    protected void initData() {
        news = (WangyiBean.NewsBean) getIntent().getSerializableExtra("news");
        if (BmobUser.isLogin()) {
            BmobQuery<WangyiBean.NewsBean> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));
            bmobQuery.addWhereEqualTo("title", news.getTitle());
            bmobQuery.findObjects(new FindListener<WangyiBean.NewsBean>() {
                @Override
                public void done(List<WangyiBean.NewsBean> list, BmobException e) {
                    if (e == null && list != null && list.size() == 1) {
                        isCollect = false;
                        for (WangyiBean.NewsBean bean : list) {
                            if (bean.getTitle().equals(news.getTitle())) {
                                titleView.setRightImage(R.mipmap.common_ic_collect_yes);
                                collectionNewsBean = list.get(0);
                                isCollect = true;
                            }
                        }
                    } else {
                        titleView.setRightImage(R.mipmap.common_ic_collect_not);
                        collectionNewsBean = null;
                        isCollect = false;
                    }
                }
            });
        } else {
            titleView.setRightImage(R.mipmap.common_ic_collect_not);
            collectionNewsBean = null;
            isCollect = false;
        }
    }

    @Override
    protected void initView() {
        if (isCollect && collectionNewsBean != null) {
            titleView.setRightImage(R.mipmap.common_ic_collect_yes);
        } else {
            titleView.setRightImage(R.mipmap.common_ic_collect_not);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
