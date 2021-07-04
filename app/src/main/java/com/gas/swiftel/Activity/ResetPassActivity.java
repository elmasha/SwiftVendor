package com.gas.swiftel.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gas.swiftel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout email,password1,password2;
    private Button BtnResetPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        mAuth = FirebaseAuth.getInstance();

        //InputText
        email = findViewById(R.id.EmailReset);

        BtnResetPass = findViewById(R.id.ResetBtn);






        BtnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPass();
            }
        });



    }

    private void ResetPass() {

       String emailAddress = email.getEditText().getText().toString();

        if (emailAddress.isEmpty()){

            email.setError("Provide your email");

        }else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();


            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                ToastBack("Link sent to your email");
                            }else {
                                progressDialog.dismiss();
                                ToastBack(task.getException().getMessage());

                            }
                        }
                    });

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

}
