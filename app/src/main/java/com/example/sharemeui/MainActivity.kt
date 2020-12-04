package com.example.sharemeui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sharemeui.ui.home.Util
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileReader

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    val TAG = "mainActivity"
    var isFabOpen = false
    val thisContext = this
    val authorizedServers = mutableListOf<String>()
    var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "mainActivity called on create")
        val SUBTAG = "oncreate"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("$TAG-$SUBTAG", "permission not granted" )
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1)
        }
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val htspFab: FloatingActionButton = findViewById(R.id.htspFab)
        val wifiFab: FloatingActionButton = findViewById(R.id.wifiFab)
        fab.setOnClickListener {
            if (isFabOpen) {
                htspFab.translationY = 200F
                wifiFab.translationY = 400F
                isFabOpen = !isFabOpen
            } else {
                htspFab.translationY = -200F
                wifiFab.translationY = -400F
                isFabOpen = !isFabOpen
            }
            Log.d("$TAG-$SUBTAG", "fab clicked")
        }
//        TODO() -> SIDDHARTH/MEET
//         all permisison check and enable location on hotspot
//         add loader in enabling and disabling hotspot and wifi
//         gracefully handle once completed connection on both serer and receiever

        htspFab.setOnClickListener {
            CoroutineScope(Main).launch {
                HtspFragment().apply {
                    show(supportFragmentManager, HtspFragment.TAG)
                }
            }
        }

        wifiFab.setOnClickListener {
            CoroutineScope(IO).launch {
                WifiFragment().apply {
                    show(supportFragmentManager, WifiFragment.TAG)
                }
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val util = Util(this)
        val sharedPreferences: SharedPreferences? = this?.getSharedPreferences("preferences",Context.MODE_PRIVATE) ?: null;
        CoroutineScope(IO).launch {  util.clearFullQueue() }
        if (sendButton !== null) {
            sendButton.setOnClickListener {
                CoroutineScope(IO).launch { util.startSendingFromQueue(authorizedServers) }
//                CoroutineScope(IO).launch { util.startSendingFromQueueTemp() }
//                CoroutineScope(IO).launch {
//                    Log.d(TAG, "called send file operation 1")
//                    if (sharedPreferences !== null) {
//                        Log.d(TAG, "called send file operation 2")
//                        var serversInFile = sharedPreferences!!.getString("servers", "null")
//                        Log.d(TAG, "preferences $serversInFile")
//                        if (serversInFile != null && serversInFile.split(',').isNotEmpty())
//                            util.startSendingFromQueue(serversInFile.split(','))
//                    }
//                }
            }
        } else {
            Log.d(TAG, "not found")
        }
        Server().createServer(this)
        CoroutineScope(IO).launch {
            if (sharedPreferences !== null) {
                val editor:SharedPreferences.Editor =  sharedPreferences!!.edit()
                editor.putString("servers", "null")
                editor.apply()
                editor.commit()
            }
            checkClient(5000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun setupPermissions() {
        Log.d(TAG, "checkinf permission")
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission not granted")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.d(TAG, "permission asking rational")
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to access the files is required for this app to get files.").setTitle("Permission required")
                builder.setPositiveButton("OK") { _, _ ->
                        Log.i(TAG, "Clicked")
                        makeRequest()
                    }
                val dialog = builder.create()
                dialog.show()
            } else {
                makeRequest()
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val SUBTAG = "perRes"
        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("$TAG-$SUBTAG", "Permission has been denied by user")
                } else {
                    Log.i("$TAG-$SUBTAG", "Permission has been granted by user")
                }
            }
        }
    }
    suspend fun checkClient(delay: Long) {
        while (true) {
//            Log.d(TAG, "checking clients .... ")
            var br: BufferedReader?
            val servers = mutableListOf<String>()
            try {
                br = BufferedReader(FileReader("/proc/net/arp"))
                var line: String?
                while (true) {
                    line = br.readLine()
                    if (line == null) {
                        break
                    }
//                    Log.d(TAG, line)
                    val splitted: Array<String?> = line.split(" +".toRegex()).toTypedArray()
                    if (splitted.isNotEmpty() && splitted[0] != null) {
                        if(isValidIp(splitted[0])) {
                            splitted[0]?.let { servers.add(it) }
                        }
                    }
                }
//                Log.d(TAG, "connected clients are $servers")
                CoroutineScope(IO).launch {
                    val newServers = servers.minus(authorizedServers)
                    newServers.forEach {
                        val res = Client(it, null).run()
                        if (res !== null && res == 1) {
                            authorizedServers.add(it)
                        }
                    }
                    val deadServers = authorizedServers.minus(servers)
                    deadServers.forEach {
                        authorizedServers.remove(it)
                    }
                    if (deadServers.isNotEmpty() || newServers.isNotEmpty()) {
                        if (sharedPreferences !== null) {
                            val editor:SharedPreferences.Editor =  sharedPreferences!!.edit()
                            editor.putString("servers", authorizedServers.joinToString(","))
                            editor.apply()
                            editor.commit()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "ex" + e.printStackTrace().toString())
            }
            delay(delay)
        }
    }
    private fun isValidIp(ip: String?) : Boolean {
        if (ip != null) {
//            Log.d(TAG, "$ip ${ip.contains("192.168.", ignoreCase = true)} ${ip.split('.')}")
            return ip.contains("192.168.", ignoreCase = true) && ip.split('.').size == 4
        }
        Log.d(TAG, "null ip")
        return false
    }
    private fun makeRequest() {
        Log.d(TAG, "permission asked")
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
    }
}