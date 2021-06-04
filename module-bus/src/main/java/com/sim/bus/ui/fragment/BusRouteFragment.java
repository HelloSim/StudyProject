package com.sim.bus.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.bus.R;

/**
 * 出行路线fragment
 */
@Route(path = ArouterUrl.Bus.bus_fragment_route)
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
//            startActivityForResult(new Intent(getActivity(), BusSearchActivity.class).putExtra(), );
            ARouter.getInstance().build(ArouterUrl.Bus.bus_activity_search)
                    .withInt("searchType", RESULT_START_STATION)
                    .navigation(getActivity(), RESULT_START_STATION);
        } else if (view == etEndStation) {
            ARouter.getInstance().build(ArouterUrl.Bus.bus_activity_search)
                    .withInt("searchType", RESULT_END_STATION)
                    .navigation(getActivity(), RESULT_END_STATION);
        } else if (view == btnRoute) {
            if (etStartStation.getText().length() > 0 && etEndStation.getText().length() > 0) {
                ARouter.getInstance().build(ArouterUrl.Bus.bus_activity_route)
                        .withString("tvStartStation", etStartStation.getText().toString())
                        .withString("tvEndStation", etEndStation.getText().toString())
                        .navigation();
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
