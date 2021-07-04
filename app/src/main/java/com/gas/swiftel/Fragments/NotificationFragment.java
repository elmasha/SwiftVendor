package com.gas.swiftel.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Model.Notifications;
import com.gas.swiftel.Adapter.NotifyAdapter;
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
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class NotificationFragment extends Fragment {
    View root;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference accessoriesRef = db.collection("Accessories");
    CollectionReference Client_Ref = db.collection("SwiftGas_Client");
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    private NotifyAdapter adapter;
    private RecyclerView mRecyclerView;
    TextView viewDelete;
    private SwipeRefreshLayout swipeRefreshLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        FetchProduct();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_notification, container, false);
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = root.findViewById(R.id.Notify_RecyclerView);
        viewDelete = root.findViewById(R.id.swipe);
        swipeRefreshLayout = root.findViewById(R.id.SwipeNotify);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FetchProduct();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });


        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                viewDelete.setVisibility(View.GONE);
            }
        }.start();
        return root;
    }

    private void FetchProduct() {

        String MUID = mAuth.getCurrentUser().getUid();

        Query query = vendorRef.document(mAuth.getCurrentUser().getUid()).collection("Notifications")
                .whereEqualTo("to", MUID)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50);
        FirestoreRecyclerOptions<Notifications> options = new FirestoreRecyclerOptions.Builder<Notifications>()
                .setQuery(query, Notifications.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new NotifyAdapter(options);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Date currentTime = Calendar.getInstance().getTime();
                String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete notification");
                builder.setIcon(R.drawable.trash);
                builder.setMessage("Are you sure.? \n" + "Date " + date);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteItem(viewHolder.getAdapterPosition());
                                Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                                FetchProduct();
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FetchProduct();
                                dialog.dismiss();
                            }
                        });

                builder.setCancelable(false);
                builder.show();
            }
        }).attachToRecyclerView(mRecyclerView);


        adapter.setOnItemClickListener(new NotifyAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {


            }
        });


    }

}
