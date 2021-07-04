package com.gas.swiftel.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Model.Orders_request;
import com.gas.swiftel.Adapter.OredersAdapter;
import com.gas.swiftel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDeliveredFragment extends Fragment {
    View root;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderRef = db.collection("Order_request");

    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    OredersAdapter adapter;
    private ImageView imageView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public OrderDeliveredFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_order_delivered, container, false);
        mAuth =FirebaseAuth.getInstance();
        mRecyclerView = root.findViewById(R.id.RecyclerviewDelivered);
        imageView = root.findViewById(R.id.View_Error);
        swipeRefreshLayout = root.findViewById(R.id.SwipeDeliver);
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
                .whereEqualTo("Order_status",3);
        FirestoreRecyclerOptions<Orders_request> options = new FirestoreRecyclerOptions.Builder<Orders_request>()
                .setQuery(userQuery1,Orders_request.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new OredersAdapter(options);



        if (adapter.getItemCount() == 0){

            imageView.setVisibility(View.VISIBLE);
        }




        adapter.setOnItemClickListener(new OredersAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String id = documentSnapshot.getId();

                Date currentTime = Calendar.getInstance().getTime();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Item");
                builder.setMessage("Are you sure you want to delete this item ?");
                builder.setIcon(R.drawable.trash);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteItem(position);
                                Toast.makeText(getContext(), "Order deleted", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.setCancelable(false);
                builder.show();
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
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
