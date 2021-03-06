package com.sim.bus.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.LogUtil;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
import com.sim.bean.BusRealTimeBean;
import com.sim.bean.BusRealTimeBusStopBean;
import com.sim.bus.R;
import com.sim.bus.adapter.BusStationListAdapter;
import com.sim.http.APIFactory;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * 显示实时公交位置的页面
 */
@Route(path = ArouterUrl.Bus.bus_activity_realtime)
public class BusRealTimeActivity extends BaseActivity {

    private TitleView titleView;
    private TextView tvFromStation,
            tvToStation,
            tvBeginTime,
            tvEndTime,
            tvPrice,
            tvReverse;
    private RecyclerView rlstationList;

    private String busName,//公交名
            lineId,//公交id
            fromStation,//公交起始站
            toStation,//公交终点站
            beginTime,//开始运营时间
            endTime,//结束运营时间
            price;//公交乘坐价格

    private ArrayList<BusRealTimeBusStopBean.DataBean> stationList = new ArrayList<>();
    private ArrayList<BusRealTimeBean.DataBean> busListOnRoadListList = new ArrayList<>();

    private BusStationListAdapter stationListAdapter;

    private Handler handler;
    private final int REFRESH = 1001;

    @Override
    protected int getLayoutRes() {
        return R.layout.bus_activity_real_time;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        rlstationList = findViewById(R.id.rl_station_list);
        tvFromStation = findViewById(R.id.tv_from_station);
        tvToStation = findViewById(R.id.tv_to_station);
        tvBeginTime = findViewById(R.id.tv_begin_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        tvPrice = findViewById(R.id.tv_price);
        tvReverse = findViewById(R.id.tv_reverse);
        setViewClick(tvReverse);
        titleView.setClickListener(new TitleView.ClickListener() {
            @Override
            public void left(View leftView) {
                finish();
            }

            @Override
            public void right(View right) {
                handler.removeMessages(REFRESH);
                getBusListOnRoad(busName, fromStation);
            }
        });
    }

    @Override
    protected void initData() {
        busName = getIntent().getStringExtra("busName");
        lineId = getIntent().getStringExtra("lineId");
        fromStation = getIntent().getStringExtra("fromStation");
        toStation = getIntent().getStringExtra("toStation");
        beginTime = getIntent().getStringExtra("beginTime");
        endTime = getIntent().getStringExtra("endTime");
        price = getIntent().getStringExtra("price");

        getStationList(lineId);
        getBusListOnRoad(busName, fromStation);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case REFRESH:
                        getBusListOnRoad(busName, fromStation);
                        break;
                }
            }
        };
    }

    @Override
    protected void initView() {
        titleView.setTitleTextView(busName);
        tvFromStation.setText(fromStation);
        tvToStation.setText(toStation);
        tvBeginTime.setText(beginTime);
        tvEndTime.setText(endTime);
        tvPrice.setText(price + "元");
    }

    @Override
    public void onMultiClick(View view) {
        if (view == tvReverse) {

        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 获取公交路线站点的网络请求
     *
     * @param lineId
     */
    private void getStationList(String lineId) {
        stationList.clear();
        APIFactory.getInstance().getStationList(new Subscriber<BusRealTimeBusStopBean>() {
            @Override
            public void onCompleted() {
                if (stationList == null) {
                    ToastUtil.toast(BusRealTimeActivity.this, "获取公交路线站点请求出错！");
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.toast(BusRealTimeActivity.this, "获取公交路线站点请求出错！");
                finish();
                LogUtil.e(getClass(), "获取公交路线站点请求出错: " + e);
            }

            @Override
            public void onNext(BusRealTimeBusStopBean data) {
                if (data.getFlag() == 1002) {
                    stationList.addAll(data.getData());
                }
            }
        }, "GetStationList", lineId);
    }

    /**
     * 获取实时公交数据的网络请求
     *
     * @param lineName
     * @param fromStation
     */
    private void getBusListOnRoad(String lineName, String fromStation) {
        busListOnRoadListList.clear();
        APIFactory.getInstance().getBusListOnRoad(new Subscriber<BusRealTimeBean>() {
            @Override
            public void onCompleted() {
                //30s做一次更新查询
                handler.sendEmptyMessageDelayed(REFRESH, 30000);
                if (stationListAdapter == null) {
                    stationListAdapter = new BusStationListAdapter(stationList, busListOnRoadListList);
                    stationListAdapter.setHasStableIds(true);
                    rlstationList.setLayoutManager(new LinearLayoutManager(BusRealTimeActivity.this));
                    rlstationList.setAdapter(stationListAdapter);
                } else {
                    stationListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.toast(BusRealTimeActivity.this, "获取实时公交数据请求出错！");
                LogUtil.e(getClass(), "获取实时公交数据的网络请求: " + e);
            }

            @Override
            public void onNext(BusRealTimeBean data) {
                if (data.getFlag() == 1002) {
                    busListOnRoadListList.addAll(data.getData());
                }
            }
        }, "GetBusListOnRoad", lineName, fromStation);
    }

}
