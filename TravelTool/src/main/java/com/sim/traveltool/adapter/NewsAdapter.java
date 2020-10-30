package com.sim.traveltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.WangYiNewsBean;

import java.util.ArrayList;

/**
 * 新闻列表界面的RecyclerView适配器
 * Created by Sim on 2020/4/27
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<WangYiNewsBean.ResultBean> news;

    private onItemClickListener mOnItemClickListerer;
    private onItemLongClickListener mOnItemLongClickListener;

    public NewsAdapter(Context context, ArrayList<WangYiNewsBean.ResultBean> news) {
        this.mContext = context;
        this.news = news;

    }

    public void setOnItemClickListerer(onItemClickListener onItemClickListerer) {
        mOnItemClickListerer = onItemClickListerer;
    }

    public void setmOnItemLongClickListener(onItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public ArrayList<WangYiNewsBean.ResultBean> getNews() {
        return news;
    }

    //    //根据channel_id()值return 1 or 2
//    @Override
//    public int getItemViewType(int position) {
//        if ("11".equals( home_arrayList.get( position ).getArticle_channel_id() )) {
//            return 2;
//        } else return 1;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_news, parent, false);
//        View view1 = LayoutInflater.from(mContext).inflate(R.layout.home_fragment_gone_view_item, parent, false);
        return new ViewHolder(view);
//        //根据getItemViewType()return的值显示item，以过滤广告
//        if (viewType == 1) {
//            return new ViewHolder(view);
//        } else {
//            return new ViewHolder(view1);
//        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(news.get(position).getImage())
                .into(holder.newsImage);
        holder.newsTitle.setText(news.get(position).getTitle());
        holder.newsTime.setText(news.get(position).getPasstime());
        holder.itemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListerer.onItemClick(view, position);
            }
        });
        holder.itemParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnItemLongClickListener.onItemLongClick(view, position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return news == null ? 0 : news.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemParent;
        ImageView newsImage;
        TextView newsTitle;
        TextView newsTime;

        public ViewHolder(View itemView) {
            super(itemView);
            itemParent = itemView.findViewById(R.id.item_parent);
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsTime = itemView.findViewById(R.id.news_time);
        }
    }

    public interface onItemClickListener {
        void onItemClick(View view, int i);
    }

    public interface onItemLongClickListener {
        void onItemLongClick(View view, int i);
    }


}
