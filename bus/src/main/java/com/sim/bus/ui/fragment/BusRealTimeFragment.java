package com.sim.bus.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.sim.common.base.BaseFragment;
import com.sim.common.AppHelper;
import com.sim.bus.R;
import com.sim.bus.ui.activity.BusSearchActivity;

/**
 * @author Sim --- 实时公交fragment
 */
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
            startActivity(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", AppHelper.RESULT_BUS));
        } else {
            super.onMultiClick(view);
        }
    }

}
