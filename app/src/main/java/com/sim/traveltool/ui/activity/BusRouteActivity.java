package com.sim.traveltool.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.baselibrary.base.BaseActivity;
import com.sim.baselibrary.base.BaseAdapter;
import com.sim.baselibrary.base.BaseViewHolder;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.BusRouteAdapter;
import com.sim.traveltool.bean.BusLocationDesignatedDataBean;
import com.sim.traveltool.bean.BusRouteDataBean;
import com.sim.traveltool.internet.APIFactory;
import com.sim.traveltool.ui.view.TitleView;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * @author Sim --- 显示出行方案的页面
 */
public class BusRouteActivity extends BaseActivity {

    private TitleView titleView;
    private TextView tvFromAndToLocation;
    private RecyclerView rlLocationList;

    private String tvStartLocation,//要显示的起始位置
            tvEndLocation,//要显示的终点位置
            origin,//用作请求的起始位置
            destination;//用作请求的终点位置

    private BusLocationDesignatedDataBean busLocationDesignatedDataBean;
    private ArrayList<BusRouteDataBean.RouteBean.TransitsBean> routeDataList = new ArrayList<>();
    private ArrayList<BusLocationDesignatedDataBean.PoisBean> startLocationList = new ArrayList<>();
    private ArrayList<BusLocationDesignatedDataBean.PoisBean> endLocationList = new ArrayList<>();
    private BusRouteAdapter routeAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bus_route;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        tvFromAndToLocation = findViewById(R.id.tv_from_and_to_location);
        rlLocationList = findViewById(R.id.rl_location_list);
        titleView.setLeftClickListener(new TitleView.LeftClickListener() {
            @Override
            public void onClick(View leftView) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        tvStartLocation = getIntent().getStringExtra("tvStartStation");
        tvEndLocation = getIntent().getStringExtra("tvEndStation");
        getLocation(true, tvStartLocation);
        getLocation(false, tvEndLocation);

    }

    @Override
    protected void initView() {
        tvFromAndToLocation.setText(tvStartLocation + " -> " + tvEndLocation);

        rlLocationList.setLayoutManager(new LinearLayoutManager(this));
        routeAdapter = new BusRouteAdapter(routeDataList);
        routeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(BaseViewHolder holder, int position) {
                Intent intent = new Intent(BusRouteActivity.this, BusRouteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", routeDataList.get(position));
                bundle.putString("tvStartLocation", tvStartLocation);
                bundle.putString("tvEndLocation", tvEndLocation);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rlLocationList.setAdapter(routeAdapter);
    }

    /**
     * 起始位置和终点位置的位置信息请求
     *
     * @param isStart
     * @param location
     */
    private void getLocation(boolean isStart, String location) {
        APIFactory.getInstance().getLocation(new Subscriber<BusLocationDesignatedDataBean>() {
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
                } else {
                    ToastUtil.toast(BusRouteActivity.this, "请求不到位置信息！");
                    finish();
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.toast(BusRouteActivity.this, "位置信息请求出错！");
                finish();
                LogUtil.e(getClass(), "位置信息请求出错: " + e);
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
    private void getRoute(String origin, String destination) {
        APIFactory.getInstance().getRoute(new Subscriber<BusRouteDataBean>() {
            @Override
            public void onCompleted() {
                if (routeDataList == null || routeDataList.size() == 0) {
                    ToastUtil.toast(BusRouteActivity.this, "未查询到换乘信息！");
                    finish();
                }
                routeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.toast(BusRouteActivity.this, "出行方案请求出错！");
                finish();
                LogUtil.e(getClass(), "出行方案请求出错: " + e);
            }

            @Override
            public void onNext(BusRouteDataBean dataBean) {
                routeDataList.addAll(dataBean.getRoute().getTransits());
            }
        }, origin, destination);
    }

}
