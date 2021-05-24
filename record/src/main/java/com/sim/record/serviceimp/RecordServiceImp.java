package com.sim.record.serviceimp;

import androidx.fragment.app.Fragment;

import com.sim.record.ui.fragment.RecordFragment;
import com.sim.router.inteface.RecordFragmentService;

/**
 * @ author: Sim
 * @ time： 2021/5/24 17:30
 * @ description：
 */
public class RecordServiceImp implements RecordFragmentService {

    @Override
    public Fragment getRecordFragment() {
        return new RecordFragment();
    }

}
