package com.sim.traveltool.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.baselibrary.utils.LogUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.BusStationListAdapter;
import com.sim.traveltool.base.AppActivity;
import com.sim.traveltool.bean.BusRealTimeBusStopDataBean;
import com.sim.traveltool.bean.BusRealTimeDataBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @Time: 2020/6/9 22:08
 * @Author: HelloSim
 * @Description :显示实时公交位置的页面
 */
public class BusRealTimeActivity extends AppActivity {
    private static final String TAG = "Sim_BusRealTimeActivity";

    @BindView(R.id.rl_station_list)
    RecyclerView rlstationList;
    @BindView(R.id.tv_bus_name)
    TextView tvBusName;
    @BindView(R.id.tv_from_station)
    TextView tvFromStation;
    @BindView(R.id.tv_to_station)
    TextView tvToStation;
    @BindView(R.id.tv_begin_time)
    TextView tvBeginTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;

    private String busName;//公交名
    private String lineId;//公交id
    private String fromStation;//公交起始站
    private String toStation;//公交终点站
    private String beginTime;//开始运营时间
    private String endTime;//结束运营时间
    private String price;//公交乘坐价格

    private ArrayList<BusRealTimeBusStopDataBean.DataBean> stationList = new ArrayList<>();
    private ArrayList<BusRealTimeDataBean.DataBean> busListOnRoadListList = new ArrayList<>();

    private BusStationListAdapter stationListAdapter;

    private Handler handler;
    private final int REFRESH = 1001;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bus_real_time;
    }

    @SuppressLint("HandlerLeak")
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

    protected void initView() {
        tvBusName.setText(busName);
        tvFromStation.setText(fromStation);
        tvToStation.setText(toStation);
        tvBeginTime.setText(beginTime);
        tvEndTime.setText(endTime);
        tvPrice.setText(price + "元");
    }

    /**
     * 获取公交路线站点的网络请求
     *
     * @param lineId
     */
    private void getStationList(String lineId) {
        stationList.clear();
        retrofitUtil.getStationList(new Subscriber<BusRealTimeBusStopDataBean>() {
            @Override
            public void onCompleted() {
                if (stationList != null) {
                } else {
                    Toast.makeText(BusRealTimeActivity.this, "数据出错！", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, "获取公交路线站点请求出错: " + e);
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
        ivRefresh.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_refresh_gray));
        retrofitUtil.getBusListOnRoad(new Subscriber<BusRealTimeDataBean>() {
            @Override
            public void onCompleted() {
                ivRefresh.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_refresh_blue));
                //30s做一次更新查询
                handler.sendEmptyMessageDelayed(REFRESH, 30000);
                if (stationListAdapter == null) {
                    stationListAdapter = new BusStationListAdapter(BusRealTimeActivity.this, stationList, busListOnRoadListList);
                    stationListAdapter.setHasStableIds(true);
                    rlstationList.setLayoutManager(new LinearLayoutManager(BusRealTimeActivity.this));
                    rlstationList.setAdapter(stationListAdapter);
                } else {
                    stationListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BusRealTimeDataBean data) {
                if (data.getFlag() == 1002) {
                    busListOnRoadListList.addAll(data.getData());
                }
            }
        }, "GetBusListOnRoad", lineName, fromStation, String.valueOf(System.currentTimeMillis()));
    }

    @OnClick({R.id.back, R.id.iv_refresh, R.id.iv_reverse})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_refresh:
                handler.removeMessages(REFRESH);
                getBusListOnRoad(busName, fromStation);
                break;
            case R.id.iv_reverse:

                break;
        }
    }

}
