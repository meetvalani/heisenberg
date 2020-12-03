package com.example.sharemeui.ui.home

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.MATCH_SYSTEM_ONLY
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharemeui.MainActivity
import com.example.sharemeui.R
import java.io.File


class AppsFragment : Fragment() {
    val TAG = "appFrag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("$TAG", "on Create")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val appsFragment  = inflater.inflate(R.layout.fragment_apps, container, false)
        val SUBTAG = "OnCreate"
        Log.d("$TAG-$SUBTAG", "on view Create")
        var newApp = mutableListOf<app>()
        var count = 0
        val appAdapter = appAdapter()
        val recyclerView = appsFragment.findViewById<RecyclerView>(R.id.appRCV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = appAdapter
        var new = true
        val pm: PackageManager? = this.context?.packageManager
        //get a list of installed apps.
        //get a list of installed apps.
        Log.d("$TAG-$SUBTAG", "started fetching apps packages")
        val packages = pm?.getInstalledApplications(0)
        val sysPackages = pm?.getInstalledApplications(MATCH_SYSTEM_ONLY)
        Log.d("$TAG-$SUBTAG", "get all apps packages")
        if (packages != null) {
            if (sysPackages != null) {
                for (i in 0..sysPackages.size-1) {
                    for (j in 0..packages.size-1) {
                        if (sysPackages[i].packageName == packages[j].packageName){
                            packages.removeAt(j)
                            break
                        }
                    }
                }
                var packagesNames = mutableListOf<String>()
                for (i in 0..packages.size-1) {
                    packagesNames.add(
                        pm.getApplicationInfo(packages[i].packageName, 0).loadLabel(pm).toString().toLowerCase()
                    )
                }
                for (i in 0..packages.size-2) {
                    for (j in i+1..packages.size-1) {
                        if (packagesNames[i] > packagesNames[j]) {
                            val tmp: ApplicationInfo = packages[i]
                            packages[i] = packages[j]
                            packages[j] = tmp
                            val tmpName: String = packagesNames[i]
                            packagesNames[i] = packagesNames[j]
                            packagesNames[j] = tmpName
                        }
                    }
                }
            }
            Log.d("$TAG-$SUBTAG", "total apps are : ${packages.size}")
            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            appAdapter.setScreen(display.height, display.width)
            for (packageInfo in packages) {
                val packageName =  packageInfo.packageName
                val size = File(packageInfo.publicSourceDir).length()
                val title = pm.getApplicationInfo(packageName, 0).loadLabel(pm)
                val icon = packageInfo.loadIcon(pm)
                newApp.add(app(title.toString(), icon, size.toString()))
            }
//            TODO() -> Siddharth
//            Add db caching for storing apps data instead loading every time.
        }
        Log.d("$TAG-$SUBTAG", "finished all apps processing")
        Log.d("$TAG-$SUBTAG", "Sending apps details to adapter" + newApp.toString())
        appAdapter.setApp(newApp)

        Log.d("$TAG-$SUBTAG", "official return")
        return appsFragment
    }

}