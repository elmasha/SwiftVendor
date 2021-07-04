package com.gas.swiftel.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.gas.swiftel.Common;
import com.gas.swiftel.Interface.RetrofitInterface;
import com.gas.swiftel.Model.Gas_Vendor;
import com.gas.swiftel.Model.ResponseStk;
import com.gas.swiftel.Model.Result;
import com.gas.swiftel.Model.StkQuey;
import com.gas.swiftel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Myshop extends Fragment {
View view;
    private static final long START_TIME_IN_MILLIS_COUNT = 27000;
    private String First_name,Email,shopNO,Shopname,User_image,No;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    CollectionReference vendorShopRef = db.collection("VendorsShop");
    CollectionReference adminRef = db.collection("Admin");
    private TextView Activation_payment,Logout,vendor_name,email,phone,shopname,earnings,shop_No,AboutApp,ShareApp;
    private TextView feeStatus,Trips_V,Earings_V,Cash_tripsV,privacy,ActivationFee,PayError;
    private EditText activateNo;
    private Button activateBtn;
    private LinearLayout ActivationLayout;
    private long Trips,Cash_Trips;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS_COUNT;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = Common.BASE_URL;
    public  String Product_Id,CheckoutRequestID,ResponseCode,
            ResultCode,ResponseDescription,ResultDesc;
    private ProgressDialog progressStk,progressDialog;
    private int BtnStatus;




    public Myshop() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_myshop, container, false);
        mAuth = FirebaseAuth.getInstance();

        Activation_payment = view.findViewById(R.id.Payment);
        vendor_name = view.findViewById(R.id.Vendor_Name);
        email = view.findViewById(R.id.Emailv);
        phone = view.findViewById(R.id.Phonev);
        shopname = view.findViewById(R.id.Vendor_ShopNmae);
        shop_No = view.findViewById(R.id.Shop_number);
        feeStatus = view.findViewById(R.id.PaymentFee);
        Trips_V = view.findViewById(R.id.Earningstrips);
        Earings_V = view.findViewById(R.id.Earnings);

        Cash_tripsV = view.findViewById(R.id.Trips);

        ActivationFee = view.findViewById(R.id.PayFee);
        activateNo = view.findViewById(R.id.ActivationNumber);
        activateBtn = view.findViewById(R.id.ActivationBtn);
        ActivationLayout = view.findViewById(R.id.ActivationLayout);
        PayError = view.findViewById(R.id.Payment);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        activateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected(getContext())){
                    showSnackBaroffline();
                }else {

                    if (!Validate()){

                    }else {
                        Mpesa_Dialog();
                    }
                }

            }
        });


        loadData();

        return view;
    }



    private void newtime(){
        new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (CheckoutRequestID != null){
                    StkQueryActivate(CheckoutRequestID);

                }else {

                    if (progressStk != null)progressStk.dismiss();
                    //Toast.makeText(getContext(), "StkPush Request timeout...", Toast.LENGTH_LONG).show();
                    ToastBack("StkPush Request timeout...");
                    // progressStk.dismiss();
                }
            }
        }.start();
    }


    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                if (CheckoutRequestID != null){

                    StkQueryActivate(CheckoutRequestID);

                }else {

                    if (progressStk != null)progressStk.dismiss();

                    Toast.makeText(getContext(), "StkPush Request timeout..", Toast.LENGTH_LONG).show();

                }

            }
        }.start();
        mTimerRunning = true;

    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;

    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS_COUNT;
        updateCountDownText();

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        // Timer.setText(timeLeftFormatted);
    }

    private void Mpesa_Dialog(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Lipa na Mpesa express");
        builder.setIcon(R.drawable.mpesa);
        builder.setMessage("Are you sure  " + Phone  + "\nis your Mpesa number ?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        stkActivate("1");
                        startTimer();
                        dialog.dismiss();
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.show();
                        progressDialog.setMessage("Please wait..");

                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setCancelable(true);
        builder.show();
    }

    private String errorMessage;
    private void stkActivate(String cash){
        No = activateNo.getText().toString();
        String PhoneNumber = "254"+No.substring(1);
        Map<String ,Object> stk_Push = new HashMap<>();
        stk_Push.put("amount",cash);
        stk_Push.put("phone", PhoneNumber);
        stk_Push.put("transDec", "Activate Account.");
        stk_Push.put("User_name", First_name);
        stk_Push.put("ActiveNo",Activeshops);
        stk_Push.put("InActiveNo",inActiveShops);
        stk_Push.put("Uid", mAuth.getCurrentUser().getUid());

        Call<ResponseStk> callStk = retrofitInterface.stk_pushActivate(stk_Push);

        callStk.enqueue(new Callback<ResponseStk>() {
            @Override
            public void onResponse(Call<ResponseStk> call, Response<ResponseStk> response) {
                if (response.code()== 200){
                    newtime();
                    progressStk = new ProgressDialog(getContext());
                    progressStk.setCancelable(false);
                    progressStk.setMessage("Mpesa StkPush transaction..");
                    progressStk.show();
                    progressDialog.dismiss();

                    ResponseStk responseStk = response.body();
                    String responseDesc = responseStk.getCustomerMessage();
                    ResponseCode = responseStk.getResponseCode();
                    errorMessage = responseStk.getErrorMessage();
                    CheckoutRequestID = responseStk.getCheckoutRequestID();
                    Log.i("TAG", "CheckoutRequestID: "+ response.body());

                    if (responseDesc != null){

                        if (responseDesc.equals("Success. Request accepted for processing")){
                            ToastBack(responseDesc);
                        }

                    }else {

                        if (errorMessage.equals("No ICCID found on NMS")){

                            ToastBack("Please provide a valid mpesa number.");
                            progressStk.dismiss();
                        }

                        ToastBack(errorMessage);
                        progressStk.dismiss();
                    }



                }else if (response.code()==404){


                    Toast.makeText(getContext(), "Bad request", Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<ResponseStk> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    ////----Mpesa stk Query-----
    private void StkQueryActivate(String checkoutRequestID){

        Map<String ,Object > stk_Query = new HashMap<>();
        stk_Query.put("checkoutRequestId",checkoutRequestID);
        Call<StkQuey> callQuery = retrofitInterface.stk_QueryActivate(stk_Query);

        callQuery.enqueue(new Callback<StkQuey>() {
            @Override
            public void onResponse(Call<StkQuey> call, Response<StkQuey> response) {
                if (response != null){
                    if (response.code()== 200){

                        StkQuey stkQuey1 = response.body();
                        Toast.makeText(getContext(), ""+stkQuey1.getResultDesc(), Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "onResponse:"+response.body());
                        String body = stkQuey1.getResultDesc();
                        ResponseDescription = stkQuey1.getResponseDescription();
                        ResultCode = stkQuey1.getResultCode();
                        progressStk.dismiss();
                        pauseTimer();
                        resetTimer();
                        if (ResultCode.equals("0")){

                            BtnStatus =0;
//                        BatchProcess();
                            new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Deposit successfully.")
                                    .show();
                            progressStk.dismiss();

                        }else if (ResultCode.equals("1032")){

                            BtnStatus = 1;

                            new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("This payment was cancelled")
                                    .setConfirmText("Close")
                                    .show();

                        }else if (ResultCode.equals("1031")){

                            BtnStatus = 1;

                            new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("This payment was cancelled")
                                    .setConfirmText("Close")
                                    .show();

                        }else if (ResultCode.equals("2001")) {
                            BtnStatus = 2;
                            new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry you entered a wrong pin. Try again")
                                    .setConfirmText("Okay")
                                    .show();


                        }else if (ResultCode.equals("1")) {
                            BtnStatus = 3;
                            //  successAlert2("Sorry you entered a wrong pin",BtnStatus);
                            new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("You current balance is insufficient.")
                                    .setConfirmText("Close")
                                    .show();
                        }


                    }else if (response.code()==404){

                        Toast.makeText(getContext(), "Bad request", Toast.LENGTH_SHORT).show();

                    }

                }

            }

            @Override
            public void onFailure(Call<StkQuey> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void BatchProcess(){
        String Doc_id = mAuth.getCurrentUser().getUid();
        WriteBatch batch = db.batch();
        DocumentReference doc1 = vendorRef.document(Doc_id);
        batch.update(doc1, "Activation_fee", 150);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {


                } else {

                }

            }
        });
    }





    //-----Load Details-----//
    private String location,Phone;
    private String activation_fee;
    private long UnremittedCash;
    long Activeshops;
    long inActiveShops;
    private void loadData() {

        adminRef.document("Elmasha").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (documentSnapshot.exists()){

                     Activeshops = documentSnapshot.getLong("Active_Shops");
                     inActiveShops = documentSnapshot.getLong("Inactive_shops");

                }
            }
        });
        vendorRef.document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (documentSnapshot.exists()) {

                    Gas_Vendor vendorUser = documentSnapshot.toObject(Gas_Vendor.class);

                    First_name =vendorUser.getFirst_name()+" "+ vendorUser.getLast_name();
                    Email =vendorUser.getEmail();
                    Shopname = vendorUser.getShopName();
                    shopNO = vendorUser.getShop_No();
                    Phone =vendorUser.getMobile();
                    User_image = vendorUser.getUser_Image();
                    activation_fee = vendorUser.getActivation_fee();
                    Trips = vendorUser.getTrips();
                    UnremittedCash = (int) vendorUser.getEarnings();
                    location = vendorUser.getLocation();
                    Cash_Trips = vendorUser.getCash_Trips();






                    shopname.setText(Shopname);
                    shop_No.setText("Shop no. #"+shopNO);
                    Cash_tripsV.setText(Cash_Trips+"");
                    activateNo.setText(Phone);
                    Trips_V.setText(Trips+"");


                    if (Cash_Trips >= 3){
                        if (activation_fee.equals("200")){
                            DeactivateShop();
                        }
                       
                    }



                    if (activation_fee.equals("0")){

                        feeStatus.setText("InActive");
                        feeStatus.setTextColor(Color.parseColor("#FA0707"));
                        Activation_payment.setVisibility(View.GONE);
                        ActivationLayout.setVisibility(View.VISIBLE);


                    }else if (activation_fee.equals("00")){

                        feeStatus.setText("InActive");
                        feeStatus.setTextColor(Color.parseColor("#FA0707"));
                        Activation_payment.setVisibility(View.GONE);
                        PayError.setVisibility(View.VISIBLE);
                        PayError.setText("You have not remitted charges for 3 cash transactions. Remit Ksh/."+ UnremittedCash +" to be able to proceed");
                        ActivationLayout.setVisibility(View.GONE);


                    }else if (activation_fee.equals("200")){

                        feeStatus.setText("Active");
                        feeStatus.setTextColor(Color.parseColor("#3DD81A"));
                        Activation_payment.setEnabled(false);
                        Activation_payment.setVisibility(View.GONE);
                        ActivationLayout.setVisibility(View.GONE);

                    }



                } else {

                }


            }
        });



    }


    //--Deactivate shop ----
    private String doc_id;
    private void DeactivateShop() {

        String MUID = mAuth.getCurrentUser().getUid();
        WriteBatch batch = db.batch();
        DocumentReference doc1 = vendorRef.document(MUID);
        batch.update(doc1, "Activation_fee", "00");
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    long minus = Activeshops - 1;
                    long add = inActiveShops + 1;


                    HashMap<String ,Object> admin = new HashMap<>();
                    admin.put("Active_Shops",minus);
                    admin.put("Inactive_shops",add);
                    adminRef.document("Elmasha").update(admin);

                    String UiD = mAuth.getCurrentUser().getUid();
                    Map<String, Object> notify = new HashMap();
                    notify.put("Name", "Shop is now inactive");
                    notify.put("User_ID", UiD);
                    notify.put("type", "You have not remitted charges for 3 cash transactions. Remit Ksh/."+ UnremittedCash +" to activate");
                    notify.put("Order_iD",UiD);
                    notify.put("to",UiD);
                    notify.put("from",UiD);
                    notify.put("timestamp", FieldValue.serverTimestamp());
                    vendorRef.document(mAuth.getCurrentUser().getUid()).collection("Notifications").add(notify);
                }else {

                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    private boolean Validate(){
        String no = activateNo.getText().toString().trim();
        if (no.isEmpty()){
            Toast.makeText(getContext(), "Provide phone number..", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            activateNo.setError(null);
            return true;
        }

    }


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


    public void showSnackBarOnline() {

        Snackbar.with(getContext(),null).type(Type.SUCCESS).message("Back Online")
                .duration(Duration.LONG)
                .fillParent(true)
                .textAlign(Align.CENTER).show();

    }



    public void showSnackBaroffline() {

        Snackbar.with(getContext(),null).type(Type.ERROR).message("Offline: Check Internet Connection")
                .duration(Duration.LONG)
                .fillParent(true)
                .textAlign(Align.LEFT).show();

    }

    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {

            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo data = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (data != null && data.isConnected() || wifi != null && wifi.isConnected())
                return true;
            else return false;


        } else {
            return false;
        }

    }





}