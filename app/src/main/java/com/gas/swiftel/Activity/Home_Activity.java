package com.gas.swiftel.Activity;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.gas.swiftel.Common;
import com.gas.swiftel.Fragments.MyShopFragment;
import com.gas.swiftel.Fragments.NotificationFragment;
import com.gas.swiftel.Fragments.OrdersFragment;
import com.gas.swiftel.Fragments.VendorProfileFragment;
import com.gas.swiftel.Interface.RetrofitInterface;
import com.gas.swiftel.Model.Result;
import com.gas.swiftel.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home_Activity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = Common.BASE_URL;

    private FirebaseAuth mAuth;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment SelectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_shop:

                    SelectedFragment = new MyShopFragment();

                    break;
                case R.id.navigation_order:

                    SelectedFragment = new OrdersFragment();

                    break;
                case R.id.navigation_profile:

                    SelectedFragment = new VendorProfileFragment();

                    break;
                     
                case R.id.navigation_notifications:

                    SelectedFragment = new NotificationFragment();

                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.Frame_cont,

                    SelectedFragment).commit();

            return true;
        }
    };

    private int order_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);


        mAuth = FirebaseAuth.getInstance();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.Frame_cont,new
                MyShopFragment()).commit();


        if (getIntent() != null){

            order_status = getIntent().getIntExtra("Order_menu",0);
            if (order_status ==1){
                getSupportFragmentManager().beginTransaction().replace(R.id.Frame_cont,new
                        OrdersFragment()).commit();

            }

        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        LoadAPI();
    }

    private void LoadAPI() {

        Call<Result> callStk = retrofitInterface.getResult();
        callStk.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() == 200){
                    Result responseStk = response.body();

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser current_user = mAuth.getCurrentUser();



    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
