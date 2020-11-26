package com.sim.traveltool.ui.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.tip.MultiLineBubbleTip;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.utils.DensityUtils;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.bean.UserInfo;
import com.sim.traveltool.db.DaKaRecordDaoUtil;
import com.sim.sqlitelibrary.bean.DaKaRecord;
import com.sim.traveltool.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Auther Sim
 * @Time 2020/10/22 14:21
 * @Description 月打卡列表页面
 */
public class RecordAllActivity extends BaseActivity {

    //    @BindView(R.id.tv_now_year_and_month)
//    TextView tv_now_year_and_month;
//    @BindView(R.id.rv_data)
//    RecyclerView rv_data;
    @BindView(R.id.table_data)
    SmartTable<DaKaRecord> table;

    private String yearAndMonth;
    private String year;
    private String month;
    private List<DaKaRecord> daKaRecordList;
//    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_all);
        ButterKnife.bind(this);
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 18)); //设置全局字体大小
        initData();
        initView();
    }

    private void initData() {
        yearAndMonth = getIntent().getStringExtra("yearAndMonth");
        year = yearAndMonth.split("-")[0];
        month = yearAndMonth.split("-")[1];
        daKaRecordList = DaKaRecordDaoUtil.getInstance().queryRecordForMonth(this, year, month);
//        recordAdapter = new RecordAdapter(this, daKaRecordList);

        table.setData(daKaRecordList);
//        table.setZoom(true, 2, 1);//设置放大最大和最小值
        table.getConfig().setShowXSequence(false);//是否显示顶部序号列
        table.getConfig().setShowYSequence(false);//是否显示左侧序号列
        table.getConfig().setTableTitleStyle(new FontStyle(40, Color.WHITE));//设置表格标题字体样式
        table.getConfig().setContentCellBackgroundFormat(new ICellBackgroundFormat<CellInfo>() {
            @Override
            public void drawBackground(Canvas canvas, Rect rect, CellInfo cellInfo, Paint paint) {
                if (cellInfo.row % 2 == 0) {
                    paint.setColor(Color.WHITE);
                } else {
                    paint.setColor(Color.GRAY);
                }
                canvas.drawRect(rect, paint);
            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
                return 0;
            }
        });//设置内容格子背景格式
        FontStyle fontStyle = new FontStyle();
        MultiLineBubbleTip<Column> tip = new MultiLineBubbleTip<Column>(this, R.mipmap.round_rect, R.mipmap.triangle, fontStyle) {
            @Override
            public boolean isShowTip(Column column, int position) {
                return true;
            }

            @Override
            public String[] format(Column column, int position) {
                DaKaRecord daKaRecord = daKaRecordList.get(position);
                String[] strings = {column.getColumnName() + ":", String.valueOf(column.getDatas().get(position))};
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

    private void initView() {
//        tv_now_year_and_month.setText(yearAndMonth + "月" + getString(R.string.all_record));

//        rv_data.setLayoutManager(new LinearLayoutManager(this));
//        rv_data.setAdapter(recordAdapter);
    }

}
