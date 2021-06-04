package com.sim.mine.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.base.BaseAdapter;
import com.sim.basicres.base.BaseViewHolder;
import com.sim.basicres.callback.DialogInterface;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
import com.sim.mine.adapter.NewsAdapter;
import com.sim.mine.R;
import com.sim.user.bean.NewsBean;
import com.sim.user.bean.User;
import com.sim.user.utils.CallBack;

import java.util.ArrayList;
import java.util.List;


@Route(path = ArouterUrl.Mine.user_activity_collect)
public class UserCollectActivity extends BaseActivity {

    private Context context;

    private TitleView titleView;
    private RecyclerView newsRecyclerView;

    private ArrayList<NewsBean> collectionNewsBeanArrayList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.mine_activity_news_collect;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        newsRecyclerView = findViewById(R.id.recycle_view);
        titleView.setLeftClickListener(new TitleView.LeftClickListener() {
            @Override
            public void onClick(View leftView) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        context = this;
        if (User.isLogin()) {
            NewsBean.Util.getNewsBean(new CallBack() {
                @Override
                public void success(Object... values) {
                    if (values != null) {
                        collectionNewsBeanArrayList.addAll((List<NewsBean>) values[0]);
                        newsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void fail(String values) {
                    ToastUtil.toast(context, "获取收藏失败：" + values);
                }
            });
        } else {
            finish();
        }
    }

    @Override
    protected void initView() {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        newsAdapter = new NewsAdapter(this, collectionNewsBeanArrayList);
        newsAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(BaseViewHolder holder, int position) {
                ARouter.getInstance()
                        .build(ArouterUrl.Web.web_activity)
                        .withSerializable("webUrl", newsAdapter.getData().get(position).getPath())
                        .navigation();
            }
        });
        newsAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClicked(BaseViewHolder holder, int position) {
                showDialog(null, "取消收藏", "确认", "取消", new DialogInterface() {
                    @Override
                    public void sureOnClick() {
                        NewsBean.Util.deleteNewsBean(collectionNewsBeanArrayList.get(position).getTitle(), new CallBack() {
                            @Override
                            public void success(Object... values) {
                                collectionNewsBeanArrayList.remove(position);
                                newsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void fail(String values) {
                                ToastUtil.toast(context, "删除收藏失败：" + values);
                            }
                        });
                    }

                    @Override
                    public void cancelOnClick() {
                    }
                });
            }
        });
        newsRecyclerView.setAdapter(newsAdapter);
    }
}
