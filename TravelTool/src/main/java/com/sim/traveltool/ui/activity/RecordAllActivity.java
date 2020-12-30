package com.sim.traveltool.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.tip.MultiLineBubbleTip;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.utils.DensityUtils;
import com.haibin.calendarview.Calendar;
import com.sim.baselibrary.base.BaseActivity;
import com.sim.sqlitelibrary.bean.RecordDataBean;
import com.sim.traveltool.R;
import com.sim.traveltool.db.RecordDataDaoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther Sim
 * @Time 2020/10/22 14:21
 * @Description 月打卡列表页面
 */
public class RecordAllActivity extends BaseActivity {

    ImageView back;
    TextView title;
    SmartTable<RecordDataBean> table;
    TextView no_record;

    private Calendar calendar;
    private List<RecordDataBean> recordDataBeanList;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_record_all;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        table = findViewById(R.id.table_data);
        no_record = findViewById(R.id.no_record);
        setViewClick(back);
    }

    @Override
    protected void initView() {
        List<String> dayColum = null;//日期列
        ArrayList list = new ArrayList();//日期列的周六日内容格
        for (Column column : table.getTableData().getColumns()) {
            if (column.getColumnName().equals("日期")) {
                dayColum = column.getDatas();
            }
        }
        table.setZoom(false);//设置不可缩放
//        table.setZoom(true, 2f, 0.5f);//设置缩放最大和最小值
        table.getConfig()
                .setShowXSequence(false)//是否显示顶部序号列
                .setShowYSequence(false)//是否显示左侧序号列
                .setShowTableTitle(false)//是否显示表格标题
//                .setTableTitleStyle(new FontStyle(RecordAllActivity.this, 18, Color.WHITE))//设置表格标题字体样式
                .setCountStyle(new FontStyle(RecordAllActivity.this, 18, Color.WHITE))//设置统计行样式
                .setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
                    @Override
                    public int getBackGroundColor(CellInfo cellInfo) {
                        switch (cellInfo.value) {
                            case "星期六":
                            case "星期日":
                                list.add(cellInfo.row);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            if (cellInfo.row == (int) list.get(i)) {
                                return Color.parseColor("#DCDCDC");
                            }
                        }
                        return Color.parseColor("#FFFFFF");
                    }
                });

        FontStyle fontStyle = new FontStyle(RecordAllActivity.this, 12, Color.parseColor("#636363"));
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

    @Override
    protected void initData() {
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 18)); //设置全局字体大小
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

    @Override
    public void onMultiClick(View view) {
        if (view == back) {
            finish();
        } else {
            super.onMultiClick(view);
        }
    }

}
