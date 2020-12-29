package com.sim.traveltool.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.callback.OnMultiClickListener;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.NewsAdapter;
import com.sim.traveltool.bean.NewsWangYiBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * @Auther Sim
 * @Time 2020/4/30 1:05
 * @Description 网易新闻的收藏页面
 */
public class NewsCollectActivity extends BaseActivity {

    ImageView back;
    LinearLayout parent;
    RecyclerView newsRecyclerView;

    private String fileName = "collect";
    private Map<String, NewsWangYiBean.ResultBean> newsMap;
    private ArrayList<NewsWangYiBean.ResultBean> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    private int position;//长按的item

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
    protected void initView() {
        newsAdapter = new NewsAdapter(this, newsList);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(NewsCollectActivity.this));
        newsAdapter.setOnItemClickListerer(new NewsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(NewsCollectActivity.this, NewsDetailActivity.class);
                intent.putExtra("news", (Serializable) newsAdapter.getNews().get(i));
                startActivity(intent);
            }
        });
        newsAdapter.setmOnItemLongClickListener(new NewsAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int i) {
                position = i;
                showDialog(null, "取消收藏", getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.callback.DialogInterface() {
                    @Override
                    public void sureOnClick() {
                        if (OnMultiClickListener.isNoFastClick()) {
                            SPUtil.remove(NewsCollectActivity.this, fileName, newsList.get(position).getTitle());
                            newsList.remove(position);
                            newsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void cancelOnClick() {
                    }
                });
            }
        });
        newsRecyclerView.setAdapter(newsAdapter);
    }

    @Override
    protected void initData() {
        Gson gson = new Gson();
        newsMap = (Map<String, NewsWangYiBean.ResultBean>) SPUtil.getAll(this, fileName);
        Iterator it = newsMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            newsList.add(gson.fromJson(String.valueOf(newsMap.get(key)), NewsWangYiBean.ResultBean.class));
        }
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
