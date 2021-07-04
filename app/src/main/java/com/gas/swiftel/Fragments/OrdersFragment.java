package com.gas.swiftel.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gas.swiftel.Adapter.OrderPager;
import com.gas.swiftel.Adapter.OredersAdapter;
import com.gas.swiftel.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class OrdersFragment extends Fragment {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderRef = db.collection("Order_request");

    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    OredersAdapter adapter;


    TabLayout tabLayout;
    TabItem pendingTab;
    TabItem transitTab;
    TabItem confirmTab;
    TabItem deliveredTab;
    TabItem canceledTab;
    ViewPager viewPager;

    OrderPager pagerAdapter;
    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        mAuth =FirebaseAuth.getInstance();

        tabLayout = root.findViewById(R.id.TablayoutOrders);
        pendingTab = root.findViewById(R.id.pending_tab);
        deliveredTab = root.findViewById(R.id.delivered_tab);
        canceledTab = root.findViewById(R.id.cancel_tab);
        confirmTab = root.findViewById(R.id.confirm_tab);
        transitTab = root.findViewById(R.id.transit_tab);
        viewPager = root.findViewById(R.id.ViewPagerOrders);


        pagerAdapter = new OrderPager(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 1){

                }else if (tab.getPosition() == 2){

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));







        return root;
    }


}
