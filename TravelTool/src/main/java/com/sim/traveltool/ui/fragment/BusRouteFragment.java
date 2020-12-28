package com.sim.traveltool.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sim.baselibrary.base.BaseFragment;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.ui.activity.BusRouteActivity;
import com.sim.traveltool.ui.activity.BusSearchActivity;

/**
 * @Auther Sim
 * @Time 2020/12/16 11:09
 * @Description 出行路线fragment
 */
public class BusRouteFragment extends BaseFragment {

    private TextView tvStartStation;
    private TextView tvEndStation;
    private Button btnRoute;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bus_route;
    }

    @Override
    protected void bindViews(View view) {
        tvStartStation = view.findViewById(R.id.tv_start_station);
        tvEndStation = view.findViewById(R.id.tv_end_station);
        btnRoute = view.findViewById(R.id.btn_route);
    }

    @Override
    protected void initView(View view) {
        setViewClick(tvStartStation, tvEndStation, btnRoute);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == tvStartStation) {
            startActivityForResult(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", AppHelper.RESULT_START_STATION), AppHelper.RESULT_START_STATION);
        } else if (view == tvEndStation) {
            startActivityForResult(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", AppHelper.RESULT_END_STATION), AppHelper.RESULT_END_STATION);
        } else if (view == btnRoute) {
            if (tvStartStation.getText().length() > 0 && tvEndStation.getText().length() > 0) {
                Intent intent = new Intent(getActivity(), BusRouteActivity.class);
                intent.putExtra("tvStartStation", tvStartStation.getText().toString());
                intent.putExtra("tvEndStation", tvEndStation.getText().toString());
                startActivity(intent);
            }
        } else {
            super.onMultiClick(view);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppHelper.RESULT_START_STATION) {
            if ((data != null)) {
                tvStartStation.setText(data.getStringExtra("name"));
            }
        } else if (requestCode == AppHelper.RESULT_END_STATION) {
            if ((data != null)) {
                tvEndStation.setText(data.getStringExtra("name"));
            }
        }
    }

}
