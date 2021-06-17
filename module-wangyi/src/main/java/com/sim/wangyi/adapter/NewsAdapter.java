package com.sim.wangyi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.sim.basicres.base.BaseAdapter;
import com.sim.basicres.base.BaseViewHolder;
import com.sim.basicres.utils.ToastUtil;
import com.sim.bean.WangyiBean;
import com.sim.user.bean.NewsBean;
import com.sim.user.bean.User;
import com.sim.user.utils.CallBack;
import com.sim.wangyi.R;

import java.util.ArrayList;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class NewsAdapter extends BaseAdapter<NewsAdapter.ViewHolder, WangyiBean.NewsBean> {

    private Context mContext;

    public NewsAdapter(Context context, ArrayList<WangyiBean.NewsBean> news) {
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
                .inflate(R.layout.wangyi_recycler_view_item_news, parent, false));
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
        WangyiBean.NewsBean resultBean = getItem(position);
        Glide.with(mContext)
                .load(resultBean.getImage())
                .into(holder.newsImage);
        holder.newsTitle.setText(resultBean.getTitle());
        holder.newsTime.setText(resultBean.getPasstime());
        holder.collect.setImageResource(R.mipmap.common_ic_collect_gray);
        NewsBean.Util.getNewsBean(resultBean.getTitle(), new CallBack() {
            @Override
            public void success(Object... values) {
                holder.collect.setImageResource(R.mipmap.common_ic_collect_red);
            }

            @Override
            public void fail(String values) {
            }
        });

        holder.collect.setOnClickListener(v -> {
            if (!User.isLogin()){
                ToastUtil.toast(mContext,"未登录！");
                return;
            }
            if ((holder.collect.getDrawable().getCurrent().getConstantState())
                    .equals(ContextCompat.getDrawable(mContext, R.mipmap.common_ic_collect_red).getConstantState())) {
                NewsBean.Util.deleteNewsBean(getData().get(position).getTitle(), new CallBack() {
                    @Override
                    public void success(Object... values) {
                        holder.collect.setImageResource(R.mipmap.common_ic_collect_gray);
                    }

                    @Override
                    public void fail(String values) {
                        ToastUtil.toast(mContext, "取消收藏失败：" + values);
                    }
                });
            } else {
                NewsBean newsBean = new NewsBean(resultBean.getPath(), resultBean.getImage(), resultBean.getTitle(), resultBean.getPasstime());
                newsBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            holder.collect.setImageResource(R.mipmap.common_ic_collect_red);
                        } else {
                            ToastUtil.toast(mContext, "收藏失败：" + e.getMessage());

                        }
                    }
                });
            }
        });
        holder.parent.setOnClickListener(v -> {
            if (getOnItemClickListener() != null)
                getOnItemClickListener().onItemClicked(holder, position);
        });
    }

    public class ViewHolder extends BaseViewHolder {
        LinearLayout parent;
        ShapeableImageView newsImage;
        TextView newsTitle;
        TextView newsTime;
        ImageView collect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            parent = findViewById(R.id.item_parent);
            newsImage = findViewById(R.id.news_image);
            newsTitle = findViewById(R.id.news_title);
            newsTime = findViewById(R.id.news_time);
            collect = findViewById(R.id.iv_collect);
        }
    }

}
