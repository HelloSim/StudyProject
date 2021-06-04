package com.sim.bus.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.bean.BannerRes;
import com.sim.bus.R;
import com.sim.http.APIFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import rx.Subscriber;

/**
 * 公交Fragment
 */
@Route(path = ArouterUrl.Bus.bus_fragment)
public class BusFragment extends BaseFragment {

    private Banner banner;
    //放图片地址的集合
    private BannerRes bannerRes;
    private ArrayList<String> list_path = new ArrayList<>();

    //导航栏模块
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Fragment busRealTimeFragment, busRouteFragment, busStationFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.bus_fragment_bus;
    }

    @Override
    protected void bindViews(View view) {
        banner = view.findViewById(R.id.banner);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
    }

    @Override
    protected void initView(View view) {
        List<String> titleDatas = new ArrayList<>();
        titleDatas.add("实时公交");
        titleDatas.add("出行路线");
        titleDatas.add("站点查询");

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
        getBanner();
    }

    @Override
    protected void initData() {

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
                    busRealTimeFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.Bus.bus_fragment_realtime).navigation();
                }
                return busRealTimeFragment;
            case 1:
                if (busRouteFragment == null) {
                    busRouteFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.Bus.bus_fragment_route).navigation();
                }
                return busRouteFragment;
            case 2:
                if (busStationFragment == null) {
                    busStationFragment = (Fragment) ARouter.getInstance().build(ArouterUrl.Bus.bus_fragment_station).navigation();
                }
                return busStationFragment;
        }
    }

    private void getBanner() {
        APIFactory.getInstance().getBanner(new Subscriber<BannerRes>() {
            @Override
            public void onCompleted() {
                for (BannerRes.DataBean dataBean : bannerRes.getData()) {
                    list_path.add(dataBean.getImagePath());
                }
                playBanner(banner, list_path);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BannerRes b) {
                bannerRes = b;
            }
        });
    }

    private void playBanner(Banner banner, ArrayList<String> urls) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)//设置内置样式，共有六种
                .setIndicatorGravity(BannerConfig.RIGHT)//设置指示器的位置，小点点，左中右。
                .setBannerAnimation(Transformer.ForegroundToBackground)//设置轮播的动画效果，内含多种特效
                .setDelayTime(5000)//设置轮播间隔时间
                .isAutoPlay(true)//设置是否为自动轮播，默认是“true”
                .setImages(urls)//设置图片网址或地址的集合
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object o, ImageView imageView) {
                        Glide.with(context).load((String) o).into(imageView);
                    }
                })//设置图片加载器
                // banner.setBannerTitles(list_title);//设置轮播图的标题集合
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int i) {
                        ToastUtil.toast(getContext(), "点击了第" + (i + 1) + "个");
                    }
                })//点击监听事件
                .start();
    }

}
