package com.sim.record.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.utils.DensityUtils;
import com.haibin.calendarview.Calendar;
import com.sim.basicres.base.BaseActivity;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.TimeUtil;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
import com.sim.record.R;
import com.sim.user.bean.RecordBean;
import com.sim.user.utils.CallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sim --- 月打卡列表页面
 */
@Route(path = ArouterUrl.Record.record_activity_all)
public class RecordAllActivity extends BaseActivity {

    private TitleView titleView;
    private SmartTable<RecordBean> table;

    private Calendar calendar;

    private List<RecordBean> allDataList = new ArrayList<>();//当月打卡数据
    private int days;//当月天数

    @Override
    protected int getLayoutRes() {
        return R.layout.record_activity_record_all;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        table = findViewById(R.id.table_data);
        titleView.setLeftClickListener(new TitleView.LeftClickListener() {
            @Override
            public void onClick(View leftView) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        calendar = (Calendar) getIntent().getSerializableExtra("calendar");
        days = TimeUtil.getDaysByYearMonth(calendar.getYear(), calendar.getMonth());
        for (int i = 1; i <= days; i++) {
            RecordBean recordBean = new RecordBean(calendar.getYear() + "-" + calendar.getMonth() + "-" + i);
            allDataList.add(recordBean);
        }
        RecordBean.queryForMonth(String.valueOf(calendar.getYear()) + calendar.getMonth(), new CallBack() {
            @Override
            public void success(Object... values) {
                List<RecordBean> list = (List<RecordBean>) values[0];
                Log.d("Sim", "success: " + list.size());
                if (list != null || list.size() > 0) {
                    for (RecordBean recordBean1 : list) {
                        for (int i = 0; i < allDataList.size(); i++) {
                            if (recordBean1.getDate().equals(allDataList.get(i).getDate())) {
                                allDataList.get(i).setStartTime(recordBean1.getStartTime());
                                allDataList.get(i).setEndTime(recordBean1.getEndTime());
                                allDataList.get(i).setOther(recordBean1.getOther());
                                break;
                            }
                        }
                    }
                    table.setData(allDataList);
                } else {
                    ToastUtil.toast(RecordAllActivity.this, "当月无打卡记录！");
                }
            }

            @Override
            public void fail(String values) {
                table.setData(allDataList);
                ToastUtil.toast(RecordAllActivity.this, "查询指定日期数据失败：" + values);
            }
        });
    }

    @Override
    protected void initView() {
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(this, 18)); //设置全局字体大小
        titleView.setTitleTextView(calendar.getYear() + "-" + calendar.getMonth());

        ArrayList list = new ArrayList();//日期列的周六日内容格
        table.setZoom(false);//设置不可缩放
        table.getConfig()
                .setShowXSequence(false)//是否显示顶部序号列
                .setShowYSequence(false)//是否显示左侧序号列
                .setShowTableTitle(false)//是否显示表格标题
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

    }

}
