package com.sim.bus.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.bus.R;

/**
 * 站点查询fragment
 */
@Route(path = ArouterUrl.Bus.bus_fragment_station)
public class BusStationFragment extends BaseFragment {

    private EditText tvStation;
    private Button btnStation;

    @Override
    protected int getLayoutRes() {
        return R.layout.bus_fragment_station;
    }

    @Override
    protected void bindViews(View view) {
        tvStation = view.findViewById(R.id.tv_station);
        btnStation = view.findViewById(R.id.btn_station);
        setViewClick(tvStation, btnStation);
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == tvStation) {

        } else if (view == btnStation) {

        } else {
            super.onMultiClick(view);
        }
    }

}
