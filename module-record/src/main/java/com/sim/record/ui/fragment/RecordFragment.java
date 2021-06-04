package com.sim.record.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.basicres.base.BaseFragment;
import com.sim.basicres.callback.DialogInterface;
import com.sim.basicres.constant.ArouterUrl;
import com.sim.basicres.utils.TimeUtil;
import com.sim.basicres.utils.ToastUtil;
import com.sim.basicres.views.TitleView;
import com.sim.record.R;
import com.sim.mine.bean.RecordBean;
import com.sim.mine.bean.User;
import com.sim.mine.utils.CallBack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Route(path = ArouterUrl.Record.record_fragment)
public class RecordFragment extends BaseFragment implements CalendarView.OnMonthChangeListener, CalendarView.OnCalendarSelectListener {

    private TextView tvNowMonth;
    private CalendarView calendarView;

    private LinearLayout parent;
    private TitleView titleView;
    private TextView tvRecordTimeStart, tvRecordTimeEnd;
    private Button btnRecord;

    //更多弹窗、添加备忘弹窗
    private PopupWindow morePopupWindow, otherPopupWindow;//弹窗
    private View moreLayout, otherLayout;//布局
    private EditText etOther;
    private Button btnAllRecord, btnOther, btnCancel, btnConfirm;

    @Override
    protected int getLayoutRes() {
        return R.layout.record_fragment_record;
    }

    @Override
    protected void bindViews(View view) {
        tvNowMonth = view.findViewById(R.id.tv_now_year_and_month);
        calendarView = view.findViewById(R.id.calendarView);

        parent = view.findViewById(R.id.parent);
        titleView = view.findViewById(R.id.titleView);
        tvRecordTimeStart = view.findViewById(R.id.tv_record_time_start);
        tvRecordTimeEnd = view.findViewById(R.id.tv_record_time_end);
        btnRecord = view.findViewById(R.id.btn_record);
        setViewClick(btnRecord);
        titleView.setRightClickListener(new TitleView.RightClickListener() {
            @Override
            public void onClick(View leftView) {
                morePopupWindow.showAsDropDown(titleView, titleView.getWidth(), 0);
            }
        });

        calendarView.setWeekStarWithSun();//设置星期日周起始
        calendarView.setWeeColor(Color.TRANSPARENT, Color.BLACK);//设置星期栏的背景、字体颜色
        calendarView.setThemeColor(Color.GREEN, Color.RED);//定制颜色：选中的标记颜色、标记背景色
        calendarView.setTextColor(Color.RED, Color.BLACK, Color.GRAY, Color.BLACK, Color.GRAY);//设置文本颜色：今天字体颜色、当前月份字体颜色、其它月份字体颜色、当前月份农历字体颜色、其它农历字体颜色
        calendarView.setOnMonthChangeListener(this);//月份改变事件监听
        calendarView.setOnCalendarSelectListener(this);//日期选择事件监听
    }

    @Override
    protected void initView(View view) {
        tvNowMonth.setText(calendarView.getSelectedCalendar().getYear() + "-" + calendarView.getSelectedCalendar().getMonth());
        showInfo(calendarView.getSelectedCalendar());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        moreLayout = inflater.inflate(R.layout.record_view_popup_record_more, null);
        otherLayout = inflater.inflate(R.layout.record_view_popup_add_other, null);
        morePopupWindow = showPopupWindow(moreLayout, 130, 130);
        otherPopupWindow = showPopupWindow(otherLayout, 300, 180);
        btnAllRecord = moreLayout.findViewById(R.id.all_record);
        btnOther = moreLayout.findViewById(R.id.btn_updata_other);
        etOther = otherLayout.findViewById(R.id.et_other);
        btnCancel = otherLayout.findViewById(R.id.btn_cancel);
        btnConfirm = otherLayout.findViewById(R.id.btn_confirm);
        setViewClick(btnAllRecord, btnOther, btnCancel, btnConfirm);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        showInfo(calendarView.getSelectedCalendar());
    }

