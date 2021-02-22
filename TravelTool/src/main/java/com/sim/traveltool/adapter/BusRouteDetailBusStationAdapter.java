package com.sim.traveltool.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.baselibrary.base.BaseAdapter;
import com.sim.baselibrary.base.BaseViewHolder;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.BusRouteDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sim ---
 */
public class BusRouteDetailBusStationAdapter extends BaseAdapter<BusRouteDetailBusStationAdapter.ViewHolder,
        BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean.BusBean.BuslinesBean> {

    private List<BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean.BusBean.BuslinesBean> buslinesBeanList = new ArrayList<>();

    public BusRouteDetailBusStationAdapter(List<BusRouteDataBean.RouteBean.TransitsBean.SegmentsBean.BusBean.BuslinesBean> buslinesBeanList) {
        super(buslinesBeanList);
        this.buslinesBeanList = buslinesBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_route_detail_bus_station, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.tv_item_bus_station_name.setText(buslinesBeanList.get(0).getDeparture_stop().getName() + " 上车");
            holder.tv_item_bus_station_name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (position == buslinesBeanList.get(0).getVia_stops().size() + 1) {
            holder.tv_item_bus_station_name.setText(buslinesBeanList.get(0).getArrival_stop().getName() + " 下车");
            holder.tv_item_bus_station_name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            holder.tv_item_bus_station_name.setText(buslinesBeanList.get(0).getVia_stops().get(position - 1).getName());
            holder.setVisibility(true);
        }
        holder.item_parent.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return buslinesBeanList.get(0).getVia_stops() == null ? 0 : buslinesBeanList.get(0).getVia_stops().size() + 2;
    }

    public class ViewHolder extends BaseViewHolder {

        private LinearLayout item_parent;
        private TextView tv_item_bus_station_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            item_parent = findViewById(R.id.item_parent);
            tv_item_bus_station_name = findViewById(R.id.tv_item_bus_station_name);
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }

    }

}
