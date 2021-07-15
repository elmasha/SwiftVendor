package com.gas.swiftel.Activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.gas.swiftel.Common;
import com.gas.swiftel.Model.Gas_Vendor;
import com.gas.swiftel.R;
import com.gas.swiftel.Model.Orders_request;
import com.gas.swiftel.Remote.IGoogleAPI;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
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
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;
import com.twilio.Twilio;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;


public class VendorMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, RoutingListener {

    private GoogleMap mMap;

    public static GoogleApiClient googleApiClient;
    public static Location lastLocation;
    public static LocationRequest locationRequest;
    private FirebaseAuth mAuth;

    private String ClientId = " ";

    private CoordinatorLayout coordinatorLayout;
    private Marker vendorMarker;
    private LatLng vendorLocation;
    private Button On_Delivered;
    private String userId;
    private long trips, earnings;

    Snackbar snackbar;
    private Button ConfirmRequest, CancelRequest;

    private String destination, deliverytKey;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 7000;

    public static final int PLAY_SERVICE_RES_REQUEST = 7001;

    private float v;
    private Handler handler;
    private int index, next;
    public static int UPDATE_INTERVAL = 1000;
    public static int FASTEST_INTERVAL = 1000;
    public static int DISPLACEMENT = 10;

    private double lat, lng;

    private LatLng destinationLatLng, pickupLoaction, currentPosition, startPosition, endpostion;

    private int radius;
    private String VendorFoundId;
    private static final int LIMIT = 3;
    private ImageView itemImage;

    List<LatLng> MarkerPoints = new ArrayList<LatLng>();
    private Polyline polyline = null;
    List<LatLng> Points = new ArrayList<LatLng>();
    List<Marker> mkrPoints = new ArrayList<Marker>();


    private FusedLocationProviderClient fusedLocationClient;
    private Marker clientMarker;

    private List<LatLng> polylineList;
    private Polyline secoPolyline, blackPolyline;
    private PolylineOptions polylineOptions, blackOptions;
    private static final int[] COLORS = new int[]{R.color.colorSecondary};

    private IGoogleAPI iService;

    private String customer_No, Price, PaymentMethod;
    private TextView showData, BackToOrders;
    private double getlat, getlng;
    private String Doc_id, doc_ID;
    private TextView userName, gasName, gasKg, textTime, textPrice, call_client, itemQuantity, itemDistance, orderTime;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    CollectionReference VendorAvailableRef = db.collection("VendorsAvailable");
    CollectionReference VendorWoringkRef = db.collection("VendorsWorking");
    CollectionReference orderRef = db.collection("Order_request");
    CollectionReference NotifyRef = db.collection("Notifications");

    SupportMapFragment mapFragment;
    private CircleImageView client_Image;
    private String Order_status;
    private Button startNavigation, ExchangeBtn;
    private FloatingActionButton fabGps, userNo;

