package com.sim.bus.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.tabs.TabLayout;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.bus.R;
import com.sim.bus.adapter.LoopViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Sim --- “首页”Fragment
 */
@Route(path = ArouterUrl.bus_fragment)
public class BusFragment extends BaseFragment {

    //轮播图模块
    private ViewPager LoopViewPager;
    private int[] mImg;
    private int[] mImg_id;
    private String[] mDec;
    private ArrayList<ImageView> mImgList;
    private LinearLayout ll_dots_container;
    private TextView loop_dec;
    private int previousSelectedPosition = 0;
    boolean isRunning = false;

    //导航栏模块
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titleDatas;

    private Fragment busRealTimeFragment, busRouteFragment, busStationFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.bus_fragment_bus;
    }

    @Override
    protected void bindViews(View view) {
        LoopViewPager = view.findViewById(R.id.loopviewpager);
        ll_dots_container = view.findViewById(R.id.ll_dots_loop);
        loop_dec = view.findViewById(R.id.loop_dec);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

        titleDatas = new ArrayList<>();
        titleDatas.add("实时公交");
        titleDatas.add("出行路线");
        titleDatas.add("站点查询");
    }

    @Override
    protected void initView(View view) {
        initLoopView();

        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return Objects.requireNonNull(showFragment(position));
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
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData() {

    }

    /**
     * 导航轮播图设置
     */
    private void initLoopView() {
        // 图片资源id数组
        mImg = new int[]{R.mipmap.common_photo1, R.mipmap.common_photo2, R.mipmap.common_photo3, R.mipmap.common_photo4, R.mipmap.common_photo5};
        // 文本描述
        mDec = new String[]{"", "", "", "", ""};
        //
        mImg_id = new int[]{R.id.bus_pager_img1, R.id.bus_pager_img2, R.id.bus_pager_img3, R.id.bus_pager_img4, R.id.bus_pager_img5};
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
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if (id == R.id.bus_pager_img1) {
                        ToastUtil.toast(getContext(), "1被点击！");
                    } else if (id == R.id.bus_pager_img2) {
                        ToastUtil.toast(getContext(), "2被点击！");
                    } else if (id == R.id.bus_pager_img3) {
                        ToastUtil.toast(getContext(), "3被点击！");
                    } else if (id == R.id.bus_pager_img4) {
                        ToastUtil.toast(getContext(), "4被点击！");
                    } else if (id == R.id.bus_pager_img5) {
                        ToastUtil.toast(getContext(), "5被点击！");
                    }
                }
            });
            mImgList.add(imageView);
            //加引导点
            dotView = new View(getActivity());
            dotView.setBackgroundResource(R.drawable.common_dot);
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
        LoopViewPager.setAdapter(new LoopViewAdapter(mImgList));//设置适配器
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % mImgList.size();
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        LoopViewPager.setCurrentItem(currentPosition);

        LoopViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                            LoopViewPager.setCurrentItem(LoopViewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 隐藏所有的fragment再显示需要的fragment
     *
     * @param type 0:实时公交fragment     1：出行路线fragment    2：站点查询fragment
     */
    private Fragment showFragment(int type) {
        switch (type) {
            default:
                if (busRealTimeFragment == null) {
//                    busRealTimeFragment = new BusRealTimeFragment();
                    busRealTimeFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.bus_fragment_realtime).navigation();
                }
                return busRealTimeFragment;
            case 1:
                if (busRouteFragment == null) {
//                    busRouteFragment = new BusRouteFragment();
                    busRouteFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.bus_fragment_route).navigation();
                }
                return busRouteFragment;
            case 2:
                if (busStationFragment == null) {
//                    busStationFragment = new BusStationFragment();
                    busStationFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.bus_fragment_station).navigation();
                }
                return busStationFragment;
        }
    }

}