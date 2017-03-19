package com.example.friendlocation.friendlocation.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.friendlocation.friendlocation.fragments.FriendListFragment;
import com.example.friendlocation.friendlocation.fragments.MFragment;
import com.example.friendlocation.friendlocation.fragments.SettingsFragment;

/**
 * Created by barte_000 on 20.03.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter { //FragmentStatePagerAdapter

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){
            case 0:
                return new MFragment();
            case 1:
                return new FriendListFragment();
            case 2:
                return new SettingsFragment();
            default:
                return new MFragment();
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Mapa";
            case 1:
                return "Lista znajomych";
            case 2:
                return "Ustawienia";
        }
        return null;
    }
}
