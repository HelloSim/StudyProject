package com.sim.traveltool.adapter.bus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.traveltool.R;
import com.sim.traveltool.bean.bus.BusRealTimeDataBean;
import com.sim.traveltool.bean.bus.BusRealTimeByLineDataBean;

import java.util.ArrayList;

/**
 * @Time: 2020/6/9 22:24
 * @Author: HelloSim
 * @Description :起始位置搜索界面的RecyclerView适配器
 */
public class StationListAdapter extends RecyclerView.Adapter<StationListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<BusRealTimeByLineDataBean.DataBean> stationList = new ArrayList<>();
    private ArrayList<BusRealTimeDataBean.DataBean> busListOnRoadListList = new ArrayList<>();


    public StationListAdapter(Context mContext, ArrayList<BusRealTimeByLineDataBean.DataBean> stationList, ArrayList<BusRealTimeDataBean.DataBean> busListOnRoadListList) {
        this.mContext = mContext;
        this.stationList = stationList;
        this.busListOnRoadListList = busListOnRoadListList;
    }

    public void refresh(ArrayList<BusRealTimeDataBean.DataBean> busListOnRoadListList) {
        this.busListOnRoadListList.clear();
        this.busListOnRoadListList = busListOnRoadListList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_station_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvStationNum.setText(String.valueOf(position + 1));
        holder.tvStationName.setText(stationList.get(position).getName());
        if (busListOnRoadListList != null) {
            StringBuffer busNumber = new StringBuffer();
            for (BusRealTimeDataBean.DataBean dataBean : busListOnRoadListList) {
                if (holder.tvStationName.getText().toString().equals(dataBean.getCurrentStation())) {
                    busNumber.append("\n" + dataBean.getBusNumber());
                }
            }
            holder.tvBusNumber.setText(busNumber);
        }
    }

    @Override
    public int getItemCount() {
        return stationList == null ? 0 : stationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvStationNum;
        private TextView tvStationName;
        private TextView tvBusNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStationNum = itemView.findViewById(R.id.tv_station_num);
            tvStationName = itemView.findViewById(R.id.tv_station_name);
            tvBusNumber = itemView.findViewById(R.id.tv_bus_number);
        }

    }

}
