package com.sim.bus.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.views.TitleView;
import com.sim.bean.RoutesBean;
import com.sim.bus.R;
import com.sim.bus.adapter.BusRouteDetailAdapter;

/**
 * 出行路线的详细方式
 */
@Route(path = ArouterUrl.Bus.bus_activity_route_detail)
public class BusRouteDetailActivity extends BaseActivity {

    private TitleView titleView;
    private TextView tvBusRoute;
    private TextView tvTimeDistance;
    private RecyclerView rlRouteDetail;

    private String tvStartLocation,//起点位置
            tvEndLocation;//终点位置
    private RoutesBean.RouteBean.TransitsBean data;//出行方式详细数据
    private StringBuffer busRoute = new StringBuffer();
    private BusRouteDetailAdapter busRouteDetailAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.bus_activity_route_detail;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        tvBusRoute = findViewById(R.id.tv_bus_route);
        tvTimeDistance = findViewById(R.id.tv_time_distance);
        rlRouteDetail = findViewById(R.id.rl_route_detail);
        titleView.setLeftClickListener(new TitleView.LeftClickListener() {
            @Override
            public void onClick(View leftView) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        tvStartLocation = getIntent().getStringExtra("tvStartLocation");
        tvEndLocation = getIntent().getStringExtra("tvEndLocation");
        data = (RoutesBean.RouteBean.TransitsBean) getIntent().getSerializableExtra("data");
        for (RoutesBean.RouteBean.TransitsBean.SegmentsBean segmentsBean : data.getSegments()) {
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

}
