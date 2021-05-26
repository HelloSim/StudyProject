package com.sim.bus.impservice;

import androidx.fragment.app.Fragment;

import com.sim.bus.ui.fragment.BusFragment;


/**
 * @ author: Sim
 * @ time： 2021/5/26 16:23
 * @ description：
 */
public class BusService implements com.sim.sharedlibrary.BusService {

    @Override
    public Fragment getBusFragment() {
        BusFragment fragment = new BusFragment();
        return fragment;
    }

}
