package com.sim.traveltool.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.sim.baselibrary.base.BaseFragment;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.ui.activity.BusRouteActivity;
import com.sim.traveltool.ui.activity.BusSearchActivity;

/**
 * @author Sim --- 出行路线fragment
 */
public class BusRouteFragment extends BaseFragment {

    private EditText etStartStation, etEndStation;
    private Button btnRoute;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bus_route;
    }

    @Override
    protected void bindViews(View view) {
        etStartStation = view.findViewById(R.id.et_start_station);
        etEndStation = view.findViewById(R.id.et_end_station);
        btnRoute = view.findViewById(R.id.btn_route);
        setViewClick(etStartStation, etEndStation, btnRoute);
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == etStartStation) {
            startActivityForResult(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", AppHelper.RESULT_START_STATION), AppHelper.RESULT_START_STATION);
        } else if (view == etEndStation) {
            startActivityForResult(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", AppHelper.RESULT_END_STATION), AppHelper.RESULT_END_STATION);
        } else if (view == btnRoute) {
            if (etStartStation.getText().length() > 0 && etEndStation.getText().length() > 0) {
                Intent intent = new Intent(getActivity(), BusRouteActivity.class);
                intent.putExtra("tvStartStation", etStartStation.getText().toString());
                intent.putExtra("tvEndStation", etEndStation.getText().toString());
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
                etStartStation.setText(data.getStringExtra("name"));
            }
        } else if (requestCode == AppHelper.RESULT_END_STATION) {
            if ((data != null)) {
                etEndStation.setText(data.getStringExtra("name"));
            }
        }
    }

}
