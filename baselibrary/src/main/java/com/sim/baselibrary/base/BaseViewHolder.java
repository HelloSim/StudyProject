package com.sim.baselibrary.base;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: Sim
 * @Time： 2020/12/29 16:12
 * @Description： BaseViewHolder
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        bindViews();
    }

    protected abstract void bindViews();

    public final <T extends View> T findViewById(@IdRes int id) {
        if (id == View.NO_ID) {
            return null;
        }
        return itemView.findViewById(id);
    }

}
