package com.sim.apublic.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sim.apublic.R;
import com.sim.basicres.base.BaseAdapter;
import com.sim.basicres.base.BaseViewHolder;
import com.sim.bean.PublicArticleBean;

import java.util.ArrayList;

public class ArticleAdapter extends BaseAdapter<ArticleAdapter.ViewHolder, PublicArticleBean.DataBean.DatasBean> {

    private Context mContext;

    public ArticleAdapter(Context context, ArrayList<PublicArticleBean.DataBean.DatasBean> articleBeanList) {
        super(articleBeanList);
        this.mContext = context;
    }

    @NonNull
    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apublic_recycler_view_item_article, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ViewHolder holder, int position) {
        holder.tvContent.setText(getData().get(position).getTitle());
        holder.tvAuthor.setText(TextUtils.isEmpty(getData().get(position).getAuthor()) ? getData().get(position).getShareUser() : getData().get(position).getAuthor());
        holder.tvChapter.setText(String.format("%s·%s", getData().get(position).getSuperChapterName(), getData().get(position).getChapterName()));
        holder.tvTime.setText(getData().get(position).getNiceDate());
        holder.tvRefresh.setVisibility(getData().get(position).isFresh() ? View.VISIBLE : View.GONE);
        holder.vItem.setOnClickListener(v -> {
            if (getOnItemClickListener() != null)
                getOnItemClickListener().onItemClicked(holder, position);
        });
    }

    public class ViewHolder extends BaseViewHolder {

        private TextView tvChapter;
        private TextView tvTime;
        private TextView tvContent;
        private TextView tvAuthor;
        private TextView tvRefresh;
        private View top;
        private View vItem;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            tvChapter = findViewById(R.id.tvChapter);
            tvTime = findViewById(R.id.tvTime);
            tvContent = findViewById(R.id.tvContent);
            tvAuthor = findViewById(R.id.tvAuthor);
            tvRefresh = findViewById(R.id.tvRefresh);
            top = findViewById(R.id.top);
            vItem = findViewById(R.id.vItem);
        }
    }

}
