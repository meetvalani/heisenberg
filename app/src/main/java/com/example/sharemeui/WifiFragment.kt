package com.example.sharemeui

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_wifi.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class WifiFragment : BottomSheetDialogFragment() {
    val TAG = "WifiFragment"
    val CODE_SCANNER_CODE = 1
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
            startActivityForResult(intent, CODE_SCANNER_CODE)
        }

    }

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CODE_SCANNER_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                if (data.hasExtra("status")) {
                    val status = data.getStringExtra("status")
                    Log.d(TAG, "connection status:- $status")
                    if (status.equals("connected")) {
                        Log.d(TAG, "connection status:- $status")
                        this.dismiss();
                    } else {
                        Toast.makeText(this.context, "ERROR in Connecting, please try again",Toast.LENGTH_SHORT)
                    }
                }

            }
            else {
                // fail
            }
        }
    }
}