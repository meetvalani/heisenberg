package com.example.xender;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.util.Log;

/**
 * This class is use to handle all Hotspot related information.
 *
 *
 *
 */
public class WifiApControl {
    private static Method getWifiApState;
    private static Method isWifiApEnabled;
    private static Method setWifiApEnabled;
    private static Method getWifiApConfiguration;

    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";

    public static final int WIFI_AP_STATE_DISABLED = WifiManager.WIFI_STATE_DISABLED;
    public static final int WIFI_AP_STATE_DISABLING = WifiManager.WIFI_STATE_DISABLING;
    public static final int WIFI_AP_STATE_ENABLED = WifiManager.WIFI_STATE_ENABLED;
    public static final int WIFI_AP_STATE_ENABLING = WifiManager.WIFI_STATE_ENABLING;
    public static final int WIFI_AP_STATE_FAILED = WifiManager.WIFI_STATE_UNKNOWN;

    public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = WifiManager.EXTRA_PREVIOUS_WIFI_STATE;
    public static final String EXTRA_WIFI_AP_STATE = WifiManager.EXTRA_WIFI_STATE;

    static {
        // lookup methods and fields not defined publicly in the SDK.
        Class<?> cls = WifiManager.class;
        for (Method method : cls.getDeclaredMethods()) {
            String methodName = method.getName();
            if (methodName.equals("getWifiApState")) {
                getWifiApState = method;
            } else if (methodName.equals("isWifiApEnabled")) {
                isWifiApEnabled = method;
            } else if (methodName.equals("setWifiApEnabled")) {
                setWifiApEnabled = method;
            } else if (methodName.equals("getWifiApConfiguration")) {
                getWifiApConfiguration = method;
            }
        }
    }

    public static boolean isApSupported() {

            Log.d("onmy1", "0");

        if (getWifiApState == null) {
            Log.d("onmy1", "1")  ;
        }
        if (isWifiApEnabled == null) {
            Log.d("onmy1", "2");
        }
        if (setWifiApEnabled == null) {
            Log.d("onmy1", "3");
        }
        if (getWifiApConfiguration == null) {
            Log.d("onmy1", "4");
        }
        return (getWifiApState != null && isWifiApEnabled != null
                && setWifiApEnabled != null && getWifiApConfiguration != null);
    }

    private WifiManager mgr;

    private WifiApControl(WifiManager mgr) {
        this.mgr = mgr;
    }

    public static WifiApControl getApControl(WifiManager mgr) {
        Log.d("onmy1", String.valueOf(isApSupported()));
        if (!isApSupported())
            return null;
        return new WifiApControl(mgr);
    }

    public boolean isWifiApEnabled() {
        try {
            return (Boolean) isWifiApEnabled.invoke(mgr);
        } catch (Exception e) {
            Log.v("BatPhone", e.toString(), e); // shouldn't happen
            return false;
        }
    }

    public int getWifiApState() {
        try {
            return (Integer) getWifiApState.invoke(mgr);
        } catch (Exception e) {
            Log.v("BatPhone", e.toString(), e); // shouldn't happen
            return -1;
        }
    }

    public WifiConfiguration getWifiApConfiguration() {
        try {
            return (WifiConfiguration) getWifiApConfiguration.invoke(mgr);
        } catch (Exception e) {
            Log.v("BatPhone", e.toString(), e); // shouldn't happen
            return null;
        }
    }

    public boolean setWifiApEnabled(WifiConfiguration config, boolean enabled) {
        try {
            return (Boolean) setWifiApEnabled.invoke(mgr, config, enabled);
        } catch (Exception e) {
            Log.v("BatPhone", e.toString(), e); // shouldn't happen
            return false;
        }
    }

    public static void turnOnOffHotspot(Context context, boolean isTurnToOn) {
        ConnectivityManager cman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Method[] methods = cman.getClass().getMethods();

        Boolean result;
        try
        {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);

            wifiManager.setWifiEnabled(false);
            Method enableWifi = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            String ssid  =   "ss" ;//your SSID
            String pass  =   " " ;// your Password
            WifiConfiguration  myConfig =  new WifiConfiguration();
            myConfig.SSID = ssid;
            myConfig.preSharedKey  = pass ;
            myConfig.status = WifiConfiguration.Status.ENABLED;
            myConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            myConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            myConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            myConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            myConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            myConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            myConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            result = (Boolean) enableWifi.invoke(wifiManager, myConfig, true);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = false;
        }


//        WifiApControl apControl = WifiApControl.getApControl(wifiManager);
//        Log.d("onmy1", "in 0");
//        if (apControl != null) {
//            Log.d("onmy1", "in 1");
//            // TURN OFF YOUR WIFI BEFORE ENABLE HOTSPOT
//            //if (isWifiOn(context) && isTurnToOn) {
//            //  turnOnOffWifi(context, false);
//            //}
//
//            apControl.setWifiApEnabled(apControl.getWifiApConfiguration(),
//                    isTurnToOn);
//        }
    }

}