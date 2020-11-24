package com.example.sharemeui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
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

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    val TAG = "mainActivity"
    var isFabOpen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        val SUBTAG = "oncreate"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d("$TAG-$SUBTAG", "permission not granted" )
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
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
            HtspFragment().apply {
                show(supportFragmentManager, HtspFragment.TAG)
            }
        }

        wifiFab.setOnClickListener {
            WifiFragment().apply {
                show(supportFragmentManager, WifiFragment.TAG)
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
                builder.setPositiveButton("OK") { dialog, id ->
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
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        val SUBTAG = "perRes"
        when (requestCode) {
            1 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("$TAG-$SUBTAG", "Permission has been denied by user")
//                    setupPermissions()
                } else {
                    Log.i("$TAG-$SUBTAG", "Permission has been granted by user")
                }
            }
        }
    }
    private fun makeRequest() {
        Log.d(TAG, "permission asked")
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
    }
}