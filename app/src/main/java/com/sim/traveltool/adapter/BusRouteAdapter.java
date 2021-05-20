package com.sim.traveltool.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sim.baselibrary.base.BaseAdapter;
import com.sim.baselibrary.base.BaseViewHolder;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.BusRouteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sim --- 出行方案界面的RecyclerView适配器
 */
public class BusRouteAdapter extends BaseAdapter<BusRouteAdapter.ViewHolder, BusRouteBean.RouteBean.TransitsBean> {

    public BusRouteAdapter(List<BusRouteBean.RouteBean.TransitsBean> routeDataList) {
        super(routeDataList);
    }

    @NonNull
    @Override
    public BusRouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_route, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BusRouteAdapter.ViewHolder holder, int position) {
        BusRouteBean.RouteBean.TransitsBean transitsBean = getItem(position);
        if (transitsBean != null) {
            ArrayList<BusRouteBean.RouteBean.TransitsBean.SegmentsBean> segmentsBeanArrayList = new ArrayList<>();
            segmentsBeanArrayList.addAll(transitsBean.getSegments());
            ArrayList<BusRouteBean.RouteBean.TransitsBean.SegmentsBean.BusBean.BuslinesBean> buslinesBeanArrayList = new ArrayList<>();
            for (int i = 0; i < segmentsBeanArrayList.size(); i++) {
                buslinesBeanArrayList.addAll(segmentsBeanArrayList.get(i).getBus().getBuslines());
            }
            StringBuffer busName = new StringBuffer();
            for (int i = 0; i < transitsBean.getSegments().size(); i++) {
                try {
                    if (busName.length() > 0) {
                        busName.append("\n" + transitsBean.getSegments().get(i).getBus().getBuslines().get(0).getName());
                    } else {
                        busName.append(transitsBean.getSegments().get(i).getBus().getBuslines().get(0).getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            holder.tvBusName.setText(busName);
            holder.tvRouteRoughly.setText(Integer.parseInt(transitsBean.getDuration()) / 60 + "分钟 | " + "步行" + transitsBean.getWalking_distance() + "米");

            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getOnItemClickListener() != null)
                        getOnItemClickListener().onItemClicked(holder, position);
                }
            });
            holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (getOnItemLongClickListener() != null)
                        getOnItemLongClickListener().onItemLongClicked(holder, position);
                    return false;
                }
            });
        }
    }

    public class ViewHolder extends BaseViewHolder {

        private LinearLayout parent;
        private TextView tvBusName;
        private TextView tvRouteRoughly;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            parent = findViewById(R.id.item_parent);
            tvBusName = findViewById(R.id.tv_bus_name);
            tvRouteRoughly = findViewById(R.id.tv_route_roughly);
        }

    }

}
