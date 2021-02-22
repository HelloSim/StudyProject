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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sim.baselibrary.base.BaseFragment;
import com.sim.baselibrary.bean.EventMessage;
import com.sim.baselibrary.callback.SuccessOrFailListener;
import com.sim.baselibrary.utils.LogUtil;
import com.sim.baselibrary.utils.SPUtil;
import com.sim.baselibrary.utils.TimeUtil;
import com.sim.baselibrary.utils.ToastUtil;
import com.sim.traveltool.AppHelper;
import com.sim.traveltool.R;
import com.sim.traveltool.bean.db.RecordBean;
import com.sim.traveltool.bean.db.User;
import com.sim.traveltool.ui.activity.RecordAllActivity;
import com.sim.traveltool.ui.view.TitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author Sim --- “打卡”Fragment
 */
public class RecordFragment extends BaseFragment implements CalendarView.OnMonthChangeListener,
        CalendarView.OnCalendarSelectListener {

    private TextView tvNowMonth;
    private CalendarView calendarView;

    private LinearLayout parent;
    private TitleView titleView;
    private TextView tvRecordTimeStart, tvRecordTimeEnd;
    private Button btnRecord;

    private User user;//已登录的用户信息
    private RecordBean recordBean;//当天的打卡数据

    //更多弹窗、添加备忘弹窗
    private PopupWindow morePopupWindow, otherPopupWindow;//弹窗
    private View moreLayout, otherLayout;//布局
    private EditText etOther;
    private Button btnAllRecord, btnOther, btnCancel, btnConfirm;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_record;
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
        if (SPUtil.contains(getContext(), AppHelper.userSpName, AppHelper.userSpStateKey)
                && ((boolean) SPUtil.get(getContext(), AppHelper.userSpName, AppHelper.userSpStateKey, false))
                && BmobUser.isLogin()) {
            user = BmobUser.getCurrentUser(User.class);
        }
        if (user == null) {
            tvRecordTimeStart.setText("未打卡");
            tvRecordTimeEnd.setText("未打卡");
            btnRecord.setText("未登录");
        } else {
            showInfo(calendarView.getSelectedCalendar());
        }

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        moreLayout = inflater.inflate(R.layout.view_popup_record_more, null);
        otherLayout = inflater.inflate(R.layout.view_popup_add_other, null);
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
        EventBus.getDefault().register(this);
    }

    @Override
    public void onMultiClick(View view) {
        if (view == btnAllRecord) {
            morePopupWindow.dismiss();
            if (user != null) {
                Intent intent = new Intent(getContext(), RecordAllActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putSerializable("calendar", calendarView.getSelectedCalendar());
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                ToastUtil.toast(getContext(), "未登录");
            }
        } else if (view == btnOther) {
            morePopupWindow.dismiss();
            if (user != null) {
                otherPopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            } else {
                ToastUtil.toast(getContext(), "未登录");
            }
        } else if (view == btnCancel) {
            otherPopupWindow.dismiss();
        } else if (view == btnConfirm) {
            otherPopupWindow.dismiss();
            if (user != null) {
                query(user, getYMD(calendarView.getSelectedCalendar()), new SuccessOrFailListener() {
                    @Override
                    public void success(Object... values) {
                        List<RecordBean> list = (List<RecordBean>) values[0];
                        if (list != null && list.size() > 0) {
                            RecordBean newRecordBean = new RecordBean(list.get(0).getUser(), list.get(0).getDate(), list.get(0).getYearAndMonth(), list.get(0).getStartTime(), list.get(0).getEndTime(), etOther.getText().toString());
                            newRecordBean.update(list.get(0).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                    } else {
                                        ToastUtil.toast(getContext(), "添加备忘失败！");
                                        LogUtil.d(getClass(), "修改指定日期备忘出错：" + e.getMessage());
                                    }
                                }
                            });
                        } else {
                            RecordBean newRecordBean = new RecordBean(user, getYMD(calendarView.getSelectedCalendar()), getYM(calendarView.getSelectedCalendar()), null, null, etOther.getText().toString());
                            newRecordBean.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        showInfo(calendarView.getSelectedCalendar());
                                    } else {
                                        ToastUtil.toast(getContext(), "添加备忘失败！");
                                        LogUtil.d(getClass(), "添加指定日期备忘出错：" + e.getMessage());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void fail(Object... values) {
                        LogUtil.e(getClass(), "修改指定日期备忘出错！");
                    }
                });
            } else {
                ToastUtil.toast(getContext(), "未登录");
            }
        } else if (view == btnRecord) {
            if (user != null) {
                if (calendarView.getSelectedCalendar().isCurrentDay()) {//是否当天
                    if (btnRecord.getText().equals("上班打卡")) {//上班卡
                        if (tvRecordTimeStart.getText().equals("未打卡")) {//未打上班卡
                            record(1);
                        } else {//已打上班卡
                            showDialog(null, "更新打卡记录！", "确认", "取消", new com.sim.baselibrary.callback.DialogInterface() {
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
                        if (tvRecordTimeEnd.getText().equals("未打卡")) {//未打下班卡
                            record(2);
                        } else {//已打下班卡
                            showDialog(null, "更新打卡记录！", "确认", "取消", new com.sim.baselibrary.callback.DialogInterface() {
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
                }
            } else {
                ToastUtil.toast(getContext(), "未登录");
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
        if (user != null) {
            query(user, getYMD(calendar), new SuccessOrFailListener() {
                @Override
                public void success(Object... values) {
                    ArrayList<RecordBean> list = (ArrayList<RecordBean>) values[0];
                    if (list != null && list.size() > 0) {
                        RecordBean selectedRecordBean = list.get(0);
                        if (calendar.isCurrentDay()) {
                            recordBean = selectedRecordBean;
                        }
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
                public void fail(Object... values) {
                    LogUtil.d(getClass(), "查询选中日期数据失败！");
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

    /**
     * 打卡
     *
     * @param type 1:上班卡 2:下班卡
     */
    private void record(int type) {
        if (user != null) {
            query(user, getYMD(calendarView.getSelectedCalendar()), new SuccessOrFailListener() {
                @Override
                public void success(Object... values) {
                    ArrayList<RecordBean> list = (ArrayList<RecordBean>) values[0];
                    if (list != null && list.size() > 0) {
                        recordBean = list.get(0);
                    }
                }

                @Override
                public void fail(Object... values) {
                    LogUtil.d(getClass(), "查询选中日期数据失败！");
                }
            });
            if (recordBean != null) {//已有当天数据
                if (type == 1) {//修改当天数据（上班）
                    String startTime = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                    RecordBean newRecordBean = new RecordBean(user, getYMD(calendarView.getSelectedCalendar()), getYM(calendarView.getSelectedCalendar()), startTime, recordBean.getEndTime(), recordBean.getOther());
                    newRecordBean.update(recordBean.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                showInfo(calendarView.getSelectedCalendar());
                            } else {
                                ToastUtil.toast(getContext(), "打卡失败！");
                                LogUtil.d(getClass(), "修改上班打卡时间失败：" + e.getMessage());
                            }
                        }
                    });
                } else {//修改当天数据（下班）
                    String endTime = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                    RecordBean newRecordBean = new RecordBean(user, getYMD(calendarView.getSelectedCalendar()), getYM(calendarView.getSelectedCalendar()), recordBean.getStartTime(), endTime, recordBean.getOther());
                    newRecordBean.update(recordBean.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                showInfo(calendarView.getSelectedCalendar());
                            } else {
                                ToastUtil.toast(getContext(), "打卡失败！");
                                LogUtil.d(getClass(), "修改下班打卡时间出错：" + e.getMessage());
                            }
                        }
                    });
                }
            } else {//没有当天数据
                if (type == 1) {//插入当天数据（上班）
                    String startTime = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                    RecordBean newRecordBean = new RecordBean(user, getYMD(calendarView.getSelectedCalendar()), getYM(calendarView.getSelectedCalendar()), startTime, null, null);
                    newRecordBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                showInfo(calendarView.getSelectedCalendar());
                            } else {
                                ToastUtil.toast(getContext(), "打卡失败！");
                                LogUtil.d(getClass(), "插入上班时间数据出错：" + e.getMessage());
                            }
                        }
                    });
                } else {//插入当天数据（下班）
                    String endTime = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
                    RecordBean newRecordBean = new RecordBean(user, getYMD(calendarView.getSelectedCalendar()), getYM(calendarView.getSelectedCalendar()), null, endTime, null);
                    newRecordBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                showInfo(calendarView.getSelectedCalendar());
                            } else {
                                ToastUtil.toast(getContext(), "打卡失败！");
                                LogUtil.d(getClass(), "插入下班时间数据出错：" + e.getMessage());
                            }
                        }
                    });
                }
            }
        } else {
            ToastUtil.toast(getContext(), "未登录");
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
    public void query(User user, String date, SuccessOrFailListener successOrFailListener) {
        BmobQuery<RecordBean> bmobQuery = new BmobQuery<RecordBean>();
        bmobQuery.addWhereEqualTo("user", user);
        bmobQuery.addWhereEqualTo("date", date);
        bmobQuery.findObjects(new FindListener<RecordBean>() {
            @Override
            public void done(List<RecordBean> list, BmobException e) {
                if (e == null) {
                    successOrFailListener.success(list);
                } else {
                    successOrFailListener.fail(e.getMessage());
                }
            }
        });
    }

    /**
     * 获取控制台最新JSON数据，不同步到缓存中
     */
    private void fetchUserJsonInfo() {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String json, BmobException e) {
                if (e == null) {
                    LogUtil.e(getClass(), "更新用户本地缓存信息成功");
                } else {
                    LogUtil.e(getClass(), "更新用户本地缓存信息失败---code:" + e.getErrorCode() + ";message:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 接收消息事件
     *
     * @param eventMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage eventMessage) {
        if (eventMessage.type == AppHelper.USER_IsLogIn) {
            user = BmobUser.getCurrentUser(User.class);
            User.fetchUserInfo();
            showInfo(calendarView.getSelectedCalendar());
        } else if (eventMessage.type == AppHelper.USER_noLogIn) {
            user = null;
            recordBean = null;
            tvRecordTimeStart.setText("未打卡");
            tvRecordTimeEnd.setText("未打卡");
            tvRecordTimeStart.setTextColor(Color.BLACK);
            tvRecordTimeEnd.setTextColor(Color.BLACK);
            btnRecord.setText("未登录");
        }
    }

}
