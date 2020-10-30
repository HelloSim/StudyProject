package com.sim.test.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;
import com.haibin.calendarview.CalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一个日历的第三方依赖库
 */
public class CalendarActivity extends BaseActivity implements CalendarView.OnMonthChangeListener {

    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.month)
    TextView month;
    @BindView(R.id.year_and_month)
    LinearLayout year_and_month;
    @BindView(R.id.now)
    Button now;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        showYearAndMonth();
        calendarView.setOnMonthChangeListener(this);
    }

    @OnClick({R.id.now})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.now:
                calendarView.scrollToCurrent(true);
                break;
        }
    }

    /**
     * 年份、月份显示
     */
    @SuppressLint("SetTextI18n")
    public void showYearAndMonth() {
        year.setText(calendarView.getSelectedCalendar().getYear() + getString(R.string.year));
        month.setText(calendarView.getSelectedCalendar().getMonth() + getString(R.string.month));
    }

    /**
     * 视图更改时更新年份、月份显示
     *
     * @param years
     * @param months
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onMonthChange(int years, int months) {
        year.setText(years + getString(R.string.year));
        month.setText(months + getString(R.string.month));
    }

}
