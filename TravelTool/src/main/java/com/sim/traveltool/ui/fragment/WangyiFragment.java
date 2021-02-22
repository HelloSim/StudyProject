package com.sim.traveltool.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sim.baselibrary.base.BaseAdapter;
import com.sim.baselibrary.base.BaseFragment;
import com.sim.baselibrary.base.BaseViewHolder;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.NewsAdapter;
import com.sim.traveltool.bean.NewsWangYiBean;
import com.sim.traveltool.internet.APIFactory;
import com.sim.traveltool.ui.activity.NewsDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;

import rx.Subscriber;

/**
 * @author Sim --- “网易”Fragment
 */
public class WangyiFragment extends BaseFragment {

    private RecyclerView newsRecyclerView;
    private ArrayList<NewsWangYiBean.NewsBean> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    private SwipeRefreshLayout refreshLayout;

    private int page = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_wangyi;
    }

    @Override
    protected void bindViews(View view) {

    }

    @Override
    protected void initView(View view) {
        initRecyclerView(view);
    }

    @Override
    protected void initData() {
        getWangYiNew();
    }

    //初始化RecyclerView
    private void initRecyclerView(View root) {
        newsRecyclerView = root.findViewById(R.id.recycle_view);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsAdapter = new NewsAdapter(getActivity(), newsList);
        newsRecyclerView.setFocusable(true);
        newsAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(BaseViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("news", (Serializable) newsAdapter.getData().get(position));
                startActivity(intent);
            }
        });
        newsRecyclerView.setAdapter(newsAdapter);

        //freshLayout控件下拉刷新
        refreshLayout = root.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newsList.clear();
                newsAdapter.notifyDataSetChanged();
                getWangYiNew();
            }
        });

        newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {//RecyclerView最后一个item继续向下滑动
            boolean isSlidingToLast = false;//用来标记是否正在向最后一个滑动，既是否向下滑动

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();//获取最后一个完全显示的ItemPosition
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {// 判断是否滚动到底部
                        getWangYiNew();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向:大于0表示，正在向下滚动；小于等于0 表示停止或向上滚动
                isSlidingToLast = dy > 0;
            }
        });
    }

    /**
     * 获取网易新闻的网络请求
     */
    private void getWangYiNew() {
        APIFactory.getInstance().getWangYiNew(new Subscriber<NewsWangYiBean>() {
            @Override
            public void onCompleted() {
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(getClass(), "获取网易新闻出错: " + e);
            }

            @Override
            public void onNext(NewsWangYiBean wangYiNewsBean) {
                newsList.addAll(wangYiNewsBean.getResult());
                newsAdapter.notifyDataSetChanged();
            }
        }, String.valueOf(page++), "20");
    }

}
