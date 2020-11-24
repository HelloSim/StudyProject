package com.sim.traveltool.ui.activity.record;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sim.traveltool.R;
import com.sim.traveltool.adapter.record.RecordAdapter;
import com.sim.traveltool.greendao.DaKaRecordDaoUtil;
import com.sim.traveltool.bean.record.DaKaRecord;
import com.sim.traveltool.ui.activity.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Auther Sim
 * @Time 2020/10/22 14:21
 * @Description 月打卡列表页面
 */
public class AllRecordActivity extends BaseActivity {

    @BindView(R.id.tv_now_year_and_month)
    TextView tv_now_year_and_month;
    @BindView(R.id.rv_data)
    RecyclerView rv_data;

    private String yearAndMonth;
    private String year;
    private String month;
    private List<DaKaRecord> daKaRecordList;
    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_record);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        yearAndMonth = getIntent().getStringExtra("yearAndMonth");
        year = yearAndMonth.split("-")[0];
        month = yearAndMonth.split("-")[1];
        daKaRecordList = DaKaRecordDaoUtil.queryRecordForMonth(year, month);
        recordAdapter = new RecordAdapter(this, daKaRecordList);
    }

    private void initView() {
        tv_now_year_and_month.setText(yearAndMonth + getString(R.string.all_record));

        rv_data.setLayoutManager(new LinearLayoutManager(this));
        rv_data.setAdapter(recordAdapter);
    }

}
