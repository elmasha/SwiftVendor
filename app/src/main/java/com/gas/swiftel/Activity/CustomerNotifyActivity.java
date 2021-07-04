package com.gas.swiftel.Activity;

import android.content.Intent;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gas.swiftel.Model.Orders_request;
import com.gas.swiftel.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class CustomerNotifyActivity extends AppCompatActivity {


    MediaPlayer mediaPlayer;
    private TextView time,distance,adress;
    private Button ConfirmRequest,CancelRequest;
    private double lat,lng;

    private String Doc_id,User_id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderRef = db.collection("Order_request");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_notify);

//        mediaPlayer = MediaPlayer.create(this,R.raw.popglass);
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();

        time = findViewById(R.id.Orders_time);
        adress = findViewById(R.id.Orders_adress);
        distance = findViewById(R.id.Orders_distance);
        ConfirmRequest = findViewById(R.id.Confirm_reqeust);
        CancelRequest = findViewById(R.id.Cancel_reqeust);


        if (getIntent() != null){

             Doc_id = getIntent().getStringExtra("doc_ID");

        }


        LoadData();

        ConfirmRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent toMap = new Intent(getApplicationContext(),VendorMapActivity.class);
                toMap.putExtra("lat",lat);
                toMap.putExtra("lng",lng);
                startActivity(toMap);
            }
        });

        CancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }

    private void LoadData() {


        orderRef.document(Doc_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {


                if (documentSnapshot.exists()){

                    Orders_request ordersRequest = documentSnapshot.toObject(Orders_request.class);

                    User_id = ordersRequest.getUser_id();
                    lat = ordersRequest.getLat();
                    lng = ordersRequest.getLng();

                }

            }
        });




    }

    @Override
    protected void onStop() {
        super.onStop();

//        mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mediaPlayer.release();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        mediaPlayer.start();
    }
}
