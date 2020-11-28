package com.sim.traveltool.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.sim.traveltool.R;
import com.sim.traveltool.adapter.LoopViewAdapter;
import com.sim.traveltool.ui.activity.BusSearchLocationEndActivity;
import com.sim.traveltool.ui.activity.BusRealTimeSearchActivity;
import com.sim.traveltool.ui.activity.BusRouteActivity;
import com.sim.traveltool.ui.activity.BusSearchLocationStartActivity;

import java.util.ArrayList;

/**
 * @Auther Sim
 * @Time 2020/4/25 1:05
 * @Description “首页”Fragment（“实时公交搜索”Fragment）
 */
public class BusFragment extends Fragment implements View.OnClickListener {

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


    //搜索功能模块
    private LinearLayout llRealTime;
    private LinearLayout llStopSign;
    private LinearLayout llRoute;
    private ImageView ivRealTime;
    private ImageView ivStopSign;
    private ImageView ivRoute;

    private LinearLayout llRealTimeContent;
    private TextView tvSearch;

    private LinearLayout llRouteContent;
    private TextView tvStartStation;
    private TextView tvEndStation;
    private Button btnRoute;

    private LinearLayout llStationContent;
    private TextView tvStation;
    private Button btnStation;


    private final int RESULT_START_STATION = 1001;
    private final int RESULT_END_STATION = 1002;
    private String origin;
    private String destination;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus, container, false);
        initView(view);
        initLoopView();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View view) {
        viewPager = view.findViewById(R.id.loopviewpager);
        ll_dots_container = view.findViewById(R.id.ll_dots_loop);
        loop_dec = view.findViewById(R.id.loop_dec);

        llRealTime = view.findViewById(R.id.ll_real_time);
        llStopSign = view.findViewById(R.id.ll_stop_sign);
        llRoute = view.findViewById(R.id.ll_route);
        ivRealTime = view.findViewById(R.id.iv_real_time);
        ivStopSign = view.findViewById(R.id.iv_stop_sign);
        ivRoute = view.findViewById(R.id.iv_route);
        llRealTime.setOnClickListener(this);
        llStopSign.setOnClickListener(this);
        llRoute.setOnClickListener(this);

        llRealTimeContent = view.findViewById(R.id.ll_real_time_content);
        tvSearch = view.findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(this);

        llRouteContent = view.findViewById(R.id.ll_route_content);
        tvStartStation = view.findViewById(R.id.tv_start_station);
        tvEndStation = view.findViewById(R.id.tv_end_station);
        btnRoute = view.findViewById(R.id.btn_route);
        tvStartStation.setOnClickListener(this);
        tvEndStation.setOnClickListener(this);
        btnRoute.setOnClickListener(this);

        llStationContent = view.findViewById(R.id.ll_station_content);
        tvStation = view.findViewById(R.id.tv_station);
        btnStation = view.findViewById(R.id.btn_station);
        tvStation.setOnClickListener(this);
        btnStation.setOnClickListener(this);

        //手指左右滑动的切换（实时公交搜索）
        llRealTimeContent.setOnTouchListener(new View.OnTouchListener() {
            float a = 0;
            float b = 0;

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        a = e.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        b = e.getX();
                        if (a > b && a - b > 200) {
                            llRoute.performClick();
                        } else {
                        }
                        break;
                }
                return true;
            }
        });

        //手指左右滑动的切换（路线搜索）
        llRouteContent.setOnTouchListener(new View.OnTouchListener() {
            float a = 0;
            float b = 0;

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        a = e.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        b = e.getX();
                        if (a < b && b - a > 200) {
                            llRealTime.performClick();
                        } else if (a > b && a - b > 200) {
                            llStopSign.performClick();
                        }
                        break;
                }
                return true;
            }
        });

        //手指左右滑动的切换（站点搜索）
        llStationContent.setOnTouchListener(new View.OnTouchListener() {
            float a = 0;
            float b = 0;

            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        a = e.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        b = e.getX();
                        if (a < b && b - a > 200) {
                            llRoute.performClick();
                        } else {
                        }
                        break;
                }
                return true;
            }
        });

        llRealTime.performClick();
    }

    private void initLoopView() {
        // 图片资源id数组
        mImg = new int[]{R.mipmap.photo1, R.mipmap.photo2, R.mipmap.photo3, R.mipmap.photo4, R.mipmap.photo5};
        // 文本描述
        mDec = new String[]{"神·艾尼路的自然系响雷果实", "人物志--乔巴", "唐吉诃德·多弗朗明哥", "红发香克斯实力", "特拉法尔加·罗"};
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_real_time:
                ivStopSign.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_stop_sign_gray));
                llStationContent.setVisibility(View.GONE);
                ivRoute.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_route_gray));
                llRouteContent.setVisibility(View.GONE);
                ivRealTime.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_real_time_blue));
                llRealTimeContent.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_route:
                ivRealTime.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_real_time_gray));
                llRealTimeContent.setVisibility(View.GONE);
                ivStopSign.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_stop_sign_gray));
                llStationContent.setVisibility(View.GONE);
                ivRoute.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_route_blue));
                llRouteContent.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_stop_sign:
                ivRealTime.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_real_time_gray));
                llRealTimeContent.setVisibility(View.GONE);
                ivRoute.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_route_gray));
                llRouteContent.setVisibility(View.GONE);
                ivStopSign.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bus_stop_sign_blue));
                llStationContent.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_search:
                startActivity(new Intent(getActivity(), BusRealTimeSearchActivity.class));
                break;
            case R.id.tv_start_station:
                startActivityForResult(new Intent(getActivity(), BusSearchLocationStartActivity.class), RESULT_START_STATION);
                break;
            case R.id.tv_end_station:
                startActivityForResult(new Intent(getActivity(), BusSearchLocationEndActivity.class), RESULT_END_STATION);
                break;
            case R.id.btn_route:
                if (tvStartStation.getText().length() > 0 && tvEndStation.getText().length() > 0) {
                    Intent intent = new Intent(getActivity(), BusRouteActivity.class);
                    intent.putExtra("tvStartStation", tvStartStation.getText().toString());
                    intent.putExtra("tvEndStation", tvEndStation.getText().toString());
                    startActivity(intent);
                }
                break;
            case R.id.tv_station:
            case R.id.btn_station:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_START_STATION) {
            if ((data != null)) {
                tvStartStation.setText(data.getStringExtra("name"));
            }
        } else if (requestCode == RESULT_END_STATION) {
            if ((data != null)) {
                tvEndStation.setText(data.getStringExtra("name"));
            }
        }
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
