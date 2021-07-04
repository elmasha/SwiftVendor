package com.gas.swiftel.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.gas.swiftel.BuildConfig;
import com.gas.swiftel.Common;
import com.gas.swiftel.Interface.RetrofitInterface;
import com.gas.swiftel.Model.Gas_Vendor;
import com.gas.swiftel.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class VendorProfileFragment extends Fragment {

    private TextView Activation_payment,Logout,vendor_name,email,phone,shopname,earnings,shop_No,AboutApp,ShareApp;
    private CircleImageView vendor_image;
    private String Phone;
    private static final long START_TIME_IN_MILLIS_COUNT = 27000;
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset,ExchangeBtn;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS_COUNT;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = Common.BASE_URL;
    public  String Product_Id,CheckoutRequestID,ResponseCode,
            ResultCode,ResponseDescription,ResultDesc;
    private String First_name,Email,shopNO,Shopname,User_image,No;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    CollectionReference Active_VendorsREf = db.collection("Active_Vendors");
    CollectionReference NotifyRef = db.collection("Notifications");
    CollectionReference FeedBack_Ref = db.collection("FeedBack_SwiftVendor");


    private int BtnStatus;
    Daraja daraja;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Button btnStk;
    private AlertDialog dialog2;
    private String PhoneNumber,PaymentName,PaymentType;
    private String activation_fee;
    private TextView feeStatus,Trips_V,Earings_V,Cash_tripsV,privacy,Avg_Rates,Feedback;
    private long Trips,Cash_Trips;

    private AlertDialog dialog3;
    private int Earnings;
    ProgressDialog progressStk,progressBar_pay,progressDialog;
    private RatingBar ratingBar;
    
    private LinearLayout MydetailsLayout;
    private FloatingActionButton ShopTab,PaymentsTab,EditProfileTab,MoreTab;
    private RelativeLayout myShop_layout,myPayment_layout,myEdit_layout,moreLayout;
    private TextView CloseOpen_tab;
    private TextInputLayout firstname_edit,phonenumber_edit,email_edit,shop_edit,loaction_edit;

    private Button SaveChanges;


    public VendorProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_vendorprofile, container, false);
        mAuth = FirebaseAuth.getInstance();


        ShopTab = view.findViewById(R.id.Myshop);
        PaymentsTab = view.findViewById(R.id.My_payment);
        EditProfileTab = view.findViewById(R.id.edit_account);
        MoreTab = view.findViewById(R.id.more);
        MydetailsLayout = view.findViewById(R.id.Linear89);
        CloseOpen_tab = view.findViewById(R.id.Close_payTab);

        myShop_layout = view.findViewById(R.id.My_shopTab);
        myPayment_layout = view.findViewById(R.id.My_paymentTab);
        myEdit_layout = view.findViewById(R.id.My_editTab);
        moreLayout = view.findViewById(R.id.My_moreTab);




        //----Edit changes---///
        firstname_edit = view.findViewById(R.id.text_edit_fullname);
        phonenumber_edit = view.findViewById(R.id.text_edit_number);
        shop_edit =view.findViewById(R.id.text_edit_shop);
        email_edit = view.findViewById(R.id.text_edit_email);
        loaction_edit = view.findViewById(R.id.text_edit_location);

        SaveChanges = view.findViewById(R.id.Submit_changes);

        Activation_payment = view.findViewById(R.id.Payment);

        vendor_name = view.findViewById(R.id.Vendor_Name);

        email = view.findViewById(R.id.Emailv);
        phone = view.findViewById(R.id.Phonev);
        vendor_image = view.findViewById(R.id.Vendor_profileImage);
        shopname = view.findViewById(R.id.Vendor_ShopNmae);
        shop_No = view.findViewById(R.id.Shop_number);
        feeStatus = view.findViewById(R.id.PaymentFee);
        Trips_V = view.findViewById(R.id.Earningstrips);
        Earings_V = view.findViewById(R.id.Earnings);

        Cash_tripsV = view.findViewById(R.id.Trips);



