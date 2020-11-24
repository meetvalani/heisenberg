package com.example.sharemeui

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_wifi.*


class WifiFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wifi, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val wifi = this.context?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifi.isWifiEnabled) {
            wifi.isWifiEnabled = true
        }
        scannerIcon.setOnClickListener {
            val intent = Intent(this.context, CodeScanner::class.java).apply {
            }
            startActivity(intent)
        }

    }

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }
}