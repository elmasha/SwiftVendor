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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Adapter.PaymentAdapter;
import com.gas.swiftel.Common;
import com.gas.swiftel.Interface.RetrofitInterface;
import com.gas.swiftel.Model.Gas_Vendor;
import com.gas.swiftel.Model.Payment_Hist;
import com.gas.swiftel.Model.ResponseStk;
import com.gas.swiftel.Model.Result;
import com.gas.swiftel.Model.StkQuey;
import com.gas.swiftel.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.ybq.android.spinkit.style.DoubleBounce;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.Calendar;
import java.util.Date;
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


 public class Payments extends Fragment {
 View view;
    private static final long START_TIME_IN_MILLIS_COUNT = 27000;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    CollectionReference vendorShopRef = db.collection("VendorsShop");
    CollectionReference Payment_HistRef = db.collection("Payment_History");
    CollectionReference NotifyRef = db.collection("Notifications");
     CollectionReference adminRef = db.collection("Admin");
    private PaymentAdapter adapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton Deposit_remits;
    private AlertDialog dialog3,dialog2,Deposit_Dialog;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS_COUNT;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = Common.BASE_URL;
    public  String Product_Id,CheckoutRequestID,ResponseCode,
            ResultCode,ResponseDescription,ResultDesc;
    private ProgressDialog progressStk;
    private int BtnStatus;
    private TextView Balance,PaymentMessage;


    public Payments() {
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
        view = inflater.inflate(R.layout.fragment_payments, container, false);
        mAuth = FirebaseAuth.getInstance();
        mRecyclerView = view.findViewById(R.id.RecyclerPayHist);
        Deposit_remits = view.findViewById(R.id.Trips_deposit);
        Balance = view.findViewById(R.id.Earnings);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Deposit_remits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_Deposit();

            }
        });



        loadData();
        FetchProduct();
        return view;
    }




     private String payname,paytype,payuser,payid,paytrips,paycash,payphone,paytime,date;
    private int deletePosition;
    private void FetchProduct() {


        Query query = Payment_HistRef.whereEqualTo("User_ID", mAuth.getCurrentUser().getUid())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50);
        FirestoreRecyclerOptions<Payment_Hist> options;
        options = new FirestoreRecyclerOptions.Builder<Payment_Hist>()
                .setQuery(query, Payment_Hist.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new PaymentAdapter(options);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new PaymentAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                deletePosition  = position;

                Payment_Hist payment_hist = documentSnapshot.toObject(Payment_Hist.class);

                long milisecond = payment_hist.getTimestamp().getTime();

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


    private void newtime(){
        new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (CheckoutRequestID != null){
                    StkQueryRemit(CheckoutRequestID);

                }else {

                    if (progressStk != null)progressStk.dismiss();
                    else if (Deposit_Dialog != null)Deposit_Dialog.dismiss();
                    //Toast.makeText(getContext(), "StkPush Request timeout...", Toast.LENGTH_LONG).show();
                    ToastBack("StkPush Request timeout...");
                    // progressStk.dismiss();
                }
            }
        }.start();
    }


    private TextView Name,Paytime,UserName,Paytype,PayID,PayTrips,PayAmount,PayPhone,closePay;
    private FloatingActionButton deletePay,printPay;
    private AlertDialog dialog;
    public void ShowPayment() {


        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete payment");
                builder.setIcon(R.drawable.trash);
                builder.setMessage("Are you sure.? \n" + "Date " + date);
                builder.show();


                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "payment "+ payid +" deleted", Toast.LENGTH_SHORT).show();
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


    private long Balance_wallet;
    private Button boost_balance;
    private TextView show_No,close_Dialog,edit_no,input_Amount,cashTrips;
    private EditText input_phone;
    private ProgressBar progressBar1;

    private String Cash,PaymentName,PaymentType,No;
    private LinearLayout linearLayoutPhone;
    private int PhoneState = 0;
    public void Dialog_Deposit() {

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialogdeporemist, null);
        mbuilder.setView(mView);
        mbuilder.setCancelable(false);
        Deposit_Dialog = mbuilder.create();
        Deposit_Dialog.show();
        boost_balance = mView.findViewById(R.id.DepositBtn);
        input_Amount =mView.findViewById(R.id.DepositAmount);
        input_phone = mView.findViewById(R.id.DepositPhone);
        show_No = mView.findViewById(R.id.deposit123);
        edit_no = mView.findViewById(R.id.Edit_phone23);
        close_Dialog = mView.findViewById(R.id.CloseDialogWallet);
        progressBar1 = mView.findViewById(R.id.spin_kitItem12);
        PaymentMessage = mView.findViewById(R.id.Message2);
        cashTrips = mView.findViewById(R.id.cashTrips);
        linearLayoutPhone = mView.findViewById(R.id.LinearPhone);
        show_No.setText("Deposit from "+Phone);

        input_phone.setText(Phone);
        input_Amount.setText("Remit : "+ UnremitedCash +"");
        cashTrips.setText("Paid in cash\n"+Cash_Trips);



        boost_balance.setText("Deposit: "+ UnremitedCash +"");
        DoubleBounce rotatingCircle = new DoubleBounce();
        progressBar1.setIndeterminateDrawable(rotatingCircle);

        if (Cash_Trips >= maxTrips){
            PaymentMessage.setVisibility(View.VISIBLE);
            PaymentMessage.setText("You have not remitted charges for "+Cash_Trips+" cash transactions. Remit  Ksh/."+UnremitedCash+" to be able to proceed.");
        }else {

        }



        close_Dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Deposit_Dialog !=null)Deposit_Dialog.dismiss();

            }
        });

        edit_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    linearLayoutPhone.setVisibility(View.GONE);

            }
        });
        boost_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Cash = String.valueOf(UnremitedCash);

                if (UnremitedCash == 0){

                    Toast.makeText(getContext(), "Invalid Payment", Toast.LENGTH_SHORT).show();


                }else {


                   if (!isConnected(getContext())){
                       showSnackBaroffline();
                   }else {
                       Mpesa_Dialog();
                   }
//
//                    boost_balance.setVisibility(View.INVISIBLE);
//                    progressBar1.setVisibility(View.VISIBLE);

                }

            }
        });

    }

    private String Doc_ID,doc_ID;
    private void PaymentHistory(String cash, String no,String paymentName,String paymentType,String  first_name, String checkoutRequestID) {

        Doc_ID = Payment_HistRef.document().getId();
        String UiD = mAuth.getCurrentUser().getUid();
        Map<String, Object> notify = new HashMap();
        notify.put("Name", paymentName);
        notify.put("Amount", cash);
        notify.put("PhoneNo", "Payment from "+no);
        notify.put("Type",paymentType);
        notify.put("User_ID",UiD);
        notify.put("User_name",first_name);
        notify.put("Trips",Cash_Trips);
        notify.put("Payment_ID",Doc_ID);
        notify.put("MpesaCheckout_ID",checkoutRequestID);
        notify.put("timestamp", FieldValue.serverTimestamp());
        Payment_HistRef.document(Doc_ID).set(notify).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    doc_ID = NotifyRef.document().getId();
                    String UiD = mAuth.getCurrentUser().getUid();
                    Map<String, Object> notify = new HashMap();
                    notify.put("Name", paymentName);
                    notify.put("User_ID", UiD);
                    notify.put("type", paymentType);
                    notify.put("Order_iD",doc_ID);
                    notify.put("to",UiD);
                    notify.put("from",UiD);
                    notify.put("timestamp", FieldValue.serverTimestamp());
                    NotifyRef.document(Doc_ID).set(notify);
                    new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Remits deposited successfully.")
                            .show();
                    Deposit_Dialog.dismiss();


                }else {

                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

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

                    StkQueryRemit(CheckoutRequestID);

                }else {

                    if (progressStk != null)progressStk.dismiss();
                    else if (Deposit_Dialog != null)Deposit_Dialog.dismiss();
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
                         dialog.dismiss();
                         String no = input_phone.getText().toString();
                         no.substring(1);
                         stkRemit(String.valueOf(UnremitedCash),no);
                         startTimer();
                         boost_balance.setVisibility(View.INVISIBLE);
                         progressBar1.setVisibility(View.VISIBLE);
                     }
                 });
         builder.setNegativeButton("No",
                 new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                         linearLayoutPhone.setVisibility(View.GONE);

                     }
                 });

         builder.setCancelable(true);
         builder.show();
     }


     private void stkRemit(String cash, String phone){
        No = input_phone.getText().toString();
        String PhoneNumber = "254"+No.substring(1);
        Doc_ID = Payment_HistRef.document().getId();
        PaymentName = "Remit deposit";
        PaymentType = "Total trips "+Cash_Trips+" for remit Ksh/="+Cash;
        Map<String ,Object> stk_Push = new HashMap<>();
        stk_Push.put("amount",cash);
        stk_Push.put("phone", PhoneNumber);
        stk_Push.put("transDec", "Remit collection.");
        stk_Push.put("Name", PaymentName);
        stk_Push.put("PhoneNo", "Payment from "+No);
        stk_Push.put("Type",PaymentType);
        stk_Push.put("User_ID",mAuth.getCurrentUser().getUid());
        stk_Push.put("User_name",First_name);
         stk_Push.put("ActiveNo",Activeshops);
         stk_Push.put("InActiveNo",inActiveShops);
        stk_Push.put("Trips",Cash_Trips);
        stk_Push.put("Payment_ID",Doc_ID);


        Call<ResponseStk> callStk = retrofitInterface.stk_pushRemit(stk_Push);

        callStk.enqueue(new Callback<ResponseStk>() {
            @Override
            public void onResponse(Call<ResponseStk> call, Response<ResponseStk> response) {
                if (response.code()== 200){
                    newtime();
                    progressStk = new ProgressDialog(getContext());
                    progressStk.setCancelable(false);
                    progressStk.setMessage("Mpesa StkPush transaction..");
                    progressStk.show();

                    ResponseStk responseStk = response.body();
                    String responeDesc = responseStk.getCustomerMessage();
                    ResponseCode = responseStk.getResponseCode();
                    CheckoutRequestID = responseStk.getCheckoutRequestID();
                    String errorMessage = responseStk.getErrorMessage();
                    String errorCode = responseStk.getErrorCode();
                    Log.i("TAG", "CheckoutRequestID: " + response.body());

                    //Toast.makeText(getContext(), responeDesc , Toast.LENGTH_LONG).show();
                    if (responeDesc != null){
                        if (responeDesc.equals("Success. Request accepted for processing")){

                            ToastBack(responeDesc);

                        }
                    }else {

                        if (errorMessage.equals("No ICCID found on NMS")){

                            ToastBack("Please provide a valid mpesa number.");
                            progressStk.dismiss();
                        }

                        ToastBack(errorMessage);
                        progressStk.dismiss();
                    }






                    if (dialog2 != null)dialog2.dismiss();
                    if (Deposit_Dialog != null)Deposit_Dialog.dismiss();
                    boost_balance.setVisibility(View.VISIBLE);
                    progressBar1.setVisibility(View.INVISIBLE);

                }else if (response.code()==404){


                    Toast.makeText(getContext(), "Bad request", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseStk> call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                startTimer();
            }
        });

    }

    ////----Mpesa stk Query-----
    private void StkQueryRemit(String checkoutRequestID){

        Map<String ,Object > stk_Query = new HashMap<>();
        stk_Query.put("checkoutRequestId",checkoutRequestID);
        Call<StkQuey> callQuery = retrofitInterface.stk_QueryRemit(stk_Query);

        callQuery.enqueue(new Callback<StkQuey>() {
            @Override
            public void onResponse(Call<StkQuey> call, Response<StkQuey> response) {
                if (response != null){

                    if (response.code()== 200){

                        StkQuey stkQuey1 = response.body();

                        Log.i("TAG", "onResponse:"+response.body());
                        String body = stkQuey1.getResultDesc();
                        ToastBack(body);
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
                            // successAlert2("This request was cancelled",BtnStatus);

                            new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("This payment was cancelled")
                                    .setConfirmText("Close")
                                    .show();

                        }else if (ResultCode.equals("1031")){

                            BtnStatus = 1;
                            // successAlert2("This request was cancelled",BtnStatus);

                            new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("This payment was cancelled")
                                    .setConfirmText("Close")
                                    .show();

                        }else if (ResultCode.equals("2001")) {
                            BtnStatus = 2;
                            // successAlert2("Sorry you entered a wrong pin",BtnStatus);
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
                if (CheckoutRequestID != null){

                    StkQueryRemit(CheckoutRequestID);

                }else {

                    if (progressStk != null)progressStk.dismiss();
                    else if (Deposit_Dialog != null)Deposit_Dialog.dismiss();

                }
            }
        });



    }


    private void BatchProcess(){
        String Doc_id = mAuth.getCurrentUser().getUid();
        WriteBatch batch = db.batch();
        DocumentReference doc1 = vendorRef.document(Doc_id);
        batch.update(doc1, "Activation_fee", 150);
        batch.update(doc1, "Cash_Trips", 0);
        batch.update(doc1, "Earnings", 0);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    PaymentHistory(Cash,No,PaymentName,PaymentType,First_name,CheckoutRequestID);

                } else {

                }

            }
        });
    }

    private TextView Dia_Header,Dia_time;
    public void successAlert(String responseDescription,int btnStatus) {

        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a",new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.dialog_activate, null);
        mbuilder.setView(mView);
        dialog3 = mbuilder.create();
        dialog3.setCancelable(false);
        dialog3.show();

        TextView showData = mView.findViewById(R.id.LablePay2);
        Dia_Header = mView.findViewById(R.id.LablePay);
        Dia_time = mView.findViewById(R.id.LablePayTime);
        final TextView showtime = mView.findViewById(R.id.LablePayTime);
        final Button confirm = mView.findViewById(R.id.Confirm_pay);
        final Button cancel_pay = mView.findViewById(R.id.Cancel_pay);





        showData.setText(responseDescription +"\n" +Phone+ "\n");
        Dia_time.setText("Date: "+date);



        if (btnStatus == 0){
            Dia_Header.setTextColor(getResources().getColor(R.color.ColorGreen));
            Dia_Header.setText("Success..!");
            confirm.setText("Done");
            confirm.setVisibility(View.VISIBLE);


            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    confirm.setVisibility(View.GONE);
                    cancel_pay.setVisibility(View.GONE);
                    dialog3.dismiss();




                }
            });

        }else if (btnStatus ==1){
            cancel_pay.setText("Cancel");
            cancel_pay.setVisibility(View.VISIBLE);
            cancel_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog3.dismiss();
                }
            });


        }else if (btnStatus ==2){

            cancel_pay.setText("Cancel");
            cancel_pay.setVisibility(View.VISIBLE);
            cancel_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog3.dismiss();
                }
            });
        }



    }

    private int Amount23;
    private boolean ValidationDeposit() {

        double val = 0.0;

        Amount23 = Integer.parseInt(input_Amount.getText().toString());

        if (Amount23 == 0) {
            Toast.makeText(getContext(), "Invalid Transaction Can't Deposit 0 Amount", Toast.LENGTH_SHORT).show();

            return false;

        } else if (Amount23 == val) {
            Toast.makeText(getContext(), "Invalid Transaction Can't Deposit 0 value", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            input_Amount.setError(null);
            return true;
        }
    }


    //-----Load Details-----//
    private String location,Phone,First_name,Email,Shopname,shopNO,User_image;
    private long UnremitedCash,Trips,Cash_Trips;
    private String activation_fee;
     long Activeshops;
     long inActiveShops;
     long maxTrips;
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
                    maxTrips = documentSnapshot.getLong("max_trips");

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
                    UnremitedCash = (int) vendorUser.getEarnings();
                    location = vendorUser.getLocation();
                    Cash_Trips = vendorUser.getCash_Trips();

                    Balance.setText(UnremitedCash +"");
                    if (Cash_Trips >= maxTrips){
                        if (activation_fee.equals("200")){
                            DeactivateShop();
                        }

                    }else {

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


                    //----Admin function ----
                long minus = Activeshops - 1;
                long add = inActiveShops + 1;
                HashMap<String ,Object> admin = new HashMap<>();
                admin.put("Active_Shops",minus);
                admin.put("Inactive_shops",add);
                adminRef.document("Elmasha").update(admin);


                    doc_ID = NotifyRef.getId();
                    String UiD = mAuth.getCurrentUser().getUid();
                    Map<String, Object> notify = new HashMap();
                    notify.put("Name", "Shop is now inactive");
                    notify.put("User_ID", UiD);
                    notify.put("type", "You have reached maximum of "+Cash_Trips+" cash transactions. Remit Ksh/."+UnremitedCash+" to activate");
                    notify.put("Order_iD",doc_ID);
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