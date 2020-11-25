package com.sim.traveltool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.traveltool.R;
import com.sim.traveltool.bean.BusRouteDataBean;

import java.util.ArrayList;

/**
 * @Time: 2020/6/11 23:02
 * @Author: HelloSim
 * @Description :出行方案界面的RecyclerView适配器
 */
public class BusRouteAdapter extends RecyclerView.Adapter<BusRouteAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<BusRouteDataBean.RouteBean.TransitsBean> routeDataList = new ArrayList<>();

    public BusRouteAdapter(Context mContext, ArrayList<BusRouteDataBean.RouteBean.TransitsBean> routeDataList) {
        this.mContext = mContext;
        this.routeDataList = routeDataList;
    }

    @NonNull
    @Override
    public BusRouteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_route, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BusRouteAdapter.ViewHolder holder, int position) {
        if (routeDataList != null) {
            ArrayList<BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean> segmentsBeanArrayList = new ArrayList<>();
            segmentsBeanArrayList.addAll(routeDataList.get(position).getSegments());
            ArrayList<BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean.BusBean.BuslinesBean> buslinesBeanArrayList = new ArrayList<>();
            for (int i = 0; i < segmentsBeanArrayList.size(); i++) {
                buslinesBeanArrayList.addAll(segmentsBeanArrayList.get(i).getBus().getBuslines());
            }
            StringBuffer busName = new StringBuffer();
            for (int i = 0; i < routeDataList.get(position).getSegments().size(); i++) {
                try {
                    if (busName.length() > 0) {
                        busName.append("\n" + routeDataList.get(position).getSegments().get(i).getBus().getBuslines().get(0).getName());
                    } else {
                        busName.append(routeDataList.get(position).getSegments().get(i).getBus().getBuslines().get(0).getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            holder.tvBusName.setText(busName);
            holder.tvRouteRoughly.setText(Integer.parseInt(routeDataList.get(position).getDuration()) / 60 + "分钟 | "
                    + "步行" + routeDataList.get(position).getWalking_distance() + "米");
        }
    }

    @Override
    public int getItemCount() {
        return routeDataList == null ? 0 : routeDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout itemParent;
        private TextView tvBusName;
        private TextView tvRouteRoughly;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemParent = itemView.findViewById(R.id.item_parent);
            tvBusName = itemView.findViewById(R.id.tv_bus_name);
            tvRouteRoughly = itemView.findViewById(R.id.tv_route_roughly);
        }

    }

}
