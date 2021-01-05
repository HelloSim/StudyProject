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

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.NewsWangYiBean;
import com.sim.traveltool.db.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @Auther Sim
 * @Time 2020/4/28 1:05
 * @Description 显示网易新闻的页面
 */
public class NewsDetailActivity extends BaseActivity {

    private ImageView back;
    private WebView webView;
    private ImageView collect;

    private NewsWangYiBean.NewsBean news;//传进来的news
    private NewsWangYiBean.NewsBean collectionNewsBean;//收藏中的news
    private User user;

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
    protected void initData() {
        news = (NewsWangYiBean.NewsBean) getIntent().getSerializableExtra("news");
        if (BmobUser.isLogin()) {
            user = BmobUser.getCurrentUser(User.class);
            BmobQuery<NewsWangYiBean.NewsBean> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("user", user);
            bmobQuery.addWhereEqualTo("title", news.getTitle());
            bmobQuery.findObjects(new FindListener<NewsWangYiBean.NewsBean>() {
                @Override
                public void done(List<NewsWangYiBean.NewsBean> list, BmobException e) {
                    if (e == null && list != null && list.size() == 1) {
                        isCollect = false;
                        for (NewsWangYiBean.NewsBean bean : list) {
                            if (bean.getTitle().equals(news.getTitle())) {
                                collect.setImageResource(R.mipmap.ic_collect_yes);
                                collectionNewsBean = list.get(0);
                                isCollect = true;
                            }
                        }
                    } else {
                        collect.setImageResource(R.mipmap.ic_collect_not);
                        collectionNewsBean = null;
                        isCollect = false;
                    }
                }
            });
        } else {
            collect.setImageResource(R.mipmap.ic_collect_not);
            collectionNewsBean = null;
            isCollect = false;
            ToastUtil.T_Error(this, getString(R.string.login_no));
            finish();
        }
    }

    @Override
    protected void initView() {
        if (isCollect && collectionNewsBean != null) {
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
    public void onMultiClick(View view) {
        if (view == back) {
            finish();
        } else if (view == collect) {
            if (isCollect && collectionNewsBean != null) {
                NewsWangYiBean.NewsBean bean = new NewsWangYiBean.NewsBean();
                bean.setObjectId(collectionNewsBean.getObjectId());
                bean.delete(new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            collectionNewsBean = null;
                            collect.setImageResource(R.mipmap.ic_collect_not);
                            isCollect = false;
                        }
                    }

                });
            } else {
                NewsWangYiBean.NewsBean bean = new NewsWangYiBean.NewsBean();
                bean.setUser(user);
                bean.setTitle(news.getTitle());
                bean.setPath(news.getPath());
                bean.setImage(news.getImage());
                bean.setPasstime(news.getPasstime());
                bean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            collectionNewsBean = bean;
                            collect.setImageResource(R.mipmap.ic_collect_yes);
                            isCollect = true;
                        }
                    }
                });
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
