package com.sim.test.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;
import com.sim.test.adapter.SmzdmDatasAdapter;
import com.sim.test.bean.SmzdmDataBean;
import com.sim.test.internet.APIFactory;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Sim on 2019/10/22
 */
@SuppressLint("Registered")
public class SmzdmActivity extends BaseActivity {

    private Context context;

    private APIFactory retrofitUtil;
    private ArrayList<SmzdmDataBean.DataBean.RowsBean> list = new ArrayList<>();

    public RecyclerView smzdm_recyclerView;
    private SmzdmDatasAdapter smzdmDatasAdapter;
    //向下滑动参数
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smzdm);
        context = this;
        initRecyclerView();
        getData();
    }

    private void initRecyclerView () {
        //获取RecyclerView
        smzdm_recyclerView = findViewById(R.id.smzdm_recycle_view);
        //数据写入适配器
        smzdmDatasAdapter = new SmzdmDatasAdapter(context, list);
        //设置线性布局管理器
        smzdm_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //给RecyclerView设置Adapter
        smzdm_recyclerView.setAdapter(smzdmDatasAdapter);
        //freshLayout控件向上滑动刷新
        refreshLayout = findViewById(R.id.smzdm_refresh);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getData();
            }
        });
        //RecyclerView最后一个item继续向下滑动
        smzdm_recyclerView.addOnScrollListener( new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动，既是否向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        //Toast.makeText(InventoryInformationActivity.this, "加载更多", Toast.LENGTH_SHORT).show();
                        getData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled( recyclerView, dx, dy );
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                //大于0表示，正在向下滚动
                //小于等于0 表示停止或向上滚动
                isSlidingToLast = dy > 0;
            }
        } );
    }

    public void getData() {
        APIFactory.getInstance().init(this);
        retrofitUtil = APIFactory.getInstance();
        retrofitUtil.getHome(new Subscriber<SmzdmDataBean>() {
            @Override
            public void onCompleted() {
                //请求完成隐藏
                refreshLayout.setRefreshing( false );
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(SmzdmDataBean smzdmDataBean) {
                list.addAll(smzdmDataBean.getData().getRows());
                smzdmDatasAdapter.notifyDataSetChanged();
            }
        }, "1", "20", String.valueOf(System.currentTimeMillis()));
    }

}
