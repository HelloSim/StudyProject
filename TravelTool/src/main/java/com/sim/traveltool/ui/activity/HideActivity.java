package com.sim.traveltool.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.baselibrary.base.BaseActivity;
import com.sim.traveltool.R;
import com.sim.traveltool.ui.view.TitleView;

/**
 * @author Sim --- 隐藏界面
 */
public class HideActivity extends BaseActivity implements CalendarView.OnMonthChangeListener, CalendarView.OnCalendarSelectListener {

    private TitleView titleView;
    private TextView tvNowMonth;
    private CalendarView calendarView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_hide;
    }

    @Override
    protected void bindViews(Bundle savedInstanceState) {
        titleView = findViewById(R.id.titleView);
        tvNowMonth = findViewById(R.id.tv_now_year_and_month);
        calendarView = findViewById(R.id.calendarView);
        titleView.setLeftClickListener(new TitleView.LeftClickListener() {
            @Override
            public void onClick(View leftView) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

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
        tvNowMonth.setText(calendarView.getSelectedCalendar().getYear() + "-" + calendarView.getSelectedCalendar().getMonth());
    }

    /**
     * 视图更改
     *
     * @param years
     * @param months
     */
    @Override
    public void onMonthChange(int years, int months) {
        tvNowMonth.setText(years + "年" + months + "月");
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
