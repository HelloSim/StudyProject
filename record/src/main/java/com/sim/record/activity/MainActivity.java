package com.sim.record.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.record.R;
import com.sim.record.greendao.DaKaRecordDaoUtil;
import com.sim.record.greendao.bean.DaKaRecord;
import com.sim.record.utils.TimeUtil;
import com.sim.record.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements CalendarView.OnMonthChangeListener,
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

    private Context mContext;

    private DaKaRecord daKaRecord;//当天打卡信息
    private List<DaKaRecord> daKaRecordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        initView();
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

        tv_now_year_and_month.setText(DaKaRecordDaoUtil.getYearMonth(calendarView.getSelectedCalendar()));
        showInfo(calendarView.getSelectedCalendar());
    }

    /**
     * 根据数据进行显示
     */
    private void showInfo(Calendar calendar) {
        if (TimeUtil.getHour() < 13 && TimeUtil.getHour() >= 6)
            btn_daka.setText(getString(R.string.start));
        else if (TimeUtil.getHour() >= 13 || TimeUtil.getHour() < 6)
            btn_daka.setText(getString(R.string.end));

        daKaRecordList = DaKaRecordDaoUtil.queryRecordForDay(calendar);
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
                    DaKaRecordDaoUtil.insertStartLate(calendarView.getSelectedCalendar());
                    ToastUtil.T_Info(mContext, (getString(R.string.success) + getString(R.string.late)));
                } else {
                    DaKaRecordDaoUtil.insertStartNormal(calendarView.getSelectedCalendar());
                    ToastUtil.T_Success(mContext, (getString(R.string.success)));
                }
            } else {
                new AlertDialog.Builder(mContext)
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
                                    DaKaRecordDaoUtil.updataRecordForDayStartLate(calendarView.getSelectedCalendar());
                                    ToastUtil.T_Info(mContext, (getString(R.string.success) + getString(R.string.late)));
                                } else {
                                    DaKaRecordDaoUtil.updataRecordForDayStartNormal(calendarView.getSelectedCalendar());
                                    ToastUtil.T_Success(mContext, (getString(R.string.success)));
                                }
                                showInfo(calendarView.getSelectedCalendar());
                            }
                        }).create().show();
            }
        } else {//下班卡
            if (tv_record_time_end.getText().equals(getString(R.string.no))) {
                if (TimeUtil.getHour() < 18 || (TimeUtil.getHour() == 18 && TimeUtil.getMinute() < 30)) {
                    DaKaRecordDaoUtil.insertEndLeaveEarly(calendarView.getSelectedCalendar());
                    ToastUtil.T_Info(mContext, (getString(R.string.success) + getString(R.string.early)));
                } else {
                    DaKaRecordDaoUtil.insertEndNormal(calendarView.getSelectedCalendar());
                    ToastUtil.T_Success(mContext, (getString(R.string.success)));
                }
            } else {
                new AlertDialog.Builder(mContext)
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
                                    DaKaRecordDaoUtil.updataRecordForDayEndLeaveEarly(calendarView.getSelectedCalendar());
                                    ToastUtil.T_Info(mContext, (getString(R.string.success) + getString(R.string.early)));
                                } else {
                                    DaKaRecordDaoUtil.updataRecordForDayEndNormal(calendarView.getSelectedCalendar());
                                    ToastUtil.T_Success(mContext, (getString(R.string.success)));
                                }
                                showInfo(calendarView.getSelectedCalendar());
                            }
                        }).create().show();
            }
        }
        showInfo(calendarView.getSelectedCalendar());
    }

    @OnClick({R.id.btn_now, R.id.btn_daka, R.id.tv_now_year_and_month})
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
                        new AlertDialog.Builder(mContext)
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
                    ToastUtil.T_Error(mContext, getString(R.string.only));
                }

                break;
            case R.id.tv_now_year_and_month:
                break;
        }
    }

    /**
     * 创建右上角菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lefttopmenu, menu);
        return true;
    }

    /**
     * 菜单的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_record:
                Intent intent = new Intent(this, AllRecordActivity.class);
                intent.putExtra("yearAndMonth", DaKaRecordDaoUtil.getYearMonth(calendarView.getSelectedCalendar()));
                startActivity(intent);
                break;
            case R.id.add_memo:
                ToastUtil.T_Default(mContext,"暂未实现");
                break;
            default:
                break;
        }
        return true;
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

    /**
     * 按两次退出程序
     */
    private long exitTime;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), getString(R.string.exit), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            // 杀死该应用进程
            try {
                System.exit(0);
            } catch (Exception e) {
                System.exit(1);
            }
        }
    }

}