package com.sim.traveltool.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.tip.MultiLineBubbleTip;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.utils.DensityUtils;
import com.haibin.calendarview.Calendar;
import com.sim.sqlitelibrary.bean.RecordDataBean;
import com.sim.traveltool.R;
import com.sim.traveltool.db.RecordDataDaoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Auther Sim
 * @Time 2020/10/22 14:21
 * @Description 月打卡列表页面
 */
public class RecordAllActivity extends BaseActivity {
    private static final String TAG = "Sim_RecordAllActivity";

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.table_data)
    SmartTable<RecordDataBean> table;
    @BindView(R.id.no_record)
    TextView no_record;

    private Calendar calendar;
    private List<RecordDataBean> recordDataBeanList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_all);
        ButterKnife.bind(this);
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 18)); //设置全局字体大小
        initData();
    }

    private void initData() {
        calendar = (Calendar) getIntent().getSerializableExtra("calendar");
        title.setText(RecordDataDaoUtil.getInstance().getMonth(this, calendar) + "月" + getString(R.string.record_all));
        recordDataBeanList = RecordDataDaoUtil.getInstance().queryRecordForMonth(this, calendar);
        if (recordDataBeanList != null && recordDataBeanList.size() > 0) {
            table.setData(recordDataBeanList);
            initView();
        } else {
            table.setVisibility(View.GONE);
            no_record.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        List<String> dayColum = null;
        for (Column column : table.getTableData().getColumns()) {
            if (column.getColumnName().equals("日期")) {
                dayColum = column.getDatas();
            }
        }
        List<String> finalDayColum = dayColum;
        table.setZoom(true, 2, 1);//设置放大最大和最小值
        ArrayList list = new ArrayList();
        table.getConfig()
                .setShowXSequence(false)//是否显示顶部序号列
                .setShowYSequence(false)//是否显示左侧序号列
                .setShowTableTitle(false)//是否显示表格标题
                .setTableTitleStyle(new FontStyle(40, Color.WHITE))//设置表格标题字体样式
                .setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
                    @Override
                    public int getBackGroundColor(CellInfo cellInfo) {
                        switch (cellInfo.value) {
                            case "星期一":
                            case "星期二":
                            case "星期三":
                            case "星期四":
                            case "星期五":
                                list.add(cellInfo.row);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            if (cellInfo.row == (int) list.get(i)) {
                                return TableConfig.INVALID_COLOR;
                            }
                        }
                        return Color.GRAY;
                    }
                });

        FontStyle fontStyle = new FontStyle();
        MultiLineBubbleTip<Column> tip = new MultiLineBubbleTip<Column>(this, R.mipmap.ic_round_rect, R.mipmap.ic_triangle, fontStyle) {
            @Override
            public boolean isShowTip(Column column, int position) {
                return true;
            }

            @Override
            public String[] format(Column column, int position) {
                String[] strings = {RecordDataDaoUtil.getInstance().getYearMonth(RecordAllActivity.this, calendar) + "-" +
                        table.getTableData().getColumns().get(0).getDatas().get(position) + ":",
                        String.valueOf(column.getDatas().get(position))};
                return strings;
            }
        };
        tip.setColorFilter(Color.parseColor("#FA8072"));
        tip.setAlpha(0.8f);
        table.getProvider().setTip(tip);//批注
//        table.getTableData().setOnRowClickListener(new TableData.OnRowClickListener<DaKaRecord>() {
//            @Override
//            public void onClick(Column column, DaKaRecord daKaRecord, int col, int row) {
//                switch (col) {
//                    case 0:
//                        ToastUtil.T_Info(RecordAllActivity.this, daKaRecord.getDay());
//                        break;
//                    case 1:
//                        ToastUtil.T_Info(RecordAllActivity.this, daKaRecord.getWeek());
//                        break;
//                    case 2:
//                        ToastUtil.T_Info(RecordAllActivity.this, daKaRecord.getStartTime());
//                        break;
//                    case 3:
//                        ToastUtil.T_Info(RecordAllActivity.this, daKaRecord.getEndTime());
//                        break;
//                    case 4:
//                        ToastUtil.T_Info(RecordAllActivity.this, daKaRecord.getOther());
//                        break;
//                }
//            }
//        });//点击事件
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
