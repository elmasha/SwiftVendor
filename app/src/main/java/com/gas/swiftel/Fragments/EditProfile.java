package com.gas.swiftel.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gas.swiftel.Activity.VendorRegActivity;
import com.gas.swiftel.Model.Gas_Vendor;
import com.gas.swiftel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;


public class EditProfile extends Fragment {
View view;


    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorRef = db.collection("SwiftGas_Vendor");
    private TextInputLayout firstname_edit,phonenumber_edit,email_edit,shop_edit,loaction_edit;
    private Button SaveChanges;
    private Uri ImageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    private ProgressBar progressBar;
    private Bitmap compressedImageBitmap;
    private CircleImageView editProfile;
    private TextView addPhoto;

    private UploadTask uploadTask;



    public EditProfile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        //----Edit changes---///
        firstname_edit = view.findViewById(R.id.text_edit_fullname);
        phonenumber_edit = view.findViewById(R.id.text_edit_number);
        shop_edit =view.findViewById(R.id.text_edit_shop);
        email_edit = view.findViewById(R.id.text_edit_email);
        loaction_edit = view.findViewById(R.id.text_edit_location);

        SaveChanges = view.findViewById(R.id.Submit_changes);
        editProfile = view.findViewById(R.id.Edit_profile);
        addPhoto = view.findViewById(R.id.textEditPhoto);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start((Activity) getContext());
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start((Activity) getContext());
            }
        });


        SaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Validation()){

                }else {
                    Update_profile();
                }

            }
        });

        loadData();
    return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    //data.getData returns the content URI for the selected Image

                    ImageUri = result.getUri();
                    if (ImageUri != null){
                        //  Client_image.setImageURI(ImageUri);
                        editProfile.setImageURI(ImageUri);
                    }



                    break;


            }
    }

    private boolean Validation(){

        input_name = firstname_edit.getEditText().getText().toString();
        input_shop = shop_edit.getEditText().getText().toString();
        input_email = email_edit.getEditText().getText().toString();
        input_number = phonenumber_edit.getEditText().getText().toString();
        input_location = loaction_edit.getEditText().getText().toString();

        if (input_name.isEmpty()){
            firstname_edit.setError("Please enter a name");
            return false;
        }else if (input_email.isEmpty()){
            email_edit.setError("Please provide an email address");
            return false;
        }else if (input_location.isEmpty()){
            loaction_edit.setError("Enter your location");
            return false;
        }else if (input_number.isEmpty()){
            phonenumber_edit.setError("Enter phone number.");
            return false;
        }else if (input_shop.isEmpty()){
            shop_edit.setError("Provide name of your shop.");
            return false;
        }else {
            shop_edit.setError(null);
            phonenumber_edit.setError(null);
            email_edit.setError(null);
            loaction_edit.setError(null);
            firstname_edit.setError(null);
            return true;
        }


    }

    private String input_name,input_shop,input_number,input_email,input_location;
    private void Update_profile() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Saving Changes");
        progressDialog.show();

        String UID= mAuth.getCurrentUser().getUid();
        input_name = firstname_edit.getEditText().getText().toString();
        input_shop = shop_edit.getEditText().getText().toString();
        input_email = email_edit.getEditText().getText().toString();
        input_number = phonenumber_edit.getEditText().getText().toString();
        input_location = loaction_edit.getEditText().getText().toString();

        HashMap<String,Object> store = new HashMap<>();
        store.put("first_name",input_name);
        store.put("last_name","");
        store.put("Location",input_location);
        store.put("mobile",input_number);
        store.put("Email",input_email);
        store.put("ShopName",input_shop);


        if (input_email.equals(Email)){
            vendorRef.document(UID).update(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        ToastBack("Saved changes..");

                    }else {
                        progressDialog.dismiss();
                        ToastBack(task.getException().getMessage());
                    }

                }
            });
        }else {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updateEmail(input_email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                vendorRef.document(UID).update(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            ToastBack("Saved changes..");

                                        }else {
                                            progressDialog.dismiss();
                                            ToastBack(task.getException().getMessage());
                                        }

                                    }
                                });
                            }
                        }
                    });

        }

    }




    //-----Load Details-----//
    private String location,Phone,First_name,Email,Shopname,shopNO,User_image;
    private long Earnings,Trips,Cash_Trips;
    private String activation_fee;
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

                    firstname_edit.getEditText().setText(First_name);
                    phonenumber_edit.getEditText().setText(Phone);
                    email_edit.getEditText().setText(Email);
                    shop_edit.getEditText().setText(Shopname);
                    loaction_edit.getEditText().setText(location);

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



}