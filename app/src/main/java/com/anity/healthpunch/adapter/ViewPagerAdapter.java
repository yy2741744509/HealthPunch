package com.anity.healthpunch.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.anity.healthpunch.fragment.ScheduleFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {



    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public Fragment getItem(int position) {
        return ScheduleFragment.newInstance(position);
    }

    public int getCount() {
        return 20;
    }

    public CharSequence getPageTitle(int position) {
        return "第" + (position + 1) + "周";
    }


}
