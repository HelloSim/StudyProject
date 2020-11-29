package com.sim.traveltool.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.traveltool.R;
import com.sim.traveltool.adapter.BusStationNameAdapter;
import com.sim.traveltool.bean.BusLocationDataBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @Time: 2020/6/10 11:54
 * @Author: HelloSim
 * @Description :位置的搜素页面
 */
public class BusRouteSearchLocationActivity extends BaseActivity {
    private static final String TAG = "Sim_BusRouteSearchLocationActivity";

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_search)
    EditText tvSearch;
    @BindView(R.id.rl_data)
    RecyclerView rlDatal;

    private int searchType;

    private Context context;
    private BusStationNameAdapter stationNameAdapter;

    private ArrayList<BusLocationDataBean.TipsBean> startLocationDataBeanList = new ArrayList<>();
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search_location);
        ButterKnife.bind(this);
        context = this;
        initData();
        initView();
    }

    private void initData() {
        searchType = getIntent().getIntExtra("searchType",1001);
    }

    private void initView() {
        stationNameAdapter = new BusStationNameAdapter(context, startLocationDataBeanList);
        stationNameAdapter.setOnItemClickListerer(new BusStationNameAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent();
                intent.putExtra("name", String.valueOf(startLocationDataBeanList.get(i).getName()));
                setResult(searchType, intent);
                finish();
            }
        });
        rlDatal.setLayoutManager(new LinearLayoutManager(this));
        rlDatal.setAdapter(stationNameAdapter);
        //editext的内容变化监听
        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null || editable.toString().equals("")) {
                    rlDatal.setVisibility(View.GONE);
                } else {
                    //内容变化请求数据
                    getStartLocation(editable.toString());
                }
            }
        });
    }

    /**
     * 搜索位置的网络请求
     *
     * @param keywords
     */
    public void getStartLocation(String keywords) {
        startLocationDataBeanList.clear();
        retrofitUtil.getStartOrEndLocation(new Subscriber<BusLocationDataBean>() {
            @Override
            public void onCompleted() {
                stationNameAdapter.notifyDataSetChanged();
                rlDatal.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Sim", "onError: " + e);
            }

            @Override
            public void onNext(BusLocationDataBean busLocationDataBean) {
                startLocationDataBeanList.addAll(busLocationDataBean.getTips());
            }
        }, keywords);
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
