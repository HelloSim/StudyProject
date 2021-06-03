package com.sim.mine.utils;

import android.content.Context;

import cn.bmob.v3.Bmob;

/**
 * 这里注意：实现前一定要调用这个初始化。否则会崩
 */
public class BmobInit {

    public static void init(Context context) {
        Bmob.resetDomain("http://open-vip.bmob.cn/8/");
        Bmob.initialize(context, "62550b32bf5600010781ceeebc0e92ac");
    }

}
