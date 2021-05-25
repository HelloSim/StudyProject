package com.sim.wangyi.serviceimp;

import androidx.fragment.app.Fragment;

import com.sim.router.inteface.WangyiFragmentService;
import com.sim.wangyi.ui.fragment.WangyiFragment;

/**
 * @ author: Sim
 * @ time： 2021/5/25 10:56
 * @ description：
 */
public class WangyiServiceImp implements WangyiFragmentService {

    @Override
    public Fragment getWangyiFragment() {
        return new WangyiFragment();
    }

}
