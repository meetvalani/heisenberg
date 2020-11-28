package com.example.sharemeui.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.example.sharemeui.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    val TAG = "homeFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val SUBTAG = "create"
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val util = this.context?.let { Util(it) }
        if (util != null) {
            CoroutineScope(IO).launch { util.clearAllHistory() }
        }
        val homeViewPager: ViewPager2 = root.findViewById(R.id.homeViewPager)
        homeViewPager.adapter = this.activity?.let { HomePageAdapter(it) }

        val tabBar: TabLayout = root.findViewById(R.id.tabBar)
        TabLayoutMediator(tabBar, homeViewPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
            when (position) {
                0 -> tab.text = "HISTORY"
                1 -> tab.text = "DOWNLOADS"
                2 -> tab.text = "APPS"
                3 -> tab.text = "PHOTOS"
                4 -> tab.text = "MUSIC"
                5 -> tab.text = "VIDEO"
                6 -> tab.text = "FILES"
            }
        }.attach()
        // selects 3rd tab "APPS" on launch
        tabBar.getTabAt(2)?.select()
        Log.d("$TAG-$SUBTAG","in layouut")
        return root
    }
}