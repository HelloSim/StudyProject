package com.sim.traveltool.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sim.baselibrary.base.BaseFragment;
import com.sim.traveltool.R;

/**
 * @Auther Sim
 * @Time 2020/12/16 11:09
 * @Description 站点查询fragment
 */
public class BusStationFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvStation;
    private Button btnStation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_station, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvStation = view.findViewById(R.id.tv_station);
        btnStation = view.findViewById(R.id.btn_station);
        tvStation.setOnClickListener(this);
        btnStation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_station:
            case R.id.btn_station:
                break;
        }
    }
}
