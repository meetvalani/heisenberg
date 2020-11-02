package com.example.xender

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.LocalOnlyHotspotReservation
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions


class WiFiDirectBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: MainActivity
) : BroadcastReceiver() {
    val wifiManager = activity.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    var currentConfig: WifiConfiguration? = null
    var hotspotReservation: LocalOnlyHotspotReservation? = null

    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }
    override fun onReceive(context: Context, intent: Intent) {
        val tag = "OnMyReceive"
        val action: String = intent.action.toString()
        Log.d(tag, action)
        when (action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                when (state) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        Log.d(tag, "WIFI ENABLED")
//                        mainScreenContent.text = "WIFI ENABLED"
                    }
                    else -> {
                        // Wi-Fi P2P is not enabled
                        Log.d(tag, "Wi-Fi P2P is not enabled")
//                        mainScreenContent.text = "WIFI ENABLED"
                    }
                }
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                Log.d(tag, "PEER CHANGED")
                if (ActivityCompat.checkSelfPermission(
                        this.activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(this.activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        2021)
                }
                manager?.requestPeers(channel) { peers: WifiP2pDeviceList? ->

                    val devices = arrayOf(peers)
                    Log.d(tag, "found " + devices.size)

                    if (peers != null) {
                        Log.d(tag, "first " + peers.deviceList)
                    }
//                    val device: WifiP2pDevice = WifiP2pDeviceList[0]
                    val config = WifiP2pConfig()
                    config.deviceAddress = "d8:32:e3:68:78:46"
                    channel?.also { channel ->
                        manager?.connect(channel, config, object : WifiP2pManager.ActionListener {

                            override fun onSuccess() {
                                Log.d(tag,"connected Successfully")
                            }

                            override fun onFailure(reason: Int) {
                                Log.d(tag,"connection failed")
                            }
                        })
                    }
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this.activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(tag, "asking permission")
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            requestPermissions(this.activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                2021)

        }



            manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {

                override fun onSuccess() {
                    Log.d(tag, "SUCCESS CHECKING AVAILABLE PEER")
                }

                override fun onFailure(reasonCode: Int) {
                    Log.d(tag, "FAILED CHECKING AVAILABLE PEER")
                }
            })


        val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        Log.d(tag, "PEER CHANGED")
        if (ActivityCompat.checkSelfPermission(
                this.activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(this.activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                2021)
        }
        Log.d("onMy res", "Starting connecting")

        val config = WifiP2pConfig()
        config.deviceAddress = "d8:32:e3:68:78:46"
        Log.d("onMy res", "processing connecting")
        channel?.also { channel ->
            manager?.connect(channel, config, object : WifiP2pManager.ActionListener {

                override fun onSuccess() {
                    Log.d("onMy res","onMy res connected Successfully")
                }

                override fun onFailure(reason: Int) {
                    Log.d("onMy res","onMy res connection failed")
                }
            })
        }


        if (success) {
            scanSuccess()
        } else {
            scanFailure()
        }

    }
    private fun scanSuccess() {
        val results = wifiManager.scanResults
        Log.d("onMy res", results.toString() + results.size.toString())
    }
    private fun scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        val results = wifiManager.scanResults
        Log.d("onMy res failed", results.toString() + results.size.toString())

    }

}