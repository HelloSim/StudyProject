package com.sim.traveltool.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.traveltool.R;
import com.sim.traveltool.adapter.NewsAdapter;
import com.sim.traveltool.bean.WangYiNewsBean;
import com.sim.traveltool.utils.SPUtil;
import com.sim.traveltool.utils.ScreenUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 网易新闻的收藏页面
 * Created by Sim on 2020/4/30
 */
public class NewsCollectActivity extends BaseActivity {

    private Context context;

    @BindView(R.id.parent)
    LinearLayout parent;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.recycle_view)
    RecyclerView newsRecyclerView;

    private String fileName = "collect";
    private Map<String, WangYiNewsBean.ResultBean> newsMap;
    private ArrayList<WangYiNewsBean.ResultBean> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    private PopupWindow deletePopupWindow;//弹窗
    private View deleteLayout;//布局
    private int deletePupDPWidth = 240;//宽度，单位DP
    private int deletePupDPHeight = 120;//高度，单位DP
    private Button btn_collect_cancel;
    private Button btn_collect_confirm;

    private int position;//长按的item

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_collect);
        ButterKnife.bind(this);
        context = this;
        initDate();
        initView();
    }

    private void initDate() {
        Gson gson = new Gson();
        newsMap = (Map<String, WangYiNewsBean.ResultBean>) SPUtil.getAll(context, fileName);
        Iterator it = newsMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            newsList.add(gson.fromJson(String.valueOf(newsMap.get(key)), WangYiNewsBean.ResultBean.class));
        }
    }

    private void initView() {
        newsAdapter = new NewsAdapter(context, newsList);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        newsAdapter.setOnItemClickListerer(new NewsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        deleteLayout = inflater.inflate(R.layout.popup_delete_collect, null);
        deletePopupWindow = new PopupWindow(context);
        deletePopupWindow.setContentView(deleteLayout);//设置主体布局
        deletePopupWindow.setWidth(ScreenUtils.dip2px(context, deletePupDPWidth));//宽度
        deletePopupWindow.setHeight(ScreenUtils.dip2px(context, deletePupDPHeight));//高度
        deletePopupWindow.setFocusable(true);
        deletePopupWindow.setBackgroundDrawable(new BitmapDrawable());//设置空白背景
        deletePopupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);//动画
        btn_collect_cancel = deleteLayout.findViewById(R.id.btn_collect_cancel);
        btn_collect_confirm = deleteLayout.findViewById(R.id.btn_collect_confirm);
        btn_collect_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePopupWindow.dismiss();
            }
        });
        btn_collect_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtil.remove(context, fileName, newsList.get(position).getTitle());
                newsList.remove(position);
                newsAdapter.notifyDataSetChanged();
                deletePopupWindow.dismiss();
            }
        });
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
