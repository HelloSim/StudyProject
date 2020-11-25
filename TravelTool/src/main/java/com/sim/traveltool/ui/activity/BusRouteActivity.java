package com.sim.traveltool.ui.activity;

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
import com.sim.traveltool.adapter.BusRouteAdapter;
import com.sim.traveltool.bean.BusLocationDesignatedDataBean;
import com.sim.traveltool.bean.BusRouteDataBean;
import com.sim.baselibrary.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @Time: 2020/6/10 22:57
 * @Author: HelloSim
 * @Description :显示出行方案的页面
 */
public class BusRouteActivity extends BaseActivity {
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


    private BusLocationDesignatedDataBean busLocationDesignatedDataBean;
    private ArrayList<BusRouteDataBean.RouteBean.TransitsBean> routeDataList = new ArrayList<>();
    private ArrayList<BusLocationDesignatedDataBean.PoisBean> startLocationList = new ArrayList<>();
    private ArrayList<BusLocationDesignatedDataBean.PoisBean> endLocationList = new ArrayList<>();
    private BusRouteAdapter routeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route);
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

        routeAdapter = new BusRouteAdapter(context, routeDataList);
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
        retrofitUtil.getLocation(new Subscriber<BusLocationDesignatedDataBean>() {
            @Override
            public void onCompleted() {
                if (isStart) {//起点
                    startLocationList.addAll(busLocationDesignatedDataBean.getPois());
                    origin = String.valueOf(startLocationList.get(0).getLocation());
                } else {//终点
                    endLocationList.addAll(busLocationDesignatedDataBean.getPois());
                    destination = String.valueOf(endLocationList.get(0).getLocation());
                }
                if (origin != null && destination != null) {
                    getRoute(origin, destination);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onNext(BusLocationDesignatedDataBean dataBean) {
                busLocationDesignatedDataBean = dataBean;
            }
        }, location);
    }

    /**
     * 出行方案的网络请求
     *
     * @param origin
     * @param destination
     */
    BusRouteDataBean busRouteDataBean;

    private void getRoute(String origin, String destination) {
        retrofitUtil.getRoute(new Subscriber<BusRouteDataBean>() {
            @Override
            public void onCompleted() {
                if (routeDataList == null || routeDataList.size() == 0) {
                    ToastUtil.toast(context, "未查询到换乘信息！");
                    finish();
                }
                routeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onNext(BusRouteDataBean dataBean) {
                routeDataList.addAll(dataBean.getRoute().getTransits());
            }
        }, origin, destination);
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
