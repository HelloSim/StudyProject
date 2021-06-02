package com.sim.user.utils;

import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * @ author: Sim
 * @ time： 2021/6/2 10:55
 * @ description：
 */
public class BmobInit {

    public static void init(Context context) {
        Bmob.resetDomain("http://open-vip.bmob.cn/8/");
        Bmob.initialize(context, "62550b32bf5600010781ceeebc0e92ac");
    }

}
