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
import com.sim.traveltool.bean.BusRealTimeLineDataBean;

import java.util.ArrayList;

/**
 * @Time: 2020/6/9 19:49
 * @Author: HelloSim
 * @Description :实时公交搜索界面的RecyclerView适配器
 */
public class BusLineNameAdapter extends RecyclerView.Adapter<BusLineNameAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<BusRealTimeLineDataBean.DataBean> busLineNameBeanList;

    private onItemClickListener mOnItemClickListerer;

    public BusLineNameAdapter(Context mContext, ArrayList<BusRealTimeLineDataBean.DataBean> busLineNameBeanList) {
        this.mContext = mContext;
        this.busLineNameBeanList = busLineNameBeanList;
    }

    public void setOnItemClickListerer(onItemClickListener onItemClickListerer) {
        mOnItemClickListerer = onItemClickListerer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_bus_line_name, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (busLineNameBeanList != null) {
            holder.tvBusName.setText(busLineNameBeanList.get(position).getName());
            holder.tvBusLineName.setText(busLineNameBeanList.get(position).getFromStation()
                    + "->" + busLineNameBeanList.get(position).getToStation());
            holder.itemParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListerer.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout itemParent;
        private TextView tvBusName;
        private TextView tvBusLineName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemParent = itemView.findViewById(R.id.item_parent);
            tvBusName = itemView.findViewById(R.id.tv_bus_name);
            tvBusLineName = itemView.findViewById(R.id.tv_bus_line_name);
        }

    }

    public interface onItemClickListener {
        void onItemClick(View view, int i);
    }

}
