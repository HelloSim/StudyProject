package com.sim.traveltool.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.baselibrary.base.BaseActivity;
import com.sim.traveltool.R;
import com.sim.traveltool.db.RecordDataDaoUtil;

/**
 * @Auther Sim
 * @Time 2020/12/11 15:05
 * @Description 隐藏界面
 */
public class HideActivity extends BaseActivity implements CalendarView.OnMonthChangeListener, CalendarView.OnCalendarSelectListener {

    ImageView back;
    TextView tvNowMonth;
    CalendarView calendarView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_hide;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        back = findViewById(R.id.back);
        tvNowMonth = findViewById(R.id.tv_now_year_and_month);
        calendarView = findViewById(R.id.calendarView);
        setViewClick(back);
    }

    @Override
    protected void initView() {
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
        tvNowMonth.setText(RecordDataDaoUtil.getInstance().getYearMonth(this, calendarView.getSelectedCalendar()));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onMultiClick(View view) {
        if (view == back) {
            finish();
        } else {
            super.onMultiClick(view);
        }
    }

    /**
     * 视图更改
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
     * 选中日期更改
     *
     * @param calendar
     * @param isClick
     */
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {

    }

}
