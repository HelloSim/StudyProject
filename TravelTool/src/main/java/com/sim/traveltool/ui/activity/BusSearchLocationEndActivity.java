package com.sim.traveltool.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.traveltool.R;
import com.sim.traveltool.adapter.BusStationNameAdapter;
import com.sim.traveltool.bean.BusLocationDataBean;
import com.sim.traveltool.ui.activity.BaseActivity;
import com.sim.baselibrary.utils.HttpUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Time: 2020/6/10 11:54
 * @Author: HelloSim
 * @Description :终点位置的搜素页面
 */
public class BusSearchLocationEndActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_search)
    EditText tvSearch;
    @BindView(R.id.rl_data)
    RecyclerView rlDatal;

    private final int RESULT_END_STATION = 1002;

    private Context context;
    private BusStationNameAdapter stationNameAdapter;

    private ArrayList<BusLocationDataBean.TipsBean> startLocationDataBeanList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search_location);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        stationNameAdapter = new BusStationNameAdapter(context, startLocationDataBeanList);
        stationNameAdapter.setOnItemClickListerer(new BusStationNameAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent();
                intent.putExtra("name", String.valueOf(startLocationDataBeanList.get(i).getName()));
                setResult(RESULT_END_STATION, intent);
                finish();
            }
        });
        rlDatal.setLayoutManager(new LinearLayoutManager(this));
        rlDatal.setAdapter(stationNameAdapter);
        //监听EditText内容变化进行网络请求
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
                    //这里请求数据
                    getStartLocation(editable.toString());
                }
            }
        });
    }

    /**
     * 位置搜索的网络请求
     *
     * @param keywords
     */
    public void getStartLocation(String keywords) {
        startLocationDataBeanList.clear();
        try {
            String apiurl = null;
            apiurl = "http://restapi.amap.com/v3/assistant/inputtips?" +
                    "s=rsv3" +
                    "&key=ceb54024fae4694f734b1006e8dc8324" +
                    "&city=0756" +
                    "&citylimit=false" +
                    "&callback=" +
                    "&platform=JS" +
                    "&logversion=2.0" +
                    "&sdkversion=1.3" +
                    "&appname=http://www.zhbuswx.com/busline/BusQuery.html?v=2.01#/" +
                    "&csid=759CACE2-2197-4E0A-ADCB-1456B16775DA" +
                    "&keywords=" + keywords;
            HttpUtil.doGetAsyn(apiurl, new HttpUtil.CallBack() {
                @Override
                public void onRequestComplete(String result) {
                    BusLocationDataBean locationDataBean = stringToBean(result, BusLocationDataBean.class);
                    startLocationDataBeanList.addAll(locationDataBean.getTips());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stationNameAdapter.notifyDataSetChanged();
                            rlDatal.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onRequestError(String result) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转成bean
     */
    public static <T> T stringToBean(String gsonString, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
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
