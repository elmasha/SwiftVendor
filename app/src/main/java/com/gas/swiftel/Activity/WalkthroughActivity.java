package com.gas.swiftel.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gas.swiftel.Adapter.SlideAdpter;
import com.gas.swiftel.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class WalkthroughActivity extends AppCompatActivity {
    private ViewPager mSliderViewPager;
    private LinearLayout mDotsLayout;

    private TextView[] mDots;
    TextView privacy;
    private SlideAdpter slideAdpter;
    private Button NextBtn;
    private  Button BackBtn,Finishbtn;
    private Button get_started;
    private  int mCurrentPage;
    private RelativeLayout relativeLayout;

    private CheckBox checkBox;

    private FirebaseAuth mAuth;
    private String current_user;

    public static SettingsClient mSettingsClient;
    public static LocationSettingsRequest mLocationSettingsRequest;
    public static final int REQUEST_CHECK_SETTINGS = 214;
    public static final int REQUEST_ENABLE_GPS = 516;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 7000;
    public static final int PLAY_SERVICE_RES_REQUEST = 7001;
    int PERMISSION_ALL = 10000;
    String[] PERMISSIONS = {Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        mSliderViewPager = (ViewPager) findViewById(R.id.Slide_viewPager);
        mDotsLayout = (LinearLayout) findViewById(R.id.Dot_layout);
        NextBtn  = (Button) findViewById(R.id.Next);
        BackBtn = (Button) findViewById(R.id.Back);
        checkBox = findViewById(R.id.checkbox12);
        relativeLayout = findViewById(R.id.LayoutWalk);

        mAuth = FirebaseAuth.getInstance();
        privacy = findViewById(R.id.privacyPolicy);

        get_started = (Button) findViewById(R.id.get_Started);
        slideAdpter = new SlideAdpter(this);

        addDotsIndicator(0);
        mSliderViewPager.setAdapter(slideAdpter);
        mSliderViewPager.addOnPageChangeListener(viewListener);




        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),PrivacyActivity.class));
            }
        });


        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCurrentPage == 2) {
                    mSliderViewPager.setCurrentItem(-1);
                }else if (mCurrentPage == 1){
                    mSliderViewPager.setCurrentItem(-1);
                }
            }
        });


        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCurrentPage ==0){
                    mSliderViewPager.setCurrentItem(+1);
                }else if (mCurrentPage == 1) {
                    mSliderViewPager.setCurrentItem(+1);
                }
            }
        });

        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (checkBox.isChecked()){

                if (checkBox.isChecked()){

                    Intent toCreate = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(toCreate);



                }else {

                    //Toast.makeText(getApplicationContext(), "Accept terms & condition", Toast.LENGTH_SHORT).show();
                    ToastBack("Accept terms & condition");
                    mSliderViewPager.setCurrentItem(+1);

                }

//                    Snackbar snackbar = Snackbar.make(relativeLayout, "Accept Terms and Conditions", Snackbar.LENGTH_LONG);
//                    snackbar.show();
//                    mSliderViewPager.setCurrentItem(-2);



            }
        });

    }


    private void Check_Location_enable() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(WalkthroughActivity.this);

        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here
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
                                    rae.startResolutionForResult(WalkthroughActivity.this, REQUEST_CHECK_SETTINGS);
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
    private Toast backToast;
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

    private void addDotsIndicator(int position) {


        mDots = new TextView[3];
        mDotsLayout.removeAllViews();

        for (int i = 0 ; i< mDots.length; i++ ){


            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparent));
            mDotsLayout.addView(mDots[i]);

        }

        if (mDots.length > 0)
        {
            mDots[position].setTextColor(getResources().getColor(R.color.colorSecondary));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPage = position;
        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
            mCurrentPage = position;

            if (position == 0)
            {
                NextBtn.setEnabled(true);
                BackBtn.setEnabled(false);
                BackBtn.setVisibility(View.INVISIBLE);
                get_started.setVisibility(View.GONE);
                get_started.setEnabled(false);
                NextBtn.setText("Next");
                checkBox.setVisibility(View.GONE);
                privacy.setVisibility(View.GONE);
                BackBtn.setText("");

            }else if (position == 1){

                NextBtn.setEnabled(true);
                BackBtn.setEnabled(false);
                BackBtn.setVisibility(View.INVISIBLE);
                checkBox.setEnabled(true);
                get_started.setVisibility(View.GONE);
                get_started.setEnabled(false);
                NextBtn.setText("Swipe");
                privacy.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.VISIBLE);
                BackBtn.setText("Back");

            }
            else if(position == 2){
                NextBtn.setEnabled(true);
                privacy.setVisibility(View.GONE);
                BackBtn.setEnabled(true);
                BackBtn.setVisibility(View.VISIBLE);
                get_started.setVisibility(View.VISIBLE);
                get_started.setEnabled(true);
                checkBox.setVisibility(View.GONE);
                checkBox.setEnabled(false);
                NextBtn.setText("");
                BackBtn.setText("Back");

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {


                    return false;
                }

            }
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Check_Location_enable();

        if (mAuth.getCurrentUser() != null)
        {
            if (mAuth.getCurrentUser().getUid() != null){

                startActivity(new Intent(getApplicationContext(),MapsVActivity.class));
            }

        }

    }


    public boolean checkLocationPermission () {
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
                                ActivityCompat.requestPermissions(WalkthroughActivity.this,
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
    public void onRequestPermissionsResult ( int requestCode,
                                             String permissions[], int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:


                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here
                    mSliderViewPager.setCurrentItem(+1);

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
                mSliderViewPager.setCurrentItem(+1);
            }
        }
    }

    private void openGpsEnableSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_ENABLE_GPS);
    }


}

