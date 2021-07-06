package com.gas.swiftel.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gas.swiftel.Adapter.CylinderAdapter;
import com.gas.swiftel.Adapter.NotifyAdapter;
import com.gas.swiftel.Model.Gas_Cylinder;
import com.gas.swiftel.Model.Notifications;
import com.gas.swiftel.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class AddGasActivity extends AppCompatActivity {


    private Uri imageUri ;
    private String Gas_Image;

    //DialogGas
    private int Gasprice,GasrefillPrice ;
    private EditText gasName,gasDesc,gasPrice,gasRefilprice;
    private Spinner gasKgs;
    private Button submitGas;
    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Bitmap compressedImageBitmap;
    private String GasName,GasDesc,GasKgs,GasImage,gaskg;

    private ImageView gasImage,itemImage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference vendorShopRef = db.collection("VendorsShop");
    private UploadTask uploadTask;

    private TextView backGas,backAccess;
    private TextView AddGasCamera,AddAccessCamera;
    CollectionReference adminRef = db.collection("Admin");
    private FloatingActionButton gallery,pickImage;
    private RelativeLayout layoutSelect;
    private TextView close;
    private RecyclerView recyclerView;

    private CylinderAdapter adapter;
    private int WinsdowState = 0;

    @Override
    protected void onStart() {
        super.onStart();
        FetchCylinder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_add_gas);
        recyclerView = findViewById(R.id.RecyclerCylinder);
        gallery = findViewById(R.id.Form_camera);
        pickImage = findViewById(R.id.From_Db);
        layoutSelect = findViewById(R.id.LayoutCylinder);
        close = findViewById(R.id.closeCylinder);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        gasImage = findViewById(R.id.Gas_image);
        gasName = findViewById(R.id.gas_name);
        gasDesc = findViewById(R.id.gas_desc);
        gasPrice = findViewById(R.id.gas_price);
        gasRefilprice = findViewById(R.id.gas_refill);
        gasKgs = findViewById(R.id.gas_kgs);
        submitGas = findViewById(R.id.Add_Gas);
        backGas = findViewById(R.id.LableBack22);


        backGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddItemsActivity.class));
            }
        });



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSelect.setVisibility(View.GONE);
            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(AddGasActivity.this);
            }
        });



        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WinsdowState =2;
                layoutSelect.setVisibility(View.VISIBLE);
            }
        });


        submitGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ProgressDialog progressDialog = new ProgressDialog(getContext());
