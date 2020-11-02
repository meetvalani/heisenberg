package com.example.xender

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.LocalOnlyHotspotCallback
import android.net.wifi.WifiManager.LocalOnlyHotspotReservation
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }
    var channel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null
    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val tag = "onMyCreate"
        val context = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainScreenContent.text = "we are gonna do it"
        channel = manager?.initialize(this, mainLooper, null)
        val wifi =
            getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager

        channel?.also { channel ->
            receiver = WiFiDirectBroadcastReceiver(manager!!, channel, this)
        }

        val wifiManager = context.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager





        htspBtn.setOnClickListener {
            if(wifiManager.isWifiEnabled())
            {
                wifiManager.setWifiEnabled(false);
            }
            Log.d(tag, "htsp btn click")
            wifi.startLocalOnlyHotspot(
                object : LocalOnlyHotspotCallback() {
                    override fun onStarted(reservation: LocalOnlyHotspotReservation) {
                        Toast.makeText(context, "Hotspot started", Toast.LENGTH_SHORT).show()
                        super.onStarted(reservation)
                        var hotspotReservation = reservation
                        val currentConfig: WifiConfiguration = hotspotReservation.getWifiConfiguration()
                        Log.d(tag,"Local Hotspot Started")
                        Log.d(tag,currentConfig.toString())
                        Log.d(tag,"password is " + currentConfig.preSharedKey)
                        imageView2.visibility = View.VISIBLE
                        val qrgEncoder =
                            QRGEncoder("ssid:"+currentConfig.SSID+",preSharedKey:"+currentConfig.preSharedKey, null, QRGContents.Type.TEXT, 1)
                        imageView2.setImageBitmap(qrgEncoder.encodeAsBitmap())
                    }
                    override fun onStopped() {
                        super.onStopped()
                        Log.d(tag,"Local Hotspot Stopped")
                    }

                    override fun onFailed(reason: Int) {
                        super.onFailed(reason)
                        Log.d(tag,"Local Hotspot failed to start")
                    }
                }, Handler())
            wifi.setWifiEnabled(false)
        }





        wifiBtn.setOnClickListener {
            imageView2.visibility = View.INVISIBLE
            if (wifi.isWifiEnabled) {
            Toast.makeText(this, "Wifi is Already Enable", Toast.LENGTH_SHORT).show()
//            channel?.also { channel ->
//                receiver = WiFiDirectBroadcastReceiver(manager!!, channel, this)
//            }
        } else {
            Toast.makeText(this, "Wifi is Disable, please Allow to Turn it On", Toast.LENGTH_SHORT).show()
            wifi.isWifiEnabled = true
            Toast.makeText(this, "Wifi is Enabled Now", Toast.LENGTH_SHORT).show()
//            channel?.also { channel ->
//                receiver = WiFiDirectBroadcastReceiver(manager!!, channel, this)
//            }
        }
            val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
            scannerView.visibility = View.VISIBLE
            htspBtn.visibility = View.INVISIBLE
            wifiBtn.visibility = View.INVISIBLE

            codeScanner = CodeScanner(this, scannerView)

            // Parameters (default values)
            codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
            // ex. listOf(BarcodeFormat.QR_CODE)
            codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
            codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
            codeScanner.isFlashEnabled = false // Whether to enable flash or not

            // Callbacks
            codeScanner.decodeCallback = DecodeCallback {
                runOnUiThread {
                    Toast.makeText(this, "Scan completed, connectiong....", Toast.LENGTH_LONG).show()
                    val data = it.text
                    val configs = data.split(",")
                    if (configs.size < 2) {
                        Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
                        Log.d(tag, "Error in getting config, only one found")
                    }
                    if (configs[0].split(':').size < 2 || configs[1].split(':').size < 2) {
                        Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
                        Log.d(tag, "Error in getting values, only one found")
                    }
                    val ssid = configs[0].split(':')[1]
                    val preSharedKey = configs[1].split(':')[1]
                    Log.d(tag, "SSID : ${ssid} and PASS : ${preSharedKey}")
                    scannerView.visibility = View.INVISIBLE
                    val conf = WifiConfiguration()
                    conf.SSID = "\"" + ssid + "\"";
                    conf.preSharedKey = "\""+ preSharedKey +"\"";
                    val wifiManager =
                        context.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                    wifiManager.addNetwork(conf)


                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Log.d(tag, "permission not given")
                    }
                    val list = wifiManager.configuredNetworks
                    for (i in list) {
                        if(i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                            Log.d(tag, "found network")
                            try {
                                wifiManager.disconnect()
                                wifiManager.enableNetwork(i.networkId, true)
                                Log.d(tag,"connecting to "+ i.toString())
                                val connResult =  wifiManager.reconnect().toString()
                                if (connResult == "true") {
                                    Log.d(tag,"connected succesfully")
                                    Toast.makeText(this, "connected to ${ssid}", Toast.LENGTH_LONG).show()
                                    htspBtn.visibility = View.INVISIBLE
                                    wifiBtn.visibility = View.INVISIBLE
                                }
                                else {

                                    htspBtn.visibility = View.INVISIBLE
                                    wifiBtn.visibility = View.INVISIBLE
                                }
                                break
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }



                }
            }
            codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                runOnUiThread {
                    Toast.makeText(this, "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
            codeScanner.startPreview()

            Log.d(tag, wifiManager.dhcpInfo.toString())
            Log.d(tag, wifiManager.is5GHzBandSupported.toString())

            /*
            val success = wifi.startScan()
            Log.d(tag, "tring on" + success)
            if (success) {
                Log.d(tag, "tring on 1")
                // scan failure handling
                val results = wifiManager.scanResults
                Log.d(tag, results.toString() + results.size.toString())
                val config = WifiP2pConfig()
                config.deviceAddress = "d8:32:e3:68:78:46"
                Log.d(tag, "processing connecting")
                channel?.also { channel ->
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                    }
                    manager?.connect(channel, config, object : WifiP2pManager.ActionListener {

                        override fun onSuccess() {
                            Log.d(tag,"onMy res connected Successfully")
                        }

                        override fun onFailure(reason: Int) {
                            Log.d(tag,"onMy res connection failed")
                        }
                    })
                }
            }
            */
        }




//        val wifiScanReceiver = object : BroadcastReceiver() {
//
//            override fun onReceive(context: Context, intent: Intent) {
//                val success = intent.getBooleanExtra(wifiManager.EXTRA_RESULTS_UPDATED, false)
//                if (success) {
//                    val results = wifiManager.scanResults
//                } else {
//                   val results = wifiManager.scanResults
//                }
//            }
//        }
//
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(wifi.SCAN_RESULTS_AVAILABLE_ACTION)
//        context.registerReceiver(wifiScanReceiver, intentFilter)

//        Log.d(tag, "tring on")


//        Log.d(tag, "connectionInfo :-" + wifi.connectionInfo.toString())
//        Log.d(tag, wifi.dhcpInfo.toString())
//        Log.d(tag, wifi.isP2pSupported.toString())
//        Log.d(tag, wifi.isWifiEnabled.toString())
//        Log.d(tag, wifi.wifiState.toString())
//        Log.d(tag, "list" + wifi.scanResults.toString())



    }

    override fun onResume() {
        super.onResume()
        receiver?.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }
//        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        receiver?.also { receiver ->
            unregisterReceiver(receiver)
        }
//        codeScanner.releaseResources()
    }
}