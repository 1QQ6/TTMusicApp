package com.wyq.ttmusicapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager


/**
 * Created by Roman on 2021/2/9
 */
object NetworkUtils {
    /**
     * 检查网络是否连接
     * @param context 全局context
     * @return true 已连接 false 未连接
     */
    fun checkNetworkConnect(context: Context?): Boolean? {
        if (context != null) {
            val mConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

    /**
     * 获取当前的网络状态
     * @param context 全局context
     * @return
     * 没有网络-NO
     * WIFI网络-WIFI
     * 4G网络-4G
     * 3G网络-3G
     * 2G网络-2G
     * 未知-Unknown
     */
    fun getAPNType(context: Context): String? {
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
            ?: //无网络
            return "NO"
        val nType = networkInfo.type
        if (nType == ConnectivityManager.TYPE_WIFI) {       //wifi
            return "WIFI"
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            val nSubType = networkInfo.subtype
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                && !telephonyManager.isNetworkRoaming
            ) {
                "4G"
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0 || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_A || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA || nSubType == TelephonyManager.NETWORK_TYPE_HSUPA || nSubType == TelephonyManager.NETWORK_TYPE_HSPA || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_B || nSubType == TelephonyManager.NETWORK_TYPE_EHRPD || (nSubType == TelephonyManager.NETWORK_TYPE_HSPAP
                        && !telephonyManager.isNetworkRoaming)
            ) {
                "3G"
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS || nSubType == TelephonyManager.NETWORK_TYPE_EDGE || nSubType == TelephonyManager.NETWORK_TYPE_CDMA || nSubType == TelephonyManager.NETWORK_TYPE_1xRTT || (nSubType == TelephonyManager.NETWORK_TYPE_IDEN
                        && !telephonyManager.isNetworkRoaming)
            ) {
                "2G"
            } else {
                "Unknown"
            }
        }
        return "Unknown"
    }
}