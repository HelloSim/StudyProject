package com.sim.traveltool.ui.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sim.baselibrary.base.BaseFragment;
import com.sim.traveltool.R;

/**
 * @Auther Sim
 * @Time 2020/12/16 11:09
 * @Description 站点查询fragment
 */
public class BusStationFragment extends BaseFragment {

    private TextView tvStation;
    private Button btnStation;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bus_station;
    }

    @Override
    protected void bindViews(View view) {
        tvStation = view.findViewById(R.id.tv_station);
        btnStation = view.findViewById(R.id.btn_station);
    }

    @Override
    protected void initView(View view) {
        setViewClick(tvStation, btnStation);
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
