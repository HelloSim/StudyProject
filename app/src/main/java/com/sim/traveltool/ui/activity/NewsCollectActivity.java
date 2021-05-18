package com.sim.traveltool.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.base.BaseAdapter;
import com.sim.baselibrary.base.BaseViewHolder;
import com.sim.baselibrary.callback.DialogInterface;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.NewsAdapter;
import com.sim.traveltool.bean.NewsWangYiBean;
import com.sim.traveltool.bean.db.User;
import com.sim.traveltool.ui.view.TitleView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Sim --- 网易新闻的收藏页面
 */
public class NewsCollectActivity extends BaseActivity {

    private TitleView titleView;
    private RecyclerView newsRecyclerView;

    private ArrayList<NewsWangYiBean.NewsBean> collectionNewsBeanArrayList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_news_collect;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        newsRecyclerView = findViewById(R.id.recycle_view);
        titleView.setLeftClickListener(new TitleView.LeftClickListener() {
            @Override
            public void onClick(View leftView) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        if (BmobUser.isLogin()) {
            BmobQuery<NewsWangYiBean.NewsBean> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));
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
            finish();
        }
    }

    @Override
    protected void initView() {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(NewsCollectActivity.this));
        newsAdapter = new NewsAdapter(this, collectionNewsBeanArrayList);
        newsAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(BaseViewHolder holder, int position) {
                Intent intent = new Intent(NewsCollectActivity.this, NewsDetailActivity.class);
                intent.putExtra("news", (Serializable) newsAdapter.getData().get(position));
                startActivity(intent);
            }
        });
        newsAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(BaseViewHolder holder, int position) {
                showDialog(null, "取消收藏", "确认", "取消", new DialogInterface() {
                    @Override
                    public void sureOnClick() {
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

                    @Override
                    public void cancelOnClick() {
                    }
                });
            }
        });
        newsRecyclerView.setAdapter(newsAdapter);
    }
}
