package com.gas.swiftel.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gas.swiftel.Fragments.OrderCanceldFragment;
import com.gas.swiftel.Fragments.OrderConfirmedFragment;
import com.gas.swiftel.Fragments.OrderDeliveredFragment;
import com.gas.swiftel.Fragments.OrderTransitFragment;
import com.gas.swiftel.Fragments.PendingOrderFragment;

public class OrderPager extends FragmentPagerAdapter {

    private int numofTabs;


    public OrderPager(FragmentManager fm , int numofTabs) {
        super(fm);
        this.numofTabs = numofTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new PendingOrderFragment();
            case 1:
                return new OrderConfirmedFragment();
            case 2:
                return new OrderTransitFragment();
            case 3:
                return new OrderDeliveredFragment();
            case 4:
                return new OrderCanceldFragment();
            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return numofTabs;
    }
}
