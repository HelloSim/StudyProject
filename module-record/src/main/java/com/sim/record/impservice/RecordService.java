package com.sim.record.impservice;

import androidx.fragment.app.Fragment;

import com.sim.record.ui.fragment.RecordFragment;

/**
 * @ author: Sim
 * @ time： 2021/5/26 16:23
 * @ description：
 */
public class RecordService implements com.sim.sharedlibrary.moduleservice.RecordService {

    @Override
    public Fragment getRecordFragment() {
        RecordFragment fragment = new RecordFragment();
        return fragment;
    }

}
