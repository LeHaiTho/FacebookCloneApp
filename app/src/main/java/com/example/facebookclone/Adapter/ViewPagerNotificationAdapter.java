package com.example.facebookclone.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.facebookclone.Fragment.NotificationTabFragment;
import com.example.facebookclone.Fragment.RequestFragment;

public class ViewPagerNotificationAdapter extends FragmentStatePagerAdapter {
    public ViewPagerNotificationAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NotificationTabFragment();
            case 1:
                return new RequestFragment();
            default:
                return new NotificationTabFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0: return title ="Thông báo";
            case 1: return title ="Yêu cầu kết bạn";
        }
        return title;
    }
}
