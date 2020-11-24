package com.example.sharemeui

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.fragment_htsp.*


// TODO: Customize parameter argument names

class HtspFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_htsp, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val wifi = this.context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifi.isWifiEnabled) {
            wifi.isWifiEnabled = false
        }
//        TODO() -> Siddharth/Meet
//        find a way to enable hotspot for version who have < 26 API support
//        below code will work on if android have >= 26 API support
        wifi.startLocalOnlyHotspot(
            @RequiresApi(Build.VERSION_CODES.O)
            object : WifiManager.LocalOnlyHotspotCallback() {
                override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation) {
                    Toast.makeText(context, "Hotspot started", Toast.LENGTH_SHORT).show()
                    super.onStarted(reservation)
                    var hotspotReservation = reservation
                    val currentConfig: WifiConfiguration = hotspotReservation.getWifiConfiguration()
                    Log.d(tag,"Local Hotspot Started")
                    Log.d(tag,currentConfig.toString())
                    Log.d(tag,"password is " + currentConfig.preSharedKey)
                    htsp_qr.visibility = View.VISIBLE
                    val qrgEncoder = QRGEncoder("ssid:"+currentConfig.SSID+",preSharedKey:"+currentConfig.preSharedKey, null, QRGContents.Type.TEXT, 1)
                    htsp_qr.setImageBitmap(qrgEncoder.encodeAsBitmap())
                }
                override fun onStopped() {
                    super.onStopped()
                    Log.d(tag,"Local Hotspot Stopped")
                }

                override fun onFailed(reason: Int) {
                    super.onFailed(reason)
                    Log.d(tag,"Local Hotspot failed to start")
                }
            }, Handler()
        )
    }
    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }
}