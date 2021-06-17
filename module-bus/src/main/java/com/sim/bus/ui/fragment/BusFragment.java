package com.sim.bus.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.ToastUtil;
import com.sim.bean.BannerBean;
import com.sim.bus.R;
import com.sim.bus.adapter.ColorFlipPagerTitleView;
import com.sim.http.APIFactory;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Subscriber;

/**
 * 公交Fragment
 */
@Route(path = ArouterUrl.Bus.bus_fragment)
public class BusFragment extends BaseFragment {

    //轮播图模块
    private Banner banner;
    private BannerBean bannerRes;//放图片地址的集合
    private ArrayList<String> list_path = new ArrayList<>();

    //导航栏模块
    private MagicIndicator magicIndicator;
    private ViewPager mViewPager;
    private static final String[] CHANNELS = new String[]{"实时公交", "出行路线", "站点查询"};
    private List<String> titleDatas = Arrays.asList(CHANNELS);

    private Fragment busRealTimeFragment, busRouteFragment, busStationFragment;

    @Override
    protected int getLayoutRes() {
        return R.layout.bus_fragment_bus;
    }

    @Override
    protected void bindViews(View view) {
        banner = view.findViewById(R.id.banner);
        magicIndicator = view.findViewById(R.id.magic_indicator);
        mViewPager = view.findViewById(R.id.view_pager);
    }

    @Override
    protected void initView(View view) {
        getBanner();
        initMagicIndicator();
    }

    @Override
    protected void initData() {

    }

    private void getBanner() {
        APIFactory.getInstance().getBanner(new Subscriber<BannerBean>() {
            @Override
            public void onCompleted() {
                for (BannerBean.DataBean dataBean : bannerRes.getData()) {
                    list_path.add(dataBean.getImagePath());
                }
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)//设置内置样式，共有六种
                        .setIndicatorGravity(BannerConfig.RIGHT)//设置指示器的位置，小点点，左中右。
                        .setBannerAnimation(Transformer.ForegroundToBackground)//设置轮播的动画效果，内含多种特效
                        .setDelayTime(5000)//设置轮播间隔时间
                        .isAutoPlay(true)//设置是否为自动轮播，默认是“true”
                        .setImages(list_path)//设置图片网址或地址的集合
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

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BannerBean b) {
                bannerRes = b;
            }
        });
    }

    private void initMagicIndicator() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
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

            @Override
            public int getCount() {
                return titleDatas == null ? 0 : titleDatas.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleDatas.get(position);
            }
        });

        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleDatas == null ? 0 : titleDatas.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(titleDatas.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#9e9e9e"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#40c4ff"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float lineHeight = 5;
                indicator.setLineHeight(lineHeight);
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

}
