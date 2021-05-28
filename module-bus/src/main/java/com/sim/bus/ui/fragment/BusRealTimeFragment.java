package com.sim.bus.ui.fragment;

import android.view.View;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.bus.R;

/**
 * @author Sim --- 实时公交fragment
 */
@Route(path = ArouterUrl.bus_fragment_realtime)
public class BusRealTimeFragment extends BaseFragment {

    private EditText etSearch;

    @Override
    protected int getLayoutRes() {
        return R.layout.bus_fragment_real_time;
    }

    @Override
    protected void bindViews(View view) {
        etSearch = view.findViewById(R.id.et_search);
        setViewClick(etSearch);
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == etSearch) {
            ARouter.getInstance().build(ArouterUrl.bus_activity_search).withInt("searchType", 1000).navigation();
        } else {
            super.onMultiClick(view);
        }
    }

}
