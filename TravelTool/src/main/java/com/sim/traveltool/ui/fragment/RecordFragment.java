package com.sim.traveltool.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.baselibrary.base.BaseFragment;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.sqlitelibrary.bean.RecordDataBean;
import com.sim.traveltool.R;
import com.sim.traveltool.db.RecordDataDaoUtil;
import com.sim.traveltool.ui.activity.RecordAllActivity;

import java.util.List;

/**
 * @Auther Sim
 * @Time 2020/4/27 1:05
 * @Description “打卡”Fragment
 */
public class RecordFragment extends BaseFragment implements CalendarView.OnMonthChangeListener,
        CalendarView.OnCalendarSelectListener {

    TextView tvNowMonth;
    CalendarView calendarView;

    TextView tvRecordTimeStart;
    TextView tvRecordTimeEnd;
    Button btnRecord;
    Button btnAllRecord;
    Button btnOther;

    private List<RecordDataBean> recordDataBeanList;//指定日期的打卡记录列表
    private RecordDataBean recordDataBean;//指定日期的打卡信息

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_record;
    }

    @Override
    protected void bindViews(View view) {
        tvNowMonth = view.findViewById(R.id.tv_now_year_and_month);
        calendarView = view.findViewById(R.id.calendarView);

        tvRecordTimeStart = view.findViewById(R.id.tv_record_time_start);
        tvRecordTimeEnd = view.findViewById(R.id.tv_record_time_end);
        btnRecord = view.findViewById(R.id.btn_record);
        btnAllRecord = view.findViewById(R.id.all_record);
        btnOther = view.findViewById(R.id.btn_updata_other);
    }

    @Override
    protected void initView(View view) {
        setViewClick(btnRecord, btnAllRecord, btnOther);
        //设置星期日周起始
        calendarView.setWeekStarWithSun();
        //设置星期栏的背景、字体颜色
        calendarView.setWeeColor(Color.TRANSPARENT, Color.WHITE);
        //定制颜色：选中的标记颜色、标记背景色
        calendarView.setThemeColor(Color.WHITE, Color.TRANSPARENT);
        //设置文本颜色：今天字体颜色、当前月份字体颜色、其它月份字体颜色、当前月份农历字体颜色、其它农历字体颜色
        calendarView.setTextColor(Color.WHITE, Color.WHITE, Color.GRAY, Color.WHITE, Color.GRAY);
        //月份改变事件监听
        calendarView.setOnMonthChangeListener(this);
        //日期选择事件监听
        calendarView.setOnCalendarSelectListener(this);

        tvNowMonth.setText(RecordDataDaoUtil.getInstance().getYearMonth(getContext(), calendarView.getSelectedCalendar()));
        showInfo(calendarView.getSelectedCalendar());
    }

    @Override
    protected void initData() {
        //数据库插入本月所有日期条目
        RecordDataDaoUtil.getInstance().insertDataForMonth(getContext(), calendarView.getSelectedCalendar());
    }

    @Override
    public void onMultiClick(View view) {
        if (view == btnAllRecord) {
            Intent intent = new Intent(getContext(), RecordAllActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("calendar", calendarView.getSelectedCalendar());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (view == btnOther) {
            EditText et = new EditText(getContext());
            new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.record_add_memo))
                    .setView(et)
                    .setNegativeButton(getString(R.string.cancel), null)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RecordDataDaoUtil.getInstance().updataRecordOther(getContext(), calendarView.getSelectedCalendar(), et.getText().toString());
                        }
                    }).show();
        } else if (view == btnRecord) {
            if (calendarView.getSelectedCalendar().isCurrentDay()) {//是否当天
                if (!calendarView.getSelectedCalendar().isWeekend()) {//是否周末
                    if (btnRecord.getText().equals(getString(R.string.record_start))) {
                        record(1);
                    } else {
                        record(2);
                    }
                } else {
                    showDialog(null, getString(R.string.record_week), getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.callback.DialogInterface() {
                        @Override
                        public void sureOnClick() {
                            if (btnRecord.getText().equals(getString(R.string.record_start))) {
                                record(1);
                            } else {
                                record(2);
                            }
                        }

                        @Override
                        public void cancelOnClick() {

                        }
                    });
                }
            } else {
                calendarView.scrollToCurrent(true);
                ToastUtil.T_Error(getContext(), getString(R.string.record_only_today));
            }
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 视图更改时更新顶部年月份显示
     *
     * @param years
     * @param months
     */
    @Override
    public void onMonthChange(int years, int months) {
        tvNowMonth.setText(years + getString(R.string.year) + months + getString(R.string.month));
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    /**
     * 选中日期更改时更新打卡记录显示
     *
     * @param calendar
     * @param isClick
     */
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        showInfo(calendar);
    }

    /**
     * 根据数据进行显示
     */
    private void showInfo(Calendar calendar) {
        recordDataBeanList = RecordDataDaoUtil.getInstance().queryRecordForDay(getContext(), calendar);
        if (recordDataBeanList != null && recordDataBeanList.size() != 0) {
            recordDataBean = recordDataBeanList.get(0);
            tvRecordTimeStart.setText(recordDataBean.getStartTime());
            tvRecordTimeEnd.setText(recordDataBean.getEndTime());
            tvRecordTimeStart.setTextColor(recordDataBean.getIsLate() ? Color.RED : Color.WHITE);
            tvRecordTimeEnd.setTextColor(recordDataBean.getIsLeaveEarly() ? Color.RED : Color.WHITE);
            if (tvRecordTimeEnd.getText().equals(getString(R.string.record_no))) {//是否已打下班卡
                if (tvRecordTimeStart.getText().equals(getString(R.string.record_no))) {//是否已打上班卡
                    if (TimeUtil.getHour() >= 14) {
                        btnRecord.setText(getString(R.string.record_end));
                    } else {
                        btnRecord.setText(getString(R.string.record_start));
                    }
                } else {
                    btnRecord.setText(getString(R.string.record_end));
                }
            } else {
                btnRecord.setText(getString(R.string.record_end));
            }
        } else {
            tvRecordTimeStart.setText(getString(R.string.record_no));
            tvRecordTimeEnd.setText(getString(R.string.record_no));
        }
    }

    /**
     * 打卡
     *
     * @param type 1:上班卡 2:下班卡
     */
    private void record(int type) {
        switch (type) {
            case 1://上班卡
                if (tvRecordTimeStart.getText().equals(getString(R.string.record_no))) {
                    if (RecordDataDaoUtil.getInstance().updataStartTime(getContext(), calendarView.getSelectedCalendar())) {
                        ToastUtil.T_Info(getContext(), (getString(R.string.record_success) + getString(R.string.late)));
                    } else {
                        ToastUtil.T_Info(getContext(), (getString(R.string.record_success)));
                    }
                    showInfo(calendarView.getSelectedCalendar());
                } else {
                    showDialog(null, getString(R.string.record_update), getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.callback.DialogInterface() {
                        @Override
                        public void sureOnClick() {
                            if (RecordDataDaoUtil.getInstance().updataStartTime(getContext(), calendarView.getSelectedCalendar())) {
                                ToastUtil.T_Info(getContext(), (getString(R.string.record_success) + getString(R.string.late)));
                            } else {
                                ToastUtil.T_Info(getContext(), getString(R.string.record_success));
                            }
                            showInfo(calendarView.getSelectedCalendar());
                        }

                        @Override
                        public void cancelOnClick() {

                        }
                    });
                }
                break;
            case 2://下班卡
                if (tvRecordTimeEnd.getText().equals(getString(R.string.record_no))) {
                    if (RecordDataDaoUtil.getInstance().updataEndTime(getContext(), calendarView.getSelectedCalendar())) {
                        ToastUtil.T_Info(getContext(), getString(R.string.record_success) + getString(R.string.leave_early));
                    } else {
                        ToastUtil.T_Info(getContext(), getString(R.string.record_success));
                    }
                    showInfo(calendarView.getSelectedCalendar());
                } else {
                    showDialog(null, getString(R.string.record_update), getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.callback.DialogInterface() {
                        @Override
                        public void sureOnClick() {
                            if (RecordDataDaoUtil.getInstance().updataEndTime(getContext(), calendarView.getSelectedCalendar())) {
                                ToastUtil.T_Info(getContext(), getString(R.string.record_success) + getString(R.string.leave_early));
                            } else {
                                ToastUtil.T_Info(getContext(), getString(R.string.record_success));
                            }
                            showInfo(calendarView.getSelectedCalendar());
                        }

                        @Override
                        public void cancelOnClick() {

                        }
                    });
                }
                break;
        }
    }

}
