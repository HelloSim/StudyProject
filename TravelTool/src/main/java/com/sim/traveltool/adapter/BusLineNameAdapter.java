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
import com.sim.traveltool.bean.BusRealTimeLineDataBean;

import java.util.List;

/**
 * @author Sim --- 实时公交搜索界面的RecyclerView适配器
 */
public class BusLineNameAdapter extends BaseAdapter<BusLineNameAdapter.ViewHolder, BusRealTimeLineDataBean.DataBean> {

    public BusLineNameAdapter(List<BusRealTimeLineDataBean.DataBean> busLineNameBeanList) {
        super(busLineNameBeanList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_bus_line_name, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusRealTimeLineDataBean.DataBean dataBean = getItem(position);
        if (dataBean != null) {
            holder.tvBusName.setText(dataBean.getName());
            holder.tvBusLineName.setText(dataBean.getFromStation() + "->" + dataBean.getToStation());
        }
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

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends BaseViewHolder {

        private LinearLayout parent;
        private TextView tvBusName;
        private TextView tvBusLineName;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            parent = findViewById(R.id.item_parent);
            tvBusName = findViewById(R.id.tv_bus_name);
            tvBusLineName = findViewById(R.id.tv_bus_line_name);
        }

    }

}
