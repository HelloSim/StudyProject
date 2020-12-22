package com.sim.traveltool.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sim.baselibrary.base.BaseFragment;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.LoopViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Auther Sim
 * @Time 2020/4/25 1:05
 * @Description “首页”Fragment
 */
public class BusFragment extends BaseFragment {
    private static final String TAG = "Sim_BusFragment";

    //轮播图模块
    private ViewPager viewPager;
    private int[] mImg;
    private int[] mImg_id;
    private String[] mDec;
    private ArrayList<ImageView> mImgList;
    private LinearLayout ll_dots_container;
    private TextView loop_dec;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;

    private TabLayout tab_layout;
    private ViewPager view_pager;
    private List<String> titleDatas;

    private BusRealTimeFragment busRealTimeFragment;
    private BusRouteFragment busRouteFragment;
    private BusStationFragment busStationFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus, container, false);
        initData();
        initView(view);
        initLoopView();
        return view;
    }

    private void initData() {
        titleDatas = new ArrayList<>();
        titleDatas.add(getContext().getString(R.string.BusRealTime));
        titleDatas.add(getContext().getString(R.string.BusRoute));
        titleDatas.add(getContext().getString(R.string.BusStation));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {
        viewPager = view.findViewById(R.id.loopviewpager);
        ll_dots_container = view.findViewById(R.id.ll_dots_loop);
        loop_dec = view.findViewById(R.id.loop_dec);

        tab_layout = view.findViewById(R.id.tab_layout);
        view_pager = view.findViewById(R.id.view_pager);

        view_pager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return Objects.requireNonNull(hideAllFragment(position));
            }

            @Override
            public int getCount() {
                return titleDatas.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleDatas.get(position);
            }
        });
        tab_layout.setupWithViewPager(view_pager);
    }

    /**
     * 隐藏所有的fragment再显示需要的fragment
     *
     * @param type 0:实时公交fragment     1：出行路线fragment    2：站点查询fragment
     */
    private Fragment hideAllFragment(int type) {
        switch (type) {
            case 0:
                if (busRealTimeFragment == null) {
                    busRealTimeFragment = new BusRealTimeFragment();
                }
                return busRealTimeFragment;
            case 1:
                if (busRouteFragment == null) {
                    busRouteFragment = new BusRouteFragment();
                }
                return busRouteFragment;
            case 2:
                if (busStationFragment == null) {
                    busStationFragment = new BusStationFragment();
                }
                return busStationFragment;
        }
        return null;
    }

    /**
     * 导航轮播图设置
     */
    private void initLoopView() {
        // 图片资源id数组
        mImg = new int[]{R.mipmap.photo1, R.mipmap.photo2, R.mipmap.photo3, R.mipmap.photo4, R.mipmap.photo5};
        // 文本描述
        mDec = new String[]{"", "", "", "", ""};
        //
        mImg_id = new int[]{R.id.pager_img1, R.id.pager_img2, R.id.pager_img3, R.id.pager_img4, R.id.pager_img5};
        // 初始化要展示的5个ImageView
        mImgList = new ArrayList<ImageView>();
        ImageView imageView;
        View dotView;
        LinearLayout.LayoutParams layoutParams;
        for (int i = 0; i < mImg.length; i++) {
            //初始化要显示的图片对象
            imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(mImg[i]);
            imageView.setId(mImg_id[i]);
            imageView.setOnClickListener(new pagerOnClickListener(getContext()));
            mImgList.add(imageView);
            //加引导点
            dotView = new View(getActivity());
            dotView.setBackgroundResource(R.drawable.dot);
            layoutParams = new LinearLayout.LayoutParams(10, 10);
            if (i != 0) {
                layoutParams.leftMargin = 10;
            }
            dotView.setEnabled(false);//设置默认所有都不可用
            ll_dots_container.addView(dotView, layoutParams);
        }

        ll_dots_container.getChildAt(0).setEnabled(true);
        loop_dec.setText(mDec[0]);
        previousSelectedPosition = 0;
        viewPager.setAdapter(new LoopViewAdapter(mImgList));//设置适配器
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImgList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        viewPager.setCurrentItem(currentPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int newPosition = i % mImgList.size();
                loop_dec.setText(mDec[newPosition]);
                ll_dots_container.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_dots_container.getChildAt(newPosition).setEnabled(true);
                previousSelectedPosition = newPosition;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        // 开启轮询
        new Thread() {
            public void run() {
                isRunning = true;
                while (isRunning) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //下一条
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }.start();
    }

    public class pagerOnClickListener implements View.OnClickListener {

        Context mContext;

        public pagerOnClickListener(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pager_img1:
                    Toast.makeText(mContext, "1被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_img2:
                    Toast.makeText(mContext, "2被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_img3:
                    Toast.makeText(mContext, "3被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_img4:
                    Toast.makeText(mContext, "4被点击", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_img5:
                    Toast.makeText(mContext, "5被点击", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
