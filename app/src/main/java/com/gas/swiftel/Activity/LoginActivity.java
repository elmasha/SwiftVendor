package com.gas.swiftel.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;

import com.gas.swiftel.R;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView toVendorReg,resetPassword;


    private TextInputLayout email_Log,password_Log;
    private Button log_In;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private FirebaseUser currentUser;

    private String refreshToken,mUID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    CollectionReference tokensRef = db.collection("Tokens");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TextView..
        toVendorReg = findViewById(R.id.Reg);
        resetPassword = findViewById(R.id.ResetPassword);

        //EditText
        email_Log = findViewById(R.id.Emaillog);
        password_Log = findViewById(R.id.Passwordlog);

        //Button
        log_In =findViewById(R.id.LogIn);

        FirebaseApp.getInstance();

        refreshToken = FirebaseInstanceId.getInstance().getToken();
        mAuth = FirebaseAuth.getInstance();



        log_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ValidaTion()){

                  return ;
                 }else {

                     SignIn();
                  }


            }
        });



        toVendorReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toreg = new Intent(getApplicationContext(),VendorRegActivity.class);
                startActivity(toreg);
                finish();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toreg = new Intent(getApplicationContext(),ResetPassActivity.class);
                startActivity(toreg);
            }
        });



    }

    private void SignIn() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign In...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        String Email = email_Log.getEditText().getText().toString();
        String Password = password_Log.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    UpdateToken();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

               // Toast.makeText(LoginActivity.this, "Log in Failed!!", Toast.LENGTH_SHORT).show();

                ToastBack(e.getMessage());

               progressDialog.dismiss();
            }
        });

    }

    private void UpdateToken() {

        mUID = mAuth.getCurrentUser().getUid();

        String refreshtoken = FirebaseInstanceId.getInstance().getToken();
        Map<String,Object> UpdateToken = new HashMap<>();
        UpdateToken.put("device_token",refreshtoken);



        vendorRef.document(mUID).update(UpdateToken).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                   // Toast.makeText(LoginActivity.this, ", Toast.LENGTH_SHORT).show();
                    ToastBack("Log in successful, Welcome.");
                    Intent toHome = new Intent(getApplicationContext(),MapsVActivity.class);
                    startActivity(toHome);
                    finish();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private boolean ValidaTion() {

        String Email = email_Log.getEditText().getText().toString();
        String Password = password_Log.getEditText().getText().toString();

        if (Email.isEmpty()) {
            //email_Log.setError("Email is empty");
            ToastBack("Email is empty");
            return false;

        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
           // email_Log.setError("");
            ToastBack("Please enter a Valid email");
            return false;
        }
        else if (Password.isEmpty()) {
           // password_Log.setError("");
            ToastBack("Password required");
            return false;
        }else {
            email_Log.setError(null);
            password_Log.setError(null);
            return true;
        }

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


    @Override
    protected void onStart() {
        super.onStart();

//         currentUser = mAuth.getCurrentUser();
//
//        if (currentUser != null){
//
//            Intent to_Home = new Intent(getApplicationContext(),Home_Activity.class);
//            startActivity(to_Home);
//            finish();
//
//        }else{
//
//
//        }





    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