//        SaveChanges.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Update_profile();
//            }
//        });

        ShopTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MydetailsLayout.setVisibility(View.VISIBLE);
                loadFragment(new Myshop());
            }
        });

        PaymentsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFragment();
                MydetailsLayout.setVisibility(View.GONE);
                loadFragment(new Payments());

            }
        });

        EditProfileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFragment();
                MydetailsLayout.setVisibility(View.GONE);
                loadFragment(new EditProfile());

            }
        });

        MoreTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFragment();
                MydetailsLayout.setVisibility(View.VISIBLE);
                loadFragment(new More());
            }
        });




        
        


//        Deposit_remits.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Dialog_Deposit();
//
//            }
//        });

//
//        Date currentTime = Calendar.getInstance().getTime();
//        String date = DateFormat.format("dd MMM ,yyyy",new Date(String.valueOf(currentTime))).toString();
//
//
//
//
//

//
//
//

//
//        Activation_payment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertSKPush();
//            }
//        });


        loadData();
//        FetchProduct();

        loadFragment(new Myshop());
        return view;
    }




    public static void shareApp(Context context) {
        final String appPackageName = BuildConfig.APPLICATION_ID;
        final String appName = context.getString(R.string.app_name);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBodyText = "https://play.google.com/store/apps/details?id=" +
                appPackageName;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
        context.startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FrameProfileLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean removeFragment(){

        if(getChildFragmentManager()
                .findFragmentById(R.id.FrameProfileLayout) != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .remove(getChildFragmentManager().findFragmentById(R.id.FrameProfileLayout)).commit();
        }
        return false;
    }


    //-----Load Details-----//
    private String location;
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


                    if (User_image != null){

                        Picasso.with(getContext()).load(User_image).placeholder(R.drawable.load).error(R.drawable.profile).into(vendor_image);

                    }

                    vendor_name.setText(First_name );
                    email.setText(Email);






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



    //---Activation alertDialog -----
    private void AlertSKPush() {

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.phone_dialog, null);
        mbuilder.setView(mView);
        dialog2 = mbuilder.create();
        dialog2.show();

        EditText phoneNumber = mView.findViewById(R.id.Mobile_number);
        phoneNumber.setText(Phone.substring(1));
         btnStk = mView.findViewById(R.id.StkPush);
        progressBar = mView.findViewById(R.id.Spin_kitPayment);

        btnStk.setOnClickListener(v -> {


             No = phoneNumber.getText().toString();
             No.substring(1);

             if (No.isEmpty()){

                 Toast.makeText(getContext(), "Phone No missing...", Toast.LENGTH_SHORT).show();

             }else {
                 PaymentName = "Activation Fee";
                 PaymentType = "Total ksh "+150+" activation fee";

//                 startTimer();
//                 stk(No,"150");
                 dialog2.dismiss();
             }



        });




    }



    //--Activate account ----
    private String doc_ID;
    private void UpdateAccountfee() {

        String MUID = mAuth.getCurrentUser().getUid();
        WriteBatch batch = db.batch();
        DocumentReference doc1 = vendorRef.document(MUID);
        batch.update(doc1, "Activation_fee", 150);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Map<String ,Object> activate = new HashMap<>();
                    activate.put("Vendor_name",First_name);
                    activate.put("Email",Email);
                    activate.put("Shop_Number",shopNO);
                    activate.put("Shop_name",Shopname);
                    activate.put("timestamp",FieldValue.serverTimestamp());
                    Active_VendorsREf.document(MUID).set(activate);

                    doc_ID = NotifyRef.getId();
                    String UiD = mAuth.getCurrentUser().getUid();
                    Map<String, Object> notify = new HashMap();
                    notify.put("Name", "Activation fee");
                    notify.put("User_ID", UiD);
                    notify.put("type", First_name+" your account successfully activated.");
                    notify.put("Order_iD",doc_ID);
                    notify.put("to",UiD);
                    notify.put("from",UiD);
                    notify.put("timestamp", FieldValue.serverTimestamp());
                    NotifyRef.document().set(notify);
                }else {

                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void errorAlert() {
        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a",new Date(String.valueOf(currentTime))).toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error Occurred .!");
        builder.setMessage("Check Mpesa balance" +"\n"+"Network connection,"+"\n"+"or Phone number"+"\n"+"\n"+"Date "+date);
        builder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setCancelable(false);
        builder.show();
    }


}
