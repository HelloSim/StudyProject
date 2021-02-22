package com.sim.traveltool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.sim.baselibrary.base.BaseAdapter;
import com.sim.baselibrary.base.BaseViewHolder;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.NewsWangYiBean;

import java.util.ArrayList;

/**
 * @author Sim --- 新闻列表界面的RecyclerView适配器
 */
public class NewsAdapter extends BaseAdapter<NewsAdapter.ViewHolder, NewsWangYiBean.NewsBean> {

    private Context mContext;

    public NewsAdapter(Context context, ArrayList<NewsWangYiBean.NewsBean> news) {
        super(news);
        this.mContext = context;
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
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_news, parent, false));
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_news, parent, false);
//        View view1 = LayoutInflater.from(mContext).inflate(R.layout.home_fragment_gone_view_item, parent, false);
//        return new ViewHolder(view);
//        //根据getItemViewType()return的值显示item，以过滤广告
//        if (viewType == 1) {
//            return new ViewHolder(view);
//        } else {
//            return new ViewHolder(view1);
//        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsWangYiBean.NewsBean resultBean = getItem(position);
        Glide.with(mContext)
                .load(resultBean.getImage())
                .into(holder.newsImage);
        holder.newsTitle.setText(resultBean.getTitle());
        holder.newsTime.setText(resultBean.getPasstime());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnItemClickListener() != null)
                    getOnItemClickListener().onItemClicked(holder, position);
            }
        });
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (getOnItemLongClickListener() != null)
                    getOnItemLongClickListener().onItemLongClicked(holder, position);
                return false;
            }
        });
    }

    public class ViewHolder extends BaseViewHolder {

        LinearLayout parent;
        ShapeableImageView newsImage;
        TextView newsTitle;
        TextView newsTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            parent = findViewById(R.id.item_parent);
            newsImage = findViewById(R.id.news_image);
            newsTitle = findViewById(R.id.news_title);
            newsTime = findViewById(R.id.news_time);
        }

    }

}
