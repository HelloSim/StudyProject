package com.sim.bus.serviceimp;

import androidx.fragment.app.Fragment;

import com.sim.bus.ui.fragment.BusFragment;
import com.sim.router.inteface.BusFragmentService;

/**
 * @ author: Sim
 * @ time： 2021/5/25 10:56
 * @ description：
 */
public class WangyiServiceImp implements BusFragmentService {

    @Override
    public Fragment getBusFragment() {
        return new BusFragment();
    }

}
