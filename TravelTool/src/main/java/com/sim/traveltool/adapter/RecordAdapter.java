package com.sim.traveltool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.sqlitelibrary.bean.DaKaRecord;
import com.sim.traveltool.R;

import java.util.List;


/**
 * @Auther Sim
 * @Time 2020/10/22 14:44
 * @Description 月打卡列表的适配器
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private Context mContext;
    private List<DaKaRecord> daKaRecordList;

    public RecordAdapter(Context context, List<DaKaRecord> daKaRecordList) {
        this.mContext = context;
        this.daKaRecordList = daKaRecordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.tv_time.setText("日期");
            holder.tv_start.setText("上班打卡");
            holder.tv_end.setText("下班打卡");
        } else {
            if (daKaRecordList != null && daKaRecordList.size() > 0) {
                holder.tv_time.setText(daKaRecordList.get(position - 1).getDay() + "日\t" + daKaRecordList.get(position - 1).getWeek());
                if (daKaRecordList.get(position - 1).getStartTime() != null) {
                    if (daKaRecordList.get(position - 1).getIsLate().equals("1"))
                        holder.tv_start.setTextColor(Color.BLUE);
                    else
                        holder.tv_start.setTextColor(Color.WHITE);
                    holder.tv_start.setText(daKaRecordList.get(position - 1).getStartTime());
                } else {
                    holder.tv_start.setText(mContext.getString(R.string.no));
                }
                if (daKaRecordList.get(position - 1).getEndTime() != null) {
                    if (daKaRecordList.get(position - 1).getIsLeaveEarly().equals("1"))
                        holder.tv_end.setTextColor(Color.RED);
                    else
                        holder.tv_end.setTextColor(Color.WHITE);
                    holder.tv_end.setText(daKaRecordList.get(position - 1).getEndTime());
                } else {
                    holder.tv_end.setText(mContext.getString(R.string.no));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return daKaRecordList == null ? 1 : daKaRecordList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_time;
        private TextView tv_start;
        private TextView tv_end;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_start = itemView.findViewById(R.id.tv_start);
            tv_end = itemView.findViewById(R.id.tv_end);
        }
    }

}
