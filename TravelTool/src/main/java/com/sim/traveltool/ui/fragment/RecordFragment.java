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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.sqlitelibrary.bean.RecordDataBean;
import com.sim.traveltool.db.RecoedDataDaoUtil;
import com.sim.traveltool.R;
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
public class RecordFragment extends Fragment implements CalendarView.OnMonthChangeListener,
        CalendarView.OnCalendarSelectListener {


    @BindView(R.id.tv_now_year_and_month)
    TextView tv_now_year_and_month;
    @BindView(R.id.calendarView)
    CalendarView calendarView;

    @BindView(R.id.tv_record_time_start)
    TextView tv_record_time_start;
    @BindView(R.id.tv_record_time_end)
    TextView tv_record_time_end;
    @BindView(R.id.btn_daka)
    Button btn_daka;
    @BindView(R.id.btn_updata_other)
    Button btn_updata_other;

    private List<RecordDataBean> recordDataBeanList;
    private RecordDataBean recordDataBean;//当天打卡信息

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

        tv_now_year_and_month.setText(RecoedDataDaoUtil.getInstance().getYearMonth(getContext(), calendarView.getSelectedCalendar()));
        showInfo(calendarView.getSelectedCalendar());
    }

    private void initData() {
        //数据库插入本月所有日期条目
        RecoedDataDaoUtil.getInstance().insertDataForMonth(getContext(), calendarView.getSelectedCalendar());
    }

    /**
     * 根据数据进行显示
     */
    private void showInfo(Calendar calendar) {
        if (TimeUtil.getHour() < 13 && TimeUtil.getHour() >= 6)
            btn_daka.setText(getString(R.string.start));
        else if (TimeUtil.getHour() >= 13 || TimeUtil.getHour() < 6)
            btn_daka.setText(getString(R.string.end));

        recordDataBeanList = RecoedDataDaoUtil.getInstance().queryRecordForDay(getContext(), calendar);
        if (recordDataBeanList != null && recordDataBeanList.size() != 0) {
            recordDataBean = recordDataBeanList.get(0);

            if (recordDataBean.getStartTime() != null) {
                tv_record_time_start.setText(recordDataBean.getStartTime());
                if (recordDataBean.getIsLate().equals("1")) {
                    tv_record_time_start.setTextColor(Color.BLUE);
                } else {
                    tv_record_time_start.setTextColor(Color.WHITE);
                }
                btn_daka.setText(getString(R.string.end));
            } else {
                tv_record_time_start.setText(getString(R.string.no));
            }
            if (recordDataBean.getEndTime() != null) {
                tv_record_time_end.setText(recordDataBean.getEndTime());
                if (recordDataBean.getIsLeaveEarly().equals("1")) {
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
                    RecoedDataDaoUtil.getInstance().insertStartLate(getContext(), calendarView.getSelectedCalendar());
                    ToastUtil.T_Info(getContext(), (getString(R.string.success) + getString(R.string.late)));
                } else {
                    RecoedDataDaoUtil.getInstance().insertStartNormal(getContext(), calendarView.getSelectedCalendar());
                    ToastUtil.T_Success(getContext(), (getString(R.string.success)));
                }
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage(getString(R.string.update))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TimeUtil.getHour() > 9 || (TimeUtil.getHour() == 9 && TimeUtil.getMinute() > 30)) {
                                    RecoedDataDaoUtil.getInstance().updataRecordForDayStartLate(getContext(), calendarView.getSelectedCalendar());
                                    ToastUtil.T_Info(getContext(), (getString(R.string.success) + getString(R.string.late)));
                                } else {
                                    RecoedDataDaoUtil.getInstance().updataRecordForDayStartNormal(getContext(), calendarView.getSelectedCalendar());
                                    ToastUtil.T_Success(getContext(), (getString(R.string.success)));
                                }
                                showInfo(calendarView.getSelectedCalendar());
                            }
                        }).create().show();
            }
        } else {//下班卡
            if (tv_record_time_end.getText().equals(getString(R.string.no))) {
                if (TimeUtil.getHour() < 18 || (TimeUtil.getHour() == 18 && TimeUtil.getMinute() < 30)) {
                    RecoedDataDaoUtil.getInstance().insertEndLeaveEarly(getContext(), calendarView.getSelectedCalendar());
                    ToastUtil.T_Info(getContext(), (getString(R.string.success) + getString(R.string.early)));
                } else {
                    RecoedDataDaoUtil.getInstance().insertEndNormal(getContext(), calendarView.getSelectedCalendar());
                    ToastUtil.T_Success(getContext(), (getString(R.string.success)));
                }
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage(getString(R.string.update))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TimeUtil.getHour() < 18 || (TimeUtil.getHour() == 18 && TimeUtil.getMinute() < 30)) {
                                    RecoedDataDaoUtil.getInstance().updataRecordForDayEndLeaveEarly(getContext(), calendarView.getSelectedCalendar());
                                    ToastUtil.T_Info(getContext(), (getString(R.string.success) + getString(R.string.early)));
                                } else {
                                    RecoedDataDaoUtil.getInstance().updataRecordForDayEndNormal(getContext(), calendarView.getSelectedCalendar());
                                    ToastUtil.T_Success(getContext(), (getString(R.string.success)));
                                }
                                showInfo(calendarView.getSelectedCalendar());
                            }
                        }).create().show();
            }
        }
        showInfo(calendarView.getSelectedCalendar());
    }

    @OnClick({R.id.btn_daka, R.id.tv_now_year_and_month, R.id.all_record, R.id.btn_updata_other})
    public void OnClick(View v) {
        switch (v.getId()) {
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
                                .setNegativeButton(getString(R.string.cancel), null)
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
                    calendarView.scrollToCurrent(true);
                    ToastUtil.T_Error(getContext(), getString(R.string.only));
                }

                break;
            case R.id.all_record:
                Intent intent = new Intent(getContext(), RecordAllActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("calendar", calendarView.getSelectedCalendar());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btn_updata_other:
                final EditText et = new EditText(getContext());
                new AlertDialog.Builder(getContext()).setTitle("添加备忘")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RecoedDataDaoUtil.getInstance().updataRecordOtherForDay(getContext(), calendarView.getSelectedCalendar(), et.getText().toString());
                            }
                        })
                        .setNegativeButton("取消",null).show();
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
