package com.sim.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sim.test.R;
import com.sim.test.bean.SmzdmDataBean;

import java.util.ArrayList;

/**
 * Created by Sim on 2019/10/22
 */
public class SmzdmDatasAdapter extends RecyclerView.Adapter<SmzdmDatasAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SmzdmDataBean.DataBean.RowsBean> list;

    public SmzdmDatasAdapter(Context context, ArrayList<SmzdmDataBean.DataBean.RowsBean> list) {
        this.mContext = context;
        this.list = list;
    }

    /**
     * 根据channel_id()值return 1 or 2
     *
     * @param position
     * @return
     */
    public int getItemViewType(int position) {
        if ("11".equals(list.get(position).getArticle_channel_id())) {
            return 2;
        } else return 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recly_view_smzdm_view_item, viewGroup, false);
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.recly_view_smzdm_gone_item, viewGroup, false);
        //根据getItemViewType()return的值显示item，以过滤广告
        if (i == 1) {
            return new ViewHolder(view);
        } else {
            return new ViewHolder(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!"11".equals( list.get( position ).getArticle_channel_id() )) {
            Glide.with( mContext ).load( list.get( position ).getArticle_pic() ).into( holder.article_pic );
            holder.article_title.setText( list.get( position ).getArticle_title() );
            holder.article_tags.setText( list.get( position ).getArticle_tag() );
            holder.article_price.setText( list.get( position ).getArticle_price() );
            holder.article_mall.setText( list.get( position ).getArticle_mall() );
            holder.article_format_date.setText( list.get( position ).getArticle_format_date() );
            holder.article_comment.setText( "评论" + list.get( position ).getArticle_comment() );
            //文章没有"值，有可能出错，放进try
            try {
                double w = Double.parseDouble( list.get( position ).getArticle_worthy() );
                double unw = Double.parseDouble( list.get( position ).getArticle_unworthy() );
                holder.article_value.setText( "值" + (int) Math.ceil( w / (w + unw) * 100 ) + "%" );
            } catch ( Exception e ) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView article_pic;
        TextView article_title;
        TextView article_tags;
        TextView article_price;
        TextView article_mall;
        TextView article_format_date;
        TextView article_comment;
        TextView article_value;
        LinearLayout holder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            article_pic = itemView.findViewById( R.id.article_pic );
            article_title = itemView.findViewById( R.id.article_title );
            article_tags = itemView.findViewById( R.id.article_tags );
            article_price = itemView.findViewById( R.id.article_price );
            article_mall = itemView.findViewById( R.id.article_mall );
            article_format_date = itemView.findViewById( R.id.article_format_date );
            article_comment = itemView.findViewById( R.id.article_comment );
            article_value = itemView.findViewById( R.id.article_value );
            holder = itemView.findViewById( R.id.holder );
        }
    }

}