    Runnable drawPathRunnable = new Runnable() {
        @Override
        public void run() {

            if (index < polylineList.size() - 1) {
                index++;

                next = index + 1;

            }
            if (index < polylineList.size() - 1) {

                startPosition = polylineList.get(index);
                endpostion = polylineList.get(next);

            }
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {

                    v = valueAnimator.getAnimatedFraction();
                    lng = v * endpostion.longitude + (1 - v) * startPosition.longitude;
                    lat = v * endpostion.latitude + (1 - v) * startPosition.latitude;

                    LatLng newPos = new LatLng(lat, lng);

                    vendorMarker.setPosition(newPos);
                    vendorMarker.setAnchor(0.5f, 0.5f);
                    vendorMarker.setRotation(getBearing(startPosition, newPos));
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder().target(newPos).zoom(15.5f).build()
                    ));


                }
            });

            valueAnimator.start();
            handler.postDelayed(this, 3000);

        }
    };

    private float getBearing(LatLng startPosition, LatLng newPosition) {
        double lat = Math.abs(startPosition.latitude - endpostion.latitude);
        double lng = Math.abs(startPosition.longitude - endpostion.longitude);

        if (startPosition.latitude < endpostion.latitude && startPosition.longitude < endpostion.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (startPosition.latitude >= endpostion.latitude && startPosition.longitude < endpostion.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (startPosition.latitude >= endpostion.latitude && startPosition.longitude >= endpostion.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (startPosition.latitude < endpostion.latitude && startPosition.longitude >= endpostion.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);

        return -1;

    }


    private String User_id, userNamE, User_image, gasImage, getGasPrice, getGasKg, getGasName, Category;
    private int Order_statuss;

    ////Oncreate----


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);


        userName = findViewById(R.id.Client_name);
        ExchangeBtn = findViewById(R.id.On_exchange);

        userNo = findViewById(R.id.Client_number);
        gasName = findViewById(R.id.Mitem_name);
        gasKg = findViewById(R.id.Mitem_kg);
        textPrice = findViewById(R.id.Mitem_price);
        textTime = findViewById(R.id.Mitem_time);
        ConfirmRequest = findViewById(R.id.Confirm_reqeust);
        CancelRequest = findViewById(R.id.Cancel_reqeust);
        itemImage = findViewById(R.id.OrdCard_Image);
        client_Image = findViewById(R.id.MClient_image);
        startNavigation = findViewById(R.id.btnNavigation);
        showData = findViewById(R.id.Show_data);
        fabGps = findViewById(R.id.ReFresh2);
        BackToOrders = findViewById(R.id.back_request);
        itemQuantity = findViewById(R.id.Mitem_qty);
        itemDistance = findViewById(R.id.Mitem_distance);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);



        BackToOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderIntent = new Intent(getApplicationContext(), Home_Activity.class);
                orderIntent.putExtra("Order_menu", 1);
                startActivity(orderIntent);
            }
        });


        fabGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationUpdates();
                DisplayVendorLocation();
            }
        });


        userNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customer_No != null) {

                    Toast.makeText(VendorMapActivity.this, "Dialing...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + customer_No));
                    startActivity(intent);

                } else {

                    Toast.makeText(VendorMapActivity.this, "Phone number is null.", Toast.LENGTH_SHORT).show();


                }
            }
        });


        if (getIntent() != null) {

            doc_ID = getIntent().getStringExtra("doc_ID");
            Doc_id = getIntent().getStringExtra("order_id");
            getlat = getIntent().getDoubleExtra("lat", -1.0);
            getlng = getIntent().getDoubleExtra("lng", -1.0);

            LoadDataInt();
        }


        iService = Common.getGoogleAPI();
        polylineList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();


        polylineList = new ArrayList<>();

        On_Delivered = findViewById(R.id.On_delivered);
        coordinatorLayout = findViewById(R.id.cont);


        LoadData();


        startNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransitOrder();

            }
        });


        ConfirmRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random dice = new Random();
                int shop_No = dice.nextInt(100000) + 1;
                deliverytKey = String.valueOf(shop_No);

                // Toast.makeText(VendorMapActivity.this, deliverytKey, Toast.LENGTH_SHORT).show();

                ConfirmOrder();

                //SendSMS(deliverytKey);

