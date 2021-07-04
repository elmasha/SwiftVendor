package com.gas.swiftel.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.gas.swiftel.Model.Gas_Vendor;
import com.gas.swiftel.R;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.gas.swiftel.Model.Token;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hsalf.smileyrating.SmileyRating;
import com.squareup.picasso.Picasso;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.GeoQueryEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.gas.swiftel.Activity.WalkthroughActivity.REQUEST_CHECK_SETTINGS;
import static com.gas.swiftel.Activity.WalkthroughActivity.REQUEST_ENABLE_GPS;
import static com.gas.swiftel.Activity.WalkthroughActivity.mLocationSettingsRequest;
import static com.gas.swiftel.Activity.WalkthroughActivity.mSettingsClient;
import static com.github.ybq.android.spinkit.animation.AnimationUtils.start;

public class MapsVActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, RoutingListener {

    private static final int GALLERY_REQUEST_CODE = 1;
    private GoogleMap mMap;
    public static GoogleApiClient googleApiClient1;
    public static  Location lastLocation;
    public static LocationRequest locationRequest;
    private FirebaseAuth mAuth;

    private String ClientId = " ";
    private CoordinatorLayout coordinatorLayout;
    private Marker vendorMarker;
    private LatLng vendorLocation;
    private Button finderRoute;
    private  String userId;

    private Snackbar snackbar;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 7000;
    public static final int PLAY_SERVICE_RES_REQUEST = 7001;


    private static int UPDATE_INTERVAL = 1000;
    private static int FAST_INTERVAL = 1000;
    private static int DISPLACEMENT = 10;

    private FloatingActionButton fabMenu,fabRefresh,Feedback;

    private SwitchCompat shop_switch;
    SupportMapFragment mapFragment;
    private Button OPen_shop;

    private String GasName,GasDesc,GasKgs;
    private long Gasprice,GasrefillPrice;
    private String ItemName,ItemDesc,ItemPrice;

    private ImageView gasImage;
    private CircleImageView MyProfile;

    private  int radius;
    private String VendorFoundId ;
    private static final int LIMIT= 3;

    private TextView lable_switch,location,ShopName,ShopNo,Shopstatus,closed_shop,refreshText;

    private long backPressedTime;
    private Toast backToast;

    private int Shop_Status;

