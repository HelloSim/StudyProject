package com.sim.traveltool.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.BusStationListAdapter;
import com.sim.traveltool.bean.BusRealTimeBusStopDataBean;
import com.sim.traveltool.bean.BusRealTimeDataBean;
import com.sim.traveltool.internet.APIFactory;
import com.sim.traveltool.ui.view.TitleView;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * @author Sim --- 显示实时公交位置的页面
 */
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

    private ArrayList<BusRealTimeBusStopDataBean.DataBean> stationList = new ArrayList<>();
    private ArrayList<BusRealTimeDataBean.DataBean> busListOnRoadListList = new ArrayList<>();

    private BusStationListAdapter stationListAdapter;

    private Handler handler;
    private final int REFRESH = 1001;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bus_real_time;
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
        APIFactory.getInstance().getStationList(new Subscriber<BusRealTimeBusStopDataBean>() {
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
            public void onNext(BusRealTimeBusStopDataBean data) {
                if (data.getFlag() == 1002) {
                    stationList.addAll(data.getData());
                }
            }
        }, "GetStationList", lineId, String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 获取实时公交数据的网络请求
     *
     * @param lineName
     * @param fromStation
     */
    private void getBusListOnRoad(String lineName, String fromStation) {
        busListOnRoadListList.clear();
        APIFactory.getInstance().getBusListOnRoad(new Subscriber<BusRealTimeDataBean>() {
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
            public void onNext(BusRealTimeDataBean data) {
                if (data.getFlag() == 1002) {
                    busListOnRoadListList.addAll(data.getData());
                }
            }
        }, "GetBusListOnRoad", lineName, fromStation, String.valueOf(System.currentTimeMillis()));
    }

}
