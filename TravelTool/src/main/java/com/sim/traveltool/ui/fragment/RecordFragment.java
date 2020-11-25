package com.sim.traveltool.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.baselibrary.utils.HttpUtil;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.sqlitelibrary.DaKaRecordDaoUtil;
import com.sim.sqlitelibrary.bean.DaKaRecord;
import com.sim.traveltool.R;
import com.sim.traveltool.ui.activity.RecordAllActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * “打卡”Fragment
 * Created by Sim on 2020/4/27
 */
public class RecordFragment extends Fragment implements CalendarView.OnMonthChangeListener,
        CalendarView.OnCalendarSelectListener {


    @BindView(R.id.tv_now_year_and_month)
    TextView tv_now_year_and_month;
    @BindView(R.id.btn_now)
    Button btn_now;
    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @BindView(R.id.tv_record_time_start)
    TextView tv_record_time_start;
    @BindView(R.id.tv_record_time_end)
    TextView tv_record_time_end;
    @BindView(R.id.btn_daka)
    Button btn_daka;

    private List<DaKaRecord> daKaRecordList;
    private DaKaRecord daKaRecord;//当天打卡信息
    private String baseUrl = "http://tool.bitefu.net/jiari?d=";
    private int isWorkingDay = 0;//0工作日 1 假日 2节日

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        initView();
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

        tv_now_year_and_month.setText(DaKaRecordDaoUtil.getInstance().getYearMonth(getContext(), calendarView.getSelectedCalendar()));
        showInfo(calendarView.getSelectedCalendar());
    }

    /**
     * 根据数据进行显示
     */
    private void showInfo(Calendar calendar) {
        String apiurl = null;
        apiurl = baseUrl + DaKaRecordDaoUtil.getInstance().getYMD(getContext(), calendar);
        HttpUtil.doGetAsyn(apiurl, new HttpUtil.CallBack() {
            @Override
            public void onRequestComplete(String result) {
                isWorkingDay = Integer.parseInt(result);
            }

            @Override
            public void onRequestError(String result) {

            }
        });
        if (TimeUtil.getHour() < 13 && TimeUtil.getHour() >= 6)
            btn_daka.setText(getString(R.string.start));
        else if (TimeUtil.getHour() >= 13 || TimeUtil.getHour() < 6)
            btn_daka.setText(getString(R.string.end));

        daKaRecordList = DaKaRecordDaoUtil.getInstance().queryRecordForDay(getContext(), calendar);
        if (daKaRecordList != null && daKaRecordList.size() != 0) {
            daKaRecord = daKaRecordList.get(0);
            if (daKaRecord.getStartTime() != null) {
                tv_record_time_start.setText(daKaRecord.getStartTime());
                if (daKaRecord.getIsLate().equals("1")) {
                    tv_record_time_start.setTextColor(Color.BLUE);
                } else {
                    tv_record_time_start.setTextColor(Color.WHITE);
                }
                btn_daka.setText(getString(R.string.end));
            } else {
                tv_record_time_start.setText(getString(R.string.no));
            }
            if (daKaRecord.getEndTime() != null) {
                tv_record_time_end.setText(daKaRecord.getEndTime());
                if (daKaRecord.getIsLeaveEarly().equals("1")) {
                    tv_record_time_end.setTextColor(Color.RED);
                } else {
                    tv_record_time_end.setTextColor(Color.WHITE);
                }
            } else {
                tv_record_time_end.setText(getString(R.string.no));
            }
        } else {
            tv_record_time_start.setText(getString(R.string.no));
            tv_record_time_start.setTextColor(Color.WHITE);
            tv_record_time_end.setText(getString(R.string.no));
            tv_record_time_end.setTextColor(Color.WHITE);
        }
    }

    /**
     * 打卡
     *
     * @param isStart 是否上班卡
     */
    private void daka(boolean isStart) {
        if (isStart) {//上班卡
            if (tv_record_time_start.getText().equals(getString(R.string.no))) {
                if (TimeUtil.getHour() > 9 || (TimeUtil.getHour() == 9 && TimeUtil.getMinute() > 30)) {
                    DaKaRecordDaoUtil.getInstance().insertStartLate(getContext(), calendarView.getSelectedCalendar());
                    ToastUtil.T_Info(getContext(), (getString(R.string.success) + getString(R.string.late)));
                } else {
                    DaKaRecordDaoUtil.getInstance().insertStartNormal(getContext(), calendarView.getSelectedCalendar());
                    ToastUtil.T_Success(getContext(), (getString(R.string.success)));
                }
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage(getString(R.string.update))
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TimeUtil.getHour() > 9 || (TimeUtil.getHour() == 9 && TimeUtil.getMinute() > 30)) {
                                    DaKaRecordDaoUtil.getInstance().updataRecordForDayStartLate(getContext(), calendarView.getSelectedCalendar());
                                    ToastUtil.T_Info(getContext(), (getString(R.string.success) + getString(R.string.late)));
                                } else {
                                    DaKaRecordDaoUtil.getInstance().updataRecordForDayStartNormal(getContext(), calendarView.getSelectedCalendar());
                                    ToastUtil.T_Success(getContext(), (getString(R.string.success)));
                                }
                                showInfo(calendarView.getSelectedCalendar());
                            }
                        }).create().show();
            }
        } else {//下班卡
            if (tv_record_time_end.getText().equals(getString(R.string.no))) {
                if (TimeUtil.getHour() < 18 || (TimeUtil.getHour() == 18 && TimeUtil.getMinute() < 30)) {
                    DaKaRecordDaoUtil.getInstance().insertEndLeaveEarly(getContext(), calendarView.getSelectedCalendar());
                    ToastUtil.T_Info(getContext(), (getString(R.string.success) + getString(R.string.early)));
                } else {
                    DaKaRecordDaoUtil.getInstance().insertEndNormal(getContext(), calendarView.getSelectedCalendar());
                    ToastUtil.T_Success(getContext(), (getString(R.string.success)));
                }
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage(getString(R.string.update))
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TimeUtil.getHour() < 18 || (TimeUtil.getHour() == 18 && TimeUtil.getMinute() < 30)) {
                                    DaKaRecordDaoUtil.getInstance().updataRecordForDayEndLeaveEarly(getContext(), calendarView.getSelectedCalendar());
                                    ToastUtil.T_Info(getContext(), (getString(R.string.success) + getString(R.string.early)));
                                } else {
                                    DaKaRecordDaoUtil.getInstance().updataRecordForDayEndNormal(getContext(), calendarView.getSelectedCalendar());
                                    ToastUtil.T_Success(getContext(), (getString(R.string.success)));
                                }
                                showInfo(calendarView.getSelectedCalendar());
                            }
                        }).create().show();
            }
        }
        showInfo(calendarView.getSelectedCalendar());
    }

    @OnClick({R.id.btn_now, R.id.btn_daka, R.id.tv_now_year_and_month, R.id.all_record})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_now:
                calendarView.scrollToCurrent(true);
                break;
            case R.id.btn_daka:
                if (calendarView.getSelectedCalendar().isCurrentDay()) {//是否当天
                    if (!calendarView.getSelectedCalendar().isWeekend()) {//是否周末
                        if (btn_daka.getText().equals(getString(R.string.start)))
                            daka(true);
                        else
                            daka(false);
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setMessage(getString(R.string.week))
                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (btn_daka.getText().equals(getString(R.string.start)))
                                            daka(true);
                                        else
                                            daka(false);
                                    }
                                }).create().show();
                    }
                } else {
                    ToastUtil.T_Error(getContext(), getString(R.string.only));
                }

                break;
            case R.id.all_record:
                Intent intent = new Intent(getContext(), RecordAllActivity.class);
                intent.putExtra("yearAndMonth", DaKaRecordDaoUtil.getInstance().getYearMonth(getContext(), calendarView.getSelectedCalendar()));
                startActivity(intent);
                break;
            case R.id.tv_now_year_and_month:
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
        if (calendar.isCurrentDay()) {
            btn_now.setVisibility(View.GONE);
        } else {
            btn_now.setVisibility(View.VISIBLE);
        }
        showInfo(calendar);
    }

}
