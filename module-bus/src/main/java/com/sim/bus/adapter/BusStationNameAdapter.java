package com.sim.bus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sim.bean.RouteLocationBean;
import com.sim.basicres.base.BaseAdapter;
import com.sim.basicres.base.BaseViewHolder;
import com.sim.bus.R;

import java.util.ArrayList;

/**
 * 终点位置搜索界面的RecyclerView适配器
 */
public class BusStationNameAdapter extends BaseAdapter<BusStationNameAdapter.ViewHolder, RouteLocationBean.TipsBean> {

    public BusStationNameAdapter(ArrayList<RouteLocationBean.TipsBean> startStationDataList) {
        super(startStationDataList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.bus_recycler_view_item_station_name, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RouteLocationBean.TipsBean tipsBean = getItem(position);
        if (tipsBean != null)
            holder.tvLocationName.setText(String.valueOf(tipsBean.getName()));
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

    public class ViewHolder extends BaseViewHolder {

        private LinearLayout parent;
        private TextView tvLocationName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            parent = findViewById(R.id.item_parent);
            tvLocationName = findViewById(R.id.tv_location_name);
        }

    }

}
