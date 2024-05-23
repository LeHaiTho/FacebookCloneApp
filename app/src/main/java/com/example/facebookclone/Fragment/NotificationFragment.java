package com.example.facebookclone.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.facebookclone.Adapter.ViewPagerNotificationAdapter;
import com.example.facebookclone.R;
import com.google.android.material.tabs.TabLayout;

public class NotificationFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_notification, container, false);

       //ánh xạ nạp adapter từ ViewPagerAdapter
       viewPager = view.findViewById(R.id.viewPager);
       viewPager.setAdapter(new ViewPagerNotificationAdapter(getFragmentManager()));
       //set up tabLayout với viewPager
       tabLayout = view.findViewById(R.id.tabLayout);
       tabLayout.setupWithViewPager(viewPager);

       return view;
    }
}
