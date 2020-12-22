package com.sim.traveltool.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.baselibrary.base.Base_Fragment;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.sqlitelibrary.bean.RecordDataBean;
import com.sim.traveltool.R;
import com.sim.traveltool.db.RecordDataDaoUtil;
import com.sim.traveltool.ui.activity.RecordAllActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Auther Sim
 * @Time 2020/4/27 1:05
 * @Description “打卡”Fragment
 */
public class RecordFragment extends Base_Fragment implements CalendarView.OnMonthChangeListener,
        CalendarView.OnCalendarSelectListener {
    private static final String TAG = "Sim_RecordFragment";

    @BindView(R.id.tv_now_year_and_month)
    TextView tv_now_year_and_month;
    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @BindView(R.id.tv_record_time_start)
    TextView tv_record_time_start;
    @BindView(R.id.tv_record_time_end)
    TextView tv_record_time_end;
    @BindView(R.id.btn_record)
    Button btn_record;
    @BindView(R.id.btn_updata_other)
    Button btn_updata_other;

    private List<RecordDataBean> recordDataBeanList;//指定日期的打卡记录列表
    private RecordDataBean recordDataBean;//指定日期的打卡信息

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initView() {
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

        tv_now_year_and_month.setText(RecordDataDaoUtil.getInstance().getYearMonth(getContext(), calendarView.getSelectedCalendar()));
        showInfo(calendarView.getSelectedCalendar());
    }

    private void initData() {
        //数据库插入本月所有日期条目
        RecordDataDaoUtil.getInstance().insertDataForMonth(getContext(), calendarView.getSelectedCalendar());
    }

    /**
     * 根据数据进行显示
     */
    private void showInfo(Calendar calendar) {
        recordDataBeanList = RecordDataDaoUtil.getInstance().queryRecordForDay(getContext(), calendar);
        if (recordDataBeanList != null && recordDataBeanList.size() != 0) {
            recordDataBean = recordDataBeanList.get(0);
            tv_record_time_start.setText(recordDataBean.getStartTime());
            tv_record_time_end.setText(recordDataBean.getEndTime());
            tv_record_time_start.setTextColor(recordDataBean.getIsLate() ? Color.RED : Color.WHITE);
            tv_record_time_end.setTextColor(recordDataBean.getIsLeaveEarly() ? Color.RED : Color.WHITE);
            if (tv_record_time_end.getText().equals(getString(R.string.record_no))) {//是否已打下班卡
                if (tv_record_time_start.getText().equals(getString(R.string.record_no))) {//是否已打上班卡
                    if (TimeUtil.getHour() >= 14) {
                        btn_record.setText(getString(R.string.record_end));
                    } else {
                        btn_record.setText(getString(R.string.record_start));
                    }
                } else {
                    btn_record.setText(getString(R.string.record_end));
                }
            } else {
                btn_record.setText(getString(R.string.record_end));
            }
        } else {
            tv_record_time_start.setText(getString(R.string.record_no));
            tv_record_time_end.setText(getString(R.string.record_no));
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
                if (tv_record_time_start.getText().equals(getString(R.string.record_no))) {
                    if (RecordDataDaoUtil.getInstance().updataStartTime(getContext(), calendarView.getSelectedCalendar())) {
                        ToastUtil.T_Info(getContext(), (getString(R.string.record_success) + getString(R.string.late)));
                    } else {
                        ToastUtil.T_Info(getContext(), (getString(R.string.record_success)));
                    }
                    showInfo(calendarView.getSelectedCalendar());
                } else {
                    showDialog(null, getString(R.string.record_update), getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.views.DialogInterface() {
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
                if (tv_record_time_end.getText().equals(getString(R.string.record_no))) {
                    if (RecordDataDaoUtil.getInstance().updataEndTime(getContext(), calendarView.getSelectedCalendar())) {
                        ToastUtil.T_Info(getContext(), getString(R.string.record_success) + getString(R.string.leave_early));
                    } else {
                        ToastUtil.T_Info(getContext(), getString(R.string.record_success));
                    }
                    showInfo(calendarView.getSelectedCalendar());
                } else {
                    showDialog(null, getString(R.string.record_update), getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.views.DialogInterface() {
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

    @OnClick({R.id.all_record, R.id.btn_updata_other, R.id.btn_record})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.all_record:
                Intent intent = new Intent(getContext(), RecordAllActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("calendar", calendarView.getSelectedCalendar());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_record:
                if (calendarView.getSelectedCalendar().isCurrentDay()) {//是否当天
                    if (!calendarView.getSelectedCalendar().isWeekend()) {//是否周末
                        if (btn_record.getText().equals(getString(R.string.record_start))) {
                            record(1);
                        } else {
                            record(2);
                        }
                    } else {
                        showDialog(null, getString(R.string.record_week), getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.views.DialogInterface() {
                            @Override
                            public void sureOnClick() {
                                if (btn_record.getText().equals(getString(R.string.record_start))) {
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
                break;
            case R.id.btn_updata_other:
                final EditText et = new EditText(getContext());
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
                break;
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
        tv_now_year_and_month.setText(years + getString(R.string.year) + months + getString(R.string.month));
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

}
