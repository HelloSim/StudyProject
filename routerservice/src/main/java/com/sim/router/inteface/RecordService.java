package com.sim.router.inteface;

import android.app.Activity;
import android.app.Fragment;

/**
 * @ author: Sim
 * @ time： 2021/5/24 14:22
 * @ description：
 */
public class RecordService {

    interface RecordActivityService {
        Activity getRecordActivity();
    }

    interface RecordFragmentService {
        Fragment getRecordFragment();
    }

}
