package com.sim.traveltool.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.ui.activity.BusRouteActivity;
import com.sim.traveltool.ui.activity.BusSearchActivity;

/**
 * @Auther Sim
 * @Time 2020/12/16 11:09
 * @Description 出行路线fragment
 */
public class BusRouteFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "Sim_BusRouteFragment";

    private TextView tvStartStation;
    private TextView tvEndStation;
    private Button btnRoute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_route, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvStartStation = view.findViewById(R.id.tv_start_station);
        tvEndStation = view.findViewById(R.id.tv_end_station);
        btnRoute = view.findViewById(R.id.btn_route);
        tvStartStation.setOnClickListener(this);
        tvEndStation.setOnClickListener(this);
        btnRoute.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_station:
                startActivityForResult(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", AppHelper.RESULT_START_STATION), AppHelper.RESULT_START_STATION);
                break;
            case R.id.tv_end_station:
                startActivityForResult(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", AppHelper.RESULT_END_STATION), AppHelper.RESULT_END_STATION);
                break;
            case R.id.btn_route:
                if (tvStartStation.getText().length() > 0 && tvEndStation.getText().length() > 0) {
                    Intent intent = new Intent(getActivity(), BusRouteActivity.class);
                    intent.putExtra("tvStartStation", tvStartStation.getText().toString());
                    intent.putExtra("tvEndStation", tvEndStation.getText().toString());
                    startActivity(intent);
                }
                break;
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
