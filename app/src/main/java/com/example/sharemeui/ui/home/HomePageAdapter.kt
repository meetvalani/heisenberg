package com.example.sharemeui.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePageAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryFragment()
            1 -> DownloadsFragment()
            2 -> AppsFragment()
            3 -> PhotoFragment()
            4 -> MusicFragment()
            5 -> VideoFragment()
            6 -> FileFragment()
            else -> AppsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 7
    }

    companion object {
        private const val TAG = "homePageAdapter"
    }
}