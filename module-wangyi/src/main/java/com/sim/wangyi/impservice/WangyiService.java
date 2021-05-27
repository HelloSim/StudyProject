package com.sim.wangyi.impservice;

import androidx.fragment.app.Fragment;

import com.sim.wangyi.ui.fragment.WangyiFragment;

/**
 * @ author: Sim
 * @ time： 2021/5/26 16:23
 * @ description：
 */
public class WangyiService implements com.sim.sharedlibrary.WangyiService {

    @Override
    public Fragment getWangyiFragment() {
        WangyiFragment fragment = new WangyiFragment();
        return fragment;
    }

}
