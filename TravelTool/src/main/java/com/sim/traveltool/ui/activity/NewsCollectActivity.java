package com.sim.traveltool.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.callback.ItemClickSupport;
import com.sim.baselibrary.callback.OnMultiClickListener;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.NewsAdapter;
import com.sim.traveltool.bean.NewsWangYiBean;
import com.sim.traveltool.db.bean.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @Auther Sim
 * @Time 2020/4/30 1:05
 * @Description 网易新闻的收藏页面
 */
public class NewsCollectActivity extends BaseActivity {

    private ImageView back;
    private LinearLayout parent;
    private RecyclerView newsRecyclerView;

    private User user;
    private ArrayList<NewsWangYiBean.NewsBean> collectionNewsBeanArrayList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_news_collect;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        back = findViewById(R.id.back);
        parent = findViewById(R.id.parent);
        newsRecyclerView = findViewById(R.id.recycle_view);
        setViewClick(back);
    }

    @Override
    protected void initData() {
        if (BmobUser.isLogin()) {
            user = BmobUser.getCurrentUser(User.class);
            BmobQuery<NewsWangYiBean.NewsBean> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("user", user);
            bmobQuery.findObjects(new FindListener<NewsWangYiBean.NewsBean>() {
                @Override
                public void done(List<NewsWangYiBean.NewsBean> list, BmobException e) {
                    if (e == null && list != null && list.size() > 0) {
                        for (NewsWangYiBean.NewsBean bean : list) {
                            collectionNewsBeanArrayList.add(bean);
                            newsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        } else {
            ToastUtil.T_Error(this, getString(R.string.login_no));
            finish();
        }
    }

    @Override
    protected void initView() {
        newsAdapter = new NewsAdapter(this, collectionNewsBeanArrayList);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(NewsCollectActivity.this));
        newsRecyclerView.setAdapter(newsAdapter);
        ItemClickSupport.addTo(newsRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(NewsCollectActivity.this, NewsDetailActivity.class);
                intent.putExtra("news", (Serializable) newsAdapter.getData().get(position));
                startActivity(intent);
            }
        });
        ItemClickSupport.addTo(newsRecyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                showDialog(null, "取消收藏", getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.callback.DialogInterface() {
                    @Override
                    public void sureOnClick() {
                        if (OnMultiClickListener.isNoFastClick()) {
                            NewsWangYiBean.NewsBean bean = new NewsWangYiBean.NewsBean();
                            bean.setObjectId(collectionNewsBeanArrayList.get(position).getObjectId());
                            bean.delete(new UpdateListener() {

                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        collectionNewsBeanArrayList.remove(position);
                                        newsAdapter.notifyDataSetChanged();
                                    }
                                }

                            });
                        }
                    }

                    @Override
                    public void cancelOnClick() {
                    }
                });
                return false;
            }
        });
    }

    @Override
    public void onMultiClick(View view) {
        if (view == back) {
            finish();
        } else {
            super.onMultiClick(view);
        }
    }

}
