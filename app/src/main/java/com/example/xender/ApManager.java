package com.example.xender;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;

public class ApManager {


    public static final String TAG = "onMyCreate -> ApManager";
    private final WifiManager mWifiManager;
    private Context context;

    public ApManager(Context context) {
        this.context = context;
        mWifiManager = (WifiManager) this.context.getSystemService(this.context.WIFI_SERVICE);
    }
    public void showWritePermissionSettings(boolean force) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (force || !Settings.System.canWrite(this.context)) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.context.startActivity(intent);
            }
        }
    }
    //check whether wifi hotspot on or off
    public boolean isApOn() {
        try {
            Method method = mWifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(mWifiManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // Turn wifiAp hotspot on
    public boolean turnWifiApOn() {
        WifiConfiguration wificonfiguration = null;
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(mWifiManager, wificonfiguration, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Turn wifiAp hotspot off
    public boolean turnWifiApOff() {
        WifiConfiguration wificonfiguration = null;
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(mWifiManager, wificonfiguration, false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean createNewNetwork(String ssid, String password) {
        mWifiManager.setWifiEnabled(false); // turn off Wifi
        Log.d(TAG, "turning off wifi" + isApOn());
        if (isApOn()) {
            Log.d(TAG, "in turnWifiApOff");
            turnWifiApOff();
            Log.d(TAG, "turnWifiApOff returning off");
//            return false;
        } else {
            Log.e(TAG, "WifiAp is turned off");

        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);

        // creating new wifi configuration
        Log.d(TAG, "started hotspot creation");
        WifiConfiguration myConfig = new WifiConfiguration();
        myConfig.SSID = ssid; // SSID name of netwok
//        myConfig.preSharedKey = password; // password for network
        myConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE); // 4 is for KeyMgmt.WPA2_PSK which is not exposed by android KeyMgmt class
        myConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN); // Set Auth Algorithms to open
        try {
//            WifiManager wifi = (WifiManager) this.context.getSystemService(this.context.WIFI_SERVICE);
//
//            Method getWifiConfig = wifi.getClass().getMethod("getWifiApConfiguration", null);
//            WifiConfiguration myConfig1 = (WifiConfiguration) getWifiConfig.invoke(wifi,null);
//
//            myConfig1.SSID = "Hello World";
//
//            Method setWifiConfig = wifi.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
//            setWifiConfig.invoke(wifi,new Object[]{myConfig1,true});
//
//            Method enableWifi = wifi.getClass().getMethod("setWifiEnabled", WifiConfiguration.class, boolean.class);
//            enableWifi.invoke(wifi,null,true);
//            return true;
            Log.d(TAG, "trying hotspot on 1");
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            Log.d(TAG, "trying hotspot on 2");
            return (Boolean) method.invoke(mWifiManager, myConfig, true);  // setting and turing on android wifiap with new configrations
        } catch (Exception e) {
            Log.d(TAG, "hotspot exception");
            e.printStackTrace();
        }
        Log.d(TAG, "hotspot failedreturn false");
        return false;

    }


}