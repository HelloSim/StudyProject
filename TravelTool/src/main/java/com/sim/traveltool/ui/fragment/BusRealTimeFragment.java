package com.sim.traveltool.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.ui.activity.BusSearchActivity;

/**
 * @Auther Sim
 * @Time 2020/12/16 11:01
 * @Description 实时公交fragment
 */
public class BusRealTimeFragment extends Fragment {
    private static final String TAG = "Sim_BusRealTimeFragment";

    private TextView tvSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_real_time, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvSearch = view.findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), BusSearchActivity.class).putExtra("searchType", AppHelper.RESULT_BUS));
            }
        });
    }

}
