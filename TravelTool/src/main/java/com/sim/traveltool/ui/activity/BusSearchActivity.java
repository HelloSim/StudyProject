package com.sim.traveltool.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.baselibrary.utils.LogUtil;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.adapter.BusLineNameAdapter;
import com.sim.traveltool.adapter.BusStationNameAdapter;
import com.sim.traveltool.bean.BusLocationDataBean;
import com.sim.traveltool.bean.BusRealTimeLineDataBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @Time: 2020/12/10 11:00
 * @Author: HelloSim
 * @Description :实时公交、出行线路站点的搜索页面
 */
public class BusSearchActivity extends BaseActivity {
    private static final String TAG = "Sim_BusSearchActivity";

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_search)
    EditText tvSearch;
    @BindView(R.id.tv_not_found)
    TextView tvNotFound;
    @BindView(R.id.rl_data)
    RecyclerView rlDatal;

    private int searchType;

    private Context context;
    private BusStationNameAdapter stationNameAdapter;
    private ArrayList<BusLocationDataBean.TipsBean> startLocationDataBeanList = new ArrayList<>();

    private ArrayList<BusRealTimeLineDataBean.DataBean> lineListByLineNameBeanList = new ArrayList<>();
    private BusLineNameAdapter busLineNameAdapter;
    private boolean hasResult = false;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search);
        ButterKnife.bind(this);
        context = this;
        initData();
        initView();
    }

    private void initData() {
        searchType = getIntent().getIntExtra("searchType",AppHelper.RESULT_BUS);
    }

    private void initView() {
        if (searchType == AppHelper.RESULT_BUS) {
            busLineNameAdapter = new BusLineNameAdapter(context, lineListByLineNameBeanList);
            busLineNameAdapter.setOnItemClickListerer(new BusLineNameAdapter.onItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    Intent intent = new Intent(context, BusRealTimeActivity.class);
                    intent.putExtra("busName", lineListByLineNameBeanList.get(i).getName());
                    intent.putExtra("lineId", lineListByLineNameBeanList.get(i).getId());
                    intent.putExtra("fromStation", lineListByLineNameBeanList.get(i).getFromStation());
                    intent.putExtra("toStation", lineListByLineNameBeanList.get(i).getToStation());
                    intent.putExtra("beginTime", lineListByLineNameBeanList.get(i).getBeginTime());
                    intent.putExtra("endTime", lineListByLineNameBeanList.get(i).getEndTime());
                    intent.putExtra("price", lineListByLineNameBeanList.get(i).getPrice());
                    startActivity(intent);
                }
            });
            rlDatal.setLayoutManager(new LinearLayoutManager(this));
            rlDatal.setAdapter(busLineNameAdapter);
        } else {
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
        }
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
                if (searchType == AppHelper.RESULT_BUS) {
                    if (editable == null || editable.toString().equals("")) {
                        tvNotFound.setVisibility(View.GONE);
                        rlDatal.setVisibility(View.GONE);
                    } else {
                        if (lineListByLineNameBeanList != null) {
                            lineListByLineNameBeanList.clear();
                        }
                        getLineListByLineName(editable.toString());
                    }
                }else {
                    if (editable == null || editable.toString().equals("")) {
                        rlDatal.setVisibility(View.GONE);
                    } else {
                        //内容变化请求数据
                        getStartLocation(editable.toString());
                    }
                }
            }
        });
    }

    /**
     * 搜索实时公交路线的网络请求
     *
     * @param key
     */
    private void getLineListByLineName(String key) {
        //这里做请求
        retrofitUtil.getLineListByLineName(new Subscriber<BusRealTimeLineDataBean>() {
            @Override
            public void onCompleted() {
                if (!hasResult) {
                    rlDatal.setVisibility(View.GONE);
                    tvNotFound.setVisibility(View.VISIBLE);
                } else {
                    if (lineListByLineNameBeanList == null) {
                        rlDatal.setVisibility(View.GONE);
                        tvNotFound.setVisibility(View.VISIBLE);
                    } else {
                        tvNotFound.setVisibility(View.GONE);
                        rlDatal.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(BusRealTimeLineDataBean data) {
                if (data.getFlag() == 1002) {
                    hasResult = true;
                    lineListByLineNameBeanList.addAll(data.getData());
                    busLineNameAdapter.notifyDataSetChanged();
                } else if (data.getFlag() == 1004) {
                    hasResult = false;
                }
            }
        }, "GetLineListByLineName", key, String.valueOf(System.currentTimeMillis()));
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
                LogUtil.d(TAG, "位置请求出错: " + e);
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