    @Override
    public void onMultiClick(View view) {
        if (view == btnAllRecord) {
            morePopupWindow.dismiss();
            if (User.isLogin()) {
                ARouter.getInstance().build(ArouterUrl.Record.record_activity_all)
                        .withSerializable("calendar", calendarView.getSelectedCalendar())
                        .navigation();
            } else {
                ToastUtil.toast(getContext(), "未登录");
            }
        } else if (view == btnOther) {
            morePopupWindow.dismiss();
            if (User.isLogin()) {
                otherPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            } else {
                ToastUtil.toast(getContext(), "未登录");
            }
        } else if (view == btnCancel) {
            otherPopupWindow.dismiss();
        } else if (view == btnConfirm) {
            otherPopupWindow.dismiss();
            if (User.isLogin()) {
                RecordBean.updateBean(getYMD(calendarView.getSelectedCalendar()), null, null, etOther.getText().toString(), new CallBack() {
                    @Override
                    public void success(Object... values) {
                        ToastUtil.toast(getContext(), "添加备忘成功！");
                    }

                    @Override
                    public void fail(String values) {
                        ToastUtil.toast(getContext(), "添加备忘失败：" + values);
                    }
                });
            } else {
                ToastUtil.toast(getContext(), "登录失效，请重新登录！");
            }
        } else if (view == btnRecord) {
            if (!User.isLogin()) {//是否登录
                ToastUtil.toast(getContext(), "未登录");
                return;
            }
            if (!calendarView.getSelectedCalendar().isCurrentDay()) {//是否当天
                calendarView.scrollToCurrent(true);
                return;
            }
            if (btnRecord.getText().equals("上班打卡")) {//上班卡
                record(true, !tvRecordTimeStart.getText().equals("未打卡"));
            } else {//下班卡
                record(false, !tvRecordTimeEnd.getText().equals("未打卡"));
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
        tvNowMonth.setText(years + "-" + months);
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
        if (User.isLogin()) {
            RecordBean.queryForDay(getYMD(calendar), new CallBack() {
                @Override
                public void success(Object... values) {
                    ArrayList<RecordBean> list = (ArrayList<RecordBean>) values[0];
                    if (list != null && list.size() > 0) {
                        RecordBean selectedRecordBean = list.get(0);
                        tvRecordTimeStart.setText((selectedRecordBean.getStartTime() == null || selectedRecordBean.getStartTime().length() == 0) ? "未打卡" : selectedRecordBean.getStartTime());
                        tvRecordTimeEnd.setText((selectedRecordBean.getEndTime() == null || selectedRecordBean.getEndTime().length() == 0) ? "未打卡" : selectedRecordBean.getEndTime());
                        tvRecordTimeStart.setTextColor(selectedRecordBean.isLate() ? Color.RED : Color.BLACK);
                        tvRecordTimeEnd.setTextColor(selectedRecordBean.isLeaveEarly() ? Color.RED : Color.BLACK);
                        if (TimeUtil.getHour() >= 14) {
                            btnRecord.setText("下班打卡");
                        } else {
                            btnRecord.setText("上班打卡");
                        }
                        if (!tvRecordTimeEnd.getText().equals("未打卡")) {
                            btnRecord.setText("下班打卡");
                        }
                        if (!tvRecordTimeStart.getText().equals("未打卡")) {//是否已打上班卡
                            btnRecord.setText("下班打卡");
                        }
                    } else {
                        tvRecordTimeStart.setText("未打卡");
                        tvRecordTimeEnd.setText("未打卡");
                        tvRecordTimeStart.setTextColor(Color.BLACK);
                        tvRecordTimeEnd.setTextColor(Color.BLACK);
                        if (TimeUtil.getHour() >= 14) {
                            btnRecord.setText("下班打卡");
                        } else {
                            btnRecord.setText("上班打卡");
                        }
                    }
                }

                @Override
                public void fail(String values) {
                    ToastUtil.toast(getContext(), "查询选中日期数据失败：" + values);
                }
            });
        } else {
            tvRecordTimeStart.setText("未打卡");
            tvRecordTimeEnd.setText("未打卡");
            tvRecordTimeStart.setTextColor(Color.BLACK);
            tvRecordTimeEnd.setTextColor(Color.BLACK);
            btnRecord.setText("未登录");
        }
    }

    private void record(boolean isStart, boolean showDialog) {
        if (showDialog) {
            showDialog(null, "更新打卡记录！", new DialogInterface() {
                @Override
                public void sureOnClick() {
                    String time = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                    RecordBean.updateBean(getYMD(calendarView.getSelectedCalendar()), isStart ? time : null, isStart ? null : time, null, new CallBack() {
                        @Override
                        public void success(Object... values) {
                            showInfo(calendarView.getSelectedCalendar());
                        }

                        @Override
                        public void fail(String values) {
                            ToastUtil.toast(getContext(), "打卡失败：" + values);
                        }
                    });
                }

                @Override
                public void cancelOnClick() {

                }
            });
        } else {
            String time = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
            RecordBean.updateBean(getYMD(calendarView.getSelectedCalendar()), isStart ? time : null, isStart ? null : time, null, new CallBack() {
                @Override
                public void success(Object... values) {
                    showInfo(calendarView.getSelectedCalendar());
                }

                @Override
                public void fail(String values) {
                    ToastUtil.toast(getContext(), "打卡失败：" + values);
                }
            });
        }
    }

    /**
     * 获取年月日
     *
     * @param calendar
     * @return
     */
    public String getYMD(Calendar calendar) {
        return calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
    }

}
