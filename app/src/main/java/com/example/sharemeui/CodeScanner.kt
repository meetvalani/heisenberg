package com.example.sharemeui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.*
import com.budiyev.android.codescanner.CodeScanner
import kotlinx.android.synthetic.main.activity_code_scanner.*


class CodeScanner : AppCompatActivity() {
    val TAG = "CodeScanner"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_scanner)

        val scannerView = scanner_view
        val codeScanner = CodeScanner(this, scannerView)
        scannerView.visibility = View.VISIBLE
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            this.runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        codeScanner.startPreview()

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            this?.runOnUiThread {
                Toast.makeText(this, "Scan completed, connectiong....", Toast.LENGTH_LONG).show()
                val data = it.text
                val configs = data.split(",")
                if (configs.size < 2) {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Error in getting config, only one found")
                }
                if (configs[0].split(':').size < 2 || configs[1].split(':').size < 2) {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Error in getting values, only one found")
                }
                val ssid = configs[0].split(':')[1]
                val preSharedKey = configs[1].split(':')[1]
                Log.d(TAG, "SSID : ${ssid} and PASS : ${preSharedKey}")
                scannerView.visibility = View.INVISIBLE
                val conf = WifiConfiguration()
                conf.SSID = "\"" + ssid + "\"";
                conf.preSharedKey = "\""+ preSharedKey +"\"";
                val wifiManager = this.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                wifiManager.addNetwork(conf)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d(TAG, "permission not given")
                }
                val list = wifiManager.configuredNetworks
                for (i in list) {
                    if(i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                        Log.d(TAG, "found network")
                        try {
                            wifiManager.disconnect()
                            wifiManager.enableNetwork(i.networkId, true)
                            Log.d(TAG,"connecting to "+ i.toString())
                            val connResult =  wifiManager.reconnect().toString()
                            val data = Intent()
                            data.putExtra("myData2", "Data 2 value")
                            if (connResult == "true") {
                                Log.d(TAG, "server ip :- ${Formatter.formatIpAddress(wifiManager.dhcpInfo.serverAddress)}")
//                                intToInetAddress(wifiManager.getDhcpInfo().serverAddress);
                                Log.d(TAG,"connected succesfully")
                                data.putExtra("status", "connected")
                                data.putExtra("ip", Formatter.formatIpAddress(wifiManager.dhcpInfo.serverAddress))
                            } else {
                                Log.d(TAG,"error in connection")
                                data.putExtra("status", "disConnected")
                            }
                            setResult(Activity.RESULT_OK, data)
                            finish()
                            break
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

    }
}