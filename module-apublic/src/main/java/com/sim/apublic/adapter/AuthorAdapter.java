package com.sim.apublic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sim.apublic.R;
import com.sim.basicres.base.BaseAdapter;
import com.sim.basicres.base.BaseViewHolder;
import com.sim.bean.PublicAuthorBean;

import java.util.ArrayList;

public class AuthorAdapter extends BaseAdapter<AuthorAdapter.ViewHolder, PublicAuthorBean.DataBean> {

    private Context mContext;

    public AuthorAdapter(Context context, ArrayList<PublicAuthorBean.DataBean> authorList) {
        super(authorList);
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apublic_recycler_view_item_author, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.ViewHolder holder, int position) {
        holder.authorName.setText("# " + getData().get(position).getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnItemClickListener() != null)
                    getOnItemClickListener().onItemClicked(holder, position);
            }
        });
    }

    public class ViewHolder extends BaseViewHolder {

        private LinearLayout item;
        private TextView authorName;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void bindViews() {
            item = findViewById(R.id.item);
            authorName = findViewById(R.id.author_name);
        }
    }

}
