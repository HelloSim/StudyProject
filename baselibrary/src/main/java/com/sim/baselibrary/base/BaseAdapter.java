package com.sim.baselibrary.base;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Sim --- BaseAdapter。项目模块BaseAdapter继承此类
 */
public abstract class BaseAdapter<VH extends BaseViewHolder, D> extends RecyclerView.Adapter<VH> {

    private List<D> mData;

    public BaseAdapter(List<D> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        mData = data;
    }

    public BaseAdapter() {
        mData = new ArrayList<>();
    }

    public void setData(List<D> data) {
        this.mData = data;
    }

    public List<D> getData() {
        return mData;
    }

    /**
     * 插入某项
     *
     * @param d
     * @return
     */
    public int insertData(D d) {
        if (d != null && !mData.contains(d)) {
            mData.add(d);
            int position = mData.size() - 1;
            notifyItemInserted(position);
            return position;
        }
        return -1;
    }

    /**
     * 插入某项
     *
     * @param d
     * @return
     */
    public int insertData(D d, boolean addIsFirst) {
        if (d != null && !mData.contains(d)) {
            mData.add(d);
            int position = mData.size();
            notifyItemInserted(position);
            return position;
        }
        return -1;
    }

    public boolean insertData(Collection<D> d) {
        if (d != null && d.size() > 0) {
            mData.addAll(d);
            notifyItemRangeInserted(mData.size() - d.size(), mData.size());
            return true;
        }
        return false;
    }

    /**
     * 更新某项
     *
     * @param d
     */
    public void changeData(D d) {
        if (d != null) {
            int i = mData.indexOf(d);
            if (i != -1) {
                mData.remove(i);
                mData.add(i, d);
                notifyItemChanged(i);
            }
        }
    }

    /**
     * 移除某项
     *
     * @param item
     * @return
     */
    public int removeData(D item) {
        if (item != null) {
            int position = mData.indexOf(item);
            if (position != -1) {
                removeData(position);
            }
            return position;
        }
        return -1;
    }

    /**
     * 移除某项
     *
     * @return
     */
    public D removeData(int position) {
        if (position != -1 && mData.size() > position) {
            D remove = mData.remove(position);
            if (remove != null) {
                notifyItemRemoved(position);
                return remove;
            }
        }
        return null;
    }

    /**
     * 清除所有项
     */
    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public D getItem(int position) {
        if (position >= 0 && mData.size() > position) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void notifyDataSetChanged(List<D> dataList) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        mData = dataList;
        notifyDataSetChanged();
    }

    /**
     * recyclerView的item事件
     */
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    /**
     * item点击事件
     */
    public interface OnItemClickListener {
        void onItemClicked(BaseViewHolder holder, int position);
    }

    /**
     * item长按事件
     */
    public interface OnItemLongClickListener{
        void onItemLongClicked(BaseViewHolder holder, int position);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

}
