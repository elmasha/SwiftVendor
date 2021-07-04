package com.gas.swiftel.Activity;

import android.content.DialogInterface;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Adapter.PaymentAdapter;
import com.gas.swiftel.Model.Payment_Hist;
import com.gas.swiftel.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Date;

public class PaymentHistActivity extends AppCompatActivity {

    private TextView OkoaBuy,OkoaRefil;
    private String GasName,GasKgs,Desc,VendorId,Product_Id;
    private long GasPrice,GasRefil;
    private TextView gasName,kgs,time,desc,okoaGas,cartList;
    private Button Buy,Refill;

    private FirebaseAuth mAuth;
    private CoordinatorLayout relative;
    private AlertDialog dialog3;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ordersRef = db.collection("Orders_request");
    CollectionReference Payment_HistRef = db.collection("Payment_History");

    MediaPlayer mediaPlayer;
    private PaymentAdapter adapter;
    private RecyclerView mRecyclerView;
    TextView viewDelete;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        Product_Id = getIntent().getStringExtra("Product_Id");
        mAuth = FirebaseAuth.getInstance();

        mRecyclerView = findViewById(R.id.RecyclerPayHist);
        swipeRefreshLayout = findViewById(R.id.SwipePayHist);
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


//        mediaPlayer = MediaPlayer.create(this,R.raw.popglass);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();

        if (getIntent() != null){

            double lat = getIntent().getDoubleExtra("lat",-1.0);
            double lng = getIntent().getDoubleExtra("lng",-1.0);

        }


        FetchProduct();



    }



    private TextView Name,Paytime,UserName,Paytype,PayID,PayTrips,PayAmount,PayPhone,closePay;
    private FloatingActionButton deletePay,printPay;
    private AlertDialog dialog;
    public void ShowPayment() {


        AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_payhist, null);
        mbuilder.setView(mView);
        dialog3 = mbuilder.create();
        dialog3.setCancelable(false);
        dialog3.show();

        Name = mView.findViewById(R.id.payhistName);
        Paytime = mView.findViewById(R.id.payhistTime);
        UserName = mView.findViewById(R.id.payhistUser);
        Paytype = mView.findViewById(R.id.payhistType);
        PayID = mView.findViewById(R.id.payhistID);
        PayTrips = mView.findViewById(R.id.payhistTrips);
        PayAmount = mView.findViewById(R.id.payhistCash);
        PayPhone = mView.findViewById(R.id.payhistPhone);
        deletePay = mView.findViewById(R.id.payhistDelete);
        closePay = mView.findViewById(R.id.Payhist_close);
        printPay = mView.findViewById(R.id.payhistPrint);

        Name.setText(payname);
        Paytime.setText(date);
        UserName.setText(payuser);
        Paytype.setText(paytype);
        PayID.setText("Payment ID "+payid);
        PayTrips.setText(paytrips);
        PayAmount.setText(paycash);
        PayPhone.setText(payphone);


        deletePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                String date = DateFormat.format("dd MMM ,yyyy | hh:mm a", new Date(String.valueOf(currentTime))).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentHistActivity.this);
                builder.setTitle("Delete payment");
                builder.setIcon(R.drawable.trash);
                builder.setMessage("Are you sure.? \n" + "Date " + date);
                builder.show();


                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "payment "+ payid +" deleted", Toast.LENGTH_SHORT).show();
                                FetchProduct();
                                if (dialog3 != null)dialog3.dismiss();
                                dialog.dismiss();
                                adapter.deleteItem(deletePosition);

                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                FetchProduct();
                                dialog.dismiss();
                            }
                        });


            }
        });

        closePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog3 != null)dialog3.dismiss();
            }
        });






    }


    private String payname,paytype,payuser,payid,paytrips,paycash,payphone,paytime,date;
    private int deletePosition;
    private void FetchProduct() {

        String MUID = mAuth.getCurrentUser().getUid();

        Query query = Payment_HistRef.whereEqualTo("User_ID", MUID)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50);
        FirestoreRecyclerOptions<Payment_Hist> options = new FirestoreRecyclerOptions.Builder<Payment_Hist>()
                .setQuery(query, Payment_Hist.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new PaymentAdapter(options);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(mRecyclerView);


        adapter.setOnItemClickListener(new PaymentAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                deletePosition  = position;

                Payment_Hist payment_hist = documentSnapshot.toObject(Payment_Hist.class);

                long milisecond = payment_hist.getTimestamp().getTime();


                date = DateFormat.format("MMMM-dd-yyyy hh:mm a",new Date(milisecond)).toString();
                payname = payment_hist.getName();
                payuser = payment_hist.getUser_name();
                paytype = payment_hist.getType();
                payphone = payment_hist.getPhoneNo();
                payid = payment_hist.getPayment_ID();
                paytrips = String.valueOf(payment_hist.getTrips());
                paycash = payment_hist.getAmount();

                ShowPayment();



            }
        });



    }





}
