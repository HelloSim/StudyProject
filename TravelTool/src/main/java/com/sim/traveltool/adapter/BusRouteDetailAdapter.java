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

import com.sim.traveltool.R;
import com.sim.traveltool.bean.BusRouteDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther Sim
 * @Time 2020/11/29 12:08
 * @Description 出行路线的详细方式页面的适配器
 */
public class BusRouteDetailAdapter extends RecyclerView.Adapter<BusRouteDetailAdapter.ViewHolder> {

    private Context mContext;
    private String tvStartLocation;//起点位置
    private String tvEndLocation;//终点位置
    private List<BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean> segmentsBeanList = new ArrayList<>();

    public BusRouteDetailAdapter(Context mContext, String tvStartLocation, String tvEndLocation, List<BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean> segmentsBeanList) {
        this.mContext = mContext;
        this.tvStartLocation = tvStartLocation;
        this.tvEndLocation = tvEndLocation;
        this.segmentsBeanList = segmentsBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_route_detail, parent, false);
        return new BusRouteDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.tv_item_start.setText(tvStartLocation);
            holder.ll_item_start.setVisibility(View.VISIBLE);
        }
        if (position == segmentsBeanList.size() - 1) {
            holder.tv_item_end.setText(tvEndLocation);
            holder.ll_item_end.setVisibility(View.VISIBLE);
        }
        if (segmentsBeanList.get(position).getWalking() != null) {
            if (segmentsBeanList.get(position).getWalking().getSteps().get(segmentsBeanList.get(position).getWalking().getSteps().size() - 1).getAssistant_action() != null &&
                    !segmentsBeanList.get(position).getWalking().getSteps().get(segmentsBeanList.get(position).getWalking().getSteps().size() - 1).getAssistant_action().toString().equals("[]")) {
                holder.tv_item_walk.setText("步行" + segmentsBeanList.get(position).getWalking().getDistance() + "米"
                        + segmentsBeanList.get(position).getWalking().getSteps().get(segmentsBeanList.get(position).getWalking().getSteps().size() - 1).getAssistant_action());
            } else {
                holder.tv_item_walk.setText("步行" + segmentsBeanList.get(position).getWalking().getDistance() + "米");
            }
            holder.ll_item_walk.setVisibility(View.VISIBLE);
        }
        if (segmentsBeanList.get(position).getBus().getBuslines() != null && segmentsBeanList.get(position).getBus().getBuslines().size() > 0) {
            holder.tv_item_bus.setText(segmentsBeanList.get(position).getBus().getBuslines().get(0).getName());
            holder.ll_item_bus.setVisibility(View.VISIBLE);
            BusRouteDetailBusStationAdapter busRouteDetailBusStationAdapter = new BusRouteDetailBusStationAdapter(mContext, segmentsBeanList.get(position).getBus().getBuslines());
            holder.rl_data.setAdapter(busRouteDetailBusStationAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return segmentsBeanList == null ? 0 : segmentsBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_parent;
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
            item_parent = itemView.findViewById(R.id.item_parent);
            ll_item_start = itemView.findViewById(R.id.ll_item_start);
            tv_item_start = itemView.findViewById(R.id.tv_item_start);
            ll_item_end = itemView.findViewById(R.id.ll_item_end);
            tv_item_end = itemView.findViewById(R.id.tv_item_end);
            ll_item_walk = itemView.findViewById(R.id.ll_item_walk);
            ll_item_bus = itemView.findViewById(R.id.ll_item_bus);
            tv_item_walk = itemView.findViewById(R.id.tv_item_walk);
            tv_item_bus = itemView.findViewById(R.id.tv_item_bus);
            rl_data = itemView.findViewById(R.id.rl_data);
            rl_data.setLayoutManager(new LinearLayoutManager(mContext));
        }

    }

}
