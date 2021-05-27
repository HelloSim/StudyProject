package com.sim.bus.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sim.bean.BusRealTimeBean;
import com.sim.bean.BusRealTimeBusStopBean;
import com.sim.basicres.base.BaseAdapter;
import com.sim.basicres.base.BaseViewHolder;
import com.sim.bus.R;

import java.util.ArrayList;

/**
 * @author Sim --- 起始位置搜索界面的RecyclerView适配器
 */
public class BusStationListAdapter extends BaseAdapter<BusStationListAdapter.ViewHolder, BusRealTimeBusStopBean.DataBean> {

    private ArrayList<BusRealTimeBean.DataBean> busListOnRoadListList = new ArrayList<>();

    public BusStationListAdapter(ArrayList<BusRealTimeBusStopBean.DataBean> stationList, ArrayList<BusRealTimeBean.DataBean> busListOnRoadListList) {
        super(stationList);
        this.busListOnRoadListList = busListOnRoadListList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_recycler_view_item_station_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvStationNum.setText(String.valueOf(position + 1));
        holder.tvStationName.setText(getItem(position).getName());
        if (busListOnRoadListList != null) {
            StringBuffer busNumber = new StringBuffer();
            for (BusRealTimeBean.DataBean dataBean : busListOnRoadListList) {
                if (holder.tvStationName.getText().toString().equals(dataBean.getCurrentStation())) {
                    busNumber.append("\n" + dataBean.getBusNumber());
                }
            }
            holder.tvBusNumber.setText(busNumber);
        }
    }

    public class ViewHolder extends BaseViewHolder {

        private TextView tvStationNum;
        private TextView tvStationName;
        private TextView tvBusNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            tvStationNum = findViewById(R.id.tv_station_num);
            tvStationName = findViewById(R.id.tv_station_name);
            tvBusNumber = findViewById(R.id.tv_bus_number);
        }

    }

}
