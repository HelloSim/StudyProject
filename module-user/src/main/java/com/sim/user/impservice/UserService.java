package com.sim.user.impservice;

import android.content.Context;
import android.view.View;

import com.sim.user.ui.view.UserView;

/**
 * @ author: Sim
 * @ time： 2021/5/26 16:23
 * @ description：
 */
public class UserService implements com.sim.sharedlibrary.UserService {

    @Override
    public View getUserView(Context context) {
        return new UserView(context);
    }

}
