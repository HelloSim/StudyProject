package com.sim.bus.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.sim.bus.R;
import com.sim.bus.ui.activity.BusRouteActivity;
import com.sim.bus.ui.activity.BusSearchActivity;
import com.sim.common.base.BaseFragment;

/**
 * @author Sim --- 出行路线fragment
 */
public class BusRouteFragment extends BaseFragment {

    private EditText etStartStation, etEndStation;
    private Button btnRoute;

    public static final int RESULT_START_STATION = 10001;//跳转出行路线起点位置搜索
    public static final int RESULT_END_STATION = 1002;//跳转出行路线终点位置搜索

    @Override
    protected int getLayoutRes() {
        return R.layout.bus_fragment_route;
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
            startActivityForResult(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", RESULT_START_STATION), RESULT_START_STATION);
        } else if (view == etEndStation) {
            startActivityForResult(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", RESULT_END_STATION), RESULT_END_STATION);
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
        if (requestCode == RESULT_START_STATION) {
            if ((data != null)) {
                etStartStation.setText(data.getStringExtra("name"));
            }
        } else if (requestCode == RESULT_END_STATION) {
            if ((data != null)) {
                etEndStation.setText(data.getStringExtra("name"));
            }
        }
    }

}
