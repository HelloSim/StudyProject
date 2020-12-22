package com.sim.baselibrary.base;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Auther Sim
 * @Time 2020/12/22 14:21
 * @Description BaseAdapter
 */
public class Base_Adapter extends RecyclerView.Adapter<Base_Adapter.ViewHolder> {
    
    @NonNull
    @Override
    public Base_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Base_Adapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
