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
import com.sim.traveltool.bean.BusLocationDataBean;

import java.util.ArrayList;

/**
 * @Time: 2020/6/9 19:49
 * @Author: HelloSim
 * @Description :终点位置搜索界面的RecyclerView适配器
 */
public class BusStationNameAdapter extends RecyclerView.Adapter<BusStationNameAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<BusLocationDataBean.TipsBean> startStationDataList = new ArrayList<>();

    private onItemClickListener mOnItemClickListerer;

    public BusStationNameAdapter(Context mContext, ArrayList<BusLocationDataBean.TipsBean> startStationDataList) {
        this.mContext = mContext;
        this.startStationDataList = startStationDataList;
    }

    public void setOnItemClickListerer(onItemClickListener onItemClickListerer) {
        mOnItemClickListerer = onItemClickListerer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_station_name, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (startStationDataList != null) {
            holder.tvLocationName.setText(String.valueOf(startStationDataList.get(position).getName()));
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
        return startStationDataList == null ? 0 : startStationDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout itemParent;
        private TextView tvLocationName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemParent = itemView.findViewById(R.id.item_parent);
            tvLocationName = itemView.findViewById(R.id.tv_location_name);
        }

    }

    public interface onItemClickListener {
        void onItemClick(View view, int i);
    }

}
