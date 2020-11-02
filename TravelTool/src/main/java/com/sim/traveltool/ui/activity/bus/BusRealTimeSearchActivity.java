package com.sim.traveltool.ui.activity.bus;

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

import com.sim.traveltool.R;
import com.sim.traveltool.adapter.bus.BusLineNameAdapter;
import com.sim.traveltool.bean.bus.BusRealTimeLineDataBean;
import com.sim.traveltool.ui.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * @Time: 2020/6/9 14:48
 * @Author: HelloSim
 * @Description :实时公交的搜索页面
 */
public class BusRealTimeSearchActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_search)
    EditText tvSearch;
    @BindView(R.id.tv_not_found)
    TextView tvNotFound;
    @BindView(R.id.rl_data)
    RecyclerView rlDatal;

    private Context context;
    private ArrayList<BusRealTimeLineDataBean.DataBean> lineListByLineNameBeanList = new ArrayList<>();
    private BusLineNameAdapter busLineNameAdapter;
    private boolean hasResult = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_real_time_search);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
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
                    tvNotFound.setVisibility(View.GONE);
                    rlDatal.setVisibility(View.GONE);
                } else {
                    if (lineListByLineNameBeanList != null) {
                        lineListByLineNameBeanList.clear();
                    }
                    getLineListByLineName(editable.toString());
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
        retrofitUtil2.getLineListByLineName(new Subscriber<BusRealTimeLineDataBean>() {
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

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