//                progressDialog.setMessage("Please wait store ing in process..");
//                progressDialog.setCancelable(false);

                if (!ValidaTion()) {

                }else {
                    UploadGas();
                }


            }
        });


        FetchCylinder();
    }




    private void UploadGas() {


        if (imageUri == null){


            if (GasImage != null && gaskg != null){

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait uploading Gas..");
                progressDialog.show();

                GasName = gasName.getText().toString();
                GasDesc = gasDesc.getText().toString();
                Gasprice = Integer.parseInt((gasPrice.getText().toString()));
                GasrefillPrice = Integer.parseInt((gasRefilprice.getText().toString()));
                GasKgs = gasKgs.getSelectedItem().toString();

                HashMap<String, Object> store = new HashMap<>();
                store.put("Gas_Name", GasName);
                store.put("Gas_Desc", GasDesc);
                store.put("Gas_Price", Gasprice);
                store.put("Gas_RefillPrice", GasrefillPrice);
                store.put("Gas_Kgs", gaskg);
                store.put("User_ID", mAuth.getCurrentUser().getUid());
                store.put("Gas_Image", GasImage);
                store.put("timestamp", FieldValue.serverTimestamp());

                vendorShopRef.document().set(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Stored Successfully..", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            gasDesc.setText("");
                            gasName.setText("");
                            gasPrice.setText("");
                            gasRefilprice.setText("");
                            gasImage.setImageURI(null);





                        } else {

                            Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });
            }else {

                Toast.makeText(getApplicationContext(), "Image file not selected", Toast.LENGTH_SHORT).show();

            }


        } else {

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait uploading Gas..");
            progressDialog.show();


            GasName = gasName.getText().toString();
            GasDesc = gasDesc.getText().toString();
            Gasprice = Integer.parseInt((gasPrice.getText().toString()));
            GasrefillPrice = Integer.parseInt((gasRefilprice.getText().toString()));
            GasKgs = gasKgs.getSelectedItem().toString();


            String User_ID = mAuth.getCurrentUser().getUid();

//        Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            File newimage = new File(imageUri.getPath());

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
            byte[] data = baos.toByteArray();


            final StorageReference ref = storageReference.child("images/thumbs" + UUID.randomUUID().toString());
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
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        String url34 = downloadUri.toString();

                        HashMap<String, Object> store = new HashMap<>();
                        store.put("Gas_Name", GasName);
                        store.put("Gas_Desc", GasDesc);
                        store.put("Gas_Price", Gasprice);
                        store.put("Gas_RefillPrice", GasrefillPrice);
                        store.put("Gas_Kgs", GasKgs);
                        store.put("User_ID", User_ID);
                        store.put("Gas_Image", url34);
                        store.put("timestamp", FieldValue.serverTimestamp());

                        vendorShopRef.document().set(store).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(), "Stored Successfully..", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                    gasDesc.setText("");
                                    gasName.setText("");
                                    gasPrice.setText("");
                                    gasRefilprice.setText("");
                                    gasImage.setImageURI(null);





                                } else {

                                    Toast.makeText(getApplicationContext(), "Storing Failed!! try again later..", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            }
                        });


                    } else {
                        // Handle failures
                        // ...
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
//            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
//                            .getTotalByteCount());
//                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
//                }
//            });


        }


    }





    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    //data.getData returns the content URI for the selected Image

                    imageUri = result.getUri();
                    gasImage.setImageURI(imageUri);



                    break;
            }


    }


    private boolean ValidaTion() {

        GasName = gasName.getText().toString();
        GasDesc = gasDesc.getText().toString();
        Gasprice = Integer.parseInt(gasPrice.getText().toString());
        GasrefillPrice = Integer.parseInt(gasRefilprice.getText().toString());

        if (GasName.isEmpty()) {
            gasName.setError("Gas Name is empty");
            return false;
        }

        else if (GasDesc.isEmpty()) {
            gasDesc.setError("Gas Description is Empty");
            return false;
        }

        else if (Gasprice == 0.00) {
            gasPrice.setError("Purchase price is Empty");
            return false;
        }
        else if (GasrefillPrice == 0) {
            gasRefilprice.setError("Refill price is Empty");
            return false;
        }
        else {
            gasName.setError(null);
            gasDesc.setError(null);
            gasPrice.setError(null);
            gasRefilprice.setError(null);
            return true;
        }

    }


    private void FetchCylinder() {

       Query query = adminRef.document("Gas_Cylinder").collection("Images&KG")
                 .orderBy("name", Query.Direction.ASCENDING)
                 .orderBy("kg", Query.Direction.DESCENDING)
                 .limit(50);
        FirestoreRecyclerOptions<Gas_Cylinder> options = new FirestoreRecyclerOptions.Builder<Gas_Cylinder>()
                .setQuery(query, Gas_Cylinder.class)
                .setLifecycleOwner(this)
                .build();
        adapter = new CylinderAdapter(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        adapter.setOnItemClickListener(new CylinderAdapter.OnItemCickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Gas_Cylinder  gas_cylinder = documentSnapshot.toObject(Gas_Cylinder.class);
                GasImage = gas_cylinder.getCylinder_image();
                gaskg = gas_cylinder.getKg();
                if (GasImage != null){
                    Picasso.with(getApplicationContext()).load(GasImage).placeholder(R.drawable.load).error(R.drawable.errorimage).into(gasImage);
                }

                layoutSelect.setVisibility(View.GONE);
            }
        });


    }



}