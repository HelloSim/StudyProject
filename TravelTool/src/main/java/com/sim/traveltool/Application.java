package com.sim.traveltool;

import android.content.Context;

import com.sim.baselibrary.utils.CrashHandler;
import com.sim.traveltool.internet.APIFactory;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

/**
 * @Auther Sim
 * @Time 2020/4/27 1:05
 * @Description Application
 */
public class Application extends android.app.Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CrashHandler.getInstance().init(getApplicationContext());
        CrashReport.setUserId(getSeries());
        CrashReport.initCrashReport(context, AppHelper.Bugly_APPID, !BuildConfig.LOG_DEBUG);//Logcat的TAG=CrashReportInfo
        APIFactory.getInstance().init(this);
    }

    private String getSeries() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("/sys/class/net/eth0/address")))) {
            String ethernetMacAddress = input.readLine();
            return new BigInteger(ethernetMacAddress.trim().replace(":", ""), 16).toString(10);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
