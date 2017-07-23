package com.healthyfish.healthyfish.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
/**
 * 描述：判断网络连接工具
 * 作者：LYQ on 2017/7/21.
 * 邮箱：feifanman@qq.com
 * 编辑：LYQ
 */

public class NetworkConnectUtils {

    /**
     * 判断网络连接是否可用
     * @param context 上下文
     * @return boolean值
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断当前连接方式是否为Wifi连接
     * @param context 上下文
     * @return boolean值
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断当前连接方式是否为Mobile连接
     * @param context 上下文
     * @return boolean值
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 跳转到Wifi设置页面
     * @param context 上下文
     */
    public static void goWifiSettings(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    /**
     * 跳转到无线网络设置页面
     * @param context 上下文
     */
    public static void goWirelessSettings(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }

    /**
     * 跳转到流量设置页面
     * @param context 上下文
     */
    public static void goDataRoamingSettings(Context context) {
        context.startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
    }
}