//                getDiretions();
//                distanceAndTime();


            }
        });

        CancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CancleRequest();

            }
        });

        On_Delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Order_Delivered();

            }
        });


        ExchangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exchange_Cylinder();
            }
        });

        Load_Vendor_Details();


        checkLocationPermission();
        setUplocation();


    }


    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    class DataParser {

        List<List<HashMap<String, String>>> parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<>();
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;

            try {

                jRoutes = jObject.getJSONArray("routes");

                /** Traversing all routes */
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<>();

                    /** Traversing all legs */
                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        /** Traversing all steps */
                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            /** Traversing all points */
                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<>();
                                hm.put("lat", Double.toString((list.get(l)).latitude));
                                hm.put("lng", Double.toString((list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }


            return routes;
        }


        /**
         * Method to decode polyline points
         * */
        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }
    }


    private long Cash_trips;

    private void Load_Vendor_Details() {

        String userid = mAuth.getCurrentUser().getUid();

        vendorRef.whereEqualTo("User_ID", userid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    Gas_Vendor vendorUser = doc.toObject(Gas_Vendor.class);

                    String First_name = vendorUser.getFirst_name();
                    String Last_name = vendorUser.getLast_name();
                    String Email = vendorUser.getEmail();
                    String Shopname = vendorUser.getShopName();
                    String shopNO = vendorUser.getShop_No();
                    String Phone = vendorUser.getMobile();
                    String User_image = vendorUser.getUser_Image();
                    Cash_trips = vendorUser.getCash_Trips();

                    trips = vendorUser.getTrips();
                    earnings = vendorUser.getEarnings();

//                    if (User_image != null){
//
//                        Picasso.with(getContext()).load(User_image).placeholder(R.drawable.load).error(R.drawable.profile).into(vendor_image);
//
//                    }

                }


            }
        });


    }


    private long TripAmount, InCash, All_Trips;

    private void Add_Trips() {

        if (PaymentMethod.equals("Cash on delivery")) {

            if (getGasKg.equals("3kg")) {

                TripAmount = 15;
                InCash = 1;
                All_Trips = 1;
                Rates(TripAmount, InCash, All_Trips);

            } else if (getGasKg.equals("6kg")) {
                TripAmount = 15;
                InCash = 1;
                All_Trips = 1;
                Rates(TripAmount, InCash, All_Trips);

            } else if (getGasKg.equals("13kg")) {
                TripAmount = 25;
                InCash = 1;
                All_Trips = 1;
                Rates(TripAmount, InCash, All_Trips);

            } else if (getGasKg.equals("15kg")) {
                TripAmount = 25;
                InCash = 1;
                All_Trips = 1;
                Rates(TripAmount, InCash, All_Trips);

            } else if (getGasKg.equals("50kg")) {
                TripAmount = 55;
                InCash = 1;
                All_Trips = 1;
                Rates(TripAmount, InCash, All_Trips);

            } else if (Category.equals("Accessory")) {
                TripAmount = 30;
                InCash = 1;
                All_Trips = 1;
                Rates(TripAmount, InCash, All_Trips);
            }

        } else if (PaymentMethod.equals("Commitment paid")) {

            if (getGasKg.equals("3kg")) {

                TripAmount = 0;
                All_Trips = 1;
                InCash = 0;
                Rates(TripAmount, InCash, All_Trips);

            } else if (getGasKg.equals("6kg")) {
                TripAmount = 0;
                All_Trips = 1;
                InCash = 0;
                Rates(TripAmount, InCash, All_Trips);

            } else if (getGasKg.equals("13kg")) {
                TripAmount = 0;
                All_Trips = 1;
                InCash = 0;
                Rates(TripAmount, InCash, All_Trips);

            } else if (getGasKg.equals("15kg")) {
                TripAmount = 0;
                All_Trips = 1;
                InCash = 0;
                Rates(TripAmount, InCash, All_Trips);

            } else if (getGasKg.equals("50kg")) {
                TripAmount = 0;
                All_Trips = 1;
                InCash = 0;
                Rates(TripAmount, InCash, All_Trips);

            } else if (Category.equals("Accessory")) {
                TripAmount = 0;
                All_Trips = 1;
                InCash = 0;
                Rates(TripAmount, InCash, All_Trips);


            }


        }


    }

    private void Rates(long tripAmount, long inCash, long all_Trips) {

        long earning = tripAmount + earnings;
        long Trps = trips + all_Trips;
        long CshTrips = Cash_trips + inCash;

        String mUID = mAuth.getCurrentUser().getUid();

        WriteBatch batch = db.batch();
        DocumentReference doc1 = vendorRef.document(mUID);
        batch.update(doc1, "Trips", Trps);
        batch.update(doc1, "Earnings", earning);
        batch.update(doc1, "Cash_Trips", CshTrips);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(VendorMapActivity.this, "Trip finished..", Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(VendorMapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    private String notifyID;

    private void CancleRequest() {

        String UID = mAuth.getCurrentUser().getUid();

        if (Doc_id != null) {


            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(Doc_id);
            batch.update(doc1, "Order_status", 12);
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(VendorMapActivity.this, "Request Rejected", Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });


        } else if (doc_ID != null) {

            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(doc_ID);
            batch.update(doc1, "Order_status", 12);
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        String UiD = mAuth.getCurrentUser().getUid();

                        Toast.makeText(VendorMapActivity.this, "Request Rejected", Toast.LENGTH_SHORT).show();


                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }


    }


    private void TransitOrder() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait request in transit");
        progressDialog.show();


        if (Doc_id != null) {

            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(Doc_id);
            batch.update(doc1, "Order_status", 2);
            batch.update(doc1, "timestamp", FieldValue.serverTimestamp());
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(VendorMapActivity.this, "Order in transit", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startNavigation.setVisibility(INVISIBLE);
                        On_Delivered.setVisibility(View.VISIBLE);
                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startNavigation.setVisibility(View.VISIBLE);


                    }


                }
            });

        } else if (doc_ID != null) {

            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(doc_ID);
            batch.update(doc1, "Order_status", 2);
            batch.update(doc1, "timestamp", FieldValue.serverTimestamp());
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(VendorMapActivity.this, "Order in transit", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startNavigation.setVisibility(INVISIBLE);
                        On_Delivered.setVisibility(View.VISIBLE);
                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startNavigation.setVisibility(View.VISIBLE);

                    }


                }
            });

        }


    }


    private void Order_Delivered() {

        if (Doc_id != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();


            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(Doc_id);
            batch.update(doc1, "Order_status", 3);
            batch.update(doc1, "timestamp", FieldValue.serverTimestamp());
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {


                        Toast.makeText(VendorMapActivity.this, "Order delivered ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        Add_Trips();


                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                    }

                }
            });


        } else if (doc_ID != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();

            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(doc_ID);
            batch.update(doc1, "Order_status", 3);
            batch.update(doc1, "timestamp", FieldValue.serverTimestamp());
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(VendorMapActivity.this, "Order delivered gas", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        Add_Trips();

                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                    }

                }
            });


        }


    }


    private void Exchange_Cylinder() {

        if (Doc_id != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();


            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(Doc_id);
            batch.update(doc1, "exCylinder", "Yes");
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {


                        Toast.makeText(VendorMapActivity.this, "Exchange was successful", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                    }

                }
            });


        } else if (doc_ID != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();

            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(doc_ID);
            batch.update(doc1, "exCylinder", "Yes");
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(VendorMapActivity.this, "Exchange was successful", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }

                }
            });


        }


    }


    private void SendSMS(String deliveryKey) {

        Twilio.init(getResources().getString(R.string.ACCOUNT_SID), getResources().getString(R.string.ACCOUNT_TOKEN));

        String msg = "Your delivery key is" + deliveryKey + "";
        Message message = Message.creator(new PhoneNumber("+254746291229"), // to
                new PhoneNumber("+13212339373"), // from
                msg)
                .create();

    }

    private void ConfirmOrder() {


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait request in transit");
        progressDialog.show();


        if (Doc_id != null) {

            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(Doc_id);
            batch.update(doc1, "Order_status", 1);
            batch.update(doc1, "timestamp", FieldValue.serverTimestamp());
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if (task.isSuccessful()) {


                        Toast.makeText(VendorMapActivity.this, "Request Confirmed", Toast.LENGTH_SHORT).show();
                        ConfirmRequest.setVisibility(INVISIBLE);
                        startNavigation.setVisibility(View.VISIBLE);
                        CancelRequest.setVisibility(INVISIBLE);
                        progressDialog.dismiss();
                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }

                }
            });

        } else if (doc_ID != null) {

            WriteBatch batch = db.batch();
            DocumentReference doc1 = orderRef.document(doc_ID);
            batch.update(doc1, "Order_status", 1);
            batch.update(doc1, "timestamp", FieldValue.serverTimestamp());
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if (task.isSuccessful()) {

                        Toast.makeText(VendorMapActivity.this, "Request Confirmed", Toast.LENGTH_SHORT).show();
                        ConfirmRequest.setVisibility(INVISIBLE);
                        startNavigation.setVisibility(View.VISIBLE);
                        CancelRequest.setVisibility(INVISIBLE);
                        progressDialog.dismiss();
                    } else {

                        Toast.makeText(VendorMapActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }

                }
            });
        }


    }


    ///---LOAD DETAILS---
    private String exCylinder;

    private void LoadDataInt() {


        if (Doc_id != null) {


            orderRef.whereEqualTo("doc_id", Doc_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot
                                            queryDocumentSnapshots, @javax.annotation.Nullable
                                            FirebaseFirestoreException e) {

                    if (e != null) {
                        return;
                    }

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        Orders_request ordersRequest = doc.toObject(Orders_request.class);

                        User_id = ordersRequest.getUser_id();


                        getGasName = ordersRequest.getName();
                        getGasKg = ordersRequest.getItem_desc();
                        getGasPrice = ordersRequest.getPrice();
                        User_image = ordersRequest.getUser_image();
                        gasImage = ordersRequest.getItem_image();
                        customer_No = ordersRequest.getCustomer_No();
                        userNamE = ordersRequest.getCustomer_name();
                        Order_statuss = ordersRequest.getOrder_status();
                        Price = ordersRequest.getPrice();
                        PaymentMethod = ordersRequest.getPayment_method();
                        Category = ordersRequest.getCategory();
                        exCylinder = ordersRequest.getExCylinder();

                        textPrice.setText(Price);
                        gasName.setText(getGasName);
                        gasKg.setText(getGasKg);
                        userName.setText(userNamE);

                        pickupLoaction = new LatLng(ordersRequest.getLat(), ordersRequest.getLng());


                        if (clientMarker != null)
                            clientMarker.remove();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickupLoaction.latitude, pickupLoaction.longitude), 15.0f));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 200, null);
                        clientMarker = mMap.addMarker(new MarkerOptions()
                                .title("drop point")
                                .position(new LatLng(pickupLoaction.latitude, pickupLoaction.longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.locationhome)));


                        int sts = ordersRequest.getOrder_status();


                        if (sts == 0) {

                            ConfirmRequest.setVisibility(View.VISIBLE);
                            startNavigation.setVisibility(INVISIBLE);
                            CancelRequest.setVisibility(View.VISIBLE);

                        } else if (sts == 1) {

                            ConfirmRequest.setVisibility(INVISIBLE);
                            startNavigation.setVisibility(View.VISIBLE);
                            CancelRequest.setVisibility(INVISIBLE);
                            ExchangeBtn.setVisibility(INVISIBLE);

                        } else if (sts == 2) {

                            ConfirmRequest.setVisibility(INVISIBLE);
                            CancelRequest.setVisibility(INVISIBLE);
                            On_Delivered.setVisibility(View.VISIBLE);
                            ExchangeBtn.setVisibility(INVISIBLE);
                        } else if (sts == 3) {

                            ConfirmRequest.setVisibility(INVISIBLE);
                            startNavigation.setVisibility(INVISIBLE);
                            ConfirmRequest.setVisibility(INVISIBLE);
                            CancelRequest.setVisibility(INVISIBLE);
                            On_Delivered.setVisibility(View.VISIBLE);
                            showData.setVisibility(INVISIBLE);

                            if (exCylinder.equals("Yes")) {
                                ConfirmRequest.setVisibility(INVISIBLE);
                                startNavigation.setVisibility(INVISIBLE);
                                ConfirmRequest.setVisibility(INVISIBLE);
                                CancelRequest.setVisibility(INVISIBLE);
                                On_Delivered.setVisibility(INVISIBLE);
                                showData.setVisibility(View.VISIBLE);
                                ExchangeBtn.setVisibility(View.GONE);
                                showData.setText("Gas cylinder exchanged & order has been delivered..");
                            } else if (exCylinder.equals("No")) {
                                ConfirmRequest.setVisibility(INVISIBLE);
                                startNavigation.setVisibility(INVISIBLE);
                                ConfirmRequest.setVisibility(INVISIBLE);
                                CancelRequest.setVisibility(INVISIBLE);
                                On_Delivered.setVisibility(INVISIBLE);
                                showData.setVisibility(INVISIBLE);
                                ExchangeBtn.setVisibility(View.VISIBLE);
                            }

                        } else if (sts == 12) {

                            ConfirmRequest.setVisibility(INVISIBLE);
                            startNavigation.setVisibility(INVISIBLE);
                            ConfirmRequest.setVisibility(INVISIBLE);
                            CancelRequest.setVisibility(INVISIBLE);
                            On_Delivered.setVisibility(INVISIBLE);
                            showData.setVisibility(View.VISIBLE);
                            ExchangeBtn.setVisibility(INVISIBLE);
                            showData.setText("Order has been rejected..");

                        }


                        if (User_image != null | gasImage != null) {

                            Picasso.with(VendorMapActivity.this).load(gasImage).placeholder(R.drawable.load).error(R.drawable.errorimage).into(itemImage);
                            Picasso.with(VendorMapActivity.this).load(User_image).placeholder(R.drawable.load).error(R.drawable.profile).into(client_Image);

                        }

                    }
                }
            });


        }


    }


    private String CustomerID, Qty;
    private double lat1, lang1;

    private void LoadData() {


        if (doc_ID != null) {


            orderRef.whereEqualTo("doc_id", doc_ID).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot
                                            queryDocumentSnapshots, @javax.annotation.Nullable
                                            FirebaseFirestoreException e) {

                    if (e != null) {
                        return;
                    }

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        Orders_request ordersRequest = doc.toObject(Orders_request.class);

                        User_id = ordersRequest.getUser_id();


                        getGasName = ordersRequest.getName();
                        getGasKg = ordersRequest.getItem_desc();
                        getGasPrice = ordersRequest.getPrice();
                        User_image = ordersRequest.getUser_image();
                        gasImage = ordersRequest.getItem_image();
                        customer_No = ordersRequest.getCustomer_No();
                        userNamE = ordersRequest.getCustomer_name();
                        Order_statuss = ordersRequest.getOrder_status();
                        Price = ordersRequest.getPrice();
                        PaymentMethod = ordersRequest.getPayment_method();
                        Category = ordersRequest.getCategory();
                        Qty = ordersRequest.getQuantity();
                        exCylinder = ordersRequest.getExCylinder();
                        lat1 = ordersRequest.getLat();
                        lang1 = ordersRequest.getLat();


                        if (PaymentMethod.equals("Cash on delivery")) {

                            if (getGasKg.equals("3kg")) {
                                textPrice.setText(Price + "(+remit ksh.15)");
                            } else if (getGasKg.equals("6kg")) {
                                textPrice.setText(Price + "(+remit ksh.15)");
                            } else if (getGasKg.equals("13kg")) {
                                textPrice.setText(Price + "(+remit ksh.15)");
                            } else if (getGasKg.equals("15kg")) {
                                textPrice.setText(Price + "(+remit ksh.25)");
                            } else if (getGasKg.equals("22kg")) {
                                textPrice.setText(Price + "(+remit ksh.25)");
                            } else if (getGasKg.equals("55kg")) {
                                textPrice.setText(Price + "(+remit ksh.55)");
                            } else if (Category.equals("Accessory")) {
                                textPrice.setText(Price + "(+remit ksh.30)");
                            }

                        } else if (PaymentMethod.equals("Commitment paid")) {
                            textPrice.setText("Ksh/=" + Price);
                        }

                        itemQuantity.setText("Qty:" + Qty);
                        gasName.setText(getGasName);
                        gasKg.setText(getGasKg);
                        userName.setText(userNamE);


                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ordersRequest.getLat(), ordersRequest.getLng()), 16.0f));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 200, null);
                        clientMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(ordersRequest.getLat(), ordersRequest.getLng())).title(userNamE)
                                .snippet("drop point")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.locationhome)));


                        int sts = ordersRequest.getOrder_status();


                        if (sts == 0) {

                            ConfirmRequest.setVisibility(View.VISIBLE);
                            startNavigation.setVisibility(INVISIBLE);
                            CancelRequest.setVisibility(View.VISIBLE);

                        } else if (sts == 1) {

                            ConfirmRequest.setVisibility(INVISIBLE);
                            startNavigation.setVisibility(View.VISIBLE);
                            CancelRequest.setVisibility(INVISIBLE);

                        } else if (sts == 2) {

                            ConfirmRequest.setVisibility(INVISIBLE);
                            CancelRequest.setVisibility(INVISIBLE);
                            On_Delivered.setVisibility(View.VISIBLE);
                        } else if (sts == 3) {

                            ConfirmRequest.setVisibility(INVISIBLE);
                            startNavigation.setVisibility(INVISIBLE);
                            ConfirmRequest.setVisibility(INVISIBLE);
                            CancelRequest.setVisibility(INVISIBLE);
                            On_Delivered.setVisibility(View.VISIBLE);
                            showData.setVisibility(INVISIBLE);

                            if (exCylinder != null) {
                                if (exCylinder.equals("Yes")) {
                                    ConfirmRequest.setVisibility(INVISIBLE);
                                    startNavigation.setVisibility(INVISIBLE);
                                    ConfirmRequest.setVisibility(INVISIBLE);
                                    CancelRequest.setVisibility(INVISIBLE);
                                    On_Delivered.setVisibility(INVISIBLE);
                                    showData.setVisibility(View.VISIBLE);
                                    ExchangeBtn.setVisibility(View.GONE);
                                    showData.setText("Gas cylinder exchanged & order has been delivered..");
                                } else if (exCylinder.equals("No")) {
                                    ConfirmRequest.setVisibility(INVISIBLE);
                                    startNavigation.setVisibility(INVISIBLE);
                                    ConfirmRequest.setVisibility(INVISIBLE);
                                    CancelRequest.setVisibility(INVISIBLE);
                                    On_Delivered.setVisibility(INVISIBLE);
                                    showData.setVisibility(INVISIBLE);
                                    ExchangeBtn.setVisibility(View.VISIBLE);
                                }
                            }


                        } else if (sts == 12) {

                            ConfirmRequest.setVisibility(INVISIBLE);
                            startNavigation.setVisibility(INVISIBLE);
                            ConfirmRequest.setVisibility(INVISIBLE);
                            CancelRequest.setVisibility(INVISIBLE);
                            On_Delivered.setVisibility(INVISIBLE);
                            showData.setVisibility(View.VISIBLE);
                            showData.setText("Order has been rejected..");

                        }


                        if (User_image != null | gasImage != null) {

                            Picasso.with(VendorMapActivity.this).load(gasImage).placeholder(R.drawable.load).error(R.drawable.errorimage).into(itemImage);
                            Picasso.with(VendorMapActivity.this).load(User_image).placeholder(R.drawable.load).error(R.drawable.profile).into(client_Image);

                        }

                    }
                }
            });


        }


    }


    private int distMeters;

    private void distanceAndTime() {


        if (lastLocation != null) {


            Location startPoint = new Location("locationA");
            startPoint.setLatitude(lastLocation.getLatitude());
            startPoint.setLongitude(lastLocation.getLongitude());

            Location endPoint = new Location("locationA");
            endPoint.setLatitude(getlat);
            endPoint.setLongitude(getlng);

            double distance = startPoint.distanceTo(endPoint);

            int speed = 60;

            double iKm = (distance / 1000);


            DecimalFormat df = new DecimalFormat("#.###");
            iKm = Double.valueOf(df.format(iKm));


            ////---distance --//
            distMeters = (int) (iKm * 1000);
            if (distMeters >= 1000) {

                distMeters = (int) (iKm / 1000);
                itemDistance.setText(distMeters + " km away");

            } else if (distMeters < 1000) {
                distMeters = (int) (iKm * 1000);
                itemDistance.setText(distMeters + " meters away");

            }


            ////---distance --//


            //--time--//
            int time = (int) (distance / speed);
            if (time <= 1) {
                textTime.setText(" less than a min");
            } else if (time >= 60) {
            } else if (time > 1) {
            }


        }


    }

    private void getDiretions() {


        if (lastLocation != null) {

            String dresti = String.valueOf(getlat + getlng);

            currentPosition = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

            LatLng drop = new LatLng(getlat, getlng);

            String requestAPI = null;
            try {

                requestAPI = "https://maps.googleapis.com/maps/api/directions/json?" +
                        "mode=driving" +
                        "transit_routing_preference=less_driving&" +
                        "origin=" + currentPosition.latitude + "," + currentPosition.longitude +
                        "&destination=" + drop.latitude + "," + drop.longitude +
                        "&key=" + getResources().getString(R.string.Api_key);

                Log.d("ELTECH", requestAPI);

                Toast.makeText(this, requestAPI, Toast.LENGTH_SHORT).show();

                iService.getPath(requestAPI)
                        .enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                                try {

                                    JSONObject jsonObject = new JSONObject(response.body().toString());

                                    Log.d("ELTECH", String.valueOf(jsonObject));
                                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject route = jsonArray.getJSONObject(i);
                                        JSONObject poly = route.getJSONObject("overview_polyline");
                                        String polyline = poly.getString("points");
                                        polylineList = decodePoly(polyline);
                                    }
                                    //Bounds

                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    for (LatLng latLng : polylineList)
                                        builder.include(pickupLoaction);
                                    LatLngBounds bounds = builder.build();
                                    CameraUpdate cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                                    mMap.animateCamera(cameraUpdateFactory);

                                    polylineOptions = new PolylineOptions();
                                    polylineOptions.color(R.color.colorSecondary);
                                    polylineOptions.width(6);
                                    polylineOptions.startCap(new SquareCap());
                                    polylineOptions.endCap(new SquareCap());
                                    polylineOptions.jointType(JointType.ROUND);
                                    secoPolyline = mMap.addPolyline(polylineOptions);


                                    blackOptions = new PolylineOptions();
                                    blackOptions.color(R.color.colorBlack);
                                    blackOptions.width(6);
                                    blackOptions.startCap(new SquareCap());
                                    blackOptions.endCap(new SquareCap());
                                    blackOptions.jointType(JointType.ROUND);
                                    blackPolyline = mMap.addPolyline(blackOptions);


                                    mMap.addMarker(new MarkerOptions().position(polylineList.get(polylineList.size() - 1))
                                            .title("Drop location"));

                                    ValueAnimator polyLineAnimator = ValueAnimator.ofInt(0, 100);
                                    polyLineAnimator.setDuration(2000);
                                    polyLineAnimator.setInterpolator(new LinearInterpolator());
                                    polyLineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                        @Override
                                        public void onAnimationUpdate(ValueAnimator valueAnimator) {

                                            List<LatLng> points = secoPolyline.getPoints();
                                            int percentValue = (int) ValueAnimator.getFrameDelay();
                                            int size = points.size();
                                            int newpoints = (int) (size * (percentValue / 100.0f));
                                            List<LatLng> p = points.subList(0, newpoints);
                                            blackPolyline.setPoints(p);

                                        }
                                    });

                                    polyLineAnimator.start();

                                    vendorMarker = mMap.addMarker(new MarkerOptions().position(currentPosition)
                                            .flat(true)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.deliveries)));

                                    handler = new Handler();
                                    index = 1;
                                    next = 1;
                                    handler.postDelayed(drawPathRunnable, 3000);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

            } catch (Exception e) {

            }


        }

    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    private void getRouteToMarker(LatLng pickupLocation) {

        if (pickupLocation != null && lastLocation != null) {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), pickupLocation)
                    .build();
            routing.execute();


        }

    }


    protected synchronized void buildGoogleApiCLient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = googleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            this, R.raw.mapstyle));
