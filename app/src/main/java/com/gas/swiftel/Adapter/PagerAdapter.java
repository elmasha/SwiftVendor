package com.gas.swiftel.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gas.swiftel.Fragments.AccessoryFragment;
import com.gas.swiftel.Fragments.GasFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numofTabs;


    public PagerAdapter(FragmentManager fm , int numofTabs) {
        super(fm);
        this.numofTabs = numofTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new GasFragment();
            case 1:
                return new AccessoryFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return numofTabs;
    }
}