    private EditText gasName,gasDesc,gasPrice,gasRefilprice,itemName,itemDesc,itemAmount;
    private Spinner gasKgs;
    private Button submitGas;
    FirebaseStorage storage;
    private Uri imageUri;
    private String Gas_Image;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ClientAvailable = db.collection("Client_location");
    CollectionReference VendorAvailableRef = db.collection("VendorsAvailable");
    CollectionReference PreferVendorAvailableRef = db.collection("PreferVendorsAvailable");
    CollectionReference adminRef = db.collection("Admin");
    CollectionReference VendorWoringkRef = db.collection("VendorsWorking");
    CollectionReference VendorAroundRef = db.collection("VendorsAround");
    CollectionReference vendorShopRef = db.collection("VendorsShop");
    GeoFirestore geoFireStoreAvailbale = new GeoFirestore(VendorAvailableRef);
    GeoFirestore geoFireStorePreferAvailbale = new GeoFirestore(PreferVendorAvailableRef);
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    CollectionReference FeedBack_Ref = db.collection("FeedBack_SwiftVendor");
    GeoFirestore geoFireStoreWorking = new GeoFirestore(VendorWoringkRef);


    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorSecondary};

    private String customerId = "", destination,vendor_name;
    private LatLng destinationLatLng,pickupLatLng;
    private float rideDistance;
    private String activation_fee;
    private double pickupLat,pickupLng;
    private AlertDialog dialog_success,dialog_close;


    private CardView shopInFoCard;
    private LinearLayout VendorAssest,LinearfeedBack;
    private RelativeLayout relativeLayoutRefresh;
    private int ShopState;

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient1!= null){
            googleApiClient1.connect();
        }

            loadData();
            if (Shop_Status ==1){

                LocationUpdates();

                VendorAssest.setVisibility(View.VISIBLE);
                fabRefresh.setVisibility(View.VISIBLE);
                OPen_shop.setText("Manage shop");
                lable_switch.setText("Switch to close ");
                OPen_shop.setBackground(getResources().getDrawable(R.drawable.roubtnsec));
                closed_shop.setVisibility(View.GONE);
                location.setVisibility(View.VISIBLE);
                shopInFoCard.setVisibility(View.VISIBLE);
                if (cash_trips >= 3){
                    if (activation_fee.equals("200")){
                        DeactivateShop();
                    }

                }

            }else if (Shop_Status== 0){

                VendorAssest.setVisibility(View.INVISIBLE);

                closed_shop.setVisibility(View.VISIBLE);
                OPen_shop.setText("Shop Closed");
                OPen_shop.setVisibility(View.GONE);
                lable_switch.setText("Switch to open");
                OPen_shop.setBackground(getResources().getDrawable(R.drawable.roundbtn_black));
                shopInFoCard.setVisibility(View.INVISIBLE);


            }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!googleApiClient1.isConnected()) {
            googleApiClient1.connect();
        }
        loadData();


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (!googleApiClient1.isConnected()) {
            googleApiClient1.connect();
        }
        loadData();


    }



    @Override
    protected void onStop() {
        super.onStop();
        Check_Location_enable();
        googleApiClient1.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_v);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();

        shop_switch =  findViewById(R.id.Shop_switch);
        OPen_shop = findViewById(R.id.Open_shop);

        lable_switch = findViewById(R.id.lableSwitch);
        location = findViewById(R.id.location);
        coordinatorLayout = findViewById(R.id.CoMap);
        shopInFoCard = findViewById(R.id.MCardInfo);
        ShopName = findViewById(R.id.MShpName);
        ShopNo = findViewById(R.id.MShpNo);
        Shopstatus = findViewById(R.id.MShpStatus);
        VendorAssest = findViewById(R.id.VendorAsset);
        fabRefresh = findViewById(R.id.reFresh);
        Feedback = findViewById(R.id.Feedback);
        LinearfeedBack = findViewById(R.id.L125);
        closed_shop = findViewById(R.id.close_shop24);
        MyProfile  = findViewById(R.id.MyProfileImage);
        relativeLayoutRefresh = findViewById(R.id.layout_refresh);
        refreshText = findViewById(R.id.refreshText);


        Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog_FeedBack();

            }
        });


        setUplocation();
        loadData();

        fabMenu = findViewById(R.id.Add_ItemFloat);


        shopInFoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shop_switch.isChecked()){

                    startActivity(new Intent(getApplicationContext(),Home_Activity.class));

                }else {

                    Snackbar snackbar = Snackbar.make(coordinatorLayout,"Shop Closed",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });


        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shop_switch.isChecked()){


                    if (activation_fee.equals("0")){

                        successAlert("To Upload your products please Visit Profile to activate your account or click " +
                                "pay later to continue");


                    }else if (activation_fee.equals("00")){

                         successAlert("You have not remitted charges for 3 cash transactions. Remit Ksh/."+ UremittedCash +" to be able to proceed");

                    }else if (activation_fee.equals("200")){

                        startActivity(new Intent(getApplicationContext(),AddItemsActivity.class));

                    }

                }else {

                    Snackbar snackbar = Snackbar.make(coordinatorLayout,"Shop Closed",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });


        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shop_switch.isChecked()){

                    LocationUpdates();
                    Vendor_Location();

                }else {

                    Snackbar snackbar = Snackbar.make(coordinatorLayout,"Shop Closed",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });


        shop_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOnline) {
                if (isOnline){
                    ShopState =1;

                    shopStatus();
                }else{
                    ShopState = 0;
                    shopStatus();

                }

            }
        });




        OPen_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shop_switch.isChecked()){

                    startActivity(new Intent(getApplicationContext(),Home_Activity.class));

                }else {

                    Snackbar snackbar = Snackbar.make(coordinatorLayout,"Shop is closed",Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });


        distanceAndTime();
        Vendor_distance_Location();

    }


    void shopStatus (){


        if (ShopState ==1){

            String UID = mAuth.getCurrentUser().getUid();
            WriteBatch batch = db.batch();
            DocumentReference doc1 = vendorRef.document(UID);
            batch.update(doc1, "Shop_status", 1);
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){

                        if (dialog_close != null) dialog_close.dismiss();
                        LocationUpdates();
                      //  Vendor_Location();
                        //getCLoseClient();
                        VendorAssest.setVisibility(View.VISIBLE);
                        fabRefresh.setVisibility(View.VISIBLE);
                        LinearfeedBack.setVisibility(View.VISIBLE);
                        Snackbar snackbar = Snackbar.make(coordinatorLayout,"Shop Opened",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        OPen_shop.setText("Manage shop");
                        closed_shop.setVisibility(View.GONE);
                        OPen_shop.setVisibility(View.VISIBLE);
                        lable_switch.setText("Switch to close ");
                        OPen_shop.setBackground(getResources().getDrawable(R.drawable.roubtnsec));
                        location.setVisibility(View.VISIBLE);
                        shopInFoCard.setVisibility(View.VISIBLE);

                        showMarker(pickupLat,pickupLng);
                        if (cash_trips >= 3){
                            if (activation_fee.equals("200")){
                                DeactivateShop();
                            }

                        }

                    }else {

                        Toast.makeText(MapsVActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }else if (ShopState ==0){


            String UID = mAuth.getCurrentUser().getUid();
            WriteBatch batch = db.batch();
            DocumentReference doc1 = vendorRef.document(UID);
            batch.update(doc1, "Shop_status", 0);
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        if (dialog_close != null)dialog_close.dismiss();
                        Dialog_Close_Shop();
                        LinearfeedBack.setVisibility(View.GONE);
                    }else {

                        Toast.makeText(MapsVActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });



        }



    }

    private void showMarker(double pickupLat, double pickupLng) {
        if (vendorMarker != null)vendorMarker.remove();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickupLat, pickupLng), 15.0f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 300, null);
        vendorMarker = mMap.addMarker(new MarkerOptions()
                .title("My Shop")
                .snippet(vendor_name)
                .position(new LatLng(pickupLat, pickupLng))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.storee)));
    }

    private TextView Dia_Header,Dia_time;
    public void successAlert(String responseDescription) {

        Date currentTime = Calendar.getInstance().getTime();
        String date = DateFormat.format("dd MMM ,yyyy | hh:mm a",new Date(String.valueOf(currentTime))).toString();

        AlertDialog.Builder mbuilder = new AlertDialog.Builder(MapsVActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_activate, null);
        mbuilder.setView(mView);
        dialog_success = mbuilder.create();
        dialog_success.show();

        TextView showData = mView.findViewById(R.id.LablePay2);
        Dia_Header = mView.findViewById(R.id.LablePay);
        Dia_time = mView.findViewById(R.id.LablePayTime);
        final TextView showtime = mView.findViewById(R.id.LablePayTime);
        final Button confirm = mView.findViewById(R.id.Confirm_pay);
        final Button cancel_pay = mView.findViewById(R.id.Cancel_pay);





        showData.setText(responseDescription );
        Dia_time.setText("Date: "+date);




        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),AddItemsActivity.class));

            }
        });

        cancel_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialog_success != null)dialog_success.dismiss();

           }
        });










    }


    //----LOAD DATA------///
    private String First_name,Last_name,Email,Shopname,shopNO,Phone,User_image;
    private long UremittedCash,cash_trips;
    long Activeshops;
    long inActiveShops;

    private void loadData() {
        adminRef.document("Elmasha").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (documentSnapshot.exists()){

                    Activeshops = documentSnapshot.getLong("Active_Shops");
                    inActiveShops = documentSnapshot.getLong("Inactive_shops");

                }
            }
        });
        String userid= mAuth.getCurrentUser().getUid();


        vendorRef.document(mAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable
                                        DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (documentSnapshot.exists()){
                    Gas_Vendor vendorUser = documentSnapshot.toObject(Gas_Vendor.class);
                    First_name =vendorUser.getFirst_name() + " "+vendorUser.getLast_name() ;
                    //Last_name =vendorUser.getLast_name();
                    Email =vendorUser.getEmail();
                    Shopname = vendorUser.getShopName();
                    shopNO = vendorUser.getShop_No();
                    Phone =vendorUser.getMobile();
                    User_image = vendorUser.getUser_Image();
                    activation_fee = vendorUser.getActivation_fee();
                    Shop_Status = vendorUser.getShop_status();
                    UremittedCash = vendorUser.getEarnings();
                    pickupLat = vendorUser.getLat();
                    pickupLng = vendorUser.getLng();
                    cash_trips = vendorUser.getCash_Trips();


                    vendor_name = First_name;
                    ShopName.setText(""+Shopname);
                    ShopNo.setText("#"+shopNO);
                    Shopstatus.setText("Opened");
                    Shopstatus.setTextColor(getResources().getColor(R.color.ColorGreen));

                    switch (Shop_Status){
                        case 1:
                            LocationUpdates();


                            if (shop_switch.isChecked()){

                            }else {
                                shop_switch.toggle();
                                distanceAndTime();
                            }

                            //getCLoseClient();

                            VendorAssest.setVisibility(View.VISIBLE);
                            fabRefresh.setVisibility(View.VISIBLE);
                            OPen_shop.setText("Manage shop");
                            OPen_shop.setVisibility(View.VISIBLE);
                            closed_shop.setVisibility(View.GONE);
                            lable_switch.setText("Switch to close ");
                            OPen_shop.setBackground(getResources().getDrawable(R.drawable.roubtnsec));
                            location.setVisibility(View.VISIBLE);
                            shopInFoCard.setVisibility(View.VISIBLE);


                            showMarker(pickupLat,pickupLng);
                            if (cash_trips >= 3){
                                if (activation_fee.equals("200")){
                                    DeactivateShop();
                                }

                            }

                         break;
                        case 2:
                            VendorAssest.setVisibility(View.INVISIBLE);
                            OPen_shop.setText("Shop Closed");
                            lable_switch.setText("Switch to open");
                            OPen_shop.setVisibility(View.GONE);
                            closed_shop.setVisibility(View.VISIBLE);
                            OPen_shop.setBackground(getResources().getDrawable(R.drawable.roundbtn_black));
                            shopInFoCard.setVisibility(View.INVISIBLE);
                        break;
                        default:
                    }

                    if (User_image != null){

                        Picasso.with(MapsVActivity.this).load(User_image).placeholder(R.drawable.load).error(R.drawable.profile).into(MyProfile);

                    }
                }
            }
        });

    }
    //.....end LOAD DATA..



    ///----Measure Distance -----////
    private int distMeters;
    private void distanceAndTime() {

        if (lastLocation != null){


            LatLng oldLocation = new LatLng(pickupLat, pickupLng);

            Location startPoint=new Location("locationA");
            startPoint.setLatitude(lastLocation.getLatitude());
            startPoint.setLongitude(lastLocation.getLongitude());

            Location endPoint=new Location("locationA");
            endPoint.setLatitude(oldLocation.latitude);
            endPoint.setLongitude(oldLocation.longitude);

            double distance =startPoint.distanceTo(endPoint);

            int speed=60;

            double iKm =  (distance/1000);


            DecimalFormat df = new DecimalFormat("#.##");
            iKm = Double.valueOf(df.format(iKm));

            ////---distance --//
            distMeters = (int) (iKm *1000);
            if (distMeters > 1000){

                distMeters = (int) (iKm /1000);
//                if (distMeters >100){
//                    ToastBack("Location has changed");
//                }

            }else if (distMeters < 1000){
                distMeters = (int) (iKm *1000);
                if (distMeters > -0){
                    ToastBack("Change of location detected for "+Shopname+" shop. Would you like to update to this Location?");
                }

            }

            //--time--//
            int time = (int) (distance/speed);
            if (time <= 1){
//                textTime.setText(" less than a min");
            }else if (time >= 60){
//                textTime.setText(time+" hrs");
            }
            else if (time > 1){
//                textTime.setText(time+" mins");
            }



//            clientMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(getlat,getlng)).title("Customer drop point")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.locationhome)));


        }

    }
    //...end Distance.




    //----Deactivate shop-----
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
                    notify.put("type", "You have not remitted charges for 3 cash transactions. Remit Ksh/."+ UremittedCash +" to activate");
                    notify.put("Order_iD",UiD);
                    notify.put("to",UiD);
                    notify.put("from",UiD);
                    notify.put("timestamp", FieldValue.serverTimestamp());
                    vendorRef.document(mAuth.getCurrentUser().getUid()).collection("Notifications").add(notify);
                }else {

                    ToastBack(task.getException().getMessage());
                }

            }
        });

    }





    private void getRouteToMarker(LatLng pickupLocation) {
        if (pickupLatLng != null && lastLocation != null){
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), pickupLocation)
                    .build();
            routing.execute();
        }
    }


    private void createLationResquet() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FAST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(10);

    }

    private void setUplocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            },MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else {

            if(checkPlayService())
            {
                buildGoogleApiClient();
                createLationResquet();


                if (shop_switch.isChecked())
                    LocationUpdates();

            }
        }

    }



    private EditText InputFeedBack;
    private TextView CloseFeedback;
    private Button submitFeedBack;
    private SmileyRating smileRating;
    private int FeedBack_ratings =0;
    private String doc_i,feedSMS;
    private AlertDialog dialog_feedback;
    private void Dialog_FeedBack() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(MapsVActivity.this);
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



    private void Submit_feedBack() {

        String uid = mAuth.getCurrentUser().getUid();

        doc_i = FeedBack_Ref.document().getId();

        HashMap<String,Object> feed = new HashMap<>();
        feed.put("User_name",vendor_name);
        feed.put("Phone_number",Phone);
        feed.put("Rate",FeedBack_ratings);
        feed.put("Feedback",feedSMS);
        feed.put("Feedback_ID",doc_i);
        feed.put("timestamp",FieldValue.serverTimestamp());
        feed.put("User_ID",uid);


        FeedBack_Ref.document().set(feed).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(getApplicationContext(), vendor_name+" thanks for your feedback.", Toast.LENGTH_SHORT).show();

                    if (dialog_feedback != null)dialog_feedback.dismiss();


                }else {

                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });





    }


    private boolean checkPlayService() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (resultCode  != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICE_RES_REQUEST).show();

            else {
                Toast.makeText(this,"This Device is not Supported", Toast.LENGTH_SHORT).show();
                finish();

            }

            return false;
        }

        return true;

    }


    private void UpDateToken() {

        String user_id = mAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference token = db.collection("Tokens");

        Token token1 = new Token(FirebaseInstanceId.getInstance().getToken());
        token.document(user_id).set(token1);

    }


    ////----LOCATION UPDATE------
    private void LocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }

        if (locationRequest == null) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient1,locationRequest,this);


        } else {
            //your location code here...
        }

    }

    ///----REMOVE LOCATION-------
    private void RemoverVendorLocation() {

        String MUID = mAuth.getCurrentUser().getUid();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient1, this);
        GeoFirestore geoFirestore = new GeoFirestore(VendorAvailableRef);
        geoFirestore.removeLocation(MUID);

        VendorAroundRef.document(MUID).delete();

        VendorAvailableRef.document(MUID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (dialog_close != null)dialog_close.dismiss();
                dialog_close.dismiss();

                ToastBack("Shop Closed");
                closed_shop.setVisibility(View.VISIBLE);
                closed_shop.setText("Shop Closed");
                OPen_shop.setVisibility(View.GONE);
                location.setVisibility(View.GONE);

            }
        });

        MUID = "";
    }

    private Button stayBtn,closeBtn;
    private TextView body,close_shop;
    private void Dialog_Close_Shop(){
        AlertDialog.Builder mbuilder1 = new AlertDialog.Builder(MapsVActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_close_shop, null);
        mbuilder1.setView(mView);
        mbuilder1.setCancelable(false);
        dialog_close = mbuilder1.create();
        dialog_close.show();
        stayBtn = mView.findViewById(R.id.StayON_Btn);
        closeBtn = mView.findViewById(R.id.closeShopBtn);
        close_shop = mView.findViewById(R.id.CloseDialogShop);


        stayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastBack("Shop open in background.");
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finish();
            }
        });


        close_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ShopState == 0){
                    if (dialog_close != null)dialog_close.dismiss();
                    dialog_close.dismiss();
                shop_switch.toggle();
            }else if (ShopState == 1){


                }



            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String UID = mAuth.getCurrentUser().getUid();
                WriteBatch batch = db.batch();
                DocumentReference doc1 = vendorRef.document(UID);
                batch.update(doc1, "Shop_status", 0);
                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            VendorAssest.setVisibility(View.INVISIBLE);
                            vendorMarker.remove();
                            mMap.clear();
                            OPen_shop.setText("Shop Closed");
                            close_shop.setVisibility(View.VISIBLE);
                            OPen_shop.setVisibility(View.GONE);
                            location.setVisibility(View.GONE);
                            lable_switch.setText("Switch to open");
                            OPen_shop.setBackground(getResources().getDrawable(R.drawable.roundbtn_black));
                            shopInFoCard.setVisibility(View.INVISIBLE);
                            RemoverVendorLocation();


                        }else {


                            Toast.makeText(MapsVActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });




    }



    ///-----ON MAP READY-----------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
            }
        } catch (Resources.NotFoundException e) {
            Log.e("SwiftGas", "Can't find style. Error: ", e);
        }




        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        buildGoogleApiClient();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);







    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient1 = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient1.connect();
    }


    //RouteAPI
    @Override
    public void onRoutingFailure(RouteException e) {

        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePolylines(){
        for(Polyline line : polylines){
            line.remove();
        }
        polylines.clear();
    }




    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if (locationRequest == null){

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient1, locationRequest, this);
        }
