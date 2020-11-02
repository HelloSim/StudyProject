package com.sim.traveltool.ui.activity.bus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.traveltool.R;
import com.sim.traveltool.adapter.bus.RouteAdapter;
import com.sim.traveltool.bean.bus.LocationDesignatedDataBean;
import com.sim.traveltool.bean.bus.RouteDataBean;
import com.sim.traveltool.ui.activity.BaseActivity;
import com.sim.traveltool.utils.HttpUtil;
import com.sim.traveltool.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Time: 2020/6/10 22:57
 * @Author: HelloSim
 * @Description :显示出行方案的页面
 */
public class RouteActivity extends BaseActivity {
    private static final String TAG = "RouteActivity";
    private Context context;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_from_and_to_location)
    TextView tvFromAndToLocation;
    @BindView(R.id.rl_location_list)
    RecyclerView rlLocationList;

    private String tvStartLocation;//要显示的起始位置
    private String tvEndLocation;//要显示的终点位置
    private String origin;//用作请求的起始位置
    private String destination;//用作请求的终点位置

    private ArrayList<RouteDataBean.RouteBean.TransitsBean> routeDataList = new ArrayList<>();
    private ArrayList<LocationDesignatedDataBean.PoisBean> startLocationList = new ArrayList<>();
    private ArrayList<LocationDesignatedDataBean.PoisBean> endLocationList = new ArrayList<>();
    private RouteAdapter routeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        ButterKnife.bind(this);
        context = this;
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        tvStartLocation = getIntent().getStringExtra("tvStartStation");
        tvEndLocation = getIntent().getStringExtra("tvEndStation");
        getLocation(true, tvStartLocation);
        getLocation(false, tvEndLocation);
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvFromAndToLocation.setText(tvStartLocation + " -> " + tvEndLocation);

        routeAdapter = new RouteAdapter(context, routeDataList);
        rlLocationList.setLayoutManager(new LinearLayoutManager(this));
        rlLocationList.setAdapter(routeAdapter);
    }

    /**
     * 起始位置和终点位置的位置信息请求
     *
     * @param isStart
     * @param location
     */
    private void getLocation(boolean isStart, String location) {
        try {
            String apiurl = null;
            apiurl = "http://restapi.amap.com/v3/place/text?" +
                    "s=rsv3" +
                    "children=" +
                    "&key=ceb54024fae4694f734b1006e8dc8324" +
                    "&extensions=all" +
                    "&page=1" +
                    "&offset=10" +
                    "&city=珠海" +
                    "&language=zh_cn" +
                    "&callback=" +
                    "&platform=JS" +
                    "&logversion=2.0" +
                    "&sdkversion=1.3" +
                    "&appname=http://www.zhbuswx.com/busline/BusQuery.html?v=2.01#/" +
                    "&csid=759CACE2-2197-4E0A-ADCB-1456B16775DA" +
                    "&keywords=" + location;
            HttpUtil.doGetAsyn(apiurl, new HttpUtil.CallBack() {
                @Override
                public void onRequestComplete(String result) {
                    LocationDesignatedDataBean locationDesignatedDataBean = stringToBean(result, LocationDesignatedDataBean.class);
                    if (isStart) {//起点
                        startLocationList.addAll(locationDesignatedDataBean.getPois());
                        origin = String.valueOf(startLocationList.get(0).getLocation());
                    } else {//终点
                        endLocationList.addAll(locationDesignatedDataBean.getPois());
                        destination = String.valueOf(endLocationList.get(0).getLocation());
                    }
                    if (origin != null && destination != null) {
                        getRoute(origin, destination);
                    }
                }

                @Override
                public void onRequestError(String result) {
                    Log.i("Sim", "getLocation.onRequestError: " + result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 出行方案的网络请求
     *
     * @param origin
     * @param destination
     */
    private void getRoute(String origin, String destination) {
        try {
            String apiurl = null;
            apiurl = "http://restapi.amap.com/v3/direction/transit/integrated?" +
                    "origin=" + origin +
                    "&destination=" + destination +
                    "&city=珠海" +
                    "&strategy=0" +
                    "&nightflag=0" +
                    "&extensions=" +
                    "&s=rsv3" +
                    "&cityd=珠海" +
                    "&key=ceb54024fae4694f734b1006e8dc8324" +
                    "&callback=" +
                    "&platform=JS" +
                    "&logversion=2.0" +
                    "&sdkversion=1.3" +
                    "&appname=http://www.zhbuswx.com/busline/BusQuery.html?v=2.01#/" +
                    "&csid=759CACE2-2197-4E0A-ADCB-1456B16775DA";
            HttpUtil.doGetAsyn(apiurl, new HttpUtil.CallBack() {
                @Override
                public void onRequestComplete(String result) {
                    RouteDataBean routeDataBean = stringToBean(result, RouteDataBean.class);
                    routeDataList.addAll(routeDataBean.getRoute().getTransits());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (routeDataList == null || routeDataList.size() == 0) {
                                ToastUtil.toast(context, "未查询到换乘信息！");
                                finish();
                            }
                            routeAdapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onRequestError(String result) {
                    Log.i("Sim", "getLocation.onRequestError: " + result);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转成bean
     */
    public static <T> T stringToBean(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
