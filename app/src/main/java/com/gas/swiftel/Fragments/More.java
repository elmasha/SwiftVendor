package com.gas.swiftel.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gas.swiftel.Activity.LoginActivity;
import com.gas.swiftel.Activity.PrivacyActivity;
import com.gas.swiftel.Model.Gas_Vendor;
import com.gas.swiftel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hsalf.smileyrating.SmileyRating;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static com.gas.swiftel.Fragments.VendorProfileFragment.shareApp;


public class More extends Fragment {
View view;
    private TextView ShareApp,AboutApp,ratingBar,privacy,Avg_Rates,Feedback,Logout;
    private FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference FeedBack_Ref = db.collection("FeedBack_SwiftVendor");
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    CollectionReference VendorAvailableRef = db.collection("VendorsAvailable");

    public More() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_more, container, false);
        mAuth = FirebaseAuth.getInstance();
        privacy = view.findViewById(R.id.Privacy);
        Avg_Rates = view.findViewById(R.id.Avg_rating);
        Logout = view.findViewById(R.id.LogOut);
        ratingBar = view.findViewById(R.id.ratingBar);
        Feedback = view.findViewById(R.id.Feedback2);
        AboutApp = view.findViewById(R.id.About);
        ShareApp = view.findViewById(R.id.Share);



        ShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp(getContext());
            }
        });


        AboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastBack("Please wait launching website..");
                String uri = "https://swiftgas.co.ke";
                GoToURL(uri);
            }
        });


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout_Alert();
            }
        });


        Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog_FeedBack();

            }
        });



        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getContext(), PrivacyActivity.class));
            }
        });

    return view;
    }

    //----Log out Alert-----
    AlertDialog dialog2;
    public void Logout_Alert() {

        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a",new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        dialog2 = builder.create();
        dialog2.show();
        builder.setMessage("Are you sure to Log out..\n");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        LogOut();

                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog2.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    //--LogOut-------
    private void LogOut(){
        Map<String,Object> delettoken = new HashMap<>();
        delettoken.put("device_token", FieldValue.delete());

        String uid = mAuth.getCurrentUser().getUid();

        if (uid !=null){

            vendorRef.document(uid).update(delettoken).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        GeoFirestore geoFirestore = new GeoFirestore(VendorAvailableRef);
                        geoFirestore.removeLocation(uid);
                        dialog2.dismiss();
                        mAuth.signOut();
                        Intent logout = new Intent(getContext(), LoginActivity.class);
                        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logout);

                    }else {


                    }

                }
            });

        }





    }


    void GoToURL(String url){
        Uri uri = Uri.parse(url);
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }


    //--Toast function---
    private Toast backToast;
    private void ToastBack(String message){


        backToast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        View view = backToast.getView();

        //Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(Color.parseColor("#242A37"), PorterDuff.Mode.SRC_IN);

        //Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#FF8900"));
        backToast.show();
    }

    //---Dialog Feedback---
    private EditText InputFeedBack;
    private TextView CloseFeedback;
    private Button submitFeedBack;
    private SmileyRating smileRating;
    private int FeedBack_ratings =0;
    private String doc_i,feedSMS;
    private AlertDialog dialog_feedback;
    private void Dialog_FeedBack() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_feedack, null);
        mbuilder.setView(mView);
        dialog_feedback = mbuilder.create();
        dialog_feedback.show();
        smileRating = mView.findViewById(R.id.smile_rating);
        InputFeedBack = mView.findViewById(R.id.inputFeedback);
        submitFeedBack = mView.findViewById(R.id.Submit_feedback);
        CloseFeedback  = mView.findViewById(R.id.feedback_close);



        CloseFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog_feedback!= null)dialog_feedback.dismiss();
            }
        });

        smileRating.setSmileySelectedListener(new SmileyRating.OnSmileySelectedListener() {
            @Override
            public void onSmileySelected(SmileyRating.Type type) {
                // You can compare it with rating Type
                if (SmileyRating.Type.GREAT == type) {
                    //                    Log.i(TAG, "Wow, the user gave high rating");
                }
                // You can get the user rating too
                // rating will between 1 to 5
                FeedBack_ratings = type.getRating();



            }
        });


        submitFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                feedSMS = InputFeedBack.getText().toString();
                if (FeedBack_ratings == 0){

                    ToastBack("Please select rating..");

                }else if (feedSMS.isEmpty()){

                    ToastBack("Enter your feedback..");

                }else {

                    Submit_feedBack();
                }




            }
        });

    }
    ///....end Dialogue


    //----Share Feedback function-----
    private void Submit_feedBack() {

        String uid = mAuth.getCurrentUser().getUid();

        doc_i = FeedBack_Ref.document().getId();

        HashMap<String,Object> feed = new HashMap<>();
        feed.put("User_name",First_name);
        feed.put("Phone_number",Phone);
        feed.put("Rate",FeedBack_ratings);
        feed.put("Feedback",feedSMS);
        feed.put("Feedback_ID",doc_i);
        feed.put("timestamp", FieldValue.serverTimestamp());
        feed.put("User_ID",uid);


        FeedBack_Ref.document().set(feed).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(getContext(), First_name+" thanks for your feedback", Toast.LENGTH_SHORT).show();

                    if (dialog_feedback != null)dialog_feedback.dismiss();


                }else {

                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });





    }



    //-----Load Details-----//
    private String location,Phone,First_name,Email,Shopname,shopNO,User_image;
    private long Earnings,Trips,Cash_Trips;
    private String activation_fee;
    private void loadData() {

        String userid= mAuth.getCurrentUser().getUid();


        vendorRef.whereEqualTo("User_ID",userid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    Gas_Vendor vendorUser = doc.toObject(Gas_Vendor.class);

                    First_name =vendorUser.getFirst_name()+" "+ vendorUser.getLast_name();
                    Email =vendorUser.getEmail();
                    Shopname = vendorUser.getShopName();
                    shopNO = vendorUser.getShop_No();
                    Phone =vendorUser.getMobile();
                    User_image = vendorUser.getUser_Image();
                    activation_fee = vendorUser.getActivation_fee();
                    Trips = vendorUser.getTrips();
                    Earnings = (int) vendorUser.getEarnings();
                    location = vendorUser.getLocation();
                    Cash_Trips = vendorUser.getCash_Trips();



                }


            }
        });


    }



}