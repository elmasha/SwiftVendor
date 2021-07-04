package com.gas.swiftel.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Model.Orders_request;
import com.gas.swiftel.Adapter.OredersAdapter;
import com.gas.swiftel.R;
import com.gas.swiftel.Activity.VendorMapActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderTransitFragment extends Fragment {
    View root;




    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderRef = db.collection("Order_request");

    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    OredersAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public OrderTransitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_order_transit, container, false);
        mAuth =FirebaseAuth.getInstance();
        mRecyclerView = root.findViewById(R.id.transit_recyclerview);

        swipeRefreshLayout = root.findViewById(R.id.SwipeTransit);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchOrders();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });


        FetchOrders();

        return root;
    }

    private void FetchOrders() {



        String User_ID = mAuth.getCurrentUser().getUid();

        Query userQuery1 = orderRef.whereEqualTo("Vendor_ID",User_ID)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("Order_status",2);
        FirestoreRecyclerOptions<Orders_request> options = new FirestoreRecyclerOptions.Builder<Orders_request>()
                .setQuery(userQuery1,Orders_request.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new OredersAdapter(options);



        adapter.setOnItemClickListener(new OredersAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String id = documentSnapshot.getId();


                Orders_request ordersRequest = documentSnapshot.toObject(Orders_request.class);
                double oRdlat = ordersRequest.getLat();
                double oRdlng = ordersRequest.getLng();

                Intent toItemView = new Intent(getContext(), VendorMapActivity.class);
                toItemView.putExtra("doc_ID",id);
                toItemView.putExtra("lat",oRdlat);
                toItemView.putExtra("lng",oRdlng);
                startActivity(toItemView);

            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);


    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }
}
