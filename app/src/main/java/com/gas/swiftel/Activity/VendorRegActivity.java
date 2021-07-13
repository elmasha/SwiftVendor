package com.gas.swiftel.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gas.swiftel.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static com.gas.swiftel.Activity.VendorMapActivity.MY_PERMISSIONS_REQUEST_LOCATION;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class VendorRegActivity extends AppCompatActivity {


    private Button Finish;
    private TextView toVendorLog,Add_image,addPic;
    private EditText firstName,lastName,id_No,phone_No,location,shopname,email,password,con_Password;


    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;


    private Uri ImageUri;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    CollectionReference tokensRef = db.collection("Tokens");
    CollectionReference adminRef = db.collection("Admin");
    FirebaseStorage storage;
    StorageReference storageReference;
    private ProgressBar progressBar;
    private Bitmap compressedImageBitmap;

    private UploadTask uploadTask;
    private AlertDialog dialog2;
    private CircleImageView Client_image,addProfile;
    private FloatingActionButton add_Profile;


    @Override
    protected void onRestart() {
        super.onRestart();
    LoadAdmin();
    }

    @Override
    protected void onResume() {
        super.onResume();
    LoadAdmin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_reg);


        checkLocationPermission();
        mAuth = FirebaseAuth.getInstance();


        //Button..
        Finish = findViewById(R.id.Submit);

        //EditText..
        firstName = findViewById(R.id.FirstName);
        lastName =findViewById(R.id.LastName);
        phone_No = findViewById(R.id.Phone_number);
        location = findViewById(R.id.Location);
        shopname = findViewById(R.id.Shop_name);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        addProfile = findViewById(R.id.Add_profile);
        add_Profile = findViewById(R.id.FabAddProfile);
        con_Password = findViewById(R.id.Con_Password);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        addPic = findViewById(R.id.textAddPhoto);

        toVendorLog = findViewById(R.id.log);


        toVendorLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toreg1 = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(toreg1);
                finish();
            }
        });



        add_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(VendorRegActivity.this);
            }
        });

        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(VendorRegActivity.this);

            }
        });



        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Validation()){

                    return ;
                }else {


                    RegisterUser();
                }
            }
        });
    }




    long Activeshops;
    long inActiveShops;
    long noOfShops;
    long regVendors;
    private void LoadAdmin (){
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
                    noOfShops = documentSnapshot.getLong("Total_No_Shops");
                    regVendors = documentSnapshot.getLong("Registered_Vendors");

                }
            }
        });

    }


    //---- Update notification token ----
    private void UpdateToken() {

      String  mUID = mAuth.getCurrentUser().getUid();

        String refreshtoken = FirebaseInstanceId.getInstance().getToken();
        Map<String,Object> UpdateToken = new HashMap<>();
        UpdateToken.put("token",refreshtoken);


        tokensRef.document(mUID).set(UpdateToken).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(VendorRegActivity.this ,"Log in successful..", Toast.LENGTH_SHORT).show();

                    Intent toHome = new Intent(getApplicationContext(),MapsVActivity.class);
                    startActivity(toHome);
                    finish();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(VendorRegActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    //---Register vendor function ----
    private String VerifyPass,Password;
    private void RegisterUser() {

        VerifyPass = con_Password.getText().toString();
         Password = password.getText().toString();


        if (ImageUri != null){


            if ( VerifyPass.equals(Password)){

                User_Authentication(Password);

            }
            else {

                ToastBack("Password do not Match");

            }


        }else {

            addPic.setText("No Image");
            addPic.setTextColor(Color.parseColor("#FA0707"));
            ToastBack("No profile image selected.");
        }



    }

    //---Sign In with email and password ---
    private void User_Authentication(String password){


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String Email = email.getText().toString();

        mAuth.createUserWithEmailAndPassword(Email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Store_Image_and_Details();
                            UpdateToken();


                        } else {
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {

                //  Toast.makeText(VendorRegActivity.this, "Sign Up failed."+e.getMessage(),

                ToastBack(e.getMessage());


                progressDialog.dismiss();

            }
        });

    }

    ///-----Profile image and details storage ---
    private void Store_Image_and_Details() {


        File newimage = new File(ImageUri.getPath());
        String first_name = firstName.getText().toString();
        String Location = location.getText().toString();
        String last_name = lastName.getText().toString();
        String mobile = phone_No.getText().toString();
        String Email = email.getText().toString();
        String ShopName = shopname.getText().toString();
        String Password = password.getText().toString();
        String Con_Password = con_Password.getText().toString();


        if (Password.equals(Con_Password)){

            try {

                Compressor compressor = new Compressor(this);
                compressor.setMaxHeight(200);
                compressor.setMaxWidth(200);
                compressor.setQuality(10);
                compressedImageBitmap = compressor.compressToBitmap(newimage);
            } catch (IOException e) {
                e.printStackTrace();
            }


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            final byte[] data = baos.toByteArray();



            final StorageReference ref = storageReference.child("Users/thumbs" + UUID.randomUUID().toString());
            uploadTask = ref.putBytes(data);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    String profileImage = downloadUri.toString();

                    Random dice = new Random();
                    int shop_No =dice.nextInt(100000000)+1;
                    String Shop_No =String.valueOf(shop_No);


                    String  token = FirebaseInstanceId.getInstance().getToken();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String User_ID = mAuth.getCurrentUser().getUid();

                    HashMap<String,Object> store = new HashMap<>();
                    store.put("first_name",first_name+" "+last_name);
                    store.put("last_name","");
                    store.put("Location",Location);
                    store.put("mobile",mobile);
                    store.put("Email",Email);
                    store.put("ShopName",ShopName);
                    store.put("Shop_No",Shop_No);
                    store.put("User_ID",User_ID);
                    store.put("User_Image",profileImage);
                    store.put("device_token",token);
                    store.put("Activation_fee","0");
                    store.put("Trips",0);
                    store.put("Earnings",0);
                    store.put("Cash_Trips",0);
                    store.put("Rating",0);
                    store.put("Avg_Rate",0);


                    vendorRef.document(User_ID).set(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                long add2 = noOfShops + 1;
                                long add = regVendors + 1;
                                long add3 = inActiveShops + 1;

                                HashMap<String ,Object> admin = new HashMap<>();
                                admin.put("Total_No_Shops",add2);
                                admin.put("Registered_Vendors",add);
                                admin.put("Inactive_shops",add3);
                                adminRef.document("Elmasha").update(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            ToastBack("Registration successful.");
                                            Intent toreg = new Intent(getApplicationContext(),MapsVActivity.class);
                                            startActivity(toreg);
                                            finish();

                                        }
                                    }
                                });


                            }else {

                                //  Toast.makeText(VendorRegActivity.this, "", Toast.LENGTH_SHORT).show();
                                ToastBack("Registration failed. Try again later");
                                progressDialog.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ToastBack(e.getMessage());

                        }
                    });




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }else {

            ToastBack("Passwords do not match");
        }




    }

    private void ImageUpload_dialog(){

        final  AlertDialog.Builder mbuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_image, null);
        mbuilder.setView(mView);
        dialog2 = mbuilder.create();
        dialog2.show();
        Client_image = mView.findViewById(R.id.My_image);
        Add_image = mView.findViewById(R.id.Image_add);
        TextView closeDia = mView.findViewById(R.id.CloseImage);

        closeDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });

        Add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(VendorRegActivity.this);

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

    ///---Registration input Validation ----
    private boolean Validation() {

        String first_name = firstName.getText().toString();
        String Location = location.getText().toString();
        String last_name = lastName.getText().toString();
        String mobile = phone_No.getText().toString();
        String Email = email.getText().toString();
        String ShopName = shopname.getText().toString();
        String Password = password.getText().toString();
        String Con_Password = con_Password.getText().toString();


        if (first_name.isEmpty()) {
            ToastBack("First Name is empty");
            return false;
        }
        else if (first_name.length() > 18) {
            ToastBack("First name is too long.");
            return false;
        }else if (first_name.length() < 3) {
            ToastBack("First name is too short.");
            return false;
        }
        else if (last_name.isEmpty()) {

            ToastBack("Last name is Empty");
            return false;

        }
        else if (last_name.length() < 2) {

            ToastBack("Password too short..");
            return false;
        }
        else if (last_name.length() > 18) {

            ToastBack("Password too long..");
            return false;
        }

        else if (mobile.isEmpty()) {

            ToastBack("Mobile Number is Empty");
            return false;
        }
        else if (mobile.length() > 16) {

            ToastBack("Phone number too long..");
            return false;
        }
        else if (mobile.length() < 6) {

            ToastBack("Phone number too short..");
            return false;
        }
        else if (ShopName.isEmpty()) {
            ToastBack("Shop name is Empty");
            return false;
        }
        else if (ShopName.length() > 18) {
            ToastBack("ShopName is too long...");
            return false;
        }
        else if (ShopName.length() < 5) {
            ToastBack("ShopName is too short..");
            return false;
        }
        else if (Location.isEmpty()) {
            ToastBack("Location is Empty");
            return false;
        }
        else if (Location.length() > 18) {

            ToastBack("Location is too long..");
            return false;
        }
        else if (Location.length() < 4) {

            ToastBack("Location is too short..");
            return false;
        }
        else if (Email.length() < 5) {

            ToastBack("Email is too short...");
            return false;
        }
        else if (Email.isEmpty()) {

            ToastBack("Email is empty");
            return false;

        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {

            ToastBack("Please enter a Valid email");
            return false;
        }
        else if (Password.isEmpty()) {
            ToastBack("Password Required");
            return false;
        }
        else if (Password.length() < 6) {
            ToastBack("Password too short.");
            return false;
        }else if (Password.length() > 8) {

            ToastBack("Password too long..");
            return false;
        }
        else if (Con_Password.isEmpty()) {

            ToastBack("Verify Password");
            return false;
        }else if (Con_Password.length() < 6) {

            ToastBack("Password too short.");
            return false;
        }else if (Con_Password.length() > 8) {

            ToastBack("Password too long..");
            return false;
        }

        else {
            firstName.setError(null);
            location.setError(null);
            lastName.setError(null);
            phone_No.setError(null);
            shopname.setError(null);
            password.setError(null);
            con_Password.setError(null);
            email.setError(null);
            return true;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

//        String currentUser = mAuth.getCurrentUser().getUid();
//
//
//        if (currentUser != null){
//
//            Intent to_Home = new Intent(getApplicationContext(),Home_Activity.class);
//            startActivity(to_Home);
//            finish();
//        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    //----Photo on result ---
   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK)
                switch (requestCode) {
                    case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);

                        //data.getData returns the content URI for the selected Image

                        ImageUri = result.getUri();
                        if (ImageUri != null){
                          //  Client_image.setImageURI(ImageUri);
                            addProfile.setImageURI(ImageUri);
                        }



                        break;


                }


        }

    ////------Checking location permission ------
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
                                ActivityCompat.requestPermissions(VendorRegActivity.this,
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

}