//        Vendor_Location();
        LocationUpdates();
        Vendor_distance_Location();


    }

    private void ToastBack(String message){


        backToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        View view = backToast.getView();

        //Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(Color.parseColor("#242A37"), PorterDuff.Mode.SRC_IN);

        //Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#FF8900"));
        backToast.show();
    }


    //----Vendor Location----///
    private void Vendor_Location() {

        String usder_id = mAuth.getCurrentUser().getUid();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient1);


        if (lastLocation != null) {

            final double lat = lastLocation.getLatitude();
            final double lng = lastLocation.getLongitude();

            //update to firestore
            geoFireStoreAvailbale.setLocation(mAuth.getCurrentUser().getUid(), new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()),
                    new GeoFirestore.CompletionListener() {
                @Override
                public void onComplete(Exception e) {

                    String uid = mAuth.getCurrentUser().getUid();
                    HashMap<String,Object> updateLocation = new HashMap<>();
                    updateLocation.put("lat",lat);
                    updateLocation.put("lng",lng);

                    vendorRef.document(mAuth.getCurrentUser().getUid()).update(updateLocation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                showMarker(pickupLat,pickupLng);
                                ToastBack("Current Shop location was updated..");
                                relativeLayoutRefresh.setVisibility(View.GONE);
                                geoFireStorePreferAvailbale.setLocation(usder_id, new GeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()));

                            }else {

                            }

                        }
                    });


                }
            });

        }
    }

    ///.....end vendor Location




    //----Vendor distance Location----///
    private void Vendor_distance_Location() {

        String usder_id = mAuth.getCurrentUser().getUid();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient1);


        if (lastLocation != null) {

            LatLng oldLocation = new LatLng(pickupLat, pickupLng);

            Location startPoint=new Location("locationA");
            startPoint.setLatitude(lastLocation.getLatitude());
            startPoint.setLongitude(lastLocation.getLongitude());

            Location endPoint=new Location("locationA");
            endPoint.setLatitude(oldLocation.latitude);
            endPoint.setLongitude(oldLocation.longitude);

            double distance =startPoint.distanceTo(endPoint);

            int speed=60;

            double iKm =  (distance/1000);


            DecimalFormat df = new DecimalFormat("#.##");
            iKm = Double.valueOf(df.format(iKm));

            ////---distance --//
            distMeters = (int) (iKm *1000);
            if (distMeters > 1000){

                distMeters = (int) (iKm /1000);
//                if (distMeters >100){
//                    ToastBack("Location has changed");
//                }

            }else if (distMeters < 1000){
                distMeters = (int) (iKm *1000);
                if (distMeters > 100){
                    relativeLayoutRefresh.setVisibility(View.VISIBLE);
                    refreshText.setText("Change of location detected for "+Shopname+" shop. Would you like to update to this Location?");

                    new CountDownTimer(20000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                relativeLayoutRefresh.setVisibility(View.GONE);
                            }
                        }.start();


                }

            }

            //--time--//
            int time = (int) (distance/speed);
            if (time <= 1){
//                textTime.setText(" less than a min");
            }else if (time >= 60){
//                textTime.setText(time+" hrs");
            }
            else if (time > 1){
//                textTime.setText(time+" mins");
            }






        }
    }

    ///.....end vendor distance Location


    private void rotateMarker(Marker vendorMarker, float i, GoogleMap mMap) {

        Handler handler = new Handler();
        long  start = SystemClock.uptimeMillis();
        float startRotation = vendorMarker.getRotation();
        long duration = 1500;

        Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float)elapsed/duration);
                float rot = t*i+(1-t)*startRotation;
                vendorMarker.setRotation(-rot >180?rot/2:rot);

                if (t<1.0){

                    handler.postDelayed(this,16);
                }
            }
        });


    }



    //----On location change----

        @Override
        public void onConnectionSuspended ( int i){
        googleApiClient1.connect();
        }

        @Override
        public void onConnectionFailed (@NonNull ConnectionResult connectionResult){

        }

        @Override
        public void onLocationChanged (Location location){
        lastLocation = location;

        }
///.....end on location change..


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish();
            return;
        } else {


//            backToast = Toast.makeText(getBaseContext(), "Double tap to exit", Toast.LENGTH_SHORT);
//            backToast.show();

            ToastBack("Double tap to exit");

//            if(ShopState == 0){
//                if (dialog_close != null)dialog_close.dismiss();
//                dialog_close.dismiss();
//            }

        }

        backPressedTime = System.currentTimeMillis();
    }




    public void Check_Location_enable() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(MapsVActivity.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here

                        if (locationSettingsResponse.getLocationSettingsStates().isGpsPresent()){



                        }else {

                            Check_Location_enable();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MapsVActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS","Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.e("GPS","Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("GPS","checkLocationSettings -> onCanceled");
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e("GPS","User denied to access location");
                    openGpsEnableSetting();
                    break;
            }
        } else if (requestCode == REQUEST_ENABLE_GPS) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGpsEnabled) {
                openGpsEnableSetting();
            } else {
            }
        }
    }


    private void openGpsEnableSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_ENABLE_GPS);
    }


}
