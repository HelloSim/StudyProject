package com.sim.test.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import com.sim.test.R;
import com.sim.test.activity.base.BaseActivity;
import com.sim.test.view.Dialog.MyDateDialog;
import com.sim.test.view.Dialog.MyUserDialog;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 一些Dialog
 */
public class DialogActivity extends BaseActivity {

    @BindView(R.id.dialog_ordinary)
    Button dialog_ordinary;
    @BindView(R.id.dialog_schedule)
    Button dialog_schedule;
    @BindView(R.id.dialog_date)
    Button dialog_date;
    @BindView(R.id.dialog_time)
    Button dialog_time;
    @BindView(R.id.dialog_list)
    Button dialog_list;
    @BindView(R.id.dialog_select)
    Button dialog_select;
    @BindView(R.id.dialog_selects)
    Button dialog_selects;
    @BindView(R.id.dialog_customize)
    Button dialog_customize;
    @BindView(R.id.dialog_customize_date)
    Button dialog_customize_date;

    @BindView(R.id.bt_success)
    Button bt_succsee;
    @BindView(R.id.bt_warning)
    Button bt_warning;
    @BindView(R.id.bt_error)
    Button bt_error;
    @BindView(R.id.bt_info)
    Button bt_info;
    @BindView(R.id.bt_default)
    Button bt_default;
    @BindView(R.id.bt_confusing)
    Button bt_confusing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.dialog_ordinary, R.id.dialog_schedule, R.id.dialog_date, R.id.dialog_time, R.id.dialog_list, R.id.dialog_select, R.id.dialog_selects, R.id.dialog_customize, R.id.dialog_customize_date,
            R.id.bt_success, R.id.bt_warning, R.id.bt_error, R.id.bt_info, R.id.bt_default, R.id.bt_confusing})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_ordinary:
                new AlertDialog.Builder(this)
                        //设置对话框的图标
                        .setIcon(R.mipmap.icon)
                        //设置对话框的标题
                        .setTitle("普通对话框")
                        //设置对话框的信息
                        .setMessage("正在拼命加载中")
                        //取消按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了取消", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //OK按钮
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了OK", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //关闭按钮
                        .setNeutralButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了关闭", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //创建对话框
                        .create()
                        //显示对话框
                        .show();
                break;

            case R.id.dialog_schedule:
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setIcon(R.mipmap.icon);//设置对话框的图标
                progressDialog.setTitle("进度对话框");//设置对话框的标题
                progressDialog.setMessage("正在拼命加载中");//设置对话框的信息
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置对话框的样式
                progressDialog.setMax(100);//设置最大进度
                progressDialog.setProgress(0);//设置当前进度
                progressDialog.show();//显示对话框
                /***
                 * millisInFuture 总时间
                 * countDownInterval 间隔时间
                 */
                new CountDownTimer(5100, 1000) {
                    //每次执行的方法
                    @Override
                    public void onTick(long millisUntilFinished) {
                        progressDialog.incrementProgressBy(20);//增加进度
                    }

                    //执行完毕后的方法
                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();//关闭对话框
                    }
                }.start();
                //ProgressDialog同样也可以设置三个按钮
                break;

            case R.id.dialog_date:
                Calendar calendarDate = Calendar.getInstance();//获得日历类对象
                /***
                 * contexrt 上下文
                 * listener 监听器
                 * year 年份
                 * month 月份
                 * dayOfMonth 天数
                 */
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TastyToast.makeText(DialogActivity.this, year + "年" + (month + 1) + "月" + dayOfMonth + "日", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    }
                }, calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.dialog_time:
                Calendar calendarTime = Calendar.getInstance();
                /***
                 * contexrt 上下文
                 * listener 监听器
                 * hourOfDay 小时
                 * minute 分钟
                 * is24HourView 是否是24小时制
                 */
                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        TastyToast.makeText(DialogActivity.this, hourOfDay + "点" + minute + "分", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                    }
                }, calendarTime.get(Calendar.HOUR), calendarTime.get(Calendar.MINUTE), true).show();
                break;

            case R.id.dialog_list:
                final String[] datasList = {"情网", "风继续吹", "饿狼传说", "当年情", "李香兰", "春夏秋冬", "吻别", "爱慕"};//数据源
                new AlertDialog.Builder(this)
                        //设置对话框的图标
                        .setIcon(R.mipmap.icon)
                        //设置对话框的标题
                        .setTitle("普通对话框")
                        //设置对话框的信息
                        //adapter 适配器；listener 监听器
                        .setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, datasList), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了" + datasList[which], TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //取消按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了取消", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //OK按钮
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了OK", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //关闭按钮
                        .setNeutralButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了关闭按钮", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //创建对话框
                        .create()
                        //显示对话框
                        .show();
                break;

            case R.id.dialog_select:
                final String[] datasSelect = {"情网", "风继续吹", "饿狼传说", "当年情", "李香兰", "春夏秋冬", "吻别", "爱慕"};
                new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("单选列表对话框")
                        //选择提示
                        //items 选项；checkedItem 默认选项；listener 监听器
                        .setSingleChoiceItems(datasSelect, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "选择了" + datasSelect[which], TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //取消按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了取消", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //OK按钮
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了OK", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //关闭按钮
                        .setNeutralButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了关闭", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //创建对话框
                        .create()
                        //显示对话框
                        .show();
                break;

            case R.id.dialog_selects:
                final String[] datasSelects = {"情网", "风继续吹", "饿狼传说", "当年情", "李香兰", "春夏秋冬", "吻别", "爱慕"};
                boolean[] flag = {false, false, false, false, false, false, false, false};
                new AlertDialog.Builder(this)
                        //设置对话框的图标
                        .setIcon(R.mipmap.ic_launcher)
                        //设置对话框的标题
                        .setTitle("多选列表对话框")
                        //选中提示
                        //items 选项；checkedItems 选项是否被选中；listener 监听器
                        .setMultiChoiceItems(datasSelects, flag, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    TastyToast.makeText(DialogActivity.this, "选择了" + datasSelects[which], TastyToast.LENGTH_SHORT, TastyToast.INFO);
                                } else {
                                    TastyToast.makeText(DialogActivity.this, "取消了" + datasSelects[which], TastyToast.LENGTH_SHORT, TastyToast.INFO);
                                }
                            }
                        })
                        //添加取消按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了取消", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //添加OK按钮
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了OK", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //添加关闭按钮
                        .setNeutralButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TastyToast.makeText(DialogActivity.this, "点击了关闭", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        //创建对话框
                        .create()
                        //显示对话框
                        .show();
                break;

            case R.id.dialog_customize:
                MyUserDialog myDialog = new MyUserDialog(this);
                //获得窗口对象
                Window window = myDialog.getWindow();
                //设置透明背景色
                window.setBackgroundDrawableResource(android.R.color.transparent);
                //获得窗口参数对象
                WindowManager.LayoutParams attributes = window.getAttributes();
                //设置宽
                attributes.width = 400;
                //设置高
                attributes.height = 80;
                //将窗口参数设置给窗口
                window.setAttributes(attributes);
                //显示对话框
                myDialog.show();
                break;

            case R.id.dialog_customize_date:
                new MyDateDialog(this).show();
                break;
            case R.id.bt_success:
                TastyToast.makeText(this, "这是成功SUCCESS的Toast提示", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                break;
            case R.id.bt_warning:
                TastyToast.makeText(this, "这是警告WARNING的Toast提示", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                break;
            case R.id.bt_error:
                TastyToast.makeText(this, "这是错误ERROR的Toast提示", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                break;
            case R.id.bt_info:
                TastyToast.makeText(this, "这是信息INFO的Toast提示", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                break;
            case R.id.bt_default:
                TastyToast.makeText(this, "这是默认DEFAULT的Toast提示", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
                break;
            case R.id.bt_confusing:
                TastyToast.makeText(this, "这是模糊CONFUSING的Toast提示", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
                break;
        }
    }

}
