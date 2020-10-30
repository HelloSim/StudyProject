package com.sim.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sim.test.R;
import com.sim.test.bean.RecycleViewBean;

import java.util.List;

/**
 * ActivityRecycle.class中的适配器
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.BeautyViewHolder>{
    /**
     * 上下文
     */
    private Context context;
    /**
     * 数据集合
     */
    private List<RecycleViewBean> data;

    public RecycleViewAdapter(List<RecycleViewBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public BeautyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycle_view, parent, false);
        return new BeautyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BeautyViewHolder holder, int position) {
        //将数据设置到item上
        RecycleViewBean beauty = data.get(position);
        holder.beautyImage.setImageResource(beauty.getImageId());
        holder.nameTv.setText(beauty.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class BeautyViewHolder extends RecyclerView.ViewHolder {
        ImageView beautyImage;
        TextView nameTv;

        public BeautyViewHolder(View itemView) {
            super(itemView);
            beautyImage = itemView.findViewById(R.id.image_item);
            nameTv = itemView.findViewById(R.id.name_item);
        }
    }
}
