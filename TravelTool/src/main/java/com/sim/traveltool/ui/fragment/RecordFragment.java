package com.sim.traveltool.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.baselibrary.base.BaseFragment;
import com.sim.baselibrary.callback.SuccessOrFailListener;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.StringUtil;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.db.bean.RecordData;
import com.sim.traveltool.ui.activity.RecordAllActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @Auther Sim
 * @Time 2020/4/27 1:05
 * @Description “打卡”Fragment
 */
public class RecordFragment extends BaseFragment implements CalendarView.OnMonthChangeListener,
        CalendarView.OnCalendarSelectListener {

    private TextView tvNowMonth;
    private CalendarView calendarView;

    private LinearLayout parent;
    private ImageView ivMore;
    private TextView tvRecordTimeStart;
    private TextView tvRecordTimeEnd;
    private Button btnRecord;

    private boolean isLogIn = false;//是否登录
    private String userSpAccountNumber;//用户账号

    private Calendar calendar;//选中的日期
    private RecordData recordData;//当天的打卡数据

    //更多弹窗
    private PopupWindow morePopupWindow;//弹窗
    private View moreLayout;//布局
    private Button btnAllRecord;
    private Button btnOther;
    //添加备忘弹窗
    private PopupWindow otherPopupWindow;//弹窗
    private View otherLayout;//布局
    private EditText etOther;
    private Button btnCancel;
    private Button btnConfirm;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_record;
    }

    @Override
    protected void bindViews(View view) {
        tvNowMonth = view.findViewById(R.id.tv_now_year_and_month);
        calendarView = view.findViewById(R.id.calendarView);

        parent = view.findViewById(R.id.parent);
        ivMore = view.findViewById(R.id.iv_more);
        tvRecordTimeStart = view.findViewById(R.id.tv_record_time_start);
        tvRecordTimeEnd = view.findViewById(R.id.tv_record_time_end);
        btnRecord = view.findViewById(R.id.btn_record);
        setViewClick(ivMore, btnRecord);

        calendarView.setWeekStarWithSun();//设置星期日周起始
        calendarView.setWeeColor(Color.TRANSPARENT, Color.WHITE);//设置星期栏的背景、字体颜色
        calendarView.setThemeColor(Color.WHITE, Color.TRANSPARENT);//定制颜色：选中的标记颜色、标记背景色
        calendarView.setTextColor(Color.WHITE, Color.WHITE, Color.GRAY, Color.WHITE, Color.GRAY);//设置文本颜色：今天字体颜色、当前月份字体颜色、其它月份字体颜色、当前月份农历字体颜色、其它农历字体颜色
        calendarView.setOnMonthChangeListener(this);//月份改变事件监听
        calendarView.setOnCalendarSelectListener(this);//日期选择事件监听
    }

    @Override
    protected void initView(View view) {
        calendar = calendarView.getSelectedCalendar();
        tvNowMonth.setText(calendar.getYear() + "-" + calendar.getMonth());
        if (SPUtil.contains(getContext(), AppHelper.userSpName, AppHelper.userSpStateKey)) {
            isLogIn = (boolean) SPUtil.get(getContext(), AppHelper.userSpName, AppHelper.userSpStateKey, false);
            if (isLogIn) {
                if (SPUtil.contains(getContext(), AppHelper.userSpName, AppHelper.userSpAccountNumber)) {
                    userSpAccountNumber = (String) SPUtil.get(getContext(), AppHelper.userSpName, AppHelper.userSpAccountNumber, "");
                    showInfo(calendar, true);
                } else {
                    isLogIn = false;
                }
            } else {
                tvRecordTimeStart.setText(getString(R.string.record_no));
                tvRecordTimeEnd.setText(getString(R.string.record_no));
                btnRecord.setText(StringUtil.getContent(getString(R.string.login_no)));
            }
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        moreLayout = inflater.inflate(R.layout.view_popup_more, null);
        otherLayout = inflater.inflate(R.layout.view_popup_add_other, null);
        morePopupWindow = showPopupWindow(moreLayout, 200, 150);
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
    public void onMultiClick(View view) {
        if (view == ivMore) {
            morePopupWindow.showAsDropDown(ivMore, 0, 0);
        } else if (view == btnAllRecord) {
            morePopupWindow.dismiss();
            if (isLogIn) {
                Intent intent = new Intent(getContext(), RecordAllActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userSpAccountNumber", userSpAccountNumber);
                bundle.putSerializable("calendar", calendar);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                ToastUtil.T_Info(getContext(), StringUtil.getContent(getString(R.string.login_no)));
            }
        } else if (view == btnOther) {
            morePopupWindow.dismiss();
            if (isLogIn) {
                otherPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            } else {
                ToastUtil.T_Info(getContext(), StringUtil.getContent(getString(R.string.login_no)));
            }
        } else if (view == btnCancel) {
            otherPopupWindow.dismiss();
        } else if (view == btnConfirm) {
            otherPopupWindow.dismiss();
            if (isLogIn) {
                query(userSpAccountNumber, getYMD(calendarView.getSelectedCalendar()), new SuccessOrFailListener() {
                    @Override
                    public void success(Object... values) {
                        List<RecordData> list = (List<RecordData>) values[0];
                        if (list != null && list.size() > 0) {
                            RecordData newRecordData = new RecordData(list.get(0).getUserSpAccountNumber(), list.get(0).getDate(), list.get(0).getYearAndMonth(), list.get(0).getStartTime(), list.get(0).getEndTime(), etOther.getText().toString());
                            newRecordData.update(list.get(0).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        ToastUtil.T_Info(getContext(), StringUtil.getContent(getString(R.string.record_add_memo_success)));
                                    } else {
                                        ToastUtil.T_Info(getContext(), StringUtil.getContent(getString(R.string.record_add_memo_fail)));
                                        LogUtil.d(this.getClass(), "修改指定日期备忘出错：" + e.getMessage());
                                    }
                                }
                            });
                        } else {
                            RecordData newRecordData = new RecordData(userSpAccountNumber, getYMD(calendar), getYM(calendar), null, null, etOther.getText().toString());
                            newRecordData.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        showInfo(calendar, true);
                                        ToastUtil.T_Info(getContext(), (getString(R.string.record_add_memo_success)));
                                    } else {
                                        ToastUtil.T_Info(getContext(), (getString(R.string.record_add_memo_fail)));
                                        LogUtil.d(this.getClass(), "添加指定日期备忘出错：" + e.getMessage());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void fail(Object... values) {
                        LogUtil.e(this.getClass(), "修改指定日期备忘出错！");
                    }
                });
            } else {
                ToastUtil.T_Info(getContext(), StringUtil.getContent(getString(R.string.login_no)));
            }
        } else if (view == btnRecord) {
            if (isLogIn) {
                if (calendar.isCurrentDay()) {//是否当天
                    if (btnRecord.getText().equals(getString(R.string.record_start))) {//上班卡
                        if (tvRecordTimeStart.getText().equals(getString(R.string.record_no))) {//未打上班卡
                            record(1);
                        } else {//已打上班卡
                            showDialog(null, getString(R.string.record_update), getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.callback.DialogInterface() {
                                @Override
                                public void sureOnClick() {
                                    record(1);
                                }

                                @Override
                                public void cancelOnClick() {

                                }
                            });
                        }
                    } else {//下班卡
                        if (tvRecordTimeEnd.getText().equals(getString(R.string.record_no))) {//未打下班卡
                            record(2);
                        } else {//已打下班卡
                            showDialog(null, getString(R.string.record_update), getString(R.string.ok), getString(R.string.cancel), new com.sim.baselibrary.callback.DialogInterface() {
                                @Override
                                public void sureOnClick() {
                                    record(2);
                                }

                                @Override
                                public void cancelOnClick() {

                                }
                            });
                        }
                    }
                } else {
                    calendarView.scrollToCurrent(true);
                    ToastUtil.T_Info(getContext(), getString(R.string.record_only_today));
                }
            } else {
                ToastUtil.T_Info(getContext(), StringUtil.getContent(getString(R.string.login_no)));
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
        this.calendar = calendar;
    }

    /**
     * 选中日期更改时更新打卡记录显示
     *
     * @param calendar
     * @param isClick
     */
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        this.calendar = calendar;
        showInfo(calendar, false);
    }

    /**
     * 根据数据进行显示
     */
    private void showInfo(Calendar calendar, boolean inInit) {
        query(userSpAccountNumber, getYMD(calendar), new SuccessOrFailListener() {
            @Override
            public void success(Object... values) {
                ArrayList<RecordData> list = (ArrayList<RecordData>) values[0];
                if (list != null && list.size() > 0) {
                    RecordData selectedRecordData = list.get(0);
                    if (inInit) {
                        recordData = selectedRecordData;
                    }
                    tvRecordTimeStart.setText((selectedRecordData.getStartTime() == null || selectedRecordData.getStartTime().length() == 0) ? getString(R.string.record_no) : selectedRecordData.getStartTime());
                    tvRecordTimeEnd.setText((selectedRecordData.getEndTime() == null || selectedRecordData.getEndTime().length() == 0) ? getString(R.string.record_no) : selectedRecordData.getEndTime());
                    tvRecordTimeStart.setTextColor(selectedRecordData.isLate() ? Color.RED : Color.WHITE);
                    tvRecordTimeEnd.setTextColor(selectedRecordData.isLeaveEarly() ? Color.RED : Color.WHITE);
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
                    tvRecordTimeStart.setText(getString(R.string.record_no));
                    tvRecordTimeEnd.setText(getString(R.string.record_no));
                    tvRecordTimeStart.setTextColor(Color.WHITE);
                    tvRecordTimeEnd.setTextColor(Color.WHITE);
                    if (TimeUtil.getHour() >= 14) {
                        btnRecord.setText(getString(R.string.record_end));
                    } else {
                        btnRecord.setText(getString(R.string.record_start));
                    }
                }
            }

            @Override
            public void fail(Object... values) {
                LogUtil.d(this.getClass(), "查询选中日期数据失败！");
            }
        });
    }

    /**
     * 打卡
     *
     * @param type 1:上班卡 2:下班卡
     */
    private void record(int type) {
        if (recordData != null) {//已有当天数据
            if (type == 1) {//修改当天数据（上班）
                String startTime = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                RecordData newRecordData = new RecordData(userSpAccountNumber, getYMD(calendar), getYM(calendar), startTime, recordData.getEndTime(), recordData.getOther());
                newRecordData.update(recordData.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            showInfo(calendar, true);
                            ToastUtil.T_Info(getContext(), (getString(R.string.record_success)));
                        } else {
                            ToastUtil.T_Info(getContext(), (getString(R.string.record_failure)));
                            LogUtil.d(this.getClass(), "修改上班打卡时间失败：" + e.getMessage());
                        }
                    }
                });
            } else {//修改当天数据（下班）
                String endTime = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                RecordData newRecordData = new RecordData(userSpAccountNumber, getYMD(calendar), getYM(calendar), recordData.getStartTime(), endTime, recordData.getOther());
                newRecordData.update(recordData.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            showInfo(calendar, true);
                            ToastUtil.T_Info(getContext(), (getString(R.string.record_success)));
                        } else {
                            ToastUtil.T_Info(getContext(), (getString(R.string.record_failure)));
                            LogUtil.d(this.getClass(), "修改下班打卡时间出错：" + e.getMessage());
                        }
                    }
                });
            }
        } else {//没有当天数据
            if (type == 1) {//插入当天数据（上班）
                String startTime = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                RecordData newRecordData = new RecordData(userSpAccountNumber, getYMD(calendar), getYM(calendar), startTime, null, null);
                newRecordData.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            showInfo(calendar, true);
                            ToastUtil.T_Info(getContext(), (getString(R.string.record_success)));
                        } else {
                            ToastUtil.T_Info(getContext(), (getString(R.string.record_failure)));
                            LogUtil.d(this.getClass(), "插入上班时间数据出错：" + e.getMessage());
                        }
                    }
                });
            } else {//插入当天数据（下班）
                String endTime = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                RecordData newRecordData = new RecordData(userSpAccountNumber, getYMD(calendar), getYM(calendar), null, endTime, null);
                newRecordData.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            showInfo(calendar, true);
                            ToastUtil.T_Info(getContext(), (getString(R.string.record_success)));
                        } else {
                            ToastUtil.T_Info(getContext(), (getString(R.string.record_failure)));
                            LogUtil.d(this.getClass(), "插入下班时间数据出错：" + e.getMessage());
                        }
                    }
                });
            }
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

    /**
     * 获取年月
     *
     * @param calendar
     * @return
     */
    public String getYM(Calendar calendar) {
        return String.valueOf(calendar.getYear()) + calendar.getMonth();
    }

    /**
     * 查询指定用户和年月日数据
     *
     * @param date
     * @param successOrFailListener
     */
    public void query(String userSpAccountNumber, String date, SuccessOrFailListener successOrFailListener) {
        BmobQuery<RecordData> bmobQuery = new BmobQuery<RecordData>();
        bmobQuery.addWhereEqualTo("userSpAccountNumber", userSpAccountNumber);
        bmobQuery.addWhereEqualTo("date", date);
        bmobQuery.findObjects(new FindListener<RecordData>() {
            @Override
            public void done(List<RecordData> list, BmobException e) {
                if (e == null) {
                    successOrFailListener.success(list);
                } else {
                    successOrFailListener.fail(e.getMessage());
                }
            }
        });
    }

}
