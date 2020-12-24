package com.sim.traveltool.ui.activity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.traveltool.R;
import com.sim.traveltool.adapter.BusRouteDetailAdapter;
import com.sim.traveltool.base.AppActivity;
import com.sim.traveltool.bean.BusRouteDataBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Auther Sim
 * @Time 2020/11/29 1:12
 * @Description 出行路线的详细方式
 */
public class BusRouteDetailActivity extends AppActivity {

    @BindView(R.id.tv_bus_route)
    TextView tv_bus_route;
    @BindView(R.id.tv_time_distance)
    TextView tv_time_distance;
    @BindView(R.id.rl_route_detail)
    RecyclerView rl_route_detail;

    private String tvStartLocation;//起点位置
    private String tvEndLocation;//终点位置
    private BusRouteDataBean.RouteBean.TransitsBean data;//出行方式详细数据
    private StringBuffer busRoute = new StringBuffer();
    private BusRouteDetailAdapter busRouteDetailAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bus_route_detail;
    }

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

    protected void initView() {
        tv_bus_route.setText(busRoute);
        tv_time_distance.setText(Integer.parseInt(data.getDuration()) / 60 + "分钟 | 步行" + data.getWalking_distance() + "米");
        busRouteDetailAdapter = new BusRouteDetailAdapter(this, tvStartLocation, tvEndLocation, data.getSegments());
        rl_route_detail.setLayoutManager(new LinearLayoutManager(this));
        rl_route_detail.setAdapter(busRouteDetailAdapter);
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
