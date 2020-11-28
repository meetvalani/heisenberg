package com.example.sharemeui.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        Log.d("$TAG-$SUBTAG", "started fetching apps packages")
        val packages = pm?.getInstalledApplications(0)

        if (packages != null) {
            Log.d("$TAG-$SUBTAG", "get all apps packages" + packages.size.toString())
            var app4List = mutableListOf<app_single>()
            for (packageInfo in packages) {
                val packageName =  packageInfo.packageName
                val size = File(packageInfo.publicSourceDir).length()
                val title = pm.getApplicationInfo(packageName, 0).loadLabel(pm)
                val icon = packageInfo.loadIcon(pm)
//                val icon = "null"
                app4List.add(app_single(title.toString(), icon, size.toString()))
                if (app4List.size >= 4){
                    newApp.add(app(app4List[0].title, app4List[0].coverImage, app4List[0].size,
                        app4List[1].title, app4List[1].coverImage, app4List[1].size,
                        app4List[2].title, app4List[2].coverImage, app4List[2].size,
                        app4List[3].title, app4List[3].coverImage, app4List[3].size))
                    app4List.removeAt(0)
                    app4List.removeAt(0)
                    app4List.removeAt(0)
                    app4List.removeAt(0)
                   
                    if (new) {
                        appAdapter.setApp(newApp)
                        new = false
                    }
                    else {
                        appAdapter.setApp(newApp)
                    }
                    Log.d("$TAG-$SUBTAG", "Still on ongoing loop")
                    if (newApp.size > 10 )
                        return appsFragment
                }
            }
//            TODO() -> Siddharth
//            last 3 apps might be not added in list, visit music fragment to understand what is added after loop iteration
        }
        Log.d("$TAG-$SUBTAG", "finished all apps processing")
        Log.d("$TAG-$SUBTAG", "Sending apps details to adapter" + newApp.toString())
        appAdapter.setApp(newApp)

        Log.d("$TAG-$SUBTAG", "official return")
        return appsFragment
    }

}