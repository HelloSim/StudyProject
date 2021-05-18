package com.sim.traveltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.baselibrary.base.BaseAdapter;
import com.sim.baselibrary.base.BaseViewHolder;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.BusRouteDataBean;

import java.util.List;

/**
 * @author Sim --- 出行路线的详细方式页面的适配器
 */
public class BusRouteDetailAdapter extends BaseAdapter<BusRouteDetailAdapter.ViewHolder,
        BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean> {

    private Context mContext;
    private String tvStartLocation;//起点位置
    private String tvEndLocation;//终点位置

    public BusRouteDetailAdapter(Context mContext, String tvStartLocation, String tvEndLocation,
                                 List<BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean> segmentsBeanList) {
        super(segmentsBeanList);
        this.mContext = mContext;
        this.tvStartLocation = tvStartLocation;
        this.tvEndLocation = tvEndLocation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_route_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.tv_item_start.setText(tvStartLocation);
            holder.ll_item_start.setVisibility(View.VISIBLE);
        }
        if (position == getData().size() - 1) {
            holder.tv_item_end.setText(tvEndLocation);
            holder.ll_item_end.setVisibility(View.VISIBLE);
        }
        BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean segmentsBean = getItem(position);
        if (segmentsBean.getWalking() != null) {
            if (segmentsBean.getWalking().getSteps().get(segmentsBean.getWalking().getSteps().size() - 1).getAssistant_action() != null &&
                    !segmentsBean.getWalking().getSteps().get(segmentsBean.getWalking().getSteps().size() - 1).getAssistant_action().toString().equals("[]")) {
                holder.tv_item_walk.setText("步行" + segmentsBean.getWalking().getDistance() + "米"
                        + segmentsBean.getWalking().getSteps().get(segmentsBean.getWalking().getSteps().size() - 1).getAssistant_action());
            } else {
                holder.tv_item_walk.setText("步行" + segmentsBean.getWalking().getDistance() + "米");
            }
            holder.ll_item_walk.setVisibility(View.VISIBLE);
        }
        if (segmentsBean.getBus().getBuslines() != null && segmentsBean.getBus().getBuslines().size() > 0) {
            holder.tv_item_bus.setText(segmentsBean.getBus().getBuslines().get(0).getName());
            holder.ll_item_bus.setVisibility(View.VISIBLE);
            BusRouteDetailBusStationAdapter busRouteDetailBusStationAdapter = new BusRouteDetailBusStationAdapter(segmentsBean.getBus().getBuslines());
            holder.rl_data.setAdapter(busRouteDetailBusStationAdapter);
        }
    }

    public class ViewHolder extends BaseViewHolder {
        private LinearLayout ll_item_start;
        private TextView tv_item_start;
        private LinearLayout ll_item_end;
        private TextView tv_item_end;
        private LinearLayout ll_item_walk;
        private LinearLayout ll_item_bus;
        private TextView tv_item_walk;
        private TextView tv_item_bus;
        private RecyclerView rl_data;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            ll_item_start = findViewById(R.id.ll_item_start);
            tv_item_start = findViewById(R.id.tv_item_start);
            ll_item_end = findViewById(R.id.ll_item_end);
            tv_item_end = findViewById(R.id.tv_item_end);
            ll_item_walk = findViewById(R.id.ll_item_walk);
            ll_item_bus = findViewById(R.id.ll_item_bus);
            tv_item_walk = findViewById(R.id.tv_item_walk);
            tv_item_bus = findViewById(R.id.tv_item_bus);
            rl_data = findViewById(R.id.rl_data);
            rl_data.setLayoutManager(new LinearLayoutManager(mContext));
        }

    }

}
