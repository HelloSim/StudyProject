package com.sim.user.serviceimp;

import android.content.Context;
import android.view.View;

import com.sim.router.inteface.UserViewService;
import com.sim.user.ui.view.UserView;

/**
 * @ author: Sim
 * @ time： 2021/5/24 17:30
 * @ description：
 */
public class UserServiceImp implements UserViewService {

    @Override
    public View getUserView(Context context) {
        return new UserView(context);
    }

}
