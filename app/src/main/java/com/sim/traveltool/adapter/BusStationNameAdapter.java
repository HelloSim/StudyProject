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
import com.sim.traveltool.bean.BusLocationDataBean;

import java.util.ArrayList;

/**
 * @author Sim --- 终点位置搜索界面的RecyclerView适配器
 */
public class BusStationNameAdapter extends BaseAdapter<BusStationNameAdapter.ViewHolder, BusLocationDataBean.TipsBean> {

    public BusStationNameAdapter(ArrayList<BusLocationDataBean.TipsBean> startStationDataList) {
        super(startStationDataList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_view_item_station_name, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusLocationDataBean.TipsBean tipsBean = getItem(position);
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
