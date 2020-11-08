package com.example.sharemeui.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomePageAdapter extends FragmentStateAdapter {
    private static  String TAG = "homePageAdapter";
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HistoryFragment();
            case 1:
                return new DownloadsFragment();
            case 2:
                return new AppsFragment();
            case 3:
                return new PhotoFragment();
            case 4:
                return new TempMusicFragment();
            case 5:
                return new VideoFragment();
            case 6:
                return new FileFragment();
            default:
                return new FileFragment();

         }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public HomePageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
}
