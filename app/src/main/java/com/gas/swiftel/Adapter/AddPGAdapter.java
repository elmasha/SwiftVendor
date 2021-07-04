package com.gas.swiftel.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gas.swiftel.Fragments.AddAccessFragment;
import com.gas.swiftel.Fragments.AddgasFragment;

public class AddPGAdapter extends FragmentPagerAdapter {

    private int numofTabs;


    public AddPGAdapter(FragmentManager fm , int numofTabs) {
        super(fm);
        this.numofTabs = numofTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new AddgasFragment();
            case 1:
                return new AddAccessFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return numofTabs;
    }
}
