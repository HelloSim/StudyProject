package com.sim.apublic.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.apublic.R;
import com.sim.apublic.adapter.ArticleAdapter;
import com.sim.apublic.adapter.AuthorAdapter;
import com.sim.basicres.base.BaseAdapter;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.base.BaseViewHolder;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
import com.sim.bean.PublicArticleBean;
import com.sim.bean.PublicAuthorBean;
import com.sim.http.APIFactory;

import java.util.ArrayList;

import rx.Subscriber;

@Route(path = ArouterUrl.Public.public_fragment)
public class PublicArticleFragment extends BaseFragment {

    private LinearLayout parent;

    private TitleView title_view;
    private SwipeRefreshLayout refresh;
    private RecyclerView articleRecycleView;

    private ArrayList<PublicAuthorBean.DataBean> authorBeanList = new ArrayList<>();//公众号列表
    private PublicAuthorBean.DataBean selectAuthor;//选中的公众号
    private ArrayList<String> authorList = new ArrayList<>();//公众号列表，只有名字

    private ArrayList<PublicArticleBean.DataBean.DatasBean> articleBeanList = new ArrayList<>();//文章列表
    private ArticleAdapter articleAdapter;

    private int pages = 1;

    private PopupWindow morePopupWindow;//弹窗
    private View moreLayout;//布局
    private RecyclerView moreRecyclerView;
    private AuthorAdapter authorAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.apublic_fragmrnt_article;
    }

    @Override
    protected void bindViews(View view) {
        parent = view.findViewById(R.id.parent);
        title_view = view.findViewById(R.id.title_view);
        refresh = view.findViewById(R.id.refresh);
        articleRecycleView = view.findViewById(R.id.article_recycle_view);
    }

    @Override
    protected void initView(View view) {
        initDialogData();
        initRacyclerView();
        initRefresh();

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        moreLayout = inflater.inflate(R.layout.apublic_view_popup_author_more, null);
        morePopupWindow = showPopupWindow(moreLayout, 350, 500);
        moreRecyclerView = moreLayout.findViewById(R.id.author_recycle_view);

        moreRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        authorAdapter = new AuthorAdapter(getActivity(), authorBeanList);
        moreRecyclerView.setFocusable(true);
        authorAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(BaseViewHolder holder, int position) {
                morePopupWindow.dismiss();
                selectAuthor = authorBeanList.get(position);
                title_view.setTitleTextView(selectAuthor.getName());
                refresh.setRefreshing(true);
                articleBeanList.clear();
                pages = 1;
                getPublicArticle(String.valueOf(selectAuthor.getId()), String.valueOf(pages));
            }
        });
        moreRecyclerView.setAdapter(authorAdapter);

        title_view.setRightClickListener(new TitleView.RightClickListener() {
            @Override
            public void onClick(View rightView) {
                morePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            }
        });

    }

    @Override
    protected void initData() {

    }

    /**
     * 初始化dialog，获取公众号列表
     */
    private void initDialogData() {
        //获取公众号列表
        APIFactory.getInstance().getPublicAuthor(new Subscriber<PublicAuthorBean>() {
            @Override
            public void onCompleted() {
                for (PublicAuthorBean.DataBean dataBean : authorBeanList) {
                    authorList.add("# " + dataBean.getName());
                }
                selectAuthor = authorBeanList.get(0);
                title_view.setTitleTextView(selectAuthor.getName());
                getPublicArticle(String.valueOf(selectAuthor.getId()), String.valueOf(pages));
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.toast(getContext(), "获取公众号失败：" + e.getMessage());
            }

            @Override
            public void onNext(PublicAuthorBean publicAuthorBean) {
                authorBeanList.clear();
                authorBeanList.addAll(publicAuthorBean.getData());
            }
        });
    }

    /**
     * 初始化 Refresh 刷新控件
     */
    private void initRefresh() {
        refresh.setColorSchemeColors(Color.BLUE, Color.RED);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articleBeanList.clear();
                pages = 1;
                getPublicArticle(String.valueOf(selectAuthor.getId()), String.valueOf(pages));
            }
        });
    }

    /**
     * 初始化RecyclerView
     */
    private void initRacyclerView() {
        articleRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        articleAdapter = new ArticleAdapter(getActivity(), articleBeanList);
        articleRecycleView.setFocusable(true);
        articleAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(BaseViewHolder holder, int position) {
                ARouter.getInstance()
                        .build(ArouterUrl.Web.web_activity)
                        .withSerializable("webUrl", articleAdapter.getData().get(position).getLink())
                        .navigation();
            }
        });
        articleRecycleView.setAdapter(articleAdapter);

        articleRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {//RecyclerView最后一个item继续向下滑动
            boolean isSlidingToLast = false;//用来标记是否正在向最后一个滑动，既是否向下滑动

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();//获取最后一个完全显示的ItemPosition
                    int totalItemCount = manager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {// 判断是否滚动到底部
                        getPublicArticle(String.valueOf(selectAuthor.getId()), String.valueOf(pages));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向:大于0表示，正在向下滚动；小于等于0 表示停止或向上滚动
                isSlidingToLast = dy > 0;
            }
        });
    }

    /**
     * 获取文章列表
     *
     * @param id
     * @param page
     */
    private void getPublicArticle(String id, String page) {
        APIFactory.getInstance().setWanAndroidInterceptorParameter(id, page);
        APIFactory.getInstance().getPublicArticle(new Subscriber<PublicArticleBean>() {
            @Override
            public void onCompleted() {
                refresh.setRefreshing(false);
                articleAdapter.notifyDataSetChanged();
                pages++;
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(PublicArticleBean publicArticleBean) {
                articleBeanList.addAll(publicArticleBean.getData().getDatas());

            }
        });
    }

}
