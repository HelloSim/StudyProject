package com.sim.wangyi.ui.fragment;

import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseAdapter;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.base.BaseViewHolder;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.LogUtil;
import com.sim.bean.WangyiBean;
import com.sim.http.APIFactory;
import com.sim.wangyi.R;
import com.sim.wangyi.adapter.NewsAdapter;

import java.util.ArrayList;

import rx.Subscriber;

@Route(path = ArouterUrl.Wangyi.wangyi_fragment)
public class WangyiFragment extends BaseFragment {

    private RecyclerView newsRecyclerView;
    private ArrayList<WangyiBean.NewsBean> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    private SwipeRefreshLayout refreshLayout;

    private int page = 0;

    @Override
    protected int getLayoutRes() {
        return R.layout.wangyi_fragment_wangyi;
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
                ARouter.getInstance()
                        .build(ArouterUrl.Web.web_activity)
                        .withSerializable("webUrl", newsAdapter.getData().get(position).getPath())
                        .navigation();
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
                page = 0;
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
        APIFactory.getInstance().getWangYiNew(new Subscriber<WangyiBean>() {
            @Override
            public void onCompleted() {
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(getClass(), "获取网易新闻出错: " + e);
            }

            @Override
            public void onNext(WangyiBean wangYiNewsBean) {
                newsList.addAll(wangYiNewsBean.getResult());
                newsAdapter.notifyDataSetChanged();
            }
        }, String.valueOf(page++));
    }

}
