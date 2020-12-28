package com.sim.traveltool.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sim.baselibrary.base.BaseFragment;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.ui.activity.BusSearchActivity;

/**
 * @Auther Sim
 * @Time 2020/12/16 11:01
 * @Description 实时公交fragment
 */
public class BusRealTimeFragment extends BaseFragment {

    private TextView tvSearch;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bus_real_time;
    }

    @Override
    protected void bindViews(View view) {
        tvSearch = view.findViewById(R.id.tv_search);
        setViewClick(tvSearch);
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == tvSearch) {
//            startActivity(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", AppHelper.RESULT_BUS));
        } else {
            super.onMultiClick(view);
        }
    }

}
