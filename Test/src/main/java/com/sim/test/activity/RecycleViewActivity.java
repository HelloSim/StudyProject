package com.sim.test.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;
import com.sim.test.adapter.RecycleViewAdapter;
import com.sim.test.bean.RecycleViewBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecycleView实现瀑布流列表
 */

public class RecycleViewActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<RecycleViewBean> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        ButterKnife.bind(this);

        //使用瀑布流布局,第一个参数 spanCount 列数,第二个参数 orentation 排列方向
        StaggeredGridLayoutManager recyclerViewLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

//        //线性布局Manager
//        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
//        recyclerViewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

//        //网络布局Manager
//        GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(this, 3);

        //给recyclerView设置LayoutManager
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        initData();

        RecycleViewAdapter adapter = new RecycleViewAdapter(data, this);

        //设置adapter
        recyclerView.setAdapter(adapter);

    }

    /**
     * 生成一些数据添加到集合中
     */
    private void initData() {
        data.add(new RecycleViewBean("photo1", R.mipmap.recycler_one));
        data.add(new RecycleViewBean("photo2", R.mipmap.recycler_two));
        data.add(new RecycleViewBean("photo3", R.mipmap.recycler_three));
        data.add(new RecycleViewBean("photo4", R.mipmap.recycler_four));
        data.add(new RecycleViewBean("photo5", R.mipmap.recycler_fives));
        data.add(new RecycleViewBean("photo6", R.mipmap.recycler_six));
        data.add(new RecycleViewBean("photo7", R.mipmap.recycler_seven));
        data.add(new RecycleViewBean("photo8", R.mipmap.recycler_eight));
        data.add(new RecycleViewBean("photo9", R.mipmap.recycler_nine));
        data.add(new RecycleViewBean("photo10", R.mipmap.recycler_ten));
    }

}