//
//            if (!success) {
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e("ElTech", "Can't find style. Error: ", e);
//        }


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);

        buildGoogleApiCLient();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        mMap.setMyLocationEnabled(true);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
//            LatLng distlatLng = new LatLng(locationResult.getLastLocation().getLongitude(),locationResult.getLastLocation().getLongitude());
//            if (mMap != null){
//                if (vendorMarker != null)vendorMarker.remove();
//                {
//                    MarkerOptions markerOptions =  new MarkerOptions();
//                    markerOptions.position(distlatLng);
//                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.deliveries));
//                    markerOptions.anchor((float)0.5 ,(float)0.5);
//                    markerOptions.rotation(locationResult.getLastLocation().getBearing());
//
//                    vendorMarker = mMap.addMarker(markerOptions);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(distlatLng,16));
//
//                }

                // setShopMarker(locationResult.getLastLocation());
            //}

        }
    };



    private void getLastLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){

                    Toast.makeText(VendorMapActivity.this,
                            location.getLatitude()+"\n "+location.getLongitude()+"",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VendorMapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("TAG", "onFailure: "+e.getLocalizedMessage());
            }
        });

    }

    private void CheckSettingsAndUpdateLocation(){
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startlocationUpdates();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                if (e instanceof ResolvableApiException){
//                    ResolvableApiException apiException = (ResolvableApiException)e;
//                    try {
//                        apiException.startResolutionForResult(WalkthroughActivity.class,3333);
//                    }catch (IntentSender.SendIntentException exception){
//                        exception.printStackTrace();
//                    }

             //   }
                Toast.makeText(VendorMapActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setShopMarker(Location location){
        LatLng distlatLng = new LatLng(location.getLongitude(),location.getLatitude());

        if (vendorMarker == null)
        {
            MarkerOptions markerOptions =  new MarkerOptions();
            markerOptions.position(distlatLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.deliveries));
//            markerOptions.anchor((float)0.5 ,(float)0.5);
            markerOptions.rotation(location.getBearing());

            vendorMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(distlatLng,16));

        }else {
//            vendorMarker.setPosition(latLng);
//            vendorMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.deliveries));
//            vendorMarker.setRotation(location.getBearing());
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));


        }

    }


    private void startlocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }


    private void stoplocationUpdate() {

        fusedLocationClient.removeLocationUpdates(locationCallback);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (locationRequest == null) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            //your location code here...
        }


        DisplayVendorLocation();
        distanceAndTime();


    }


    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);



    }


    private void getRouteTomarker(LatLng clientLat) {

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()),clientLat)
                .build();
        routing.execute();

    }



    @Override


    protected void onStart() {
        super.onStart();
        CheckSettingsAndUpdateLocation();
        startlocationUpdates();
        LoadData();
        buildGoogleApiCLient();
        checkLocationPermission();
        googleApiClient.connect();

        // Call GoogleApiClient connection when starting the Activity

    }



    @Override
    protected void onResume() {
        super.onResume();

        LoadData();
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }


    }




    @Override
    protected void onRestart() {
        super.onRestart();
        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
        stoplocationUpdate();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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


            }

        }

    }

    private void createLationResquet() {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);

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

    private void RemovVendorLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);

    }


    private void LocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }

        if (locationRequest == null) {

            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);


        } else {
            //your location code here...
        }

    }




    private void DisplayVendorLocation() {
         userId =mAuth.getCurrentUser().getUid();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        GeoFirestore geoFirestoreAvailable = new GeoFirestore(VendorAvailableRef);

        String usder_id = mAuth.getCurrentUser().getUid();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);


        if (lastLocation != null) {

            final double lat = lastLocation.getLatitude();
            final double lng = lastLocation.getLongitude();


            LatLng distlatLng = new LatLng(lastLocation.getLongitude(),lastLocation.getLongitude());
            if (mMap != null){
                if (vendorMarker != null)vendorMarker.remove();
                {
                    MarkerOptions markerOptions =  new MarkerOptions();
                    markerOptions.position(distlatLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.deliveries));
                    markerOptions.anchor((float)0.5 ,(float)0.5);
                    markerOptions.rotation(lastLocation.getBearing());

                    vendorMarker = mMap.addMarker(markerOptions);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(distlatLng,16));

                }

