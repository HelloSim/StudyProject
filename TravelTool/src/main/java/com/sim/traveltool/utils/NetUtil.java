package com.sim.traveltool.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtil {

    private static final String TAG = "Sim";

    //没有连接网络 
    private static final int NETWORK_NONE = -1;
    //移动网络 
    private static final int NETWORK_MOBILE = 0;
    //无线网络 
    private static final int NETWORK_WIFI = 1;

    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                Log.d(TAG, "getNetWorkState: WIFI");
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_ETHERNET)) {
                Log.d(TAG, "getNetWorkState: MOBILE");
                return NETWORK_MOBILE;
            }
        } else {
            Log.d(TAG, "getNetWorkState: NONETWORK");
            return NETWORK_NONE;
        }
        Log.d(TAG, "getNetWorkState: NONETWORK");
        return NETWORK_NONE;
    }

}
