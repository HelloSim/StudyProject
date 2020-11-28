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
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.sqlitelibrary.bean.RecordDataBean;
import com.sim.traveltool.R;
import com.sim.traveltool.db.RecoedDataDaoUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Auther Sim
 * @Time 2020/10/22 14:21
 * @Description 月打卡列表页面
 */
public class RecordAllActivity extends BaseActivity {

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
        title.setText(RecoedDataDaoUtil.getInstance().getMonth(this, calendar) + "月" + getString(R.string.record_all));
        recordDataBeanList = RecoedDataDaoUtil.getInstance().queryRecordForMonth(this, calendar);
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
        table.getConfig()
                .setShowXSequence(false)//是否显示顶部序号列
                .setShowYSequence(false)//是否显示左侧序号列
                .setShowTableTitle(false)//是否显示表格标题
                .setTableTitleStyle(new FontStyle(40, Color.WHITE))//设置表格标题字体样式
                .setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
                    @Override
                    public int getBackGroundColor(CellInfo cellInfo) {
                        if (cellInfo.value.equals(getString(R.string.record_no))) {
                            switch (TimeUtil.getWeek(RecoedDataDaoUtil.getInstance().getYearMonth(RecordAllActivity.this, calendar) + "-" + finalDayColum.get(cellInfo.row))) {
                                case "星期六":
                                case "星期日":
                                    return TableConfig.INVALID_COLOR;
                                default:
                                    return Color.WHITE;
                            }
                        } else {
                            if (cellInfo.col == 2) {
                                int hour = Integer.parseInt(cellInfo.value.split(":", 2)[0]);
                                int minute = Integer.parseInt(cellInfo.value.split(":", 2)[1]);
                                if (hour > 9 || (hour == 9 && minute > 30)) {
                                    return TableConfig.INVALID_COLOR;
                                } else {
                                    return Color.WHITE;
                                }
                            } else if (cellInfo.col == 3) {
                                int hour = Integer.parseInt(cellInfo.value.split(":", 2)[0]);
                                int minute = Integer.parseInt(cellInfo.value.split(":", 2)[1]);
                                if (hour < 18 || (hour == 18 && minute < 30)) {
                                    return TableConfig.INVALID_COLOR;
                                } else {
                                    return Color.WHITE;
                                }
                            }
                            return TableConfig.INVALID_COLOR; //返回无效颜色，不会绘制
                        }
                    }
                });

        FontStyle fontStyle = new FontStyle();
        MultiLineBubbleTip<Column> tip = new MultiLineBubbleTip<Column>(this, R.mipmap.round_rect, R.mipmap.triangle, fontStyle) {
            @Override
            public boolean isShowTip(Column column, int position) {
                return true;
            }

            @Override
            public String[] format(Column column, int position) {
                String[] strings = {RecoedDataDaoUtil.getInstance().getYearMonth(RecordAllActivity.this, calendar) + "-" +
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

}