//             setShopMarker(locationResult.getLastLocation());
            }




            //update to firestore
            geoFirestoreAvailable.setLocation(usder_id, new GeoPoint(lat, lng), new GeoFirestore.CompletionListener() {
                @Override
                public void onComplete(Exception e) {

//                    if (vendorMarker != null)
//
//                        vendorMarker.remove();
//                        vendorMarker = mMap.addMarker(new MarkerOptions()
//                            .title("My Shop")
////                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.deliveries))
//                            .position(new LatLng(lat, lng)));


                }
            });

        }



    }

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


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("User Permission")
                        .setMessage("Allow user to access Location")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(VendorMapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:

                        if (checkPlayService()){



                        }


                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


    private void getAssingedLient() {

        String VendorID =mAuth.getCurrentUser().getUid();
        CollectionReference assignedClient = db.collection("VendorFound");
        assignedClient.document(VendorID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()){

                    Map<String, Object> map = (Map<String, Object>) documentSnapshot.getData();
                    if(map.get("ClientID") !=null){
                        ClientId =map.get("ClientID").toString();

                        getAssingedLientPickUplocation();

                    }else {
                        onRouteCancel();
                        ClientId = "";
                        if (clientMarker != null){

                            clientMarker.remove();
                        }

                    }

                }


            }
        });


    }



    private void getAssingedLientPickUplocation(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ClientOrderRefPickLocation = db.collection("Orders_request Request");
        ClientOrderRefPickLocation.document(ClientId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {


                if(documentSnapshot.exists()){


                    double locationlat =0;
                    double locationlog = 0;

                    ArrayList<Double> arrayList = (ArrayList<Double>) documentSnapshot.get("l");

                    if (arrayList.get(0) != null){

                        locationlat = Double.parseDouble(arrayList.get(0).toString());

                    }
                    if (arrayList.get(1) != null ){
                        locationlog = Double.parseDouble(arrayList.get(1).toString());

                    }


                    LatLng clientLat = new LatLng(locationlat,locationlog);

                    if(clientMarker != null)
                    {
                        clientMarker.remove();


                        clientMarker = mMap.addMarker(new MarkerOptions().position(clientLat).title("Customer drop point")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)));
                    }

//                   getRouteTomarker(clientLat);

                }
            }
        });


    }



    @Override
    public void onRoutingFailure(RouteException e) {

        if (e != null){

            Toast.makeText(this, "Error !"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Something went Wrong ,try again",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {


//        if(polylineList.size()>0) {
//            for (Polyline poly : polylineList) {
//                poly.remove();
//            }
//        }
//
//        polylineList = new ArrayList<>();
//        //add route(s) to the map.
//        for (int i = 0; i <route.size(); i++) {
//
//            //In case of more than 5 alternative routes
//            int colorIndex = i % COLORS.length;
//
//            PolylineOptions polyOptions = new PolylineOptions();
//            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
//            polyOptions.width(10 + i * 3);
//            polyOptions.addAll(route.get(i).getPoints());
//            Polyline polyline = mMap.addPolyline(polyOptions);
//            polylineList.add(polyline);
//
//            Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+
//                    ": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
//
//        }


    }

    @Override
    public void onRoutingCancelled() {

    }

    private void onRouteCancel (){

//        for (Polyline lines : polylineList){
//            lines.remove();
//        }
//        polylineList.clear();
    }


    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }
}