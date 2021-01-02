package com.sim.traveltool.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.BusRouteDetailAdapter;
import com.sim.traveltool.bean.BusRouteDataBean;

/**
 * @Auther Sim
 * @Time 2020/11/29 1:12
 * @Description 出行路线的详细方式
 */
public class BusRouteDetailActivity extends BaseActivity {

    private ImageView back;
    private TextView tvBusRoute;
    private TextView tvTimeDistance;
    private RecyclerView rlRouteDetail;

    private String tvStartLocation;//起点位置
    private String tvEndLocation;//终点位置
    private BusRouteDataBean.RouteBean.TransitsBean data;//出行方式详细数据
    private StringBuffer busRoute = new StringBuffer();
    private BusRouteDetailAdapter busRouteDetailAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bus_route_detail;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        back = findViewById(R.id.back);
        tvBusRoute = findViewById(R.id.tv_bus_route);
        tvTimeDistance = findViewById(R.id.tv_time_distance);
        rlRouteDetail = findViewById(R.id.rl_route_detail);
        setViewClick(back);
    }

    @Override
    protected void initData() {
        tvStartLocation = getIntent().getStringExtra("tvStartLocation");
        tvEndLocation = getIntent().getStringExtra("tvEndLocation");
        data = (BusRouteDataBean.RouteBean.TransitsBean) getIntent().getSerializableExtra("data");
        for (BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean segmentsBean : data.getSegments()) {
            if (segmentsBean.getBus().getBuslines() != null && segmentsBean.getBus().getBuslines().size() > 0) {
                if (busRoute.length() > 0) {
                    busRoute.append(" > ");
                }
                busRoute.append(segmentsBean.getBus().getBuslines().get(0).getName().split("\\(", 2)[0]);
            }
        }
    }

    @Override
    protected void initView() {
        tvBusRoute.setText(busRoute);
        tvTimeDistance.setText(Integer.parseInt(data.getDuration()) / 60 + "分钟 | 步行" + data.getWalking_distance() + "米");
        busRouteDetailAdapter = new BusRouteDetailAdapter(this, tvStartLocation, tvEndLocation, data.getSegments());
        rlRouteDetail.setLayoutManager(new LinearLayoutManager(this));
        rlRouteDetail.setAdapter(busRouteDetailAdapter);
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
