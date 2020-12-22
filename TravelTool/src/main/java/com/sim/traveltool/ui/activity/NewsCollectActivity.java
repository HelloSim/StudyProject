package com.sim.traveltool.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.ScreenUtil;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.NewsAdapter;
import com.sim.traveltool.base.AppActivity;
import com.sim.traveltool.bean.NewsWangYiBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Auther Sim
 * @Time 2020/4/30 1:05
 * @Description 网易新闻的收藏页面
 */
public class NewsCollectActivity extends AppActivity {
    private static final String TAG = "Sim_NewsCollectActivity";

    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.recycle_view)
    RecyclerView newsRecyclerView;

    private String fileName = "collect";
    private Map<String, NewsWangYiBean.ResultBean> newsMap;
    private ArrayList<NewsWangYiBean.ResultBean> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    private PopupWindow deletePopupWindow;//弹窗
    private View deleteLayout;//布局
    private int deletePupDPWidth = 240;//宽度，单位DP
    private int deletePupDPHeight = 120;//高度，单位DP
    private Button btn_collect_cancel;
    private Button btn_collect_confirm;

    private int position;//长按的item

    @Override
    protected int getContentViewId() {
        return R.layout.activity_news_collect;
    }

    protected void initData() {
        Gson gson = new Gson();
        newsMap = (Map<String, NewsWangYiBean.ResultBean>) SPUtil.getAll(this, fileName);
        Iterator it = newsMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            newsList.add(gson.fromJson(String.valueOf(newsMap.get(key)), NewsWangYiBean.ResultBean.class));
        }
    }

    protected void initView() {
        newsAdapter = new NewsAdapter(this, newsList);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(NewsCollectActivity.this));
        newsAdapter.setOnItemClickListerer(new NewsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(NewsCollectActivity.this, NewsDetailActivity.class);
                intent.putExtra("news", (Serializable) newsAdapter.getNews().get(i));
                startActivity(intent);
            }
        });
        newsAdapter.setmOnItemLongClickListener(new NewsAdapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int i) {
                position = i;
                deletePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            }
        });
        newsRecyclerView.setAdapter(newsAdapter);

        LayoutInflater inflater = (LayoutInflater) NewsCollectActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        deleteLayout = inflater.inflate(R.layout.view_popup_delete_collect, null);
        deletePopupWindow = new PopupWindow(NewsCollectActivity.this);
        deletePopupWindow.setContentView(deleteLayout);//设置主体布局
        deletePopupWindow.setWidth(ScreenUtil.dip2px(NewsCollectActivity.this, deletePupDPWidth));//宽度
        deletePopupWindow.setHeight(ScreenUtil.dip2px(NewsCollectActivity.this, deletePupDPHeight));//高度
        deletePopupWindow.setFocusable(true);
        deletePopupWindow.setBackgroundDrawable(new BitmapDrawable());//设置空白背景
        deletePopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);//动画
        btn_collect_cancel = deleteLayout.findViewById(R.id.btn_collect_cancel);
        btn_collect_confirm = deleteLayout.findViewById(R.id.btn_collect_confirm);
        btn_collect_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimeUtil.isFastClick()) {
                    deletePopupWindow.dismiss();
                }
            }
        });
        btn_collect_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimeUtil.isFastClick()) {
                    SPUtil.remove(NewsCollectActivity.this, fileName, newsList.get(position).getTitle());
                    newsList.remove(position);
                    newsAdapter.notifyDataSetChanged();
                    deletePopupWindow.dismiss();
                }
            }
        });
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        if (TimeUtil.isFastClick()) {
            switch (view.getId()) {
                case R.id.back:
                    finish();
                    break;
            }
        }
    }

}